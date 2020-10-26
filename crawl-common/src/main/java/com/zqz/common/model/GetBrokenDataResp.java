package com.zqz.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 16:29 2020/10/26
 */
@Data
public class GetBrokenDataResp implements Serializable {

    private static final long serialVersionUID = -6307678601510341110L;

    @JsonFormat(pattern = "MM/dd", timezone="GMT+8")
    private Date processDate;

    private BigDecimal currentPrice;
}
