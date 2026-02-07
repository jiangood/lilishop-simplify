package cn.lili.consumer.timetask;

import cn.lili.consumer.timetask.handler.EveryDayExecute;
import cn.lili.consumer.timetask.handler.EveryHourExecute;
import cn.lili.consumer.timetask.handler.EveryMinuteExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时器任务
 *
 * @author Chopper
 * @version v1.0
 * 2020-12-24 11:51
 */
@Slf4j
@Component
public class TimedTaskJobHandler {

    @Autowired
    private List<EveryMinuteExecute> everyMinuteExecutes;


    @Autowired
    private List<EveryHourExecute> everyHourExecutes;


    @Autowired
    private List<EveryDayExecute> everyDayExecutes;

    /**
     * 每分钟任务
     *
     * @throws Exception
     */
    @Scheduled(fixedDelay = 60000) // 使用 fixedDelay，以免任务并发
    public void everyMinuteExecute()  {
        log.info("每分钟任务执行");
        if (everyMinuteExecutes == null || everyMinuteExecutes.size() == 0) {
            return;
        }

        for (int i = 0; i < everyMinuteExecutes.size(); i++) {
            try {
                everyMinuteExecutes.get(i).execute();
            } catch (Exception e) {
                log.error("每分钟任务异常", e);
            }
        }
    }

    /**
     * 每小时任务
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void everyHourExecuteJobHandler() {
        log.info("每小时任务执行");
        if (everyHourExecutes == null || everyHourExecutes.size() == 0) {
            return;
        }

        for (int i = 0; i < everyHourExecutes.size(); i++) {
            try {
                everyHourExecutes.get(i).execute();
            } catch (Exception e) {
                log.error("每小时任务异常", e);
            }
        }
    }

    /**
     * 每日任务
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void everyDayExecuteJobHandler() {

        log.info("每日任务执行");
        if (everyDayExecutes == null || everyDayExecutes.size() == 0) {
            return;
        }

        for (int i = 0; i < everyDayExecutes.size(); i++) {
            try {
                everyDayExecutes.get(i).execute();
            } catch (Exception e) {
                log.error("每日任务异常", e);
            }
        }
    }


}
