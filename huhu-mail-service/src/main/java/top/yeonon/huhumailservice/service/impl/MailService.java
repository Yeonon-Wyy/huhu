package top.yeonon.huhumailservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @Author yeonon
 * @date 2019/4/17 0017 20:01
 **/
@Slf4j
@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }


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

        //用线程池去执行发送邮件任务，即同步改异步
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
