package com.ckb.explorer.config.mybatis;

import com.ckb.explorer.domain.resp.AddressBalanceRanking;
import com.ckb.explorer.util.JsonUtil;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

@MappedTypes({List.class})
public class AddressBalanceRankingTypeHandler extends BaseTypeHandler<List<AddressBalanceRanking>> {

  @Override
  public void setNonNullParameter(java.sql.PreparedStatement ps, int i, List<AddressBalanceRanking> parameter, JdbcType jdbcType) throws java.sql.SQLException {
    String json = JsonUtil.toJSONString(parameter);
    PGobject pgo = new PGobject();
    pgo.setType("jsonb");  // 显式声明为 jsonb
    pgo.setValue(json);

    ps.setObject(i, pgo); // 传入 PGobject
  }

  @Override
  public List<AddressBalanceRanking> getNullableResult(java.sql.ResultSet rs, String columnName) throws java.sql.SQLException {
    return parseJson(rs.getString(columnName));
  }

  @Override
  public List<AddressBalanceRanking> getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return parseJson(rs.getString(columnIndex));
  }

  @Override
  public List<AddressBalanceRanking> getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return parseJson(cs.getString(columnIndex));
  }

  private List<AddressBalanceRanking> parseJson(String json) {
    if (json == null || json.trim().isEmpty() || "null".equals(json.trim())) {
      return null;
    }
    return JsonUtil.parseList(json, AddressBalanceRanking.class);
  }
}
