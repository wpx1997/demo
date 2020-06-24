package com.joinwe.demo.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    USER_NOT_EXISTENCE(2001,"用户名或密码不正确"),
    UNIFIED_ORDER_FAIL(2002,"唤起支付失败"),
    ;

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
