package com.mycompany.spring_server;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Set;

@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver htmlResolver() {
        SpringResourceTemplateResolver htmlResolver = new SpringResourceTemplateResolver();
        htmlResolver.setApplicationContext(this.applicationContext);
        htmlResolver.setPrefix("/WEB-INF/templates/");
        htmlResolver.setSuffix(".html");
        htmlResolver.setTemplateMode(TemplateMode.HTML);
        htmlResolver.setCacheable(false);
        htmlResolver.setCharacterEncoding("UTF-8");
        return htmlResolver;
    }

    @Bean
    public SpringResourceTemplateResolver cssResolver() {
        SpringResourceTemplateResolver cssResolver = new SpringResourceTemplateResolver();
        cssResolver.setApplicationContext(this.applicationContext);
        cssResolver.setPrefix("/WEB-INF/templates/");
        cssResolver.setSuffix(".css");
        cssResolver.setTemplateMode(TemplateMode.CSS);
        cssResolver.setCacheable(false);
        cssResolver.setCharacterEncoding("UTF-8");
        return cssResolver;
    }

    @Bean
    public SpringResourceTemplateResolver jsResolver() {
        SpringResourceTemplateResolver jsResolver = new SpringResourceTemplateResolver();
        jsResolver.setApplicationContext(this.applicationContext);
        jsResolver.setPrefix("/WEB-INF/templates/");
        jsResolver.setSuffix(".js");
        jsResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        jsResolver.setCacheable(false);
        jsResolver.setCharacterEncoding("UTF-8");
        return jsResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolvers(Set.of(htmlResolver(), cssResolver(), jsResolver()));
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        return new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }
}
