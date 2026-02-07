package cn.lili.modules.permission.repository;

import cn.lili.modules.permission.entity.dos.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * 系统日志
 *
 * @author paulG
 * @since 2021/12/13
 **/
public interface SystemLogRepository extends JpaRepository<SystemLog, String> {

    /**
     * 查询系统日志
     *
     * @param storeId      店铺ID
     * @param operatorName 操作人名称
     * @param key          关键词
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @param pageable     分页参数
     * @return 系统日志列表
     */
    @Query(value = "SELECT s FROM SystemLog s WHERE " +
            "(:storeId IS NULL OR s.storeId = :storeId) AND " +
            "(:operatorName IS NULL OR s.username LIKE %:operatorName%) AND " +
            "(:key IS NULL OR s.requestUrl LIKE %:key% OR s.requestParam LIKE %:key% OR " +
            "s.responseBody LIKE %:key% OR s.name LIKE %:key% OR s.customerLog LIKE %:key% OR " +
            "s.ipInfo LIKE %:key%) AND " +
            "(:startDate IS NULL OR s.createTime >= :startDate) AND " +
            "(:endDate IS NULL OR s.createTime <= :endDate)")
    Page<SystemLog> queryLog(String storeId, String operatorName, String key, Date startDate, Date endDate, Pageable pageable);

}
