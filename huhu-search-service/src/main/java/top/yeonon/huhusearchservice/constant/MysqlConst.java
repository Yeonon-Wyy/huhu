package top.yeonon.huhusearchservice.constant;

/**
 * @Author yeonon
 * @date 2019/5/1 0001 13:16
 **/
public class MysqlConst {

    public static class QABase {
        public static final String DATABASE = "huhu-qa";

        public static final String QUESTION_TABLE = "question";

        public static final String ANSWER_TABLE = "answer";
    }

    public static class UserBase {
        public static final String DATABASE = "huhu-user";

        public static final String USER_TABLE = "user";
    }

    public static final String SCHEMA_INFO_SQL = "SELECT table_schema,table_name,column_name,ordinal_position " +
            "FROM information_schema.`COLUMNS` " +
            "WHERE table_schema = ? and table_name = ?";
}
