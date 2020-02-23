package com.ncu.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找的问题不在了，换个试试吧"),
    QUESTION_UPDATE_FAIL(2002,"更新问题失败，检查所有参数后重试"),

    TARGET_PARAM_NOT_FOUND(3001, "未选中问题进行回复, 请重新操作"),
    TYPE_PARAM_WRONG(3002,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(3003,"回复的评论不在了, 刷新后试试"),

    NO_LOGIN(4001, "当前操作需要登陆, 请登陆后重试"),

    SYS_ERROR(5001, "未知错误, 联系下管理员 ?"),
    ;

    private String message;
    private Integer code;


    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
