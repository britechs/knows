package org.knows.service;

public class RestResponse<T> {
	public static String CODE_SUCCESS="0";
	public static String CODE_INVALID="1001";
	
	private String errorCode;
	private T respsonse;
	
	public RestResponse(T re, String code)
	{
		this.respsonse=re;
		this.errorCode=code;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public T getRespsonse() {
		return respsonse;
	}
	public void setRespsonse(T respsonse) {
		this.respsonse = respsonse;
	}
	

}
