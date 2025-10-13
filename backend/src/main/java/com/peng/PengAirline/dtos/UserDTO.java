package com.peng.PengAirline.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.peng.PengAirline.entities.Role;
import com.peng.PengAirline.enums.AuthMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean emailVerified;

    private AuthMethod provider;

    private String providerId;

    private List<Role> roles;

    private boolean active;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;
}
