package com.timsedam;

import com.timsedam.models.Permission;
import com.timsedam.models.Role;
import com.timsedam.repository.PermissionRepository;
import com.timsedam.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SiemCentarApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SiemCentarApplication.class, args);
	}

}
