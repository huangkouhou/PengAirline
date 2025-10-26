package com.peng.PengAirline.services;

import java.util.List;

import com.peng.PengAirline.dtos.Response;
import com.peng.PengAirline.dtos.RoleDTO;

public interface RoleService {
    Response<?> createRole(RoleDTO roleDTO);
    Response<?> updateRole(RoleDTO roleDTO);
    Response<List<RoleDTO>> getAllRoles();
}
