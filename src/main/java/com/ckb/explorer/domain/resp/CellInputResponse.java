package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellInputResponse 用于将 CellInput 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellInputResponse extends BaseResponse<Long> {

  /**
   * 是否来自cellbase交易
   */
  private Boolean fromCellbase;
  
  /**
   * 单元格容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long capacity;
  
  /**
   * 占用的容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long occupiedCapacity;
  
  /**
   * 地址哈希
   */
  private String addressHash;

  /**
   * 目标块高
   */
  private Long targetBlockNumber;
  
  /**
   * 生成的交易哈希
   */
  private String generatedTxHash;
  
  /**
   * 单元格索引
   */
  private Integer cellIndex;
  
  /**
   * 单元格类型
   */
  private String cellType;
  
  /**
   * since信息
   */
  private SinceResponse since;
  
  /**
   * 类型脚本
   */
  private ScriptResponse typeScript;

  // ========== DAO 相关字段（可选）==========
  private String compensationStartedBlockNumber;
  private String compensationStartedTimestamp;
  private String compensationEndedBlockNumber;
  private String compensationEndedTimestamp;
  private String interest;
  private String lockedUntilBlockNumber;
  private String lockedUntilBlockTimestamp;

  // ========== UDT / XUDT / SSRI / XUDT Compatible（可选）==========
  /*private UdtInfo udtInfo;
  private UdtInfo xudtInfo;
  private UdtInfo ssriInfo;
  private UdtInfo xudtCompatibleInfo;
  private UdtInfo extraInfo; // 多个类型共用

  // ========== M-NFT（可选）==========
  private MnftInfo mNftInfo;

  // ========== NRC-721（可选）==========
  private Nrc721TokenInfo nrc721TokenInfo;

  // ========== Omiga Inscription（可选）==========
  private OmigaInscriptionInfo omigaInscriptionInfo;

  // ========== RGB / Bitcoin Vout（可选）==========
  private RgbInfo rgbInfo;

  // ========== DOB (Spore, DID)（可选）==========
  private DobInfo dobInfo;

  // ========== Fiber Graph Channel（可选）==========
  private FiberGraphChannelInfo fiberGraphChannelInfo;*/
}