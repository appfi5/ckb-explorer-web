package com.ckb.explorer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MonetaryData {

  // 常量定义（与原逻辑一致）
  public static final List<String> VALID_INDICATORS = Arrays.asList("nominal_apc", "nominal_inflation_rate", "real_inflation_rate");
  // 匹配"nominal_apc+数字"的正则（对应Ruby的/^nominal_apc(\d+)$/）
  public static final String NOMINAL_APC_REGEX = "^nominal_apc\\d+$";
  public static final BigDecimal INITIAL_SUPPLY = new BigDecimal("33.6");
  public static final BigDecimal SECONDARY_SUPPLY_PER_YEAR = new BigDecimal("1.344");
  public static final BigDecimal PRIMARY_BASE = new BigDecimal("4.2"); // 4.2 基础值
  public static final BigDecimal TWO = new BigDecimal("2"); // 2的幂次计算用
  public static final BigDecimal TWELVE = new BigDecimal("12"); // 年转月的除数

  // 预计算的 primary 月供应量（50年内）
  private static volatile List<BigDecimal> primarySuppliesPerYear;
  private static final Map<Integer, List<BigDecimal>> totalSuppliesPerYearCache = new ConcurrentHashMap<>();
  // 缓存：计算结果缓存（nominal_apc、nominal_inflation_rate、real_inflation_rate）
  private static final ConcurrentHashMap<String, List<BigDecimal>> resultCache = new ConcurrentHashMap<>();

  /**
   * 生成唯一 ID（当前秒级时间戳）
   */
  public long id() {
    return System.currentTimeMillis() / 1000;
  }

  /**
   * 计算名义 APC
   */
  public static List<BigDecimal> nominalApc(int maxYear) {
    String cacheKey = "nominal_apc" + maxYear;
    // 缓存命中则直接返回，未命中则计算并缓存
    return resultCache.computeIfAbsent(cacheKey, key -> calculateNominalApc(maxYear));

  }

  // 重载默认参数（对应 Ruby 的 nominal_apc(max_year = 20)）
  public static List<BigDecimal> nominalApc() {
    return nominalApc(20);
  }

  /**
   * 计算名义通胀率
   */
  public static List<BigDecimal> nominalInflationRate(int maxYear) {
    String cacheKey = "nominal_inflation_rate" + maxYear;
    return resultCache.computeIfAbsent(cacheKey, key -> calculateNominalInflationRate(maxYear));
  }

  // 重载默认参数（对应 Ruby 的 nominal_inflation_rate(max_year = 50)）
  public static List<BigDecimal> nominalInflationRate() {
    return nominalInflationRate(50);
  }

  /**
   * 计算实际通胀率
   */
  public static List<BigDecimal> realInflationRate(int maxYear) {
    String cacheKey = "real_inflation" + maxYear;
    return resultCache.computeIfAbsent(cacheKey, key -> calculateRealInflationRate(maxYear));
  }

  // 重载默认参数（对应 Ruby 的 real_inflation_rate(max_year = 50)）
  public static List<BigDecimal> realInflationRate() {
    return realInflationRate(50);
  }

  /**
   * 实际计算名义 APC 的逻辑（与缓存解耦）
   */
  private static List<BigDecimal> calculateNominalApc(int maxYear) {
    List<BigDecimal> totalSupplies = totalSuppliesPerYear(maxYear);
    return IntStream.range(0, totalSupplies.size())
        .mapToObj(index -> {
          // 计算累计总供应量（0到index的总和）
          BigDecimal cumulativeTotalSupply = index == 0 ? BigDecimal.ZERO :
              IntStream.rangeClosed(0, index)
                  .mapToObj(totalSupplies::get)
                  .reduce(BigDecimal.ZERO, BigDecimal::add);

          // 计算总供应量（初始供应 + 累计供应）
          BigDecimal totalSupplySoFar = INITIAL_SUPPLY.add(cumulativeTotalSupply);

          // 计算 APC
          return SECONDARY_SUPPLY_PER_YEAR.divide(totalSupplySoFar, 20, RoundingMode.HALF_UP)
              .multiply(new BigDecimal("100")).setScale(8, RoundingMode.DOWN).stripTrailingZeros();

        })
        .collect(Collectors.toList());
  }


  /**
   * 实际计算名义通胀率的逻辑（与缓存解耦）
   */
  private static List<BigDecimal> calculateNominalInflationRate(int maxYear) {
    BigDecimal secondaryIssuanceMonthly = SECONDARY_SUPPLY_PER_YEAR.divide(TWELVE, 20, RoundingMode.HALF_UP);
    List<BigDecimal> totalSupplies = totalSuppliesPerYear(maxYear);

    // 计算 rs：INITIAL_SUPPLY + 累计总供应量（对应 Ruby 的 rs 变量）
    List<BigDecimal> rs = IntStream.range(0, totalSupplies.size())
        .mapToObj(index -> {
          BigDecimal cumulativeTotalSupply = index == 0 ? BigDecimal.ZERO :
              IntStream.rangeClosed(0, index)
                  .mapToObj(totalSupplies::get)
                  .reduce(BigDecimal.ZERO, BigDecimal::add);
          return INITIAL_SUPPLY.add(cumulativeTotalSupply);
        })
        .toList();

    // 计算 primarySuppliesPerYear（懒加载）
    List<BigDecimal> primarySupplies = getPrimarySuppliesPerYear();

    // 计算名义通胀率：( (primary + secondary) / rs ) * 12 * 100，
    return IntStream.range(0, Math.min(primarySupplies.size(), rs.size()))
        .mapToObj(i -> {
          BigDecimal primaryPlusSecondary = primarySupplies.get(i).add(secondaryIssuanceMonthly);
          BigDecimal ratio = primaryPlusSecondary.divide(rs.get(i),20, RoundingMode.HALF_UP);
          return ratio.multiply(TWELVE).multiply(new BigDecimal("100")).setScale(8, RoundingMode.DOWN).stripTrailingZeros();
        })
        .collect(Collectors.toList());
  }


  /**
   * 实际计算实际通胀率的逻辑（与缓存解耦）
   */
  private static List<BigDecimal> calculateRealInflationRate(int maxYear) {
    List<BigDecimal> nominalInflation = nominalInflationRate(maxYear);
    List<BigDecimal> nominalApc = nominalApc(maxYear);

    // 计算差值
    return IntStream.range(0, Math.min(nominalInflation.size(), nominalApc.size()))
        .mapToObj(i -> nominalInflation.get(i).subtract(nominalApc.get(i)).stripTrailingZeros())
        .collect(Collectors.toList());
  }

  /**
   * 私有方法：计算指定max_year内的月粒度总供应量（primary + secondary）
   * 对应Ruby的total_supplies_per_year方法，结果缓存
   */
  private static List<BigDecimal> totalSuppliesPerYear(int maxYear) {
    return totalSuppliesPerYearCache.computeIfAbsent(maxYear, year -> {
      // 舍入模式：四舍五入，保留10位小数（可根据业务调整精度）
      BigDecimal secondarySupplyPerMonth = SECONDARY_SUPPLY_PER_YEAR
          .divide(TWELVE, 20, RoundingMode.HALF_UP);
      List<BigDecimal> totalSupplies = new ArrayList<>();

      for (int y = 0; y < year; y++) { // 对应Ruby的(0...max_year)
        // 计算每年的primary供应（年粒度）
        int power = y / 4;
        BigDecimal twoPower = TWO.pow(power);
        BigDecimal primarySupplyPerYear = PRIMARY_BASE
            .divide(twoPower, 20, RoundingMode.HALF_UP); // 4.2 / 2^power
        BigDecimal primarySupplyPerMonth = primarySupplyPerYear
            .divide(TWELVE, 20, RoundingMode.HALF_UP);

        // 每月总供应 = primary月供应 + secondary月供应
        BigDecimal monthlyTotal = primarySupplyPerMonth.add(secondarySupplyPerMonth);


        // 第0年（index=0）：第一个月为0，后续11个月为monthlyTotal
        if (y == 0) {
          totalSupplies.add(BigDecimal.ZERO); // 第一个月
          for (int m = 1; m < 12; m++) {
            totalSupplies.add(monthlyTotal);
          }
        } else {
          // 非第0年：12个月均为monthlyTotal
          for (int m = 0; m < 12; m++) {
            totalSupplies.add(monthlyTotal);
          }
        }
      }
      return totalSupplies;
    });
  }

  /**
   * 获取 50 年内的月粒度 primary 供应量（懒加载 + 双重检查锁确保线程安全）
   * 对应 Ruby 的 private 方法 primary_supplies_per_year
   */
  private static List<BigDecimal> getPrimarySuppliesPerYear() {
    if (primarySuppliesPerYear == null) {
      synchronized (MonetaryData.class) {
        if (primarySuppliesPerYear == null) {
          int maxYear = 50;
          List<BigDecimal> primarySupplies = new ArrayList<>();

          for (int y = 0; y < maxYear; y++) { // 对应 Ruby 的 (0...50) 循环
            int power = y / 4;
            BigDecimal twoPower = TWO.pow(power);
            BigDecimal primarySupplyPerYear = PRIMARY_BASE
                .divide(twoPower, 20, RoundingMode.HALF_UP); // 4.2 / 2^power
            BigDecimal primarySupplyPerMonth = primarySupplyPerYear
                .divide(TWELVE, 20, RoundingMode.HALF_UP);

            // 每年12个月，每月均为 primarySupplyPerMonth（对应 Ruby 的 [value] * 12）
            for (int m = 0; m < 12; m++) {
              primarySupplies.add(primarySupplyPerMonth);
            }
          }
          primarySuppliesPerYear = primarySupplies;
        }
      }
    }
    return primarySuppliesPerYear;
  }
}
