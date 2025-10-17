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
  public static final double INITIAL_SUPPLY = 33.6;
  public static final double SECONDARY_SUPPLY_PER_YEAR = 1.344;

  // 预计算的 primary 月供应量（50年内）
  private static volatile List<Double> primarySuppliesPerYear;
  private static final Map<Integer, List<Double>> totalSuppliesPerYearCache = new ConcurrentHashMap<>();
  // 缓存：计算结果缓存（nominal_apc、nominal_inflation_rate、real_inflation_rate）
  private static final ConcurrentHashMap<String, List<Double>> resultCache = new ConcurrentHashMap<>();

  /**
   * 生成唯一 ID（当前秒级时间戳）
   */
  public long id() {
    return System.currentTimeMillis() / 1000;
  }

  /**
   * 计算名义 APC
   */
  public static List<Double> nominalApc(int maxYear) {
    String cacheKey = "nominal_apc" + maxYear;
    // 缓存命中则直接返回，未命中则计算并缓存
    return resultCache.computeIfAbsent(cacheKey, key -> calculateNominalApc(maxYear));

  }

  // 重载默认参数（对应 Ruby 的 nominal_apc(max_year = 20)）
  public static List<Double> nominalApc() {
    return nominalApc(20);
  }

  /**
   * 计算名义通胀率
   */
  public static List<Double> nominalInflationRate(int maxYear) {
    String cacheKey = "nominal_inflation_rate" + maxYear;
    return resultCache.computeIfAbsent(cacheKey, key -> calculateNominalInflationRate(maxYear));
  }

  // 重载默认参数（对应 Ruby 的 nominal_inflation_rate(max_year = 50)）
  public static List<Double> nominalInflationRate() {
    return nominalInflationRate(50);
  }

  /**
   * 计算实际通胀率
   */
  public static List<Double> realInflationRate(int maxYear) {
    String cacheKey = "real_inflation" + maxYear;
    return resultCache.computeIfAbsent(cacheKey, key -> calculateRealInflationRate(maxYear));
  }

  // 重载默认参数（对应 Ruby 的 real_inflation_rate(max_year = 50)）
  public static List<Double> realInflationRate() {
    return realInflationRate(50);
  }

  /**
   * 实际计算名义 APC 的逻辑（与缓存解耦）
   */
  private static List<Double> calculateNominalApc(int maxYear) {
    List<Double> totalSupplies = totalSuppliesPerYear(maxYear);
    return IntStream.range(0, totalSupplies.size())
        .mapToObj(index -> {
          // 计算累计总供应量（0到index的总和）
          double cumulativeTotalSupply = index == 0 ? 0 :
              IntStream.rangeClosed(0, index)
                  .mapToDouble(totalSupplies::get)
                  .sum();

          // 计算总供应量（初始供应 + 累计供应）
          double totalSupplySoFar = INITIAL_SUPPLY + cumulativeTotalSupply;

          // 计算 APC 并截断到8位小数（对应 Ruby 的 truncate(8)）
          double apc = (SECONDARY_SUPPLY_PER_YEAR / totalSupplySoFar * 100);
          return truncateTo8Decimals(apc);
        })
        .collect(Collectors.toList());
  }


  /**
   * 实际计算名义通胀率的逻辑（与缓存解耦）
   */
  private static List<Double> calculateNominalInflationRate(int maxYear) {
    double secondaryIssuanceMonthly = SECONDARY_SUPPLY_PER_YEAR / 12;
    List<Double> totalSupplies = totalSuppliesPerYear(maxYear);

    // 计算 rs：INITIAL_SUPPLY + 累计总供应量（对应 Ruby 的 rs 变量）
    List<Double> rs = IntStream.range(0, totalSupplies.size())
        .mapToObj(index -> {
          double cumulativeTotalSupply = index == 0 ? 0 :
              IntStream.rangeClosed(0, index)
                  .mapToDouble(totalSupplies::get)
                  .sum();
          return INITIAL_SUPPLY + cumulativeTotalSupply;
        })
        .toList();

    // 计算 primarySuppliesPerYear（懒加载）
    List<Double> primarySupplies = getPrimarySuppliesPerYear();

    // 计算名义通胀率：( (primary + secondary) / rs ) * 12 * 100，截断8位小数
    return IntStream.range(0, Math.min(primarySupplies.size(), rs.size()))
        .mapToObj(i -> {
          double primaryPlusSecondary = primarySupplies.get(i) + secondaryIssuanceMonthly;
          double ratio = primaryPlusSecondary / rs.get(i);
          double inflationRate = ratio * 12 * 100;
          return truncateTo8Decimals(inflationRate);
        })
        .collect(Collectors.toList());
  }


  /**
   * 实际计算实际通胀率的逻辑（与缓存解耦）
   */
  private static List<Double> calculateRealInflationRate(int maxYear) {
    List<Double> nominalInflation = nominalInflationRate(maxYear);
    List<Double> nominalApc = nominalApc(maxYear);

    // 计算差值并截断8位小数
    return IntStream.range(0, Math.min(nominalInflation.size(), nominalApc.size()))
        .mapToObj(i -> {
          double real = nominalInflation.get(i) - nominalApc.get(i);
          return truncateTo8Decimals(real);
        })
        .collect(Collectors.toList());
  }

  /**
   * 私有方法：计算指定max_year内的月粒度总供应量（primary + secondary）
   * 对应Ruby的total_supplies_per_year方法，结果缓存
   */
  private static List<Double> totalSuppliesPerYear(int maxYear) {
    return totalSuppliesPerYearCache.computeIfAbsent(maxYear, year -> {
      double secondarySupplyPerMonth = SECONDARY_SUPPLY_PER_YEAR / 12;
      List<Double> totalSupplies = new ArrayList<>();

      for (int y = 0; y < year; y++) { // 对应Ruby的(0...max_year)
        // 计算每年的primary供应（年粒度）
        double primarySupplyPerYear = 4.2 / Math.pow(2, y / 4);
        double primarySupplyPerMonth = primarySupplyPerYear / 12;

        // 每月总供应 = primary月供应 + secondary月供应
        double monthlyTotal = primarySupplyPerMonth + secondarySupplyPerMonth;

        // 第0年（index=0）：第一个月为0，后续11个月为monthlyTotal
        if (y == 0) {
          totalSupplies.add(0.0); // 第一个月
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
  private static List<Double> getPrimarySuppliesPerYear() {
    if (primarySuppliesPerYear == null) {
      synchronized (MonetaryData.class) {
        if (primarySuppliesPerYear == null) {
          int maxYear = 50;
          List<Double> primarySupplies = new ArrayList<>();

          for (int y = 0; y < maxYear; y++) { // 对应 Ruby 的 (0...50) 循环
            double primarySupplyPerYear = 4.2 / Math.pow(2, y / 4);
            double primarySupplyPerMonth = primarySupplyPerYear / 12;

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


  /**
   * 工具方法：将小数截断到8位（不四舍五入）
   */
  private static double truncateTo8Decimals(double value) {
    return new BigDecimal(value)
        .setScale(8, RoundingMode.DOWN) // 截断模式
        .doubleValue();
  }
}
