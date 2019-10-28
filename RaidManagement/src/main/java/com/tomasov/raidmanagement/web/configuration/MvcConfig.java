package com.tomasov.raidmanagement.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("redirect:/secure/index");
        registry.addViewController("/secure/index").setViewName("secure/index");
        registry.addViewController("/secure").setViewName("secure/index");
	    //registry.addViewController("/browse").setViewName("browse");
	    //registry.addViewController("/secure/browse").setViewName("secure/browse");
	    //registry.addViewController("/secure/book/{id}").setViewName("secure/book");
	   
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseRegisteredSuffixPatternMatch(false);
    }

}