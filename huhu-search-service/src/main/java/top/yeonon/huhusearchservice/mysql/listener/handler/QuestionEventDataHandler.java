package top.yeonon.huhusearchservice.mysql.listener.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import top.yeonon.huhusearchservice.constant.MysqlConst;
import top.yeonon.huhusearchservice.entity.Question;
import top.yeonon.huhusearchservice.repository.QuestionRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/5/2 0002 11:06
 **/
@Component
@Slf4j
@Qualifier("questionEventDataHandler")
public class QuestionEventDataHandler implements EventDataHandler {


    private final QuestionRepository questionRepository;

    private final JdbcTemplate jdbcTemplate;

    private final Map<Integer, String> questionPosToName;

    private final ObjectMapper objectMapper;

    @Autowired
    public QuestionEventDataHandler(QuestionRepository questionRepository, JdbcTemplate jdbcTemplate) {
        this.questionRepository = questionRepository;
        this.jdbcTemplate = jdbcTemplate;
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        questionPosToName = Maps.newConcurrentMap();
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.query(MysqlConst.SCHEMA_INFO_SQL, new Object[]{MysqlConst.QABase.DATABASE, MysqlConst.QABase.QUESTION_TABLE}, rs -> {
            int pos = rs.getInt("ordinal_position");
            String columnName = rs.getString("column_name");
            questionPosToName.put(pos - 1, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
        });

    }


    @Override
    public void handleWriteRowData(WriteRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                values.put(questionPosToName.get(i), row[i]);
            }
            saveQuestion(values);
            values.clear();
        });
        log.info("sync question data to elasticsearch");
    }

    @Override
    public void handleUpdateRowData(UpdateRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(entry -> {
            for (int i = 0; i < entry.getValue().length; i++) {
                values.put(questionPosToName.get(i), entry.getValue()[i]);
            }
            saveQuestion(values);
            values.clear();
        });
        log.info("sync question data to elasticsearch");

    }

    @Override
    public void handleDeleteRowData(DeleteRowsEventData data) {
        data.getRows().forEach(row -> {
            String id = "";
            for (int i = 0; i < row.length; i++) {
                if ("id".equals(questionPosToName.get(i))) {
                    id = String.valueOf(row[i]);
                    questionRepository.deleteById(id);
                    return;
                }
            }
        });
        log.info("delete question data from elasticsearch");

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
