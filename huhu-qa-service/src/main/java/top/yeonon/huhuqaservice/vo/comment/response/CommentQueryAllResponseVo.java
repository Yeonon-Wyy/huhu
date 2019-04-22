package top.yeonon.huhuqaservice.vo.comment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/22 0022 11:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentQueryAllResponseVo {


    private List<CommentInfo> commentInfoList;

    private Integer pageNum;

    private Integer pageSize;

    private boolean hasNext;

    private boolean hasPrevious;

    private boolean isFirst;

    private boolean isLast;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfo {
        private Long id;
        private Long userId;
        private Integer status;
        private String content;
        private Integer type;
        private Long approvalCount;
        private Date createTime;
        private Date updateTime;
    }
}
