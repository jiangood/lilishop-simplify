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


    private final DelayedTaskService service;

    public void add(DelayTask task) {
        service.save(task);
    }


    public void delete(String businessKey) {
        service.deleteByBusinessKey(businessKey);
    }




}