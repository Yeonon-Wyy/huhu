package top.yeonon.huhusearchservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.service.ISearchService;
import top.yeonon.huhusearchservice.vo.SearchQuestionRequestVo;
import top.yeonon.huhusearchservice.vo.SearchQuestionResponseVo;

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
    public SearchQuestionResponseVo searchQuestion(@RequestBody SearchQuestionRequestVo request) throws HuhuException {
        return searchService.searchQuestion(request);
    }
}
