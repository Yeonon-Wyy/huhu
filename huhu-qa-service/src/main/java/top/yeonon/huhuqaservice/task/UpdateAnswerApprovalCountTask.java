package top.yeonon.huhuqaservice.task;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.yeonon.huhuqaservice.constant.Const;
import top.yeonon.huhuqaservice.repository.AnswerRepository;
import top.yeonon.huhuqaservice.service.IAnswerService;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author yeonon
 * @date 2019/4/24 0024 13:42
 **/
@Component
@Slf4j
public class UpdateAnswerApprovalCountTask {

    private final RedissonClient redissonClient;

    private final IAnswerService answerService;

    @Autowired
    public UpdateAnswerApprovalCountTask(RedissonClient redissonClient,
                                         IAnswerService answerService) {
        this.redissonClient = redissonClient;
        this.answerService = answerService;
    }


    @Scheduled(cron = "0 0 */2 * * ? ")
    public void updateAnswerApprovalCount() {
        log.info("定时任务开启");
        RLock lock = redissonClient.getLock(Const.RedisConst.UPDATE_ANSWER_APPROVAL_LOCK);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(3, TimeUnit.SECONDS)) {
                log.info("获得分布式锁，开始更新数据");
                answerService.updateAnswerApprovalCount();
            }
        } catch (InterruptedException e) {
            log.error("获取分布式锁异常");
        } finally {
            if (getLock) {
                lock.unlock();
                log.info("释放分布式锁");
            }
        }
    }
}
