package com.backup.zgqsgw.Config;

import lombok.Data;

/**
 * @author hwb
 * @create 2019/1/9 15:14
 */
@Data
public class GlobalException extends Exception {


    private String message;

    public GlobalException() {
    }

    public GlobalException(String message) {
        super(message);
    }
}
