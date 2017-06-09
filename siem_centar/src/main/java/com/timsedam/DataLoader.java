package com.timsedam;

import com.timsedam.models.Permission;
import com.timsedam.models.Role;
import com.timsedam.models.User;
import com.timsedam.repository.PermissionRepository;
import com.timsedam.repository.RoleRepository;
import com.timsedam.repository.UserRepository;
import org.kie.api.runtime.KieSession;
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

    private KieSession kieSession;

    @Autowired
    public DataLoader(RoleRepository roleRepository, PermissionRepository permissionRepository,
                      UserRepository userRepository, KieSession kieSession) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.kieSession = kieSession;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Permission p = permissionRepository.getOneByName("SEND_LOG");
        if(p == null) {
            Permission sendLogPermission = new Permission("SEND_LOG");
            Role operatorRole = new Role("OPERATOR");
            Role adminRole = new Role("ADMINISTRATOR");
            Role agentRole = new Role("AGENT");

            agentRole.getPermissions().add(sendLogPermission);

            User admin = new User("admin@admin.com", adminRole);
            User agent = new User("agent", agentRole);

            admin.setPassword(encoder.encode("admin"));
            agent.setPassword(encoder.encode("agent"));

            permissionRepository.save(sendLogPermission);

            roleRepository.save(operatorRole);
            roleRepository.save(adminRole);
            roleRepository.save(agentRole);

            userRepository.save(admin);
            userRepository.save(agent);

        }
        // kieSession.insert(admin);
        // kieSession.fireAllRules();

    }
}
