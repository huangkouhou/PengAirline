package com.peng.PengAirline.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peng.PengAirline.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{

    Optional<Role> findByName(String name);

}
