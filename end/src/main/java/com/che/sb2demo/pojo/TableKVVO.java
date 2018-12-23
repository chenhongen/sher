package com.che.sb2demo.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.che.sb2demo.common.CONSTANT;

@Document(indexName = CONSTANT.INDEX_KV, type = CONSTANT.TYPE_KV)
public class TableKVVO {
	@Id
	private String id;
	private String name;
	private String code;
	private String dimension;
	private String keys;
	private String values;
	private String remark;
	private String date;
	
	
	
	public TableKVVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableKVVO(String id, String name, String code, String dimension, String keys, String values,
			String remark, String date) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.dimension = dimension;
		this.keys = keys;
		this.values = values;
		this.remark = remark;
		this.date = date;
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDimension() {
		return dimension;
	}
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	public String getKeys() {
		return keys;
	}
	public void setKeys(String keys) {
		this.keys = keys;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
