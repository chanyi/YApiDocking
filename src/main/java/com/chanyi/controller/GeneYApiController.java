package com.chanyi.controller;

import lombok.extern.log4j.Log4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chanyi.server.YApiDescService;


@RequestMapping("/geneYApi")
@Component
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GeneYApiController {

	private static final Log log = LogFactory.getLog(GeneYApiController.class);

	@Autowired
	private YApiDescService yApiDescService;

	/**
	 * 批量录入接口到YApi中
	 * @param:packagePath 要导出接口的包路径(执行之前自行制定)
	 * @param:projectId 项目id(执行之前自行制定)
	 */
	@RequestMapping("/geneYApi")
	@ResponseBody
	@Test
	public void geneYApi(){
		try {
			yApiDescService.geneYApi("com.chanyi.controller", 90);
		}catch (Exception e){
			log.info("exception",e);
		}
	}
}


