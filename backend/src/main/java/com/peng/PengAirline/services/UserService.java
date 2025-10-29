package com.peng.PengAirline.services;

import java.util.List;

import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.dtos.UserDTO;
import com.peng.PengAirline.entities.User;

public interface UserService {

    User currentUser();

    Response<?> updateMyAccount(UserDTO userDTO);

    Response<List<UserDTO>> getAllPilots();

    Response<UserDTO> getAccountDetails();

}
