package com.balinasoft.firsttask.dto;

import com.balinasoft.firsttask.system.json.UnixTimestampSereliazer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDtoOut {

    @ApiModelProperty(required = true)
    int id;

    @ApiModelProperty(required = true)
    String name;

}
