package com.chanyi.server;

import com.chanyi.constant.ClassTypes;
import com.chanyi.constant.ErrorCodeException;
import com.chanyi.constant.JsonResult;
import com.chanyi.constant.YApiUrl;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanyi.annotation.YApiDesc;
import com.chanyi.model.AddFailedObj;
import com.chanyi.model.AddYApiCat;
import com.chanyi.model.AddYApiCatResponse;
import com.chanyi.model.AddYApiResponse;
import com.chanyi.model.AddYApiVo;
import com.chanyi.model.CustomClassFieldInfo;
import com.chanyi.model.CustomClassMethod;
import com.chanyi.model.CustomMethodParam;
import com.chanyi.model.CustomParamInfo;
import com.chanyi.model.YApiRequestBodyForm;
import com.chanyi.model.YApiRequestHeader;
import com.chanyi.model.YApiRequestQuery;
import com.chanyi.util.ClassUtil;
import com.chanyi.util.YApiUtil;

@Component
public class YApiDescService {

  @Autowired
  private YApiUtil yApiUtil;

  @Value(value = "${YApiDesc.yApiToken:c3ffe90af735cf6fa74620f69d5d43636f70f368955352d19038afc8ebda386b}")
  private String yApiToken;

  @Value(value = "${YApiDesc.cookie:_yapi_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjE1LCJpYXQiOjE1OTE0MzY4MjIsImV4cCI6MTU5MjA0MTYyMn0.rz1ab2LbX3wxdASX69RbAyp-hMQbB-8lZgsvpC7QVE0; _yapi_uid=15}")
  private String cookie;

  private final static Log logger = LogFactory.getLog(YApiDescService.class);

  /**
   * 批量录入接口到YApi中
   */
  public void geneYApi(String packagePath, int projectId) {
    //首先添加所有分组
    Map<String, String> catMap = addYApiCat(getCat(packagePath, projectId));
    logger.info("catMap:" + catMap.toString());
    //然后添加所有接口
    addYApi(getYApi(packagePath, catMap));
  }

  /**
   * 录入单个接口到YApi中
   */
  public boolean addYApi(List<AddYApiVo> addYApiVoList) {
    if (CollectionUtils.isEmpty(addYApiVoList)) {
      return false;
    }
    addYApiVoList.forEach(addYApiVo -> {
      HttpClient client = new HttpClient();
      String response = null;
      PostMethod method = initPostMethod(YApiUrl.ADD_INTERFACE, JSONObject.toJSONString(addYApiVo));
      try {
        client.executeMethod(method);
        response = method.getResponseBodyAsString();
        AddYApiResponse addYApiResponse = JSON.parseObject(response, AddYApiResponse.class);
        if (addYApiResponse.getErrcode() != 0) {
          logger.info("接口添加失败：" + addYApiResponse);
          //如果是已存在的接口则忽略此次添加
          if (addYApiResponse.getErrcode() != 40022) {
            throw new ErrorCodeException("添加接口错误");
          }
        } else {
          logger.info("接口添加成功：" + addYApiResponse);
        }
      } catch (Exception e) {
        logger.info("exception", e);
        throw new ErrorCodeException(e.getMessage());
      }
    });
    return true;
  }

  /**
   * 添加分组group
   *
   * @return 返回 Map<类名,分组id>
   */
  public Map<String, String> addYApiCat(List<AddYApiCat> addYApiCatList) {
    Map<String, String> map = new HashMap<>();
    if (CollectionUtils.isEmpty(addYApiCatList)) {
      return map;
    }
    addYApiCatList.forEach(addYApiCat -> {
      HttpClient client = new HttpClient();
      PostMethod method = initPostMethod(YApiUrl.ADD_CAT, JSONObject.toJSONString(addYApiCat));
      try {
        client.executeMethod(method);
        AddYApiCatResponse addYApiCatResponse = JSON
            .parseObject(method.getResponseBodyAsString(), AddYApiCatResponse.class);
        if (addYApiCatResponse.getErrcode() == 0) {
          logger.info("分组添加成功：" + method.getResponseBodyAsString());
          map.put(addYApiCat.getClassName(), addYApiCatResponse.getData().get_id() + "");
        } else {
          logger.info("分组添加失败：" + method.getResponseBodyAsString());
        }
      } catch (Exception e) {
        throw new ErrorCodeException("添加分组失败" + e.getMessage());
      }
    });
    return map;
  }

  /**
   * 获取包下的所有带有YApiDesc注解的方法
   */
  private List<CustomClassMethod> getClassMethod(String packagePath) throws ClassNotFoundException {
    List<CustomClassMethod> customClassMethodList = new ArrayList<CustomClassMethod>();
    Set<Class<?>> classSets = ClassUtil.getClassSet(packagePath);
    logger.info("classSets:" + classSets.toString());
    for (Class<?> set : classSets) {
      CustomClassMethod customClassMethod = new CustomClassMethod();
      // 获取类名
      customClassMethod.setClassName(set.getName());
      Method[] methods = set.getDeclaredMethods();
      logger.info("methods:" + Arrays.toString(methods));
      List<CustomMethodParam> customMethodParamList = new ArrayList<CustomMethodParam>();
      for (Method method : methods) {
        // 获取方法上的注解信息
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
          YApiDesc yApiDesc = method.getAnnotation(YApiDesc.class);
          logger.info("api:" + (yApiDesc == null ? "" : yApiDesc.toString()));
          CustomMethodParam customMethodParam = new CustomMethodParam();
          customMethodParam.setMethod(method);
          customMethodParam.setPath(yApiDesc.path());
          customMethodParam.setTitle(yApiDesc.title());
          String typeName = parameterTypes[i].getSimpleName();
          logger.info("typeName:"+typeName);
          //基本类型
          if (ClassTypes.ALL_BASIC_CLASS_TYPES.contains(typeName)) {
            if (yApiDesc != null) {
              List<CustomParamInfo> customParamInfoList = new ArrayList<CustomParamInfo>();
              customParamInfoList = getMethodParamInfo(method, yApiDesc.paramsInfo());
              customMethodParam.setCustomParamInfoList(customParamInfoList);
              customMethodParamList.add(customMethodParam);
            }
          } else {
            //自定义类
            logger.info(parameterTypes[i].getName());
            Class customClass = Class.forName(parameterTypes[i].getName());
            Field[] fields = customClass.getDeclaredFields();
            List<CustomParamInfo> customParamInfoList = getCustomMethodParamInfo(fields);
            customMethodParam.setCustomParamInfoList(customParamInfoList);
            customMethodParamList.add(customMethodParam);
          }
        }
      }
      if (!CollectionUtils.isEmpty(customMethodParamList)) {
        customClassMethod.setCustomMethodParamList(customMethodParamList);
        customClassMethodList.add(customClassMethod);
      }
    }
    return customClassMethodList;
  }

  /**
   * 获取MethodParamInfoList
   */
  private List<CustomParamInfo> getMethodParamInfo(Method method, String[] paramDesc)
      throws ClassNotFoundException {
    ArrayList<CustomParamInfo> customParamInfoList = new ArrayList<CustomParamInfo>();
    Parameter[] parameters = method.getParameters();
    if (paramDesc.length != parameters.length) {
      throw new ErrorCodeException("方法:" + method.getName() + "，参数个数和参数说明的长度不相等，请修改后重试");
    }
    for (int i = 0; i < parameters.length; i++) {
      logger.info("parameters[i].getType().toString()" + parameters[i].getType().toString());
      CustomParamInfo methodParamInfo = new CustomParamInfo();
      methodParamInfo.setDesc(paramDesc[i]);
      methodParamInfo.setName(parameters[i].getName());
      methodParamInfo.setType(parameters[i].getType().toString());
      customParamInfoList.add(methodParamInfo);
    }
    return customParamInfoList;
  }

  /**
   * 获取自定义类的MethodParamInfoList
   */
  private List<CustomParamInfo> getCustomMethodParamInfo(Field[] fields)
      throws ClassNotFoundException {
    ArrayList<CustomParamInfo> customParamInfoList = new ArrayList<CustomParamInfo>();
    for (int i = 0; i < fields.length; i++) {
      YApiDesc yApiDesc = fields[i].getAnnotation(YApiDesc.class);
      CustomParamInfo methodParamInfo = new CustomParamInfo();
      methodParamInfo.setDesc(yApiDesc.filed());
      methodParamInfo.setName(fields[i].getName());
      methodParamInfo.setType(fields[i].getType().toString());
      customParamInfoList.add(methodParamInfo);
    }
    return customParamInfoList;
  }

  /**
   * 类信息转换为YApi信息
   */
  private List<AddYApiVo> classMethod2AddYApiVo(CustomClassMethod customClassMethod, String catId)
      throws ClassNotFoundException {
    List<AddYApiVo> volist = new ArrayList<AddYApiVo>();
    List<CustomMethodParam> customMethodParamList = customClassMethod.getCustomMethodParamList();
    if (CollectionUtils.isEmpty(customMethodParamList)) {
      return null;
    }
    customMethodParamList.forEach(method -> {
      AddYApiVo vo = new AddYApiVo();
      vo.setCatid(catId);
      vo.setDesc(method.getTitle());
      vo.setMessage("");
      vo.setMethod("POST");
      vo.setPath(method.getPath());
      vo.setStatus("undone");
      vo.setSwitch_notice(false);
      vo.setTitle(method.getTitle());
      vo.setToken(yApiToken);
      vo.setRes_body_type("json");
      // 获取注解下的参数
      List<YApiRequestBodyForm> req_body_forms = new ArrayList<YApiRequestBodyForm>();
      req_body_forms.add(yApiUtil.initYApiRequestBodyForm());
      List<YApiRequestHeader> req_headers = new ArrayList<YApiRequestHeader>();
      req_headers.add(yApiUtil.initYApiRequestHeader());
      List<String> req_params = new ArrayList<>();
      List<YApiRequestQuery> req_query = new ArrayList<YApiRequestQuery>();
      String res_body = "{\"code\":200,\"msg\":\"\",\"date\":\"\"} ";
      method.getCustomParamInfoList().forEach(params -> {
        // 参数写入requestQuery
        YApiRequestQuery query = new YApiRequestQuery();
        query.setName(params.getName());
        query.setRequired("1");
        query.setDesc(params.getDesc());
        req_query.add(query);
      });
      vo.setReq_body_form(req_body_forms);
      vo.setReq_headers(req_headers);
      vo.setReq_params(req_params);
      vo.setReq_query(req_query);
      vo.setRes_body(res_body);
      volist.add(vo);
    });
    return volist;
  }

  /**
   * 获取自定义类上的注释@DescAnnotation的属性
   */
  private List<CustomClassFieldInfo> getModelFieldDesc(String classPath)
      throws ClassNotFoundException {
    Class<?> clazz = Class.forName(classPath);
    // 获取类名
    // String strName01 = clazz.getName();//
    // 获取完整类名com.sg.myReflection.bean.User
    // String classSimpleName = clazz.getSimpleName();// 直接获取类名 User
    // 获取属性
    Field[] fields = clazz.getDeclaredFields(); // 返回所有的属性
    // Field field03 = clazz.getDeclaredField("id"); // 获取属性为id的字段
    List<CustomClassFieldInfo> list = new ArrayList<CustomClassFieldInfo>();
    for (Field field : fields) {
      CustomClassFieldInfo customClassFieldInfo = new CustomClassFieldInfo();
      customClassFieldInfo.setName(field.getName());
      customClassFieldInfo.setDesc("");
      list.add(customClassFieldInfo);
    }
    return list;
  }

  /**
   * 获取包下所有类名，返回字符串，多类名之间用逗号隔开
   */
  private List<String> getModelString(String packagePath) throws ClassNotFoundException {
    Set<Class<?>> set = ClassUtil.getClassSet(packagePath);
    List<String> classNames = new ArrayList<String>();
    set.forEach(c -> {
      classNames.add(c.getName());
    });
    // 获取类名
    // String strName01 = clazz.getName();//
    // 获取完整类名com.sg.myReflection.bean.User
    // String classSimpleName = clazz.getSimpleName();// 直接获取类名 User
    // 获取属性
    return classNames;
  }

  //初始化PostMethod
  private PostMethod initPostMethod(String url, String jsonStr) {
    PostMethod method = new PostMethod(url);
    method.setRequestHeader("Content-type", "application/json; charset=UTF-8");
    method.setRequestHeader("Cookie", cookie);
    RequestEntity requestEntity = null;
    try {
      requestEntity = new StringRequestEntity(jsonStr, "application/json", "UTF-8");
    } catch (Exception e) {
      logger.info("String Request Entity 创建失败！", e);
      throw new ErrorCodeException("String Request Entity 创建失败！");
    }
    method.setRequestEntity(requestEntity);
    return method;
  }

  //获取接口信息
  private List<AddYApiVo> getYApi(String packagePath, Map<String, String> catMap) {
    List<AddYApiVo> voList = new ArrayList<>();
    List<CustomClassMethod> customClassMethodList = new ArrayList<>();
    try {
      customClassMethodList = getClassMethod(packagePath);
      logger.info("customClassMethodList:"+customClassMethodList);
    } catch (Exception e) {
      logger.info("exception", e);
      return voList;
    }
    if (CollectionUtils.isEmpty(customClassMethodList)) {
      throw new ErrorCodeException("当前包下没有使用注释YApiDesc的类");
    }
    customClassMethodList.forEach(classInfo -> {
      String className = classInfo.getClassName()
          .substring(classInfo.getClassName().lastIndexOf(".") + 1);
      try {
        voList.addAll(classMethod2AddYApiVo(classInfo, catMap.get(className)));
      } catch (Exception e) {
        logger.info("exception", e);
        throw new ErrorCodeException(e.getMessage());
      }
    });
    return voList;
  }

  //获取分组名
  private List<AddYApiCat> getCat(String packagePath, Integer projectId) {
    List<AddYApiCat> yApiCatList = new ArrayList<>();
    Set<Class<?>> classSets = ClassUtil.getClassSet(packagePath);
    if (CollectionUtils.isEmpty(classSets)) {
      logger.info("包下类为空");
      return yApiCatList;
    }
    //如果类上有注释@YApiDesc而且注释的值groupName不为空
    classSets.forEach(clazz -> {
      if (clazz.isAnnotationPresent(YApiDesc.class) && !""
          .equals(clazz.getAnnotation(YApiDesc.class).groupName())) {
        AddYApiCat addYApiCat = new AddYApiCat();
        YApiDesc yApiDesc = clazz.getAnnotation(YApiDesc.class);
        addYApiCat.setClassName(clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1));
        addYApiCat.setName(yApiDesc.groupName());
        addYApiCat.setDesc(yApiDesc.groupDesc());
        addYApiCat.setProject_id(projectId);
        yApiCatList.add(addYApiCat);
      }
    });
    return yApiCatList;
  }

}
