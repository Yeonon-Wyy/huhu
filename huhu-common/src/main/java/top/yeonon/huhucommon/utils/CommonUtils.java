package top.yeonon.huhucommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.Base64Codec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.huhucommon.exception.HuhuException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:42
 **/
@Slf4j
public class CommonUtils {


    public static String md5(String originStr) {
        return DigestUtils.md5DigestAsHex(originStr.getBytes());
    }


    /**
     * 随机生成简单的x位验证码
     * @param x 验证码位数
     * @return 随机验证码
     */
    public static String generateValidateCode(int x) {
        final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        //获取随机chars下标
        int index;
        for (int i = 0; i < x; i++) {
            //获取随机chars下标
            index = random.nextInt(chars.length);
            buffer.append(chars[index]);
        }
        return buffer.toString();
    }

    /**
     * 解析jwtToken，获取携带的信息
     * @param jwtToken jwtToken
     * @param sign 签名（未编码）
     * @return 携带的负载信息
     */
    public static Claims parseJwtToken(String jwtToken, String sign) {
        return Jwts.parser()
                .setSigningKey(Base64Codec.BASE64.encode(sign))
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * 检查id不为空且大于0（即合法ID）
     * @param ids id数组
     * @return 有任何一个不满足条件则返回false，否则返回true
     */
    public static boolean checkId(Long ...ids) {
        return Arrays.stream(ids)
                .allMatch(id -> id != null && id > 0);
    }


    public static String uploadFile(String ftpHost, Integer ftpPort,
                                  String ftpUser, String ftpPassword,
                                  String ftpPath, MultipartFile file) throws HuhuException {
        FTPClient ftpClient = getFtpClient(ftpHost, ftpPort, ftpUser, ftpPassword);
        try {

            ftpClient.changeWorkingDirectory(ftpPath);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                log.error("上传文件失败");
                throw new HuhuException("上传文件失败");
            }

            //包含"."
            String fileExtend = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtend;
            InputStream in = file.getInputStream();
            boolean res = ftpClient.storeFile(newFilename, in);
            if (!res) {
                log.error("上传文件失败");
                throw new HuhuException("上传文件失败");
            }
            in.close();
            return ftpPath + newFilename;
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    private static FTPClient getFtpClient(String ftpHost, Integer ftpPort,
                                         String ftpUsername, String ftpPassword) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpHost, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("连接FTP服务器成功");
        return ftpClient;
    }

}
