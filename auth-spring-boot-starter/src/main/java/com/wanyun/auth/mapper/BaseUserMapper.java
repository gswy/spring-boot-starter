package com.wanyun.auth.mapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.Map;

public interface BaseUserMapper {

    public Map<String, Object> findUserById(TableInfo tableInfo, String identity);

}
