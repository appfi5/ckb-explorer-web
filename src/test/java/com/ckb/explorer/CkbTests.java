package com.ckb.explorer;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Script.HashType;
import org.nervos.ckb.type.concrete.Byte32Vec;
import org.nervos.ckb.type.concrete.BytesVec;
import org.nervos.ckb.type.concrete.Uint256;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

public class CkbTests {

  public static void main(String[] args) {

    //testWitness();
    //byteToHeaderDeps();
    //System.out.println(scriptHAshToAddress());

    System.out.println(ConvertToUInt256(Numeric.hexStringToByteArray("0x00000000000000000000000000000000000000000000000000000000029BFB50")));
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
}
