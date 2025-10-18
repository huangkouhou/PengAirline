package com.peng.PengAirline.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peng.PengAirline.entities.User;

public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r where r.name = :roleName")
    List<User> findByName(@Param("roleName") String roleName);
    
}
