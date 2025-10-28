package com.peng.PengAirline.services;

import com.peng.PengAirline.dtos.LoginRequest;
import com.peng.PengAirline.dtos.LoginResponse;
import com.peng.PengAirline.dtos.RegistrationRequest;
import com.peng.PengAirline.dtos.Response;

public interface AuthService {

    Response<?> register(RegistrationRequest registrationRequest);
    Response<LoginResponse> login(LoginRequest loginRequest);

}
