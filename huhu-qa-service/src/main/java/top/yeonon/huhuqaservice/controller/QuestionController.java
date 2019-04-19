package top.yeonon.huhuqaservice.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuqaservice.service.IQuestionService;
import top.yeonon.huhuqaservice.vo.request.QuestionCreateRequestVo;
import top.yeonon.huhuqaservice.vo.response.QuestionCreateResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 19:27
 **/
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final IQuestionService questionService;

    @Autowired
    public QuestionController(IQuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public QuestionCreateResponseVo createQuestion(@RequestBody QuestionCreateRequestVo request,
                                                   Authentication authentication) throws HuhuException {
        //获取当前用户ID
        String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        Claims body = CommonUtils.parseJwtToken(token, "huhu-project");
        Long userId = Long.parseLong(String.valueOf(body.get("id")));
        request.setUserId(userId);
        //调用服务
        return questionService.createQuestion(request);
    }
}
