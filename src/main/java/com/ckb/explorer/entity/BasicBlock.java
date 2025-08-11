package com.ckb.explorer.entity;

import com.ckb.explorer.config.ProposalsConverter;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "basic_block")
public class BasicBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer version;

    @Column(precision = 20, scale = 0)
    private BigDecimal compactTarget;

    private Long blockTimestamp;

    private Long number;

    private Long epoch;

    @Column(columnDefinition = "bytea")
    private byte[] parentHash;

    @Column(columnDefinition = "bytea")
    private byte[] transactionsRoot;

    @Column(columnDefinition = "bytea")
    private byte[] proposalsHash;

    @Column(columnDefinition = "bytea")
    private byte[] extraHash;

    private String dao;

    @JdbcTypeCode(SqlTypes.JSON)
    private String extension;

    @Column(precision = 50, scale = 0)
    private BigDecimal nonce;

    @Column(columnDefinition = "bytea")
    private byte[] blockHash;

    @Column(columnDefinition = "bytea")
    @Convert(converter = ProposalsConverter.class)
    private List<String> proposals;

    private Integer unclesCount;
}