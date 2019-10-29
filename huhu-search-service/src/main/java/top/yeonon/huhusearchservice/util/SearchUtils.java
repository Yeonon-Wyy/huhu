package top.yeonon.huhusearchservice.util;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.response.ResponseCode;

/**
 * @Author yeonon
 * @date 2019/5/2 0002 12:03
 **/
public class SearchUtils {

    /**
     * 从JDBC URL中获取HOST
     * @param url jdbc URL
     * @return HOST
     * @throws HuhuException 解析失败抛出异常
     */
    public static String getHostFromJdbcUrl(String url) throws HuhuException {
        String[] str = url.split("/");
        if (str.length < 3) {
            throw new HuhuException(ResponseCode.MYSQL_URL_PARSE__ERROR.getCode(),
                    ResponseCode.MYSQL_URL_PARSE__ERROR.getDescription());
        }
        return str[2].split(":")[0];
    }

    /**
     * 从JDBC URL中获取端口号
     * @param url JDBC URL
     * @return 端口号
     * @throws HuhuException 解析失败抛出的异常
     */
    public static Integer getPortFromJdbcUrl(String url) throws HuhuException {
        String[] str = url.split("/");
        if (str.length < 3) {
            throw new HuhuException(ResponseCode.MYSQL_URL_PARSE__ERROR.getCode(),
                    ResponseCode.MYSQL_URL_PARSE__ERROR.getDescription());
        }
        return Integer.parseInt(str[2].split(":")[1]);
    }
}
