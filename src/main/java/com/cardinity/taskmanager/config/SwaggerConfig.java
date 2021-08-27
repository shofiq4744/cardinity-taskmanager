package com.cardinity.taskmanager.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Parameter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		ParameterBuilder aParameterBuilder = new ParameterBuilder();        
        aParameterBuilder.name("Authorization").modelRef(new ModelRef("string")).parameterType("header").required(true).defaultValue("Bearer ").build();        
        List<Parameter> aParameters = new ArrayList<Parameter>();
        
        aParameters.add(aParameterBuilder.build());
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cardinity.taskmanager")).paths(PathSelectors.regex("/.*"))
				.build()
				.apiInfo(apiEndPointsInfo())
				.globalOperationParameters(aParameters);
	}

	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Cardinity REST API").description("Task Manager").version("1.0.0").build();
	}
}