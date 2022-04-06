package com.fyr.activiti.learning.security;

import com.fyr.activiti.learning.utils.JsonUtil;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomizedTokenFilter extends OncePerRequestFilter {
    // private StringRedisTemplate stringRedisTemplate;
    private LoadingCache loadingCache;

    @Autowired
    public CustomizedTokenFilter(LoadingCache loadingCache) {
        this.loadingCache = loadingCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        String key = "TOKEN:" + token;
        if (StringUtils.hasLength(token)) {
            // String value = stringRedisTemplate.opsForValue().get(key);
            try {
                String value = loadingCache.get(key).toString();
                if (StringUtils.hasLength(value)) {
                    CustomizedUserDetails customizedUserDetails = JsonUtil.string2Obj(value, CustomizedUserDetails.class);
                    if (null != customizedUserDetails && null == SecurityContextHolder.getContext().getAuthentication()) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customizedUserDetails, null, customizedUserDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        //  刷新token
                        //  如果生存时间小于10分钟，则再续1小时
                        // long time = stringRedisTemplate.getExpire(key);
                        // if (time < 600) {
                        //     stringRedisTemplate.expire(key, (time + 3600), TimeUnit.SECONDS);
                        // }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}
