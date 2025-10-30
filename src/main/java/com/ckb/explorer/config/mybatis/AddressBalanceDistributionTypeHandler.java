package com.ckb.explorer.config.mybatis;

import com.ckb.explorer.domain.dto.AddressBalanceDistributionDto;
import com.ckb.explorer.domain.dto.AddressBalanceDistributionWrapper;
import com.ckb.explorer.util.JsonUtil;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MappedTypes({AddressBalanceDistributionWrapper.class})
public class AddressBalanceDistributionTypeHandler extends
    BaseTypeHandler<AddressBalanceDistributionWrapper> {

  @Override
  public void setNonNullParameter(java.sql.PreparedStatement ps, int i, AddressBalanceDistributionWrapper parameter, JdbcType jdbcType) throws SQLException {
    String json = JsonUtil.toJSONString(parameter.getData());
    PGobject pgo = new PGobject();
    pgo.setType("jsonb");  // 显式声明为 jsonb
    pgo.setValue(json);

    ps.setObject(i, pgo); // 传入 PGobject
  }

  @Override
  public AddressBalanceDistributionWrapper getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parseJson(rs.getString(columnName));
  }

  @Override
  public AddressBalanceDistributionWrapper getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return parseJson(rs.getString(columnIndex));
  }

  @Override
  public AddressBalanceDistributionWrapper getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return parseJson(cs.getString(columnIndex));
  }

  private AddressBalanceDistributionWrapper parseJson(String json) {
    if (json == null || json.trim().isEmpty() || "null".equals(json.trim())) {
      return null;
    }

    List<AddressBalanceDistributionDto> list = JsonUtil.parseList(json, AddressBalanceDistributionDto.class);
    AddressBalanceDistributionWrapper wrapper = new AddressBalanceDistributionWrapper();
    wrapper.setData(list);
    return wrapper;
  }
}
