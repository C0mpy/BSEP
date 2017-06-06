package com.timsedam;

import com.timsedam.models.Permission;
import com.timsedam.models.Role;
import com.timsedam.repository.PermissionRepository;
import com.timsedam.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {


    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Permission loginPermission = new Permission("LOGIN");
        Role operatorRole = new Role("OPERATOR");

        operatorRole.getPermissions().add(loginPermission);

        permissionRepository.save(loginPermission);
        roleRepository.save(operatorRole);
    }
}
