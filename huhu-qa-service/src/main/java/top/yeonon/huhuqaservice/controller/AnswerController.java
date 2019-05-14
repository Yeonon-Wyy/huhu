package top.yeonon.huhuqaservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.service.IAnswerService;
import top.yeonon.huhuqaservice.utils.QAUtils;
import top.yeonon.huhuqaservice.vo.answer.request.*;
import top.yeonon.huhuqaservice.vo.answer.response.*;

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

    //查询该questionId下的所有回答
    @GetMapping("/{questionId}")
    public AnswerBatchQueryResponseVo batchQueryAnswer(AnswerBatchQueryRequestVo request,
                                                       @PathVariable("questionId") Long questionId,
                                                       @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws HuhuException {
        request.setQuestionId(questionId);
        request.setPageSize(pageSize);
        request.setPageNum(pageNum);
        return answerService.batchQueryAnswer(request);
    }

    //查询某个用户的所有回答
    @GetMapping
    public AnswerBatchQueryByUserIdResponseVo batchQueryByUserId(AnswerBatchQueryByUserIdRequestVo request,
                                                                 @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws HuhuException {
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        return answerService.queryAnswerByUserId(request);
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

    @PostMapping("/{id}/approval")
    public AnswerApprovalResponseVo approvalAnswer(@PathVariable("id") Long id,
                               Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        AnswerApprovalRequestVo request = new AnswerApprovalRequestVo(
                id,
                userId
        );

        return answerService.approvalAnswer(request);
    }

}
