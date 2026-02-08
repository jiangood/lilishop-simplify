package cn.lili.framework.queue;

import cn.lili.framework.queue.MessageQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface MessageQueueRepository extends JpaRepository<MessageQueue, Long> {


    int deleteByStatusAndCreateTimeBefore(Integer status, LocalDateTime createTime);

    Page<MessageQueue> findByStatus(int i, Pageable pageable);
}