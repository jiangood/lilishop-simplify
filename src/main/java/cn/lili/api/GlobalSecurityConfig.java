package cn.lili.api;

import cn.lili.api.buyer.security.BuyerAuthenticationFilter;
import cn.lili.api.manager.security.ManagerAuthenticationFilter;
import cn.lili.api.seller.security.StoreAuthenticationFilter;
import cn.lili.cache.Cache;
import cn.lili.common.properties.IgnoredUrlsProperties;
import cn.lili.modules.member.service.ClerkService;
import cn.lili.modules.member.service.StoreMenuRoleService;
import cn.lili.modules.member.token.StoreTokenGenerate;
import cn.lili.modules.permission.service.MenuService;
import cn.lili.modules.system.token.ManagerTokenGenerate;
import io.github.jiangood.openadmin.framework.config.init.OpenLifecycle;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.web.cors.CorsConfigurationSource;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class GlobalSecurityConfig implements OpenLifecycle {

    /**
     * 忽略验权配置
     */
    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;



    @Autowired
    private Cache<String> cache;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;


    @Autowired
    private StoreTokenGenerate storeTokenGenerate;

    @Autowired
    private StoreMenuRoleService storeMenuRoleService;

    @Autowired
    private ClerkService clerkService;


    @Autowired
    public MenuService menuService;

    @Autowired
    private ManagerTokenGenerate managerTokenGenerate;

    @Override
    public void onConfigSecurityAuthorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        String[] ignoredUrls = ignoredUrlsProperties.getUrls().toArray(new String[0]);

        authz.requestMatchers(ignoredUrls).permitAll();
    }

    @Override
    public void onConfigSecurity(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        // 禁止网页iframe
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        // 配置登出
        http.logout(LogoutConfigurer::permitAll);

        // 允许跨域
        http.cors(cors -> cors.configurationSource(corsConfigurationSource));
        // 关闭跨站请求防护
        http.csrf(AbstractHttpConfigurer::disable);


        // 添加JWT认证过滤器
        http.addFilter(new BuyerAuthenticationFilter(authenticationManager, cache));
        http.addFilter(new StoreAuthenticationFilter(authenticationManager, storeTokenGenerate, storeMenuRoleService, clerkService, cache));

        http.addFilter(new ManagerAuthenticationFilter(authenticationManager, menuService, managerTokenGenerate, cache));
    }





}
