package top.yeonon.huhusearchservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @Author yeonon
 * @date 2019/4/29 0029 18:57
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "bank", type = "account",refreshInterval = "-1")
public class Account {

    @Id
    private String id;

    //使用@JsonProperty手动给出mapping,spring-data-elasticsearch并不做这层转换
    @JsonProperty("account_number")
    private Long accountNumber;

    private Long balance;

    private String firstname;

    private String lastname;

    private Integer age;

    private String gender;

    private String address;

    private String employer;

    private String email;

    private String city;

    private String state;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", employer='" + employer + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
