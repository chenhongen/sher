package com.che.sb2demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.che.sb2demo.common.CONSTANT;
import com.che.sb2demo.pojo.ResultVO;
import com.che.sb2demo.pojo.TableKVVO;
import com.che.sb2demo.repository.ESRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tablekv")
public class TableController {
	
	@Autowired
	private ESRepository es;
	
	/**
	 * 增加
	 * @param index
	 * @param type
	 * @param jsonStr
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String insertTable(@RequestBody TableKVVO kv) {
		ObjectMapper objectMapper = new ObjectMapper();
		String index = CONSTANT.INDEX_KV; //kv.getCode();
		String type = CONSTANT.TYPE_KV; //kv.getDimension();
		
		kv.setDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		
		try {
			return es.insert(index, type, objectMapper.writeValueAsString(kv));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 更新
	 * @param kv
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public String updateTable(@RequestBody TableKVVO kv) {
		ObjectMapper objectMapper = new ObjectMapper();
		String index = CONSTANT.INDEX_KV; //kv.getCode();
		String type = CONSTANT.TYPE_KV; //kv.getDimension();
		String id = kv.getId();
		
		try {
			return es.update(index, type, id, objectMapper.writeValueAsString(kv));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 查询/加载
	 * @param keyword
	 * @return
	 */
	@RequestMapping("list")
	public ResultVO listTable(@RequestBody Map<String, String> params) {
		String index = CONSTANT.INDEX_KV; //params.get("index");
		String type = CONSTANT.TYPE_KV; //params.get("type");
		String keyword = params.get("keyword");
		
		if(StringUtils.isEmpty(index) || StringUtils.isEmpty(type)) {
			return new ResultVO().error();
		}
		
		@SuppressWarnings("unchecked")
		List<TableKVVO> list = es.query(index, type, keyword);
		
		Map<String, Object> data = new HashMap<>();
		data.put("list", list);
//		data.put("total", total);
		return new ResultVO().ok(data);
		
	}
	
	
}
