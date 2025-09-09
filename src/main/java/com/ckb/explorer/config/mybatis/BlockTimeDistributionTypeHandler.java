package com.ckb.explorer.config.mybatis;

import com.ckb.explorer.util.JsonUtil;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

@MappedTypes({LinkedHashMap.class})
public class BlockTimeDistributionTypeHandler extends
    BaseTypeHandler<LinkedHashMap<String, String>> {

  @Override
  public void setNonNullParameter(java.sql.PreparedStatement ps, int i, LinkedHashMap<String, String> parameter, JdbcType jdbcType) throws SQLException {
    String json = JsonUtil.toJSONString(parameter);
    PGobject pgo = new PGobject();
    pgo.setType("jsonb");  // 显式声明为 jsonb
    pgo.setValue(json);

    ps.setObject(i, pgo); // 传入 PGobject
  }

  @Override
  public LinkedHashMap<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parseJson(rs.getString(columnName));
  }

  @Override
  public LinkedHashMap<String, String> getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return parseJson(rs.getString(columnIndex));
  }

  @Override
  public LinkedHashMap<String, String> getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return parseJson(cs.getString(columnIndex));
  }

  private LinkedHashMap<String, String> parseJson(String json) {
    if (json == null || json.trim().isEmpty() || "null".equals(json.trim())) {
      return null;
    }
    return JsonUtil.parseObject(json, LinkedHashMap.class);
  }
}
