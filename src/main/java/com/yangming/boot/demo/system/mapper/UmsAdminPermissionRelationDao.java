package com.yangming.boot.demo.system.mapper;

import com.yangming.boot.demo.system.model.UmsAdminPermissionRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户权限自定义Dao
 * Created by macro on 2018/10/8.
 */
@Component
public interface UmsAdminPermissionRelationDao {
    int insertList(@Param("list") List<UmsAdminPermissionRelation> list);
}
