package cn.lili.cache.limit.interceptor;

import cn.lili.cache.limit.annotation.LimitPoint;
import cn.lili.cache.limit.enums.LimitTypeEnums;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.IpUtils;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 流量拦截
 *
 * @author Chopper
 */
@Aspect
@Configuration
@Slf4j
public class LimitInterceptor {


    private Limiter limiter = new Limiter(1, TimeUnit.HOURS);

    @Before("@annotation(limitPointAnnotation)")
    public void interceptor(LimitPoint limitPointAnnotation) {
        LimitTypeEnums limitTypeEnums = limitPointAnnotation.limitType();

        String key;
        int limitPeriod = limitPointAnnotation.period();
        int limitCount = limitPointAnnotation.limit();
        if (limitTypeEnums == LimitTypeEnums.CUSTOMER) {
            key = limitPointAnnotation.key();
        } else {
            key = limitPointAnnotation.key() + IpUtils
                    .getIpAddress(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        }
        String cacheKey = StringUtils.join(limitPointAnnotation.prefix(), key);



        try {
            boolean b = limiter.tryAcquire(cacheKey, limitCount, limitPeriod, TimeUnit.SECONDS);

            log.info("限制请求{},缓存key{}", limitCount,  key);
            //如果缓存里没有值，或者他的值小于限制频率
            if(!b){
                throw new ServiceException(ResultCode.LIMIT_ERROR);
            }

        }
        //如果从redis中执行都值判定为空，则这里跳过
        catch (NullPointerException e) {
            return;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("限流异常", e);
            throw new ServiceException(ResultCode.ERROR);
        }
    }

}