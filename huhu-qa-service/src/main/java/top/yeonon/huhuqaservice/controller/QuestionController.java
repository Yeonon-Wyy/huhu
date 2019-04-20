package top.yeonon.huhuqaservice.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuqaservice.interceptor.annatation.CheckUserId;
import top.yeonon.huhuqaservice.service.IQuestionService;
import top.yeonon.huhuqaservice.utils.QAUtils;
import top.yeonon.huhuqaservice.vo.question.request.*;
import top.yeonon.huhuqaservice.vo.question.response.*;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 19:27
 **/
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final IQuestionService questionService;

    @Value("${huhu.security.jwt.signKey}")
    private String signKey;

    @Autowired
    public QuestionController(IQuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public QuestionCreateResponseVo createQuestion(@RequestBody QuestionCreateRequestVo request,
                                                   Authentication authentication) throws HuhuException {
        //获取当前用户ID
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setUserId(userId);
        //调用服务
        return questionService.createQuestion(request);
    }

    @GetMapping("{id}")
    public QuestionQueryResponseVo queryQuestion(@PathVariable("id") Long id) throws HuhuException {
        QuestionQueryRequestVo request = new QuestionQueryRequestVo(id);
        return questionService.queryQuestion(request);
    }

    @GetMapping
    public QuestionQueryAllResponseVo queryAllQuestion(@RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QuestionQueryAllRequestVo request = new QuestionQueryAllRequestVo(pageNum, pageSize);
        return questionService.queryAll(request);
    }

    @PutMapping("{id}")
    public QuestionUpdateResponseVo updateQuestion(@PathVariable("id") Long id,
                                                   @RequestBody QuestionUpdateRequestVo request,
                                                   Authentication authentication) throws HuhuException {
        request.setId(id);
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setUserId(userId);
        return questionService.updateQuestion(request);
    }

    @DeleteMapping("{id}")
    public QuestionDeleteResponseVo deleteQuestion(@PathVariable("id") Long id,
                                                   Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        QuestionDeleteRequestVo request = new QuestionDeleteRequestVo(
                id,
                userId
        );

        return questionService.deleteQuestion(request);
    }
}
