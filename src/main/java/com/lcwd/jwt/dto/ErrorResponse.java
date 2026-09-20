package com.lcwd.jwt.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {

    private int           status;
    private String        error;
    private String        message;
    private LocalDateTime timestamp;
    private String        path;

}