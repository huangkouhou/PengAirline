package com.peng.PengAirline.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peng.PengAirline.entities.User;

public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);//根据用户的 email 字段查找用户。

    boolean existsByEmail(String email);

    //查找所有拥有特定角色名的用户列表。
    @Query("SELECT u FROM User u JOIN u.roles r where r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
}
