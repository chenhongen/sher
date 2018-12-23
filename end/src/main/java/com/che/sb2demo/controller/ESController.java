package com.che.sb2demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.IndexNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.che.sb2demo.common.CONSTANT.ResultCode;
import com.che.sb2demo.pojo.ResultVO;
import com.che.sb2demo.repository.ESRepository;


@RestController
@RequestMapping("/table")
public class ESController {

	@Autowired
	private ESRepository es;
	
	@RequestMapping(method=RequestMethod.POST)
	public String insertTable(String index, String type, String jsonStr) {
		
		if(!"".equals(jsonStr))
			es.save(index, type, jsonStr);

		
		return null;
	}
	
	@RequestMapping("list")
	public ResultVO listTable(String index, String type) {
		
		if(StringUtils.isEmpty(index) || StringUtils.isEmpty(type)) {
			return new ResultVO().error();
		}
		
		List<?> list = null;
		
		try {
			list = es.queryAll(index, type);
		} catch (IndexNotFoundException e) {
//			e.printStackTrace();
			return new ResultVO().error(ResultCode.NOT_FOUND, "无该表数据");
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("list", list);
//		data.put("total", total);
		return new ResultVO().ok(data);
		
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public String deleteTable(String index, String type, String id) {
		
		if(!"".equals(id))
			es.deleteById(index, type, id);

		return null;
	}
}
