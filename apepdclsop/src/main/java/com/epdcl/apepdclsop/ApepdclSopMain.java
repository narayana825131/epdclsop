package com.epdcl.apepdclsop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = "com.epdcl.apepdclsop")
@EnableJpaRepositories
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ApepdclSopMain extends SpringBootServletInitializer  {
	
	public static void main(String[] args) {
		SpringApplication.run(ApepdclSopMain.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApepdclSopMain.class);
	}
}
