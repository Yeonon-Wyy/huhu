package top.yeonon.huhucommon.request;

public interface RequestVo {

    default boolean validate() {
        return true;
    }
}
