package com.finlay.scaffold.reliable.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Finlay
 * @description:
 * @date: 2020-07-23 10:19 上午
 */
@Data
public class Activity implements Serializable {
    private static final long serialVersionUID = 7775661633817222341L;

    private String id;
    private BigDecimal pay;
}
