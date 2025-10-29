package com.peng.PengAirline.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.dtos.UserDTO;
import com.peng.PengAirline.entities.User;
import com.peng.PengAirline.exceptions.NotFoundException;
import com.peng.PengAirline.repo.UserRepo;
import com.peng.PengAirline.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User currentUser() {
        //用 Spring Security 获取当前登录用户的 email。
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional//让一组数据库操作在同一个事务（Transaction）中执行，要么全部成功，要么全部失败。
    public Response<?> updateMyAccount(UserDTO userDTO) {
        log.info("Inside updateMyAccount()");

        User user = currentUser();

        if (userDTO.getName() != null && !userDTO.getName().isBlank()){
            user.setName(userDTO.getName());
        }

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isBlank()){
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
    
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()){
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encodedPassword);
        }

        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account Updated Successfully")
                .build();

    }

    @Override
    public Response<List<UserDTO>> getAllPilots() {
        log.info("Inside getAllPilots");

        //用 ModelMapper 把 User 实体转换成 UserDTO，只返回必要信息（防止敏感数据暴露）。
        List<UserDTO> pilots = userRepo.findByRoleName("PILOT").stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(pilots.isEmpty() ? "No pilots Found": "pilots Retrieved successfully")
                .data(pilots)
                .build();  
        
    }

    @Override
    public Response<UserDTO> getAccountDetails() {
        log.info("Inside getAccountDetails()");

        User user = currentUser();

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("success")
                .data(userDTO)
                .build();

    }

}
