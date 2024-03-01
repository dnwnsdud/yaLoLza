//package com.web.project.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//
//@Configuration
//public class SwaggerConfig {
//	
//	@Value("${swagger.config.title}")
//	String title;
//	@Value("${swagger.config.description}")
//	String description;
//	@Value("${swagger.config.version}")
//	String version;
//	@Value("${swagger.config.contact.email}")
//	String contact_email;
//	@Value("${swagger.config.contact.name}")
//	String contact_name;
//	@Value("${swagger.config.contact.url}")
//	String contact_url;
//	@Value("${swagger.config.license.name}")
//	String license_name;
//	@Value("${swagger.config.license.url}")
//	String license_url;
//	
//	@Bean
//	public OpenAPI api() {
//		return new OpenAPI()
//				.components(new Components())
//				.info(new Info()
//						.title(title)
//						.description(description)
//						.version(version)
//						.contact(new Contact()
//								.email(contact_email)
//								.name(contact_name)
//								.url(contact_url)
//						)
//						.license(new License()
//								.name(license_name)
//								.url(license_url)
//						)
//				);
//	}
//}
