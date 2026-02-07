package cn.lili.modules.permission.repository;

import cn.lili.modules.permission.entity.vo.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 日志
 *
 * @author paulG
 * @since 2021/12/13
 **/
public interface SystemLogRepository extends JpaRepository<SystemLog, String> {

}
