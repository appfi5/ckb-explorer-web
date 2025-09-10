package com.ckb.explorer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.ckb.explorer.entity.Udts;
import com.ckb.explorer.enums.UdtType;
import com.ckb.explorer.mapper.UdtHolderAllocationsMapper;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.service.UdtsService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class CkbExplorerWebApplicationTests {
	@Resource
	UdtsService udtsService;

	@Resource
	ScriptConfig scriptConfig;

	@Resource
	ScriptService scriptService;

	@Test
	void contextLoads() {
	}


	@Test
	void testPage(){
		List type = Arrays.asList(1,2,3);

		UdtsPageReq pageReq = new UdtsPageReq();
//		pageReq.setTags("cdc,d1d");
//		pageReq.setUnion(true);
//		pageReq.setSymbol("1234");
//		pageReq.setUdtType(type);
//		pageReq.setSort("created_time.desc");

		Page<Udts> page =  udtsService.getUdtsPageBy(pageReq);
	}

	@Resource
	UdtHolderAllocationsMapper udtHolderAllocationsMapper;
	@Test
    void testAddUdt(){
		List<Udts> udts = udtsService.list();
		  String[] xudtTags = {"invalid", "suspicious", "out-of-length-range", "rgb++", "layer-1-asset", "supply-limited", "utility", "layer-2-asset", "supply-unlimited"};

//		  List<ScriptConfig.LockScript> lockScripts = scriptConfig.getLockScripts();
//		   udts.forEach(udts1 -> {
//			if(UdtType.XUDT.getCode()==udts1.getUdtType()){
//				Set<String> stringSet = new HashSet<>();
//				stringSet.add(xudtTags[new Random().nextInt(xudtTags.length)]);
//				stringSet.add(xudtTags[new Random().nextInt(xudtTags.length)]);
//				stringSet.add(xudtTags[new Random().nextInt(xudtTags.length)]);
//				udts1.setXudtTags(stringSet.toArray(new String[0]));
//				udtsService.updateById(udts1);
//			}
//		  });
	}


}
