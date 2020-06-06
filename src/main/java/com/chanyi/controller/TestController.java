package com.chanyi.controller;

import com.chanyi.constant.JsonResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanyi.annotation.YApiDesc;
import com.chanyi.model.AddYApiCat;

@RequestMapping("test")
@Component
@YApiDesc(groupName = "测试分组",groupDesc = "这里是分组的描述")
public class TestController {

	@RequestMapping("test1")
	@ResponseBody
	@YApiDesc(title = "test1接口",path="/test/test23",paramsInfo={"包路径","项目id"})
	public JsonResult test1(String packagePath,int projectId) {
		return new JsonResult();
	}
	
	@RequestMapping("test2")
	@ResponseBody
	@YApiDesc(title = "test2接口",path="/test/test24",paramsInfo={"自定义类"})
	public JsonResult test2(AddYApiCat addYApiCat) {
		return new JsonResult();
	}

}


