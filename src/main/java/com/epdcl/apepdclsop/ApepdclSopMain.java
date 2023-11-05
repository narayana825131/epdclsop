package com.epdcl.apepdclsop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = "com.epdcl.apepdclsop")
@EnableJpaRepositories
@EnableAutoConfiguration
public class ApepdclSopMain {
	public static void main(String[] args) {
		SpringApplication.run(ApepdclSopMain.class, args);
	}
}
