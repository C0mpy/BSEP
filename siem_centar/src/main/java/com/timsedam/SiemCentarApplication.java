package com.timsedam;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SiemCentarApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(SiemCentarApplication.class, args);
	}

	@Bean
	public KieContainer kieContainer() {
		return KieServices.Factory.get().getKieClasspathContainer();
	}

	@Bean
	public KieSession kieSession(@Autowired KieContainer kieContainer) {
		return kieContainer.newKieSession("session");
	}
	
	@Bean
	public KieSession kieSession2(@Autowired KieContainer kieContainer) {
		return kieContainer.newKieSession("session2");
	}

}
