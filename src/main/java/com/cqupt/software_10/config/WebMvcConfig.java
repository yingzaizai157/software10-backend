package com.cqupt.software_10.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件磁盘图片url映射
        // 配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
//        registry.addResourceHandler("/fig/**").addResourceLocations("file:D:/Code/Java/software10/software-software_backend/src/main/resources/alg/fig/");
//        registry.addResourceHandler("/**").addResourceLocations("file:D:/Code/Java/software10/software-software_backend/src/main/resources/Files/");
//        registry.addResourceHandler("/fig/**").addResourceLocations("file:/home/data/WorkSpace/Backend/software10/alg/fig/");
//        registry.addResourceHandler("/**").addResourceLocations("file:/home/data/WorkSpace/Backend/software10/Files/");
        registry.addResourceHandler("/fig/**").addResourceLocations("file:/root/keti3/backend/software10/alg/fig/");
        registry.addResourceHandler("/**").addResourceLocations("file:/root/keti3/backend/software10/Files/");

    }

}
