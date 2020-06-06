package com.chanyi.constant;

import com.alibaba.fastjson.JSON;

public class JsonResult {
  public static final int SUCCESSCODE = 200;
  public static final int FAILCODE = 400;
  private int code = 200;
  private String msg = "操作成功";
  private Object data;

  public int getCode() {
    return this.code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return this.data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public JsonResult() {
    this.code = 200;
  }

  public JsonResult(Object data) {
    this.data = data;
    this.code = 200;
  }

  public JsonResult(Object data, String msg) {
    this.data = data;
    this.code = 200;
    this.msg = msg;
  }

  public JsonResult(Object data, String msg, int code) {
    this.data = data;
    this.code = code;
    this.msg = msg;
  }

  public JsonResult(String msg, int code) {
    this.code = code;
    this.msg = msg;
  }

  public JsonResult(int code) {
    this.code = code;
  }

  public String toJson() {
    return JSON.toJSONString(this);
  }

  public String toString() {
    return this.toJson();
  }

  public void check(String tipMessage) {
    if (this.code != 200) {
      throw new RuntimeException(tipMessage + ":" + this.getMsg());
    }
  }

  public static JsonResult code(int code) {
    return new JsonResult(code);
  }

  public static JsonResult data(Object data) {
    return new JsonResult(data);
  }

  public static JsonResult msg(String msg, int code) {
    return new JsonResult(msg, code);
  }

  public static JsonResult data(Object data, String msg, int code) {
    return new JsonResult(data, msg, code);
  }

  public static JsonResult data(Object data, String msg) {
    return new JsonResult(data, msg);
  }
}
