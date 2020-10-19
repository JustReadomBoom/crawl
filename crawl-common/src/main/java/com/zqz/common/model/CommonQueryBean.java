package com.zqz.common.model;


import lombok.Data;

/**
 * 通用分页查询条件Bean
 */
@Data
public class CommonQueryBean {
	private Integer pageNum;
	private Integer pageSize;
	private Integer start;
	private String sort;
	private String order;
	private Integer page;
	private Integer rows;
}