package com.ckb.explorer;


import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.concrete.Byte32Vec;
import org.nervos.ckb.type.concrete.BytesVec;
import org.nervos.ckb.utils.Numeric;

public class CkbTests {

  public static void main(String[] args) {

    //testWitness();
    byteToHeaderDeps();
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
}
