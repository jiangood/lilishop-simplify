package cn.lili.modules.permission.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.SearchVO;
import cn.lili.modules.permission.entity.vo.SystemLog;
import cn.lili.modules.permission.repository.SystemLogRepository;
import cn.lili.modules.permission.service.SystemLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void saveLog(SystemLog systemLogVO) {
        systemLogRepository.save(systemLogVO);
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
    public Page<SystemLog> queryLog(String storeId, String operatorName, String key, SearchVO searchVo, PageVO pageVO) {
       // TODO
        return null;
    }




}
