package cn.lili.common.message.queue.service;

import cn.lili.common.message.queue.entity.MessageQueue;
import cn.lili.modules.page.entity.dos.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface MessageQueueRepository extends JpaRepository<MessageQueue, Long> {


    Page<MessageQueue> findByTopicAndStatus(String topic, Integer status, Pageable pageable);


    int deleteByStatusAndCreateTimeBefore(Integer status, LocalDateTime createTime);

    Page<MessageQueue> findByStatus(int i, Pageable pageable);
}