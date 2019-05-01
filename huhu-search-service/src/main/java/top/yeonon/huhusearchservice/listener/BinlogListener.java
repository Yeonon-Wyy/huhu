package top.yeonon.huhusearchservice.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.constant.ErrMessage;
import top.yeonon.huhusearchservice.constant.MysqlConst;
import top.yeonon.huhusearchservice.entity.Answer;
import top.yeonon.huhusearchservice.entity.Question;
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

    private final ObjectMapper objectMapper;

    @Autowired
    public BinlogListener(QuestionRepository questionRepository,
                          AnswerRepository answerRepository,
                          UserRepository userRepository,
                          JdbcTemplate jdbcTemplate) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
                    handleUpdateRowEvent(event);
                    break;
                case EXT_DELETE_ROWS:
                    handleDeleteRowEvent(event);
                default:
                    //ignore
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
        String tableName = tablesById.get(data.getTableId()).getTable();
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                writeQuestionToElasticsearch(data);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                writeAnswerToElasticsearch(data);
            case MysqlConst.UserBase.USER_TABLE:
                writeUserToElasticsearch(data);
            default:
                //ignore
        }
    }


    private void handleUpdateRowEvent(Event event) {
        UpdateRowsEventData data = event.getData();
        String tableName = tablesById.get(data.getTableId()).getTable();
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                updateQuestionToElasticsearch(data);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                updateAnswerToElasticsearch(data);
                break;
            case MysqlConst.UserBase.USER_TABLE:
                updateUserToElasticsearch(data);
                break;
            default:
                //ignore
        }
    }

    private void handleDeleteRowEvent(Event event) {
        DeleteRowsEventData data = event.getData();
        String tableName = tablesById.get(data.getTableId()).getTable();
        switch (tableName) {
            case MysqlConst.QABase.QUESTION_TABLE:
                deleteDataById(data, questionRepository, questionPosToName);
                break;
            case MysqlConst.QABase.ANSWER_TABLE:
                deleteDataById(data, answerRepository, answerPosToName);
                break;
            case MysqlConst.UserBase.USER_TABLE:
                deleteDataById(data, userRepository, userPosToName);
                break;
                default:
                    //ignore
        }
    }


    /***
     * 以下几个方法是处理写事件不同的表数据
     *
     * */

    private void writeUserToElasticsearch(WriteRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                values.put(userPosToName.get(i), row[i]);
            }
            saveUser(values);
            values.clear();
            log.info("sync user data to elasticsearch");

        });
    }

    private void writeAnswerToElasticsearch(WriteRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                values.put(answerPosToName.get(i), row[i]);
            }
            saveAnswer(values);
            values.clear();
            log.info("sync answer data to elasticsearch");

        });
    }

    private void writeQuestionToElasticsearch(WriteRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                values.put(questionPosToName.get(i), row[i]);
            }
            saveQuestion(values);
            values.clear();
            log.info("sync question data to elasticsearch");
        });
    }

    /***
     * 以下几个方法是处理更新事件不同的表数据
     *
     * */

    private void updateUserToElasticsearch(UpdateRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(entry -> {
            for (int i = 0; i < entry.getValue().length; i++) {
                values.put(userPosToName.get(i), entry.getValue()[i]);
            }
            saveUser(values);
            values.clear();
            log.info("sync user data to elasticsearch");

        });
    }

    private void updateQuestionToElasticsearch(UpdateRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(entry -> {
            for (int i = 0; i < entry.getValue().length; i++) {
                values.put(questionPosToName.get(i), entry.getValue()[i]);
            }
            saveQuestion(values);
            values.clear();
            log.info("sync question data to elasticsearch");
        });
    }

    private void updateAnswerToElasticsearch(UpdateRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(entry -> {
            for (int i = 0; i < entry.getValue().length; i++) {
                values.put(answerPosToName.get(i), entry.getValue()[i]);
            }
            saveAnswer(values);
            values.clear();
            log.info("sync answer data to elasticsearch");
        });
    }

    @SuppressWarnings("unchecked")
    private void deleteDataById(DeleteRowsEventData data, ElasticsearchRepository repository, Map<Integer, String> posToName) {
        data.getRows().forEach(row -> {
            String id = "";
            for (int i = 0; i < row.length; i++) {
                if (posToName.get(i).equals("id")) {
                    id = String.valueOf(row[i]);
                }
            }
            repository.deleteById(id);
        });
    }


    /**
     * 将用户数据保存到es
     * @param values 用户数据
     */
    private void saveUser(Map<String, Object> values) {
        try {
            String jsonStr = objectMapper.writeValueAsString(values);
            User user = objectMapper.readValue(jsonStr, User.class);
            userRepository.save(user);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 将回答数据保存到ES
     * @param values 回答数据
     */
    private void saveAnswer(Map<String, Object> values) {
        try {
            String jsonStr = objectMapper.writeValueAsString(values);
            Answer answer = objectMapper.readValue(jsonStr, Answer.class);
            //解码，重新设置content
            byte[] contentBytes = Base64Utils.decodeFromString(answer.getContent());
            answer.setContent(new String(contentBytes));
            answerRepository.save(answer);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 将问题数据保存到ES
     * @param values 问题数据
     */
    private void saveQuestion(Map<String, Object> values) {
        try {
            String jsonStr = objectMapper.writeValueAsString(values);
            Question question = objectMapper.readValue(jsonStr, Question.class);
            //解码，重新设置content
            byte[] contentBytes = Base64Utils.decodeFromString(question.getContent());
            question.setContent(new String(contentBytes));
            questionRepository.save(question);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
