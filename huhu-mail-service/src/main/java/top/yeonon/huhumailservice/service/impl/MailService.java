package top.yeonon.huhumailservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhumailservice.constants.ErrMessage;
import top.yeonon.huhumailservice.service.IMailService;
import top.yeonon.huhumailservice.vo.TemplateMessageRequestVo;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 20:01
 **/
@Slf4j
@Service
public class MailService implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Override
    public void sendSimpleMail(String from, String to, String subject, String content) {
        threadPoolTaskExecutor.execute(() -> {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(content);
            mailSender.send(mailMessage);
        });
    }

    @Override
    public void sendChangePassValidateCodeMail(TemplateMessageRequestVo request, String templateName) throws HuhuException {


        if (!request.validate()) {
            throw new HuhuException(ErrMessage.INVALID_FORMATE);
        }

        threadPoolTaskExecutor.execute(() -> {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setFrom(request.getFrom());
                helper.setTo(request.getTo());
                helper.setSubject(request.getSubject());
                Context context = new Context();
                context.setVariables(request.getContent());
                String text = templateEngine.process(templateName, context);
                helper.setText(text, true);

                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                log.error(e.getMessage());
            }
        });

    }
}
