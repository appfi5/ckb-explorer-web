package com.ckb.explorer.util;

import com.ckb.explorer.domain.dto.EpochDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EstimatedApcCalculator {

  public static final String CONTRACT_NAME = "nervos_dao";
  public static final long GENESIS_ISSUANCE = 336L * (long) Math.pow(10, 8); // 336 * 10^8
  public static final long ANNUAL_PRIMARY_ISSUANCE_BASE = GENESIS_ISSUANCE / 8; // 336*10^8 / 8
  public static final BigDecimal PRIMARY_ISSUANCE_PER_YEAR_BASE = new BigDecimal("42").multiply(new BigDecimal(10).pow(8)); // 42 * 10^8
  public static final int EPOCHS_IN_ONE_NATURAL_YEAR = 2190;
  public static final int YEARS_IN_PERIOD = 4;
  public static final BigDecimal EPOCHS_IN_PERIOD = new BigDecimal(EPOCHS_IN_ONE_NATURAL_YEAR * YEARS_IN_PERIOD); // 2190 * 4
  // ruby: BigDecimal(1344 * 10**6) / 2190;
  public static final BigDecimal SECONDARY_ISSUANCE_PER_EPOCH = new BigDecimal(1344).multiply(new BigDecimal(10).pow(6)).divide(
      new BigDecimal(2190),
      20,  // 保留20位小数
      RoundingMode.HALF_UP  // 四舍五入（与Ruby默认舍入行为一致）
  );
  public static final int EPOCH_LENGTH = 1800;

  /**
   * 计算估计APC（对应Ruby的estimated_apc方法，默认参数：deposited_epochs=2190）
   * @param depositEpoch 存款纪元（入参模型）
   * @return 估计APC值（保留4位小数，截断）
   */
  public static BigDecimal estimatedApc(EpochDto depositEpoch) {
    // 调用带默认参数的重载方法
    return estimatedApc(depositEpoch, EPOCHS_IN_ONE_NATURAL_YEAR);
  }

  /**
   * 计算估计的APC（对应Ruby的estimated_apc方法）
   * @param depositEpoch 存款纪元（包含number、index、length字段）
   * @param depositedEpochs 存款持续的纪元数（默认值为EPOCHS_IN_ONE_NATURAL_YEAR）
   * @return 估计的APC值（保留4位小数）
   */
  public static BigDecimal estimatedApc(EpochDto depositEpoch, Integer depositedEpochs) {
    // 处理默认参数
    if (depositedEpochs == null) {
      depositedEpochs = EPOCHS_IN_ONE_NATURAL_YEAR;
    }

    // 1. 计算起始和结束纪元编号
    long startEpochNumber = depositEpoch.getNumber();
    long endEpochNumber = startEpochNumber + depositedEpochs - 1;
    long scaledEndEpochNumber = endEpochNumber;

    // 计算ratio（存款周期与自然年的比例）
    BigDecimal ratio = BigDecimal.valueOf(endEpochNumber - startEpochNumber)
        .divide(BigDecimal.valueOf(EPOCHS_IN_ONE_NATURAL_YEAR), 20, RoundingMode.HALF_UP);

    // 若ratio < 1，调整scaledEndEpochNumber和ratio
    if (ratio.compareTo(BigDecimal.ONE) < 0) {
      scaledEndEpochNumber = startEpochNumber + EPOCHS_IN_ONE_NATURAL_YEAR - 1;
      ratio = BigDecimal.ONE;
    }

    // 2. 计算检查点起始和结束（checkpoint_start和checkpoint_end）
    // (start_epoch_number + 1) / EPOCHS_IN_PERIOD 的向上取整，再乘以EPOCHS_IN_PERIOD
    BigDecimal startPlus1 = BigDecimal.valueOf(startEpochNumber + 1);
    BigDecimal checkpointStartRaw = startPlus1.divide(EPOCHS_IN_PERIOD, 0, RoundingMode.CEILING);
    long checkpointStart = checkpointStartRaw.multiply(EPOCHS_IN_PERIOD).longValue();

    // (scaled_end_epoch_number + 1) / EPOCHS_IN_PERIOD 的向下取整，再乘以EPOCHS_IN_PERIOD
    BigDecimal scaledEndPlus1 = BigDecimal.valueOf(scaledEndEpochNumber + 1);
    BigDecimal checkpointEndRaw = scaledEndPlus1.divide(EPOCHS_IN_PERIOD, 0, RoundingMode.FLOOR);
    long checkpointEnd = checkpointEndRaw.multiply(EPOCHS_IN_PERIOD).longValue();

    // 计算检查点数量
    long checkpointsSize = (checkpointEnd - checkpointStart) / EPOCHS_IN_PERIOD.longValue() + 1;

    // 3. 生成检查点列表（checkpoints）
    List<Long> checkpoints = new ArrayList<>();
    for (int index = 0; index < checkpointsSize; index++) {
      long checkpoint = index * EPOCHS_IN_PERIOD.longValue() + checkpointStart - 1;
      checkpoints.add(checkpoint);
    }

    // 调整检查点：确保包含start_epoch_number（若需要）
    if (checkpoints.isEmpty() || checkpoints.get(0) > startEpochNumber) {
      checkpoints.add(0, startEpochNumber); // 对应Ruby的unshift
    }

    // 调整检查点：确保包含scaled_end_epoch_number（若需要）
    if (!checkpoints.isEmpty() && checkpoints.get(checkpoints.size() - 1) < scaledEndEpochNumber) {
      checkpoints.add(scaledEndEpochNumber); // 对应Ruby的push
    }

    // 4. 提取end_epoch_numbers（checkpoints从索引1到最后）
    List<Long> endEpochNumbers = new ArrayList<>();
    if (checkpoints.size() > 1) {
      endEpochNumbers = checkpoints.subList(1, checkpoints.size());
    }

    // 5. 计算每个区间的利率（rate）
    List<Double> rates = new ArrayList<>(endEpochNumbers.size());
    for (int index = 0; index < endEpochNumbers.size(); index++) {
      long innerEndEpochNumber = endEpochNumbers.get(index);
      // 计算epoch_index（与Ruby逻辑一致：deposit_epoch.index * 1800 / deposit_epoch.length）
      int epochIndex = (depositEpoch.getIndex() * EPOCH_LENGTH) / depositEpoch.getLength();

      // 创建start_epoch和end_epoch对象（对应Ruby的OpenStruct）
      EpochDto startEpoch = new EpochDto(checkpoints.get(index), epochIndex, EPOCH_LENGTH);
      EpochDto endEpoch = new EpochDto(innerEndEpochNumber, epochIndex, EPOCH_LENGTH);

      // 计算单期利率（需实现rate方法，逻辑同Ruby）
      double rate = rate(startEpoch, endEpoch);
      rates.add(rate);
    }

    // 6. 汇总利率：(1+rate1)*(1+rate2)*... - 1
    double totalRate = rates.stream()
        .reduce(1.0, (memo, r) -> memo * (1 + r)) // 累乘(1+rate)
        - 1.0;  // 减1得到总利率

    // 7. 计算最终结果：(rate * 100) / ratio，保留4位小数（截断）
    return new BigDecimal(totalRate)
        .multiply(new BigDecimal(100))
        .divide(ratio, 4, RoundingMode.DOWN) // truncate(4)对应向下截断
        .stripTrailingZeros(); // 移除末尾多余的0（可选，保持格式整洁）
  }

  /**
   * 计算两个纪元之间的利率（对应Ruby的rate方法，需补充具体逻辑）
   * @param startEpoch 起始纪元
   * @param endEpoch 结束纪元
   * @return 利率值
   */
  private static double rate(EpochDto startEpoch, EpochDto endEpoch) {
    // 原Ruby代码中rate方法的逻辑需在此实现
    // 1. 计算alpha值（调用alpha方法，参数为start_epoch.number）
    BigDecimal alpha = alpha(startEpoch.getNumber());

    // 2. 计算sn：二次发行总量
    // 计算(end_epoch.number + end_epoch.index/end_epoch.length)
    BigDecimal endTerm = new BigDecimal(endEpoch.number)
        .add(new BigDecimal(endEpoch.index)
            .divide(new BigDecimal(endEpoch.length), 20, RoundingMode.HALF_UP));
    // 计算(start_epoch.number + start_epoch.index/start_epoch.length)
    BigDecimal startTerm = new BigDecimal(startEpoch.number)
        .add(new BigDecimal(startEpoch.index)
            .divide(new BigDecimal(startEpoch.length), 20, RoundingMode.HALF_UP));
    // sn = 二次发行常量 * (endTerm - startTerm)
    BigDecimal sn = SECONDARY_ISSUANCE_PER_EPOCH.multiply(endTerm.subtract(startTerm));

    // 3. 计算总发行量（调用totalIssuance方法）
    BigDecimal totalIssuance = totalIssuance(startEpoch);

    // 4. 计算对数表达式：ln(1 + (alpha + 1) * sn / totalIssuance) / (alpha + 1)
    BigDecimal numerator = BigDecimal.ONE
        .add(alpha.add(BigDecimal.ONE)
            .multiply(sn)
            .divide(totalIssuance, 20, RoundingMode.HALF_UP));
    double logResult = Math.log(numerator.doubleValue()); // 自然对数（对应Ruby的Math.log）
    return logResult / alpha.add(BigDecimal.ONE).doubleValue();
  }

  /**
   * 对应Ruby的alpha方法
   * @param startEpochNumber 起始纪元编号
   * @return alpha计算结果
   */
  public static BigDecimal alpha(long startEpochNumber) {
    // i = ((start_epoch_number + 1) / EPOCHS_IN_PERIOD).floor
    BigDecimal startPlus1 = new BigDecimal(startEpochNumber + 1);
    BigDecimal iBig = startPlus1.divide(EPOCHS_IN_PERIOD, 0, RoundingMode.FLOOR); // 向下取整
    int i = iBig.intValue();

    // p = PRIMARY_ISSUANCE_PER_YEAR_BASE / 2^i / 2190
    BigDecimal twoPowerI = new BigDecimal(2).pow(i); // 2^i
    BigDecimal p = PRIMARY_ISSUANCE_PER_YEAR_BASE
        .divide(twoPowerI, 20, RoundingMode.HALF_UP)
        .divide(new BigDecimal(EPOCHS_IN_ONE_NATURAL_YEAR), 20, RoundingMode.HALF_UP);

    // p / SECONDARY_ISSUANCE_PER_EPOCH
    return p.divide(SECONDARY_ISSUANCE_PER_EPOCH, 20, RoundingMode.HALF_UP);
  }

  /**
   * 对应Ruby的total_issuance方法
   * @param startEpoch 起始纪元
   * @return 总发行量（primary + secondary）
   */
  public static BigDecimal totalIssuance(EpochDto startEpoch) {
    return primaryIssuance(startEpoch).add(secondaryIssuance(startEpoch));
  }

  /**
   * 对应Ruby的primary_issuance方法
   * @param startEpoch 起始纪元
   * @return 主要发行量
   */
  public static BigDecimal primaryIssuance(EpochDto startEpoch) {

    // epochs = (start_epoch.number / EPOCHS_IN_PERIOD).floor
    BigDecimal startNumber = new BigDecimal(startEpoch.getNumber());
    BigDecimal epochsBig = startNumber.divide(EPOCHS_IN_PERIOD, 0, RoundingMode.FLOOR);
    int epochs = epochsBig.intValue();

    // 累加部分：epochs.times.reduce(GENESIS_ISSUANCE) { ... }
    BigDecimal genesis = new BigDecimal(GENESIS_ISSUANCE);
    BigDecimal sum = genesis;
    BigDecimal annualPrimary = new BigDecimal(ANNUAL_PRIMARY_ISSUANCE_BASE);
    BigDecimal yearsInPeriod = new BigDecimal(YEARS_IN_PERIOD);

    for (int item = 0; item < epochs; item++) { // 迭代0到epochs-1（共epochs次）
      BigDecimal twoPowerItem = new BigDecimal(2).pow(item); // 2^item
      BigDecimal term = annualPrimary.multiply(yearsInPeriod)
          .divide(twoPowerItem, 20, RoundingMode.HALF_UP);
      sum = sum.add(term);
    }

    // 额外部分：(ANNUAL_PRIMARY_ISSUANCE_BASE * (...) ) / 2^epochs
    BigDecimal part1 = new BigDecimal(startEpoch.getNumber() + 1)
        .subtract(epochsBig.multiply(EPOCHS_IN_PERIOD)); // (start_epoch.number + 1 - epochs * EPOCHS_IN_PERIOD)
    BigDecimal part2 = part1.divide(new BigDecimal(EPOCHS_IN_ONE_NATURAL_YEAR), 20, RoundingMode.HALF_UP);
    BigDecimal term2 = annualPrimary.multiply(part2)
        .divide(new BigDecimal(2).pow(epochs), 20, RoundingMode.HALF_UP);

    return sum.add(term2);
  }

  /**
   * 对应Ruby的secondary_issuance方法
   * @param startEpoch 起始纪元
   * @return 二次发行量
   */
  public static BigDecimal secondaryIssuance(EpochDto startEpoch) {

    // deposit_fraction = start_epoch.number + (start_epoch.index / start_epoch.length)
    BigDecimal index = new BigDecimal(startEpoch.getIndex());
    BigDecimal length = new BigDecimal(startEpoch.getLength());
    BigDecimal depositFraction = new BigDecimal(startEpoch.getNumber())
        .add(index.divide(length, 20, RoundingMode.HALF_UP));

    // epochs = deposit_fraction > 0 ? (deposit_fraction + 1) : deposit_fraction
    BigDecimal epochs;
    if (depositFraction.compareTo(BigDecimal.ZERO) > 0) {
      epochs = depositFraction.add(BigDecimal.ONE);
    } else {
      epochs = depositFraction;
    }

    // epochs * SECONDARY_ISSUANCE_PER_EPOCH
    return epochs.multiply(SECONDARY_ISSUANCE_PER_EPOCH)
        .setScale(20, RoundingMode.HALF_UP);
  }
}
