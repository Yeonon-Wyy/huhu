package top.yeonon.huhuqaservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class HuhuQaServiceApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void dumpQuestion() {
        ObjectMapper objectMapper = new ObjectMapper();
        Iterable<Question> questions = questionRepository.findAll();

        try (BufferedWriter writer = Files.newBufferedWriter(constructPath("question.json"))) {
            for (Question question : questions) {
                Index index = new Index(new ID(question.getId().toString()));
                writeLine(writer, objectMapper.writeValueAsString(index));
                writeLine(writer, objectMapper.writeValueAsString(question));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dumpAnswer() {
        ObjectMapper objectMapper = new ObjectMapper();
        Iterable<Answer> answers = answerRepository.findAll();

        try (BufferedWriter writer = Files.newBufferedWriter(constructPath("answer.json"))) {
            for (Answer answer : answers) {
                Index index = new Index(new ID(answer.getId().toString()));
                writeLine(writer, objectMapper.writeValueAsString(index));
                writeLine(writer, objectMapper.writeValueAsString(answer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path constructPath(String filename) {
        filename = "\\..\\mysql-data\\" + filename;
        String rootPath = System.getProperty("user.dir");
        return Paths.get(rootPath + filename);
    }

    private void writeLine(BufferedWriter writer, String jsonStr) {
        try {
            writer.write(jsonStr);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
