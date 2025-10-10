package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.dto.EpochDto;
import com.ckb.explorer.domain.resp.DaoContractResponse;
import com.ckb.explorer.entity.DaoContract;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.mapper.DailyStatisticsMapper;
import com.ckb.explorer.mapper.DaoContractMapper;
import com.ckb.explorer.service.DaoContractService;
import com.ckb.explorer.util.EstimatedApcCalculator;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class DaoContractServiceImpl extends
    ServiceImpl<DaoContractMapper, DaoContract> implements DaoContractService {

  @Resource
  private DailyStatisticsMapper dailyStatisticsMapper;

  @Resource
  private BlockMapper blockMapper;

  @Override
  public DaoContractResponse getDefaultContract() {
    var daoContract = baseMapper.selectById(1);
    if (daoContract == null) {
      return null;
    }

    // 上一次的统计
    var lastStatistics = dailyStatisticsMapper.getLastDayDailyStatistics();
    var tipBlockFractionEpoch = blockMapper.getRecentEpoch();
    if (tipBlockFractionEpoch == null) {
      tipBlockFractionEpoch = new EpochDto(0, 0, 1800);
    }
    var result = new DaoContractResponse();
    result.setTotalDeposit(daoContract.getTotalDeposit().toString());
    result.setDepositorsCount(daoContract.getDepositorsCount().toString());
    result.setDepositChanges(lastStatistics == null || lastStatistics.getTotalDaoDeposit() == null
        ? daoContract.getTotalDeposit().toString() : daoContract.getTotalDeposit()
        .subtract(new BigDecimal(lastStatistics.getTotalDaoDeposit())).toString());

    result.setUnclaimedCompensationChanges(
        lastStatistics == null || lastStatistics.getUnclaimedCompensation() == null
            ? daoContract.getUnclaimedCompensation().toString()
            : daoContract.getUnclaimedCompensation()
                .subtract(new BigDecimal(lastStatistics.getUnclaimedCompensation())).toString());
    result.setClaimedCompensationChanges(
        lastStatistics == null || lastStatistics.getClaimedCompensation() == null
            ? daoContract.getClaimedCompensation().toString() : daoContract.getClaimedCompensation()
            .subtract(new BigDecimal(lastStatistics.getClaimedCompensation())).toString());
    result.setDepositorChanges(
        lastStatistics == null || lastStatistics.getDaoDepositorsCount() == null
            ? daoContract.getDepositorsCount().toString() : String.valueOf(
            daoContract.getDepositorsCount() - Integer.valueOf(
                lastStatistics.getDaoDepositorsCount())));
    result.setUnclaimedCompensation(daoContract.getUnclaimedCompensation().toString());
    result.setClaimedCompensation(daoContract.getClaimedCompensation().toString());
    result.setAverageDepositTime(
        lastStatistics == null || lastStatistics.getAverageDepositTime() == null ? null
            : lastStatistics.getAverageDepositTime().toString());
    result.setMiningReward(lastStatistics == null || lastStatistics.getMiningReward() == null ? null
        : lastStatistics.getMiningReward().toString());
    result.setDepositCompensation(
        lastStatistics == null || lastStatistics.getDepositCompensation() == null ? null
            : lastStatistics.getDepositCompensation().toString());
    result.setTreasuryAmount(
        lastStatistics == null || lastStatistics.getTreasuryAmount() == null ? null
            : lastStatistics.getTreasuryAmount().toString());
    result.setEstimatedApc(EstimatedApcCalculator.estimatedApc(tipBlockFractionEpoch).toString());
    return result;


  }
}
