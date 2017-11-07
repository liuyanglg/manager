package com.aisino.admin.global.paging;

import java.util.Map;

public class Page<T> {

	/**
	 * 起始下标,在有当前页码的时候允许为空，
	 */
	private Integer startIndex;
	/**
	 * 结束下标,不同数据库分页方式不同，可能为空
	 */
	private Integer endIndex;
	/**
	 * 每页步长，必须是正整数
	 */
	private int pageSize = 15;
	/**
	 * 当前页码，网页传递参数方式不同，可能为空，当前页码与起始下标不能同时为空
	 */
	private Integer curPageIndex;
	/**
	 * 排序字段
	 */
	private String orderCol;
	public String getOrderCol() {
		return orderCol;
	}
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}
	/**
	 * 数据总量
	 */
	private long totalSize;
	/**
	 * 分页查询对应的参数对象
	 */
	private T paramObj;
	
	public Page(T t){
		this.paramObj = t;
	}
	
	public T getParamObj() {
		return paramObj;
	}
	public void setParamObj(T paramObj) {
		this.paramObj = paramObj;
	}
	/**
	 * 数据库类型:1-mysql,2-oracle,3-db2
	 */
	private int dbType = 1;
	
	public int getDbType() {
		return dbType;
	}
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}
	/**
	 * 一些特殊查询条件，在paramObj中无法对应的字段，如时间范围等
	 */
	@SuppressWarnings("rawtypes")
	private Map otherParams;
	
	@SuppressWarnings("rawtypes")
	public Map getOtherParams() {
		return otherParams;
	}
	@SuppressWarnings("rawtypes")
	public void setOtherParams(Map otherParams) {
		this.otherParams = otherParams;
	}
	public Integer getStartIndex() {
		if(startIndex==null&&curPageIndex!=null){
			startIndex = (curPageIndex-1)*pageSize;
		}
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Integer getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(Integer endIndex) {
		if(endIndex<0){
			throw new IllegalArgumentException("endIndex can not be negative integer");
		}
		this.endIndex = endIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		if(pageSize<=0){
			throw new IllegalArgumentException("pageSize must be positive integer");
		}
		this.pageSize = pageSize;
	}
	public Integer getCurPageIndex() {
		return curPageIndex;
	}
	public void setCurPageIndex(Integer curPageIndex) {
		if(curPageIndex<0){
			throw new IllegalArgumentException("curPageIndex can not be negative integer");
		}
		this.curPageIndex = curPageIndex;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		if(totalSize<0){
			throw new IllegalArgumentException("totalSize can not be negative");
		}
		this.totalSize = totalSize;
	}
}
