package com.sky.dto;

import com.sky.entity.ShoppingCart;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;
    private Long setmealId;
    private String dishFlavor;

}
