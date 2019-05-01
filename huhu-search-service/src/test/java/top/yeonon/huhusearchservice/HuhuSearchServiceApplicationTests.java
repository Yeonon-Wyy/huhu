package top.yeonon.huhusearchservice;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import top.yeonon.huhusearchservice.entity.Question;
import top.yeonon.huhusearchservice.repository.QuestionRepository;
import java.io.*;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class HuhuSearchServiceApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    private QuestionRepository questionRepository;

    Map<Long, TableMapEventData> tablesById = Maps.newHashMap();

    @Test
    public void testBinlogListener() {
        BinaryLogClient client = new BinaryLogClient(
                "localhost",
                3306,
                "root",
                "124563"
        );

        client.registerEventListener(event -> {
            EventType eventType = event.getHeader().getEventType();

            switch (eventType) {
                case TABLE_MAP:
                    TableMapEventData tableMapEventData = event.getData();
                    tablesById.put(tableMapEventData.getTableId(), tableMapEventData);
                    break;
                case EXT_WRITE_ROWS:
                    WriteRowsEventData data = event.getData();
                    System.out.println(data.toString());
                    break;
                case EXT_UPDATE_ROWS:
                    System.out.println("update row");
                    break;
                    default:
                        //ignore
            }


        });


        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private static final String SQL_SCHEMA = "SELECT table_schema,table_name,column_name,ordinal_position " +
            "FROM information_schema.`COLUMNS` " +
            "WHERE table_schema = ? and table_name = ?";

    private static Map<Integer, String> posToName = Maps.newHashMap();

    private static Map<String, Object> values = Maps.newHashMap();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void processUpdate(UpdateRowsEventData updateData) {

        jdbcTemplate.query(SQL_SCHEMA, new Object[]{"huhu-qa", "question"}, rs -> {
            int pos = rs.getInt("ordinal_position");
            String columnName = rs.getString("column_name");
            posToName.put(pos - 1, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
        });

        updateData.getRows().forEach(entry -> {
            for (int i = 0; i < entry.getValue().length; i++) {
                values.put(posToName.get(i), entry.getValue()[i]);
            }
        });

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String res = objectMapper.writeValueAsString(values);
//            System.out.println(res);

            Question question = objectMapper.readValue(res, Question.class);
            byte[] contentBytes = Base64Utils.decodeFromString(question.getContent());
            question.setContent(new String(contentBytes));

            questionRepository.save(question);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
