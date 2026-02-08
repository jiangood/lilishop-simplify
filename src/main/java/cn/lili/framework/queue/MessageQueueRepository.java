package cn.lili.framework.queue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageQueueRepository extends JpaRepository<MessageQueue, Long> {


    int deleteByStatusAndCreateTimeBefore(Integer status, LocalDateTime createTime);

    Page<MessageQueue> findByStatus(int i, Pageable pageable);
}