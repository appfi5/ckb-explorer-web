package com.ckb.explorer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollectionUtils {
  /**
   * 将大集合拆分成小批次
   * @param set 原始大集合
   * @param batchSize 每批大小（建议1000-5000）
   * @return 小批次列表
   */
  public static <T> List<List<T>> splitIntoBatches(Set<T> set, int batchSize) {
    List<List<T>> batches = new ArrayList<>();
    List<T> currentBatch = new ArrayList<>(batchSize);
    for (T item : set) {
      currentBatch.add(item);
      if (currentBatch.size() >= batchSize) {
        batches.add(currentBatch);
        currentBatch = new ArrayList<>(batchSize);
      }
    }
    if (!currentBatch.isEmpty()) {
      batches.add(currentBatch);
    }
    return batches;
  }
}
