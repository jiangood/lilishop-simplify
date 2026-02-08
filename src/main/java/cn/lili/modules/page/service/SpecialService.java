package cn.lili.modules.page.service;

import cn.lili.modules.page.entity.dos.PageData;
import cn.lili.modules.page.entity.dos.Special;
import cn.lili.modules.page.mapper.SpecialMapper;
import cn.lili.modules.page.service.PageDataService;
import cn.lili.modules.page.service.SpecialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 专题活动业务层实现
 *
 * @author Bulbasaur
 * @since 2020/12/7 11:27
 */
@Service
public class SpecialService extends ServiceImpl<SpecialMapper, Special>  {

    /**
     * 页面数据
     */
    @Autowired
    private PageDataService pageDataService;

    
    @Transactional(rollbackFor = Exception.class)
    public Special addSpecial(Special special) {
        //新建页面
        PageData pageData = new PageData();
        pageDataService.save(pageData);

        //设置专题页面
        special.setPageDataId(pageData.getId());
        this.save(special);
        return special;
    }

    
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSpecial(String id) {

        //删除页面内容
        Special special = this.getById(id);
        pageDataService.removeById(special.getPageDataId());

        //删除专题
        return this.removeById(id);
    }
}
