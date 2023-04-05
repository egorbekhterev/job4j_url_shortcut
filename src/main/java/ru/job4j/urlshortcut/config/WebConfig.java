package ru.job4j.urlshortcut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.job4j.urlshortcut.resolver.JsonArgumentResolver;

import java.util.List;

/**
 * @author: Egor Bekhterev
 * @date: 05.04.2023
 * @project: job4j_url_shortcut
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        JsonArgumentResolver jsonArgumentResolver = new JsonArgumentResolver();
        argumentResolvers.add(jsonArgumentResolver);
    }
}
