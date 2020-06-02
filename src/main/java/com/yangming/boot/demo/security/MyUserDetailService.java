package com.yangming.boot.demo.security;

import com.yangming.boot.demo.system.model.UmsAdmin;
import com.yangming.boot.demo.system.model.UmsPermission;
import com.yangming.boot.demo.system.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UmsAdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UmsAdmin admin = adminService.getAdminByUsername(username);

        if (admin != null){
            List<UmsPermission> permissionList = adminService.getPermissionList(admin.getId());

            return new AdminUserDetails(admin,permissionList);
        }else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
    }
}
