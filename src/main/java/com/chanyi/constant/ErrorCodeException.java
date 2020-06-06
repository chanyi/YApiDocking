package com.chanyi.constant;

/**
 * @program: YApiDocking
 * @description: 错误码异常类
 * @author: lilei
 * @create: 2020/06/05
 **/
public class ErrorCodeException extends RuntimeException{
  private static final long serialVersionUID = -2320399934788651830L;
  private int code = 400;

  public int getCode() {
    return this.code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public ErrorCodeException(String message) {
    super(message);
  }

  public ErrorCodeException(String message, int code) {
    super(message);
    this.code = code;
  }
}
