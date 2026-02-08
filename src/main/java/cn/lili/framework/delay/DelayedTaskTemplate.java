package cn.lili.framework.delay;

import cn.hutool.extra.spring.SpringUtil;
import cn.lili.framework.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class DelayedTaskTemplate {


    private final DelayTaskRepository repo;

    public void add(DelayTask task) {
        repo.save(task);
    }


    public void delete(String businessKey) {
        repo.deleteByBusinessKey(businessKey);
    }

    @Scheduled(fixedDelay = 1000)
    public void loopExecute() {
        Date now = new Date();
        List<DelayTask> list = repo.findByTimeBefore(now, PageRequest.of(0, 100, Sort.by("time")));
        if (list.isEmpty()) {
            return;
        }

        for (DelayTask task : list) {
            String beanId = task.getBeanId();
            Task taskBean = SpringUtil.getBean(beanId);
            try {
                taskBean.execute(task.getParams());
                repo.deleteById(task.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}