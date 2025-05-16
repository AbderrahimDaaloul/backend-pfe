package com.daaloul.BackEnd.DTOs;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDTO {
    private String type;
    private String url;
    private Integer version=0;


}
