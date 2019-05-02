package top.yeonon.huhusearchservice.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.constant.MysqlConst;
import top.yeonon.huhusearchservice.constant.RedisConst;
import top.yeonon.huhusearchservice.mysql.listener.handler.EventDataHandler;
import top.yeonon.huhusearchservice.util.SearchUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author yeonon
 * @date 2019/5/1 0001 11:45
 **/
@Component
@DependsOn(value = {
        "questionRepository",
        "answerRepository",
        "userRepository",
        "jdbcTemplate"
})
@Slf4j
public class BinlogListener {

    private final EventDataHandler questionEventDataHandler;

    private final EventDataHandler answerEventDataHandler;

    private final EventDataHandler userEventDataHandler;

    private final RedissonClient redissonClient;


    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;


    @Autowired
    public BinlogListener(@Qualifier("questionEventDataHandler") EventDataHandler questionEventDataHandler,
                          @Qualifier("answerEventDataHandler") EventDataHandler answerEventDataHandler,
                          @Qualifier("userEventDataHandler") EventDataHandler userEventDataHandler, RedissonClient redissonClient) {
        this.questionEventDataHandler = questionEventDataHandler;
        this.answerEventDataHandler = answerEventDataHandler;
        this.userEventDataHandler = userEventDataHandler;
        this.redissonClient = redissonClient;
    }

    private static Map<Long, String> tableNameById = Maps.newConcurrentMap();

    private static ExecutorService executor = Executors.newFixedThreadPool(4);


    @PostConstruct
    public void init() throws HuhuException {
        String host = SearchUtils.getHostFromJdbcUrl(url);
        Integer port = SearchUtils.getPortFromJdbcUrl(url);

        //将其扔进线程池，避免阻塞主线程
        executor.execute(() -> {
            BinaryLogClient client = new BinaryLogClient(
                    host,
                    port,
                    username,
                    password
            );
            client.registerEventListener(new DelegatingEventListener());
            try {
                client.connect();
                log.info("listening mysql binlog....");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }



    private class DelegatingEventListener implements BinaryLogClient.EventListener {

        @Override
        public void onEvent(Event event) {
            //获取分布式锁
            RLock lock = redissonClient.getLock(RedisConst.BINLOG_CHANGE_LOCK);
            boolean getLock = false;
            try {
                if (getLock = lock.tryLock(RedisConst.BINLOG_GET_LOCK_TIMEOUT, TimeUnit.SECONDS)) {
                    EventType eventType = event.getHeader().getEventType();
                    switch (eventType) {
                        case TABLE_MAP:
                            TableMapEventData tableMapEventData = event.getData();
                            tableNameById.putIfAbsent(tableMapEventData.getTableId(), tableMapEventData.getTable());
                            break;
                        case EXT_WRITE_ROWS:
                            handleWriteRowEvent(event);
                            break;
                        case EXT_UPDATE_ROWS:
                            handleUpdateRowEvent(event);
                            break;
                        case EXT_DELETE_ROWS:
                            handleDeleteRowEvent(event);
                        default:
                            //ignore
                    }
                }

            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } finally {
                //如果成功获取锁了，最后释放锁
                //没有成获取则不需要也不能重复释放锁
                if (getLock) {
                    lock.unlock();
                }
            }

        }
    }

    /**
     * 处理写事件
     *
     * @param event 事件
     */
    private void handleWriteRowEvent(Event event) {
        WriteRowsEventData data = event.getData();
        String tableName = tableNameById.get(data.getTableId());
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                questionEventDataHandler.handleWriteRowData(data);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                answerEventDataHandler.handleWriteRowData(data);
                break;
            case MysqlConst.UserBase.USER_TABLE:
                userEventDataHandler.handleWriteRowData(data);
                break;
            default:
                //ignore
        }
    }

    /**
     * 处理更新事件
     * @param event 更新事件
     */
    private void handleUpdateRowEvent(Event event) {
        UpdateRowsEventData data = event.getData();
        String tableName = tableNameById.get(data.getTableId());
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                questionEventDataHandler.handleUpdateRowData(data);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                answerEventDataHandler.handleUpdateRowData(data);
                break;
            case MysqlConst.UserBase.USER_TABLE:
                userEventDataHandler.handleUpdateRowData(data);
                break;
            default:
                //ignore
        }
    }

    /**
     * 处理删除事件
     * @param event 事件
     */
    private void handleDeleteRowEvent(Event event) {
        DeleteRowsEventData data = event.getData();
        String tableName = tableNameById.get(data.getTableId());
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                questionEventDataHandler.handleDeleteRowData(data);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                answerEventDataHandler.handleDeleteRowData(data);
                break;
            case MysqlConst.UserBase.USER_TABLE:
                userEventDataHandler.handleDeleteRowData(data);
                break;
                default:
                    //ignore
        }
    }
}
