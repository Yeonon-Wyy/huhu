package top.yeonon.huhusearchservice.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.constant.ErrMessage;
import top.yeonon.huhusearchservice.constant.MysqlConst;
import top.yeonon.huhusearchservice.entity.User;
import top.yeonon.huhusearchservice.repository.AnswerRepository;
import top.yeonon.huhusearchservice.repository.QuestionRepository;
import top.yeonon.huhusearchservice.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final String SQL_SCHEMA = "SELECT table_schema,table_name,column_name,ordinal_position " +
            "FROM information_schema.`COLUMNS` " +
            "WHERE table_schema = ? and table_name = ?";

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final UserRepository userRepository;

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @Autowired
    public BinlogListener(QuestionRepository questionRepository,
                          AnswerRepository answerRepository,
                          UserRepository userRepository,
                          JdbcTemplate jdbcTemplate) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Map<Integer, String> questionPosToName = Maps.newConcurrentMap();
    private static Map<Integer, String> answerPosToName = Maps.newConcurrentMap();
    private static Map<Integer, String> userPosToName = Maps.newConcurrentMap();

    private static Map<Long, TableMapEventData> tablesById = Maps.newConcurrentMap();

    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    @PostConstruct
    public void init() throws HuhuException {

        handlePosToName(MysqlConst.QABase.DATABASE, MysqlConst.QABase.QUESTION_TABLE, questionPosToName);
        handlePosToName(MysqlConst.QABase.DATABASE, MysqlConst.QABase.ANSWER_TABLE, answerPosToName);
        handlePosToName(MysqlConst.UserBase.DATABASE, MysqlConst.UserBase.USER_TABLE, userPosToName);


        String host = getHostFromUrl(url);
        Integer port = getPortFromUrl(url);
        BinaryLogClient client = new BinaryLogClient(
                host,
                port,
                username,
                password
        );

        client.registerEventListener(new DelegatingEventListener());


        executor.execute(() -> {
            try {
                client.connect();
                log.info("listening mysql binlog....");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }


    private String getHostFromUrl(String url) throws HuhuException {
        String[] strs = url.split("/");
        if (strs.length < 4) {
            throw new HuhuException(ErrMessage.PARSE_MYSQL_URL_ERROR);
        }
        return strs[2].split(":")[0];
    }

    private Integer getPortFromUrl(String url) throws HuhuException {
        String[] strs = url.split("/");
        if (strs.length < 4) {
            throw new HuhuException(ErrMessage.PARSE_MYSQL_URL_ERROR);
        }
        return Integer.parseInt(strs[2].split(":")[1]);
    }


    private void handlePosToName(String database, String tableName, Map<Integer, String> posToName) {
        jdbcTemplate.query(SQL_SCHEMA, new Object[]{database, tableName}, rs -> {
            int pos = rs.getInt("ordinal_position");
            String columnName = rs.getString("column_name");
            posToName.put(pos - 1, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
        });
    }


    private class DelegatingEventListener implements BinaryLogClient.EventListener {

        @Override
        public void onEvent(Event event) {
            EventType eventType = event.getHeader().getEventType();

            switch (eventType) {
                case TABLE_MAP:
                    TableMapEventData tableMapEventData = event.getData();
                    tablesById.put(tableMapEventData.getTableId(), tableMapEventData);
                    break;
                case EXT_WRITE_ROWS:
                    handleWriteRowEvent(event);
                    break;
                case EXT_UPDATE_ROWS:
                    System.out.println("update row");
                    break;
                default:
                    //ignore
            }
        }
    }

    private void handleWriteRowEvent(Event event) {
        WriteRowsEventData data = event.getData();
        String tableName = tablesById.get(data.getTableId()).getTable();
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                syncQuestionToElasticsearch(data);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                syncAnswerToElasticsearch(data);
            case MysqlConst.UserBase.USER_TABLE:
                syncUserToElasticsearch(data);
                default:
                    //ignore
        }
    }

    private void syncUserToElasticsearch(WriteRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(columns -> {
            for (int i = 0; i < columns.length; i++) {
                values.put(userPosToName.get(i), columns[i]);
            }
        });

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            String jsonStr = objectMapper.writeValueAsString(values);
            User user = objectMapper.readValue(jsonStr, User.class);
            userRepository.save(user);
            log.info("sync user data to elasticsearch");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void syncAnswerToElasticsearch(WriteRowsEventData data) {

    }

    private void syncQuestionToElasticsearch(WriteRowsEventData data) {

    }

}
