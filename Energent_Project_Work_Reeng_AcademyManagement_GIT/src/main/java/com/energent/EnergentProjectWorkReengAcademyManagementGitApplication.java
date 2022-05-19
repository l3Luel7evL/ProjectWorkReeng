package com.energent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class EnergentProjectWorkReengAcademyManagementGitApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergentProjectWorkReengAcademyManagementGitApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				   .apiInfo(apiInfo())
				   .select()
				   .apis(RequestHandlerSelectors.any())
				   .paths(PathSelectors.any())
				   .build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				   .title("Documentation: Academy Management Project Work.")
				   .description("MySite Service Exposed Operations.\nSee Website to watch an implementation of those\n\n<u>Realized by</u>:<ul><b><li>Krist</li><li>Luca</li><li>Matteo</li><li>Simone</li></b></ul>")
				   .version("1.0")
				   .contact(new Contact("Matteo", "https://github.com/l3Luel7evL/ProjectWork", "mtt.bff98@gmail.com"))
				   .build();
	}
}
