package top.yeonon.huhuqaservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.service.IAnswerService;
import top.yeonon.huhuqaservice.utils.QAUtils;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerBatchQueryRequestVo;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerCreateRequestVo;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerDeleteRequestVo;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerUpdateRequestVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerBatchQueryResponseVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerCreateResponseVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerDeleteResponseVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerUpdateResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 18:01
 **/
@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Value("${huhu.security.jwt.signKey}")
    private String signKey;

    private final IAnswerService answerService;

    @Autowired
    public AnswerController(IAnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/{questionId}")
    public AnswerCreateResponseVo createAnswer(@RequestBody AnswerCreateRequestVo request,
                                               @PathVariable("questionId") Long questionId,
                                               Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setUserId(userId);
        request.setQuestionId(questionId);
        return answerService.createAnswer(request);
    }

    @GetMapping("/{questionId}")
    public AnswerBatchQueryResponseVo batchQueryAnswer(@RequestBody AnswerBatchQueryRequestVo request,
                                                       @PathVariable("questionId") Long questionId,
                                                       @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws HuhuException {
        request.setQuestionId(questionId);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        return answerService.batchQueryAnswer(request);
    }

    @PutMapping("/{id}")
    public AnswerUpdateResponseVo updateAnswer(@RequestBody AnswerUpdateRequestVo request,
                                               @PathVariable("id") Long id,
                                               Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setId(id);
        request.setUserId(userId);
        return answerService.updateAnswer(request);
    }

    @DeleteMapping("/{id}")
    public AnswerDeleteResponseVo deleteAnswer(@RequestBody AnswerDeleteRequestVo request,
                                               @PathVariable("id") Long id,
                                               Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setId(id);
        request.setUserId(userId);
        return answerService.deleteAnswer(request);
    }



}
