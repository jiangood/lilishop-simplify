package cn.lili.modules.member.service;

import cn.lili.common.security.context.UserContext;
import cn.lili.modules.member.mapper.StoreLogisticsMapper;
import cn.lili.modules.member.service.StoreLogisticsService;
import cn.lili.modules.store.entity.dos.StoreLogistics;
import cn.lili.modules.store.entity.dto.StoreLogisticsCustomerDTO;
import cn.lili.modules.system.entity.vo.StoreLogisticsVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 物流公司业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 8:02 下午
 */
@Service
public class StoreLogisticsService extends ServiceImpl<StoreLogisticsMapper, StoreLogistics>  {

    
    public List<StoreLogisticsVO> getStoreLogistics(String storeId) {
        return this.baseMapper.getStoreLogistics(storeId);
    }

    
    public List<StoreLogisticsVO> getStoreSelectedLogistics(String storeId) {
        return this.baseMapper.getSelectedStoreLogistics(storeId);

    }

    
    public List<String> getStoreSelectedLogisticsName(String storeId) {
        return this.baseMapper.getSelectedStoreLogisticsName(storeId);
    }

    
    public List<StoreLogisticsVO> getStoreSelectedLogisticsUseFaceSheet(String storeId) {
        return this.baseMapper.getSelectedStoreLogisticsUseFaceSheet(storeId);
    }

    
    public StoreLogistics update(String logisticsId, String storeId, StoreLogisticsCustomerDTO storeLogisticsCustomerDTO) {
        LambdaQueryWrapper<StoreLogistics> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreLogistics::getLogisticsId, logisticsId);
        lambdaQueryWrapper.eq(StoreLogistics::getStoreId, storeId);
        this.remove(lambdaQueryWrapper);
        StoreLogistics ResultstoreLogistics = new StoreLogistics(storeLogisticsCustomerDTO);
        ResultstoreLogistics.setStoreId(storeId);
        ResultstoreLogistics.setLogisticsId(logisticsId);
        this.save(ResultstoreLogistics);
        return ResultstoreLogistics;
    }

    
    public StoreLogistics getStoreLogisticsInfo(String logisticsId) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return this.getOne(new LambdaQueryWrapper<StoreLogistics>().eq(StoreLogistics::getStoreId, storeId).eq(StoreLogistics::getLogisticsId, logisticsId));
    }

    
    public List<StoreLogisticsVO> getOpenStoreLogistics(String storeId) {
        List<StoreLogisticsVO> openStoreLogistics = this.baseMapper.getOpenStoreLogistics(storeId);
        for (StoreLogisticsVO storeLogisticsVO : openStoreLogistics) {
            storeLogisticsVO.setSelected("1");
        }
        return openStoreLogistics;
    }

    
    public List<StoreLogisticsVO> getCloseStoreLogistics(String storeId) {
        return this.baseMapper.getCloseStroreLogistics(storeId);
    }

    
    public StoreLogistics add(String logisticsId, String storeId, StoreLogisticsCustomerDTO storeLogisticsCustomerDTO) {
        //判断是否已经选择过，如果没有选择则进行添加
        LambdaQueryWrapper<StoreLogistics> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreLogistics::getLogisticsId, logisticsId);
        lambdaQueryWrapper.eq(StoreLogistics::getStoreId, storeId);
        StoreLogistics storeLogistics = null;
        if (this.getOne(lambdaQueryWrapper) == null) {
            storeLogistics = new StoreLogistics(storeLogisticsCustomerDTO);
            storeLogistics.setStoreId(storeId);
            storeLogistics.setLogisticsId(logisticsId);
            this.save(storeLogistics);
            return storeLogistics;
        }
        return null;
    }


}