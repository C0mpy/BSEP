package com.timsedam;

import com.timsedam.models.Permission;
import com.timsedam.models.Role;
import com.timsedam.models.User;
import com.timsedam.repository.PermissionRepository;
import com.timsedam.repository.RoleRepository;
import com.timsedam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {


    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public DataLoader(RoleRepository roleRepository, PermissionRepository permissionRepository,
                      UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Permission loginPermission = new Permission("LOGIN");

        Role operatorRole = new Role("OPERATOR");
        operatorRole.getPermissions().add(loginPermission);
        Role adminRole = new Role("ADMINISTRATOR");

        User admin = new User("admin@admin.com", adminRole);
        admin.setPassword(encoder.encode("admin"));;

        permissionRepository.save(loginPermission);
        roleRepository.save(operatorRole);
        roleRepository.save(adminRole);
        userRepository.save(admin);
    }
}
