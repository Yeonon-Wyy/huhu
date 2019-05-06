package top.yeonon.huhusearchservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.constant.ElasticSearchConst;
import top.yeonon.huhusearchservice.service.ISearchService;
import top.yeonon.huhusearchservice.vo.request.AutoCompletionRequestVo;
import top.yeonon.huhusearchservice.vo.request.GeneralSearchRequestVo;
import top.yeonon.huhusearchservice.vo.response.AutoCompletionResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchAnswerResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchQuestionResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchUserResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 17:22
 **/
@RestController
@RequestMapping("search")
public class SearchController {

    private final ISearchService searchService;

    @Autowired
    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("question")
    public SearchQuestionResponseVo searchQuestion(@RequestBody GeneralSearchRequestVo request) throws HuhuException {
        return searchService.searchQuestion(request);
    }

    @GetMapping("answer")
    public SearchAnswerResponseVo searchAnswer(@RequestBody GeneralSearchRequestVo request) throws HuhuException {
        return searchService.searchAnswer(request);
    }

    @GetMapping("user")
    public SearchUserResponseVo searchUser(@RequestBody GeneralSearchRequestVo request) throws HuhuException {
        return searchService.searchUser(request);
    }

    @GetMapping("question/autoCompletion")
    public AutoCompletionResponseVo questionAutoCompletion(@RequestBody AutoCompletionRequestVo request) throws HuhuException {
        request.setSuggestName(ElasticSearchConst.QA.SUGGEST_NAME);
        request.setIndices(new String[]{
                ElasticSearchConst.QA.INDEX_NAME
        });
        return searchService.autoCompletion(request);
    }

    @GetMapping("user/autoCompletion")
    public AutoCompletionResponseVo userAutoCompletion(@RequestBody AutoCompletionRequestVo request) throws HuhuException {
        request.setSuggestName(ElasticSearchConst.User.SUGGEST_NAME);
        request.setIndices(new String[]{
                ElasticSearchConst.User.INDEX_NAME
        });
        return searchService.autoCompletion(request);
    }

    @GetMapping("composite/autoCompletion")
    public AutoCompletionResponseVo compositeAutoCompletion(@RequestBody AutoCompletionRequestVo request) throws HuhuException {
        request.setSuggestName(ElasticSearchConst.COMPOSITE_SUGGEST_NAME);
        request.setIndices(new String[]{
                ElasticSearchConst.User.INDEX_NAME,
                ElasticSearchConst.QA.INDEX_NAME
        });
        return searchService.autoCompletion(request);
    }


}
