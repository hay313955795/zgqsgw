package com.backup.zgqsgw.Vo;

import lombok.Data;

@Data
public class FileCreateVo {
    private boolean isSuccess;
    private String FilePath;

    public FileCreateVo(String filePath,boolean isSuccess) {
        this.isSuccess = isSuccess;
        FilePath = filePath;
    }
}
