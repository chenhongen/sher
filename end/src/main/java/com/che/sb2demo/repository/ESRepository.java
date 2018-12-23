package com.che.sb2demo.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchResult;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.che.sb2demo.pojo.TableKVVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ESRepository {
	private static final Logger log = LoggerFactory.getLogger(ESRepository.class);
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	/**
	 * 逐条插入指定index, type下
	 * 
	 * @param index
	 * @param type
	 * @param jsonStr
	 */
//	@SuppressWarnings("unchecked")
//	public boolean save(String index, String type, String jsonStr) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		Client client = elasticsearchTemplate.getClient();
//		List elements = null;
//		
//		try {
//			elements = objectMapper.readValue(jsonStr, List.class);
//			
//			for(Object e: elements) {
//				log.info(objectMapper.writeValueAsString(e));
//				IndexResponse resp = client.prepareIndex(index, type).setSource(objectMapper.writeValueAsString(e), XContentType.JSON).get();
//				log.info(resp.getId());
//			};
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//        return true;
//		
//	}
	
	/**
	 * 插入
	 * @param index
	 * @param type
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String insert(String index, String type, String jsonStr) {

		IndexResponse resp = elasticsearchTemplate.getClient().prepareIndex(index, type).setSource(jsonStr, XContentType.JSON).get();

        return resp.getId();
	}
	
	/**
	 * 批量插入
	 * @param index
	 * @param type
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean save(String index, String type, String jsonStr) {
		ObjectMapper objectMapper = new ObjectMapper();
		List queries = new ArrayList();
		List updateQueries = new ArrayList();
		List elements = null;
		
		try {
			elements = objectMapper.readValue(jsonStr, List.class);
			
			for(Object e: elements) {
				log.info(objectMapper.writeValueAsString(e));
				
				// 
				LinkedHashMap table = (LinkedHashMap) e;
				String id = (String) table.get("id");

				if(StringUtils.isEmpty(id)) {
					IndexQuery indexQuery = new IndexQuery();
			        indexQuery.setIndexName(index);
			        indexQuery.setType(type);
			        indexQuery.setSource(objectMapper.writeValueAsString(e));
			        queries.add(indexQuery);
				} else {
					UpdateRequest updateRequest = new UpdateRequest();
					updateRequest.doc(table);
					
					UpdateQuery updateQuery = new UpdateQuery();
					updateQuery.setIndexName(index);
					updateQuery.setType(type);
					updateQuery.setId(id);
					updateQuery.setUpdateRequest(updateRequest);
					
					updateQueries.add(updateQuery);
				}
			};
			
			if(queries.size() > 0)
				elasticsearchTemplate.bulkIndex(queries);
			
			if(updateQueries.size() > 0)
				elasticsearchTemplate.bulkUpdate(updateQueries);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

        return true;
		
	}
	
	/**
	 * 根据id更新
	 * @param index
	 * @param type
	 * @param id
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String update(String index, String type, String id, String jsonStr) {

		UpdateResponse resp = elasticsearchTemplate.getClient().prepareUpdate(index, type,id)
                .setDoc(jsonStr, XContentType.JSON)
                .get();

        return resp.getId();
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void deleteById(String index, String type, String id) {
		elasticsearchTemplate.getClient().prepareDelete(index, type, id).get();
	}
	
//	public List query(String keyword, @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
//		SearchQuery searchQuery = new NativeSearchQueryBuilder()
//				.withQuery(QueryBuilders.queryStringQuery(keyword))
//                .withPageable(pageable)
//                .build();
//
//		return elasticsearchTemplate.queryForList(searchQuery, TableKVVO.class);
//	}
	public List query(String index, String type, String keyword) {
		SearchQuery searchQuery = null;
		if(StringUtils.isEmpty(keyword)) {
			searchQuery = new NativeSearchQueryBuilder()
					.withIndices(index)
					.withTypes(type)
	                .build();
		} else {
			searchQuery = new NativeSearchQueryBuilder()
					.withIndices(index)
					.withTypes(type)
					.withQuery(QueryBuilders.queryStringQuery(keyword))
	                .build();
		}
		return elasticsearchTemplate.queryForList(searchQuery, TableKVVO.class);
	}
	
	
	public List queryAll(String index, String type) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices(index)
				.withTypes(type)
                .build();
		
//		return elasticsearchTemplate.queryForList(searchQuery, Object.class);
		
		List<Object> list = new ArrayList<>();
		elasticsearchTemplate.query(searchQuery, new ResultsExtractor<SearchResult>() {

		    @Override
		    public SearchResult extract(SearchResponse response) {
		    	response.getHits().forEach(hit -> {
		    		Map<String, Object> map = hit.getSourceAsMap();
		    		map.put("id", hit.getId());
		    		
		    		list.add(map);
		    	});
		    	
				return null;
		    }
		});
		
		return list;
	}
	
	/**
	 * 关键词筛选
	 * @param keyword
	 * @return
	 */
	public List queryByKeyword(String keyword) {
		ObjectMapper objectMapper = new ObjectMapper();
		keyword = "*" + keyword + "*";
		
		SearchResponse response = elasticsearchTemplate.getClient().prepareSearch()
//				  .setTypes()
//				  .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				  .setPostFilter(QueryBuilders.queryStringQuery(keyword))
				  .execute()
				  .actionGet();
		List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
		List results = new ArrayList();
		searchHits.forEach(
		  hit -> {
			//hit.getIndex();
			//hit.getType();
			try {
				results.add(objectMapper.readValue(hit.getSourceAsString(), Object.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return results;
    }
	
}
