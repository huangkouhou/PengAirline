package com.peng.PengAirline.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.peng.PengAirline.enums.AuthMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    private boolean emailVerified;

    //FOR OATH 2 authentication
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)//在数据库里，用字符串来保存这个枚举值
    private AuthMethod provider;//用来记录用户是通过哪种方式认证进来的

    private String providerId;

    @ManyToMany(fetch = FetchType.EAGER)//一个用户（User）可以拥有多个role， EAGER查询 User 时，立刻同时加载所有关联的 Role
    //这个多对多关系（User ↔ Role）通过一张叫 users_roles 的中间表来建立连接
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    private boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

}

// User                users_roles              Role
// -----               -------------            -----
// id  ---------------- user_id
//                      role_id ---------------  id

