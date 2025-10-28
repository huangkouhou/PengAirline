package com.peng.PengAirline.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.peng.PengAirline.dtos.LoginRequest;
import com.peng.PengAirline.dtos.LoginResponse;
import com.peng.PengAirline.dtos.RegistrationRequest;
import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.entities.Role;
import com.peng.PengAirline.entities.User;
import com.peng.PengAirline.enums.AuthMethod;
import com.peng.PengAirline.exceptions.BadRequestException;
import com.peng.PengAirline.exceptions.NotFoundException;
import com.peng.PengAirline.repo.RoleRepo;
import com.peng.PengAirline.repo.UserRepo;
import com.peng.PengAirline.security.JwtUtils;
import com.peng.PengAirline.services.AuthService;
import com.peng.PengAirline.services.EmailNotificationService;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor //(Lombok) 的作用就是帮您自动生成这个构造函数。省略this.roleRepo = roleRepo;
public class AuthServiceImpl implements AuthService{

    private final UserRepo userRepo;
    private final JwtUtils jwtUtils;
    private final RoleRepo roleRepo;
    private final EmailNotificationService emailNotificationService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Response<?> register(RegistrationRequest registrationRequest){
        log.info("Inside register()");
        if (userRepo.existsByEmail(registrationRequest.getEmail())){
            throw new BadRequestException("Email already exist");
        }

        List<Role> userRoles;

        if (registrationRequest.getRoles() != null && !registrationRequest.getRoles().isEmpty()){
            userRoles = registrationRequest.getRoles().stream()
                    .map(roleName -> roleRepo.findByName(roleName.toUpperCase())
                        .orElseThrow(() -> new NotFoundException("Role" + roleName + "Not Found")))
                    .toList();
        }else{
            Role defaultRole = roleRepo.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException("Role CUSTOMER DOESTN'T EXISTS"));
            userRoles = List.of(defaultRole);
        }

        User userToSave = new User();
        userToSave.setName(registrationRequest.getName());
        userToSave.setEmail(registrationRequest.getEmail());
        userToSave.setPhoneNumber(registrationRequest.getPhoneNumber());
        userToSave.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userToSave.setRoles(userRoles);
        userToSave.setCreateAt(LocalDateTime.now());
        userToSave.setUpdatedAt(LocalDateTime.now());
        userToSave.setProvider(AuthMethod.LOCAL);
        userToSave.setActive(true);

        User savedUser = userRepo.save(userToSave);

        emailNotificationService.sendWelcomeEmail(savedUser);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("user registered successfully")
                .build();

    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest){
        
        log.info("Inside login()");
        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("Email Not Found"));

        if (!user.isActive()){
            throw new BadRequestException("Account not active, please reach out to customer care.");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid Password");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        List<String > roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRoles(roleNames);

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login SUccessful")
                .data(loginResponse)
                .build();
    }
}
