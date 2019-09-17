package cn.com.yeexun;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = "cn.com.**.dao")
public class XgovApplication {
	
	public static Environment env;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(XgovApplication.class, args);
		env = applicationContext.getEnvironment();
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
//		ApplicationHome applicationHome = new ApplicationHome();
//		File dir = applicationHome.getDir();
//		System.out.println(dir.getAbsolutePath());
		ApplicationHome h = new ApplicationHome(XgovApplication.class);
        File jarF = h.getSource();
        System.out.println(jarF.getParentFile().toString());
        
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
	
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大 5M
        factory.setMaxFileSize("50MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("200MB");
        return factory.createMultipartConfig();
    }

	
}
