package com.balinasoft.firsttask.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    private Integer status;

    private String message;

    public ErrorDto(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
