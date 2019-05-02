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
import top.yeonon.huhusearchservice.constant.MysqlConst;
import top.yeonon.huhusearchservice.entity.User;
import top.yeonon.huhusearchservice.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/5/2 0002 11:46
 **/
@Component
@Slf4j
@Qualifier("userEventDataHandler")
public class UserEventDataHandler implements EventDataHandler {

    private final UserRepository userRepository;

    private final JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper;

    private final Map<Integer, String> userPosToName;

    @PostConstruct
    public void init() {
        jdbcTemplate.query(MysqlConst.SCHEMA_INFO_SQL, new Object[]{MysqlConst.UserBase.DATABASE, MysqlConst.UserBase.USER_TABLE}, rs -> {
            int pos = rs.getInt("ordinal_position");
            String columnName = rs.getString("column_name");
            userPosToName.put(pos - 1, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
        });
    }

    @Autowired
    public UserEventDataHandler(UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        userPosToName = Maps.newConcurrentMap();
    }


    @Override
    public void handleWriteRowData(WriteRowsEventData data) {
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

    @Override
    public void handleUpdateRowData(UpdateRowsEventData data) {
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

    @Override
    public void handleDeleteRowData(DeleteRowsEventData data) {
        data.getRows().forEach(row -> {
            for (int i = 0; i < row.length; i++) {
                if (userPosToName.get(i).equals("id")) {
                    String id = String.valueOf(row[i]);
                    userRepository.deleteById(id);
                    return;
                }
            }
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
}
