package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HistoryOrdersPageQueryDTO implements Serializable {
    private int page;

    private int pageSize;

    private Integer status;
}
