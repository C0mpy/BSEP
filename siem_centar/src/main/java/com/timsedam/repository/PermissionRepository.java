package com.timsedam.repository;

import com.timsedam.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

     Permission getOneByName(String name);

}
