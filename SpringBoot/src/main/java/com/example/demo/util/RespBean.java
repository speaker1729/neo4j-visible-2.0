package com.example.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    public static final int SUCCESS_CODE = 200; // 成功状态码
    public static final int ERROR_CODE = 500; // 错误状态码

    private int code;
    private String message;
    private Object object;

    /**
     * 成功返回结果
     *
     * @param message 相应的提示信息
     * @param object  准备返回对象
     * @return 成功的响应对象
     */
    public static RespBean success(String message, Object object) {
        return new RespBean(SUCCESS_CODE, message, object);
    }

    /**
     * 失败返回结果
     *
     * @param message 相应的提示信息
     * @param object  准备返回对象
     * @return 失败的响应对象
     */
    public static RespBean error(String message, Object object) {
        return new RespBean(ERROR_CODE, message, object);
    }
}
