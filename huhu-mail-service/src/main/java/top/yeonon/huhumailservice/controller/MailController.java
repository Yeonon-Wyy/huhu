package top.yeonon.huhumailservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhumailservice.constants.MailType;
import top.yeonon.huhumailservice.service.IMailService;
import top.yeonon.huhumailservice.vo.TemplateMessageRequestVo;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 20:23
 **/
@RestController
@RequestMapping("/mail")
public class MailController {


    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private IMailService mailService;

    @PostMapping("/send/forget_password")
    public void forgetPassword(@RequestBody TemplateMessageRequestVo request) throws MessagingException, HuhuException {
        request.setFrom(from);
        mailService.sendChangePassValidateCodeMail(request, MailType.FORGET_PASSWORD.getTemplateName());
    }
}
