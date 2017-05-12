package com.nearbuytools.service.xmpp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

	@Value("${swagger.host}")
	private String swaggerHost;

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
				.host(swaggerHost)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/api/.*"))
				.build()
				.apiInfo(apiInfo());
	}

	private springfox.documentation.service.ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Nearbuy XMPP Service APIs")
				.description("All the APIs related to XMPP service are listed here.")
				.contact(new Contact("adminengineering", "https://www.nearbuy.com", "admin.engineering@nearbuy.com"))
				.version("1.0.0")
				.build();

	}

}