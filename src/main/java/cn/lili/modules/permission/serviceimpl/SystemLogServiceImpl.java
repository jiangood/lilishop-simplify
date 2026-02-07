package cn.lili.modules.permission.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.SearchVO;
import cn.lili.modules.permission.entity.dos.SystemLog;
import cn.lili.modules.permission.repository.SystemLogRepository;
import cn.lili.modules.permission.service.SystemLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统日志
 *
 * @author Chopper
 * @since 2020/11/17 3:45 下午
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public void saveLog(SystemLog systemLog) {
        systemLogRepository.save(systemLog);
    }

    @Override
    public void deleteLog(List<String> id) {
        for (String s : id) {
            systemLogRepository.deleteById(s);
        }
    }

    @Override
    public void flushAll() {
        systemLogRepository.deleteAll();
    }

    @Override
    public IPage<SystemLog> queryLog(String storeId, String operatorName, String key, SearchVO searchVo, PageVO pageVO) {
        pageVO.setNotConvert(true);
        IPage<SystemLog> iPage = new Page<>();

        // 创建分页参数
        Sort sort;
        if (CharSequenceUtil.isNotEmpty(pageVO.getOrder()) && CharSequenceUtil.isNotEmpty(pageVO.getSort())) {
            sort = Sort.by(Sort.Direction.valueOf(pageVO.getOrder().toUpperCase()), pageVO.getSort());
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createTime");
        }
        Pageable pageable = PageRequest.of(pageVO.getPageNumber() - 1, pageVO.getPageSize(), sort);

        // 转换storeId为Long类型
        Long storeIdLong = null;
        if (CharSequenceUtil.isNotEmpty(storeId)) {
            try {
                storeIdLong = Long.parseLong(storeId);
            } catch (NumberFormatException e) {
                // 忽略转换错误
            }
        }

        // 查询数据
        org.springframework.data.domain.Page<SystemLog> jpaPage = systemLogRepository.queryLog(
                storeIdLong != null ? storeIdLong.toString() : null,
                CharSequenceUtil.isEmpty(operatorName) ? null : operatorName,
                CharSequenceUtil.isEmpty(key) ? null : key,
                searchVo.getConvertStartDate(),
                searchVo.getConvertEndDate(),
                pageable
        );

        // 转换为MyBatis Plus的IPage
        iPage.setTotal(jpaPage.getTotalElements());
        iPage.setRecords(jpaPage.getContent());
        return iPage;
    }

}
