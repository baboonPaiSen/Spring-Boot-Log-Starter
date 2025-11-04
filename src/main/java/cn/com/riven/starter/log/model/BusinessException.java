package cn.com.riven.starter.log.model;

import cn.com.riven.starter.log.enums.CommonErrorCode;
import cn.com.riven.starter.log.enums.ExceptionEnum;
import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 抛出的异常
     */
    private Throwable throwable;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 异常消息
     */
    private String msg ;


    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 使用枚举传参
     *
     * @param errorCode 异常枚举
     */
    public BusinessException(Throwable cause , CommonErrorCode errorCode) {
        super(errorCode.getMessage(),cause);
        this.code = errorCode.getCode();
    }

    public BusinessException(ExceptionEnum exceptionEnum) {
        this.msg = exceptionEnum.getMsg();
        this.code= exceptionEnum.getCode();
    }


    public BusinessException(String message) {
        this.msg = message;
        this.code= String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    public BusinessException(ExceptionEnum exceptionEnum ,Throwable throwable) {
        super(exceptionEnum.getMsg(),throwable);
        this.msg = exceptionEnum.getMsg();
        this.throwable = throwable;
        this.code= exceptionEnum.getCode();
    }


    public BusinessException(String message,Throwable throwable) {
        super(message,throwable);
        this.msg = message;
        this.throwable = throwable;
        this.code= String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    public BusinessException(String message,Throwable throwable,Object[] args) {
        super(message,throwable);
        this.msg = message;
        this.throwable = throwable;
        this.code= String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        this.args= args;
    }



    public BusinessException(ExceptionEnum exceptionEnum, Throwable throwable, Object[] args) {
        super(exceptionEnum.getMsg(),throwable);
        this.msg = exceptionEnum.getMsg();
        this.code= exceptionEnum.getCode();
        this.throwable = throwable;
        this.args= args;
    }


    @Override
    public String getMessage() {
        return msg;
    }
}