package com.peng.PengAirline.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.entities.User;
import com.peng.PengAirline.exceptions.NotFoundException;
import com.peng.PengAirline.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service//告诉 Spring 这是一个服务类，会自动注册进容器。
@RequiredArgsConstructor//Lombok 注解，会自动生成带 final 字段的构造函数。
public class CustomUserDetailsService implements UserDetailsService{

    //表示依赖注入了你的用户数据访问层（Repository）。
    private final UserRepo userRepo;//final 在 Java 中是 一旦被赋值，就不能被修改

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepo.findByEmail(username)
                .orElseThrow(()-> new NotFoundException("User Not Found"));

        return AuthUser.builder()//返回用户详情对象
                .user(user)
                .build();
    }

}
