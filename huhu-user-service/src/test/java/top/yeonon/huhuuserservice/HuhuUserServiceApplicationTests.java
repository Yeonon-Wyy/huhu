package top.yeonon.huhuuserservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.yeonon.huhuuserservice.entity.User;
import top.yeonon.huhuuserservice.repository.UserRepository;
import top.yeonon.huhuuserservice.vo.ID;
import top.yeonon.huhuuserservice.vo.Index;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HuhuUserServiceApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void dumpUser() {
        ObjectMapper objectMapper = new ObjectMapper();
        Iterable<User> users = userRepository.findAll();

        try (BufferedWriter writer = Files.newBufferedWriter(constructPath("user.json"))) {
            for (User user : users) {
                Index index = new Index(new ID(user.getId().toString()));
                writeLine(writer, objectMapper.writeValueAsString(index));
                writeLine(writer, objectMapper.writeValueAsString(user));
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
