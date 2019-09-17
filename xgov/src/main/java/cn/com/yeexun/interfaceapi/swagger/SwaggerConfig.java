package cn.com.yeexun.interfaceapi.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2  
@EnableWebMvc  
@ComponentScan(basePackages = "cn.com.yeexun.*.controller")
public class SwaggerConfig {
	
	@Bean  
	public Docket api() {  
	return new Docket(DocumentationType.SWAGGER_2)  
		.select()  
		.apis(RequestHandlerSelectors.any())  
		.paths(PathSelectors.any())  
		.build(); 
	}  
	
	// 配置 API 文档标题、描述、作者等等相关信息。
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        	.title("数据管理工具API接口文档")
        	.description("ssm框架搭建 RESTful风格后端服务器")
            .termsOfServiceUrl("xxx")
            .contact(new Contact("yeexun", "xxx", "xxx@yeexun.com.cn"))
            .build();
    }
   
}
