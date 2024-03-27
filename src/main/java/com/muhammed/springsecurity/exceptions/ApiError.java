package com.muhammed.springsecurity.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private int statusCode;
    private Map<String, String> errors;
    private String path;
    private LocalDateTime localDateTime;

}
