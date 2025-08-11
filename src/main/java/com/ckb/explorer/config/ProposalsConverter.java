package com.ckb.explorer.config;


import com.ckb.explorer.utils.CkbArrayHashType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class ProposalsConverter implements AttributeConverter<List<String>, byte[]> {

    // 为proposals创建CKBArrayHash实例，使用0x前缀和10字节长度
    private static final CkbArrayHashType CKB_ARRAY_HASH = new CkbArrayHashType("0x", 10);

    @Override
    public byte[] convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return new byte[0];
        }
        return CKB_ARRAY_HASH.serialize(attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(byte[] dbData) {
        if (dbData == null || dbData.length == 0) {
            return List.of();
        }
        try {
            return CKB_ARRAY_HASH.deserialize(dbData);
        } catch (Exception e) {
            System.err.println("Error deserializing proposals: " + e.getMessage());
            System.err.println("Data length: " + dbData.length);
            System.err.println("Expected hash length: 10");
            e.printStackTrace();
            return List.of();
        }
    }
}