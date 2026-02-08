package cn.lili.framework.delay;

import cn.lili.framework.delay.DelayTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DelayTaskRepository extends JpaRepository<DelayTask, Long> {

    List<DelayTask> findByTimeBefore(Date now, Pageable page);

    void deleteByBusinessKey(String businessKey);
}
