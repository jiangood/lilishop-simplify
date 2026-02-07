package cn.lili.api;

import cn.lili.api.buyer.security.BuyerAuthenticationFilter;
import cn.lili.api.manager.security.ManagerAuthenticationFilter;
import cn.lili.api.seller.security.StoreAuthenticationFilter;
import cn.lili.cache.Cache;
import cn.lili.common.properties.IgnoredUrlsProperties;
import cn.lili.common.security.CustomAccessDeniedHandler;
import cn.lili.modules.member.service.ClerkService;
import cn.lili.modules.member.service.StoreMenuRoleService;
import cn.lili.modules.member.token.StoreTokenGenerate;
import cn.lili.modules.permission.service.MenuService;
import cn.lili.modules.system.token.ManagerTokenGenerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;



@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class GlobalSecurityConfig {

    /**
     * 忽略验权配置
     */
    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    /**
     * spring security -》 权限不足处理
     */
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

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
    // 新增：用于获取 AuthenticationManager
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 配置不需要授权的URL
        String[] ignoredUrls = ignoredUrlsProperties.getUrls().toArray(new String[0]);
        
        http
            // 配置授权规则
            .authorizeHttpRequests(authz -> authz
                // 配置的url不需要授权
                .requestMatchers(ignoredUrls).permitAll()
                // 任何其他请求都需要身份认证
                .anyRequest().authenticated()
            )
            // 禁止网页iframe
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            // 配置登出
            .logout(LogoutConfigurer::permitAll)
            // 允许跨域
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // 关闭跨站请求防护
            .csrf(AbstractHttpConfigurer::disable)
            // 前后端分离采用JWT，不需要session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 自定义权限拒绝处理类
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint((req, res, ex) ->
                    cn.lili.common.utils.ResponseUtil.output(res, 403,
                        cn.lili.common.utils.ResponseUtil.resultMap(false, 403, "未登录或token失效"))
                )
            )
            // 禁用表单与Basic认证
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            // 添加JWT认证过滤器
            .addFilter(new BuyerAuthenticationFilter(authenticationManager(authenticationConfiguration()), cache))
            .addFilter(new StoreAuthenticationFilter(authenticationManager(authenticationConfiguration()), storeTokenGenerate, storeMenuRoleService, clerkService, cache))
        // 添加 JWT 认证过滤器（保持你原有的构造参数）
       .addFilter(new ManagerAuthenticationFilter(
                authenticationConfiguration.getAuthenticationManager(),
                menuService,
                managerTokenGenerate,
                cache,
                ignoredUrlsProperties
        ));

        return http.build();
    }

    /**
     * 获取AuthenticationManager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    /**
     * 获取AuthenticationConfiguration Bean
     */
    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return this.authenticationConfiguration;
    }
}
