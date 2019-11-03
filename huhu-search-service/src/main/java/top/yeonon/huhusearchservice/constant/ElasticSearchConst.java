package top.yeonon.huhusearchservice.constant;

/**
 * @Author yeonon
 * @date 2019/5/5 0005 12:59
 **/
public class ElasticSearchConst {

    public static final String SUGGEST_PROPERTIES_KEY = "suggest";

    public static final Integer SUGGEST_COMPLETION_SIZE = 10;

    public static final Integer SEARCH_RESULT_SIZE = 10;

    public static final String COMPOSITE_SUGGEST_NAME = "composite_suggest";

    public static class Question {
        public static final String INDEX_NAME = "huhu-qa";

        public static final String TYPE_NAME = "question";

        public static final String SUGGEST_INPUT_KEY = "title";

        public static final String SUGGEST_WEIGHT_KEY = "followerCount";

        public static final String SUGGEST_NAME = "question-title-suggest";
    }

    public static class Answer {
        public static final String INDEX_NAME = "huhu-answer";

        public static final String TYPE_NAME = "answer";
    }

    public static class User {
        public static final String INDEX_NAME = "huhu-user";

        public static final String USER_TYPE_NAME = "user";

        public static final String SUGGEST_INPUT_KEY = "username";

        public static final String SUGGEST_WEIGHT_KEY = "followerCount";

        public static final String SUGGEST_NAME = "username-suggest";
    }

}
