package cn.lili.modules.system.serviceimpl;

import cn.lili.cache.Cache;
import cn.lili.modules.system.entity.dos.SensitiveWords;
import cn.lili.modules.system.mapper.SensitiveWordsMapper;
import cn.lili.modules.system.service.SensitiveWordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感词业务层实现
 *
 * @author Bulbasaur
 * @since 2020/11/17 8:02 下午
 */
@Service
public class SensitiveWordsServiceImpl extends ServiceImpl<SensitiveWordsMapper, SensitiveWords> implements SensitiveWordsService {



    @PostConstruct
    public void init() {
        resetSensitiveWordsFilter();
    }

    @Override
    public void resetSensitiveWordsFilter() {
        List<SensitiveWords> sensitiveWordsList = this.list();

        if (sensitiveWordsList == null || sensitiveWordsList.isEmpty()) {
            return;
        }
        List<String> sensitiveWords = sensitiveWordsList.stream().map(SensitiveWords::getSensitiveWord).collect(Collectors.toList());
        // 实时更新敏感词过滤器
        cn.lili.common.sensitive.SensitiveWordsFilter.init(sensitiveWords);
    }
}