package com.ProductService.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
