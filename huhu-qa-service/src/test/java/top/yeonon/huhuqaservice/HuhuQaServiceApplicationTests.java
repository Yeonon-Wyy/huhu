package top.yeonon.huhuqaservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.yeonon.huhucommon.response.ServerResponse;
import top.yeonon.huhuqaservice.client.HuhuUserClient;
import top.yeonon.huhuqaservice.entity.Answer;
import top.yeonon.huhuqaservice.entity.Question;
import top.yeonon.huhuqaservice.repository.AnswerRepository;
import top.yeonon.huhuqaservice.repository.QuestionRepository;
import top.yeonon.huhuqaservice.vo.ID;
import top.yeonon.huhuqaservice.vo.Index;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HuhuQaServiceApplicationTests {

    @Autowired
    private HuhuUserClient huhuUserClient;

    @Test
    public void testGetBriefInfo() throws IOException {
        ServerResponse response = huhuUserClient.queryBriefUserInfo(5L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String json = objectMapper.writeValueAsString(((LinkedHashMap)response.getData()).get("briefUserInfo"));
        BriefUserInfo userInfo = objectMapper.readValue(json,BriefUserInfo.class);

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BriefUserInfo {
        private String username;
        private Integer status;
        private String avatar;
        private Integer followerCount;
        private Integer followingCount;
        private String profile;
        private Integer degree;

        private Integer answerCount;
        private Integer questionCount;
    }
}
