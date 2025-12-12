package com.ckb.explorer;


import com.ckb.explorer.domain.dto.DaoCellDto;
import com.ckb.explorer.util.CkbUtil;
import com.ckb.explorer.util.DaoCompensationCalculator;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Script.HashType;
import org.nervos.ckb.type.concrete.Byte32Vec;
import org.nervos.ckb.type.concrete.BytesVec;
import org.nervos.ckb.type.concrete.Uint256;
import org.nervos.ckb.type.concrete.Uint64;
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

public class CkbTests {

  public static void main(String[] args) throws IOException {

    //testWitness();
    //byteToHeaderDeps();
    //System.out.println(scriptHAshToAddress());

    //System.out.println(ConvertToUInt256(Numeric.hexStringToByteArray("0x00000000000000000000000000000000000000000000000000000000029BFB50")));
    //getTransaction();
    // daoCompensationCalculator();
    //occupiedCapacity();
    testSince();
  }
  private static void testWitness(){
    var witnesses = "4d000000080000004100000055f49d7979ba246aa2f05a6e9afd25a23dc39ed9085a0b1e33b6b3bb80d34dbd4031a04ea389d6d8ff5604828889aa06a827e930a7e89411b80f6c3e1404951f00";
    var bytesVec = BytesVec.builder(Numeric.hexStringToByteArray(witnesses)).build();
    var bytesList = bytesVec.getItems();
    List<String> witnessList = new ArrayList<>();
    for(int i = 0; i < bytesList.length; i++){
      var bytes = bytesList[i];
      witnessList.add(Numeric.toHexString(bytes.getItems()));
    }
  }

  private static List<String> byteToHeaderDeps() {
    //var value = Numeric.hexStringToByteArray("01000000EC25F84CDA128647B4D56ACE5A02FD41BA66E8876E9CB8F5C48316CB9D5B3AFD");
    var value=Numeric.hexStringToByteArray("020000006360A9A66BF542BCC36ADB8DC8DCCAA39F8913DFC1D07666F9F7D109C4D56E9A7C7C8E013E68ADDB882D0AF1A4035FD3DB0E6167855789429FE3CCCED43F9968");
    var byte32Vec = Byte32Vec.builder(value).build();
    var byte32List = byte32Vec.getItems();
    List<String> headerDeps = new ArrayList<>();
    for(int i = 0; i < byte32List.length; i++){
      var bytes = byte32List[i];
      headerDeps.add(Numeric.toHexString(bytes.getItems()));
    }
    return headerDeps;
  }
  private static String scriptToAddress(){
    Script script = new Script(Numeric.hexStringToByteArray("0x9BD7E06F3ECF4BE0F2FCD2188B23F1B9FCC88E5D4B65A8637B17723BBDA3CCE8"),
    Numeric.hexStringToByteArray("0x3F1573B44218D4C12A91919A58A863BE415A2BC3"),
        HashType.TYPE);
    return new Address(script, Network.TESTNET).encode().toLowerCase();
  }

  private static String scriptHAshToAddress(){
    var hash = "0x8ABF38905F28FD36088EBBBFDB021C2F4A853D2C9E8809338A381561A77BB241";
    var hbyte = Numeric.hexStringToByteArray(hash);
    org.nervos.ckb.type.concrete.Script script = org.nervos.ckb.type.concrete.Script.builder(hbyte).build();
    var scriptNew = new Script(script.getCodeHash().getItems(),script.getArgs().getItems(),
        HashType.unpack(script.getHashType()));
    var addrResult = new Address(scriptNew, Network.TESTNET);
    return addrResult.encode();
  }

  public static BigInteger ConvertToUInt256(byte[] bytes)
  {
    var uint256 = Uint256.builder( bytes).build();
    return new BigInteger(uint256.getItems());
  }

  public static void getTransaction() throws IOException {
    CkbRpcApi ckbApi = new Api("https://testnet.ckb.dev");
    var hash = "0x23076A3814EBAF06E0037A471387A701B6718275678FD2B45205B80F6AE4F79A";
    var hbyte = Numeric.hexStringToByteArray(hash);
    var transaction = ckbApi.getTransaction(hbyte);
    System.out.println(transaction.txStatus);
  }

  public static void daoCompensationCalculator() throws IOException {
    CkbRpcApi ckbApi = new Api("https://testnet.ckb.dev");
    var maxBlockNumber = 60429L;
    var maxBlock = ckbApi.getBlockByNumber(maxBlockNumber);
    var depositBlock = ckbApi.getBlockByNumber(20007L);
    DaoCellDto cell = new DaoCellDto();
    cell.setBlockNumber(20007L);
    cell.setValue(BigInteger.valueOf(20012345678L));
    cell.setOccupiedCapacity(BigInteger.valueOf(10200000000L));

    var maxBlockNumberDao = maxBlock.header.dao;
    var depositBlockDao =depositBlock.header.dao;
    var value = DaoCompensationCalculator.call(cell, maxBlockNumberDao,
        depositBlockDao);
    // 理论上= 10646265
    System.out.println(value);
  }

  public static void occupiedCapacity() throws IOException {
    // 时间1639612780336 块高3766946 每日统计表里41527936800000000
    CkbRpcApi ckbApi = new Api("https://testnet.ckb.dev");
    var blockNumber = 3766946L;
    var block = ckbApi.getBlockByNumber(blockNumber);
    var dao = block.header.dao;
    var parsedDao = CkbUtil.parseDao(dao);
    System.out.println("s_i:" +parsedDao.getSI());
    System.out.println("ar_i:" +parsedDao.getArI());
    System.out.println("c_i:" +parsedDao.getCI());
    System.out.println("u_i:" +parsedDao.getUI());
  }

  public static void testSince() throws IOException {
    var since = "0x290000d200f00020";
    var byteSince =Numeric.hexStringToByteArray(since);
    var uint64Since = Uint64.builder(byteSince).build();
    //BigInteger sinceLongValue = Numeric.littleEndianBytesToBigInteger(Numeric.hexStringToByteArray(since));
    //System.out.println("BigInteger "+ sinceLongValue);
    //Uint64 uint64SinceNew = MoleculeConverter.packUint64(sinceLongValue.longValue());
    BigInteger uint64Value = new BigInteger(uint64Since.getItems());
    var littleEndian = MoleculeConverter.toByteArrayLittleEndianUnsigned(uint64Value, byteSince.length);
    System.out.println(Numeric.toHexString(uint64Since.getItems()));
    //System.out.println(Numeric.littleEndian(sinceLongValue.longValue()));
    System.out.println(Numeric.toHexString(littleEndian));
    //System.out.println(Numeric.toHexString(MoleculeConverter.toByteArrayLittleEndianUnsigned(sinceLongValue, 8)));
    System.out.println(Numeric.littleEndian(uint64Value.longValue()));
  }
}
