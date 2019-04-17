package top.yeonon.huhumailservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhumailservice.constants.MailType;
import top.yeonon.huhumailservice.vo.TemplateMessageRequestVo;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 19:35
 **/
public interface IMailService {

    /**
     * 发送简单邮件
     * @param from 发件人
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     *
     */
    void sendSimpleMail(String from, String to, String subject, String content);

    void sendChangePassValidateCodeMail(TemplateMessageRequestVo request,
                                        String templateName) throws MessagingException, HuhuException;
}
