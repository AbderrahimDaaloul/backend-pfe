package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.enums.FileType;
import lombok.Data;

@Data
public class UploadedFileDTO {
    private FileType type;
    private String url;
    private int version;
}
