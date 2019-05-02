package top.yeonon.huhusearchservice.listener.handler;

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
import top.yeonon.huhusearchservice.entity.Answer;
import top.yeonon.huhusearchservice.repository.AnswerRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/5/2 0002 11:39
 **/
@Component
@Slf4j
@Qualifier("answerEventDataHandler")
public class AnswerEventDataHandler implements EventDataHandler {

    private final AnswerRepository answerRepository;

    private final JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper;

    private final Map<Integer, String> answerPosToName;


    @Autowired
    public AnswerEventDataHandler(AnswerRepository answerRepository,
                                  JdbcTemplate jdbcTemplate) {
        this.answerRepository = answerRepository;
        this.jdbcTemplate = jdbcTemplate;
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        answerPosToName = Maps.newConcurrentMap();
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.query(MysqlConst.SCHEMA_INFO_SQL, new Object[]{MysqlConst.QABase.DATABASE, MysqlConst.QABase.ANSWER_TABLE}, rs -> {
            int pos = rs.getInt("ordinal_position");
            String columnName = rs.getString("column_name");
            answerPosToName.put(pos - 1, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
        });
    }


    @Override
    public void handleWriteRowData(WriteRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                values.put(answerPosToName.get(i), row[i]);
            }
            saveAnswer(values);
            values.clear();
        });
        log.info("sync answer data to elasticsearch");
    }

    @Override
    public void handleUpdateRowData(UpdateRowsEventData data) {
        Map<String, Object> values = Maps.newHashMap();
        data.getRows().forEach(entry -> {
            for (int i = 0; i < entry.getValue().length; i++) {
                values.put(answerPosToName.get(i), entry.getValue()[i]);
            }
            saveAnswer(values);
            values.clear();
        });
        log.info("sync answer data to elasticsearch");
    }

    @Override
    public void handleDeleteRowData(DeleteRowsEventData data) {
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                if ("id".equals(answerPosToName.get(i))) {
                    answerRepository.deleteById(String.valueOf(row[i]));
                    return;
                }
            }
        });
        log.info("delete answer data from elasticsearch");
    }

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
}
