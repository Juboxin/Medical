package com.ujiuye.utils.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author JuBoxin
 * @date 2021/5/20 - 17:11
 */
@Configuration
public class MyWebMvcAutoConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        表示前段访问路径包含/setmeal/时,默认到"D:/fileupload/img/"来查找原素材,路径需要和文件上传路径保持一致
//        注意一定要以/结尾,否则拼接图片名称是,无法与文件夹名分割
        registry.addResourceHandler("/setmeal/**").addResourceLocations("file:D:/fileupload/img/");
    }
}
