package xin.wanyun.auth.mapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


public interface AuthMapper {

    public Map<String, Object> findUserById(TableInfo tableInfo, String identity);

}
