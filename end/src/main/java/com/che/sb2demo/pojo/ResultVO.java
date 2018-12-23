package com.che.sb2demo.pojo;

import com.che.sb2demo.common.CONSTANT;
import com.che.sb2demo.common.CONSTANT.ResultCode;

/**
 * 返回数据封装
 * @author che
 *
 */
public class ResultVO {
	private int code;
	private String message;
	private Object data;
	
	public ResultVO setCode(ResultCode resultCode) { 
		this.code = resultCode.code; 
		return this; 
	}
	
	public ResultVO() {
		super();
	}
	
	public ResultVO ok(Object data) { 
		this.code = ResultCode.SUCCESS.code; 
		this.message = CONSTANT.SUCCESS;
		this.data = data;
		
		return this; 
	}
	
	public ResultVO ok() { 
		this.code = ResultCode.SUCCESS.code; 
		this.message = CONSTANT.SUCCESS;
		
		return this; 
	}
	
	public ResultVO error() { 
		this.code = ResultCode.FAIL.code; 
		this.message = CONSTANT.FAIL;
		
		return this; 
	}
	
	public ResultVO error(ResultCode resultCode, String message) { 
		this.code = resultCode.code;
		this.message = message;
		
		return this; 
	}


	public void setCode(int code) {
		this.code = code;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void setData(Object data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}
}
