package com.chanyi.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.chanyi.model.GenYApiInfoVo;
import com.chanyi.model.YApiInfo;
import com.chanyi.model.YApiInfoDetail;
import com.chanyi.model.YApiParams;
import com.chanyi.model.YApiQueryPath;
import com.chanyi.model.YApiRequestBodyForm;
import com.chanyi.model.YApiRequestHeader;
import com.chanyi.model.YApiRequestQuery;
import com.chanyi.model.YApiResponseBody;

@Component
public class YApiUtil {

	public List<YApiInfo> genYApiInfo(List<GenYApiInfoVo> voList){
		List<YApiInfo> infoList = new ArrayList<>();
		for(GenYApiInfoVo vo:voList){
			YApiInfo yApiInfo = initYApiInfo();
			YApiInfoDetail yApiInfoDetail = initYApiInfoDetail();
			yApiInfoDetail.setPath(vo.getPath());
			yApiInfoDetail.setMethod(vo.getMethod());
			yApiInfoDetail.setProject_id(vo.getProject_id());
			yApiInfoDetail.setTitle(vo.getTitle());
			yApiInfoDetail.setReq_body_form(vo.getyApiRequestBodyForms());
			infoList.add(yApiInfo);
		}
		return infoList;
	}
	
	
	public YApiInfo initYApiInfo() {
		YApiInfo yApiInfo = new YApiInfo();
		List<YApiInfoDetail> yApiInfoDetails = new ArrayList<YApiInfoDetail>();
		yApiInfoDetails.add(initYApiInfoDetail());
		yApiInfo.setList(yApiInfoDetails);
		yApiInfo.setIndex(1);
		yApiInfo.setName("默认分组");
		yApiInfo.setDesc("默认分组描述");
		Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
		yApiInfo.setUp_time(milliSecond);
		yApiInfo.setAdd_time(milliSecond);
		return yApiInfo;
	}
	
	public YApiInfoDetail initYApiInfoDetail() {
		YApiInfoDetail yApiInfoDetail = new YApiInfoDetail();
		Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
		yApiInfoDetail.set__v(0);
		yApiInfoDetail.set_id(11);
		yApiInfoDetail.setAdd_time(milliSecond);
		yApiInfoDetail.setUp_time(milliSecond);
		yApiInfoDetail.setApi_opened(true);
		yApiInfoDetail.setCatid(11);
		yApiInfoDetail.setDesc("<p>这里是备注</p>\n");
		yApiInfoDetail.setEdit_uid(0);
		yApiInfoDetail.setIndex(0);
		yApiInfoDetail.setMarkdown("这里是备注");
		yApiInfoDetail.setMethod("POST");
		yApiInfoDetail.setPath("/userHost/bindHost?json");
		yApiInfoDetail.setProject_id(11);
		List<YApiRequestBodyForm> req_body_forms = new ArrayList<>();
		req_body_forms.add(initYApiRequestBodyForm());
		yApiInfoDetail.setReq_body_form(req_body_forms);
		yApiInfoDetail.setReq_body_is_json_schema(true);
		yApiInfoDetail.setReq_body_type("json");
		List<YApiRequestHeader> req_headers = new ArrayList<>();
		req_headers.add(initYApiRequestHeader());
		yApiInfoDetail.setReq_headers(req_headers);
		List<String> req_params = new ArrayList<>(); 
		yApiInfoDetail.setReq_params(req_params);
		List<YApiRequestQuery> req_query = new ArrayList<>();
		req_query.add(initYApiRequestQuery());
		yApiInfoDetail.setReq_query(req_query);
		//暂时只使用返回固定string的方式
//		List<YApiResponseBody> res_body = new ArrayList<>();
//		res_body.add(initYApiResponseBody());
		String res_body_string ="{\"code\":\"200\",\"msg\":\"get success\",\"data\":\"\"}";
		yApiInfoDetail.setRes_body(res_body_string);
		yApiInfoDetail.setRes_body_is_json_schema(true);
		yApiInfoDetail.setRes_body_type("json");
		yApiInfoDetail.setStatus("done");
		List<String> tags = new ArrayList<>(Arrays.asList("tag1"));
		yApiInfoDetail.setTags(tags);
		yApiInfoDetail.setTitle("testapi");
		yApiInfoDetail.setType("static");
		yApiInfoDetail.setUid(15);
		List<YApiQueryPath> query_path = new ArrayList<>();
		query_path.add(initYApiQueryPath());
		yApiInfoDetail.setQuery_path(query_path);;
		return yApiInfoDetail;
	}
	
	public YApiParams initYApiParams() {
		YApiParams yApiParams = new YApiParams();
		yApiParams.setName("json");
		yApiParams.setValue("");
		return yApiParams;
		
	}
	
	public YApiQueryPath initYApiQueryPath() {
		YApiQueryPath yApiQueryPath = new YApiQueryPath();
		yApiQueryPath.setPath("/userHost/bindHost");
		List<YApiParams> yApiParams = new ArrayList<>();
		yApiParams.add(initYApiParams());
		yApiQueryPath.setParams(yApiParams);
		return yApiQueryPath;
		
	}
	/**
	 * 初始化请求提，这里录入参数
	 * @return
	 */
	public YApiRequestBodyForm initYApiRequestBodyForm() {
		YApiRequestBodyForm yApiRequestBodyForm = new YApiRequestBodyForm();
		yApiRequestBodyForm.setExample("");//参数值
		yApiRequestBodyForm.setName("参数名1");
		yApiRequestBodyForm.setRequired("1");//是否必须1必须0不必须
		yApiRequestBodyForm.setType("text");
		return yApiRequestBodyForm;
		
	}
	/**
	 * 初始化请求头
	 * @return
	 */
	public YApiRequestHeader initYApiRequestHeader() {
		YApiRequestHeader yApiRequestHeader = new YApiRequestHeader();
		yApiRequestHeader.setName("Content-Type");
		yApiRequestHeader.setRequired("1");
		yApiRequestHeader.setValue("application/x-www-form-urlencoded");
		return yApiRequestHeader;
	}
	/**
	 * 初始化请求query
	 * @return
	 */
	public YApiRequestQuery initYApiRequestQuery() {
		YApiRequestQuery yApiRequestQuery = new YApiRequestQuery();
		yApiRequestQuery.setDesc("");
		yApiRequestQuery.setName("query_value");
		yApiRequestQuery.setRequired("1");
		return yApiRequestQuery;
	}
	
	/**
	 * 初始化返回体
	 * @return
	 */
	public YApiResponseBody initYApiResponseBody() {
		YApiResponseBody yApiResponseBody = new YApiResponseBody();
		yApiResponseBody.setProperties(new ArrayList<String>());
		yApiResponseBody.setTitle("empty object");
		yApiResponseBody.setType("object");
		return yApiResponseBody;
	}
	
	
}


