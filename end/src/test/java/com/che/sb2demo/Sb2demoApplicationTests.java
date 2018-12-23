package com.che.sb2demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import com.che.sb2demo.pojo.TableKVVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sb2demoApplicationTests {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void contextLoads() {
		String jsonStr = "{\n" +
                "    \"title\": \"你好，2019\",\n" +
                "    \"author\": \"che\"\n" +
                "}";
		String index = "week";
		String type = "news";
		String id = "1"; // 为空时默认生成 
		
//		IndexResponse resp = elasticsearchTemplate.getClient().prepareIndex(index, type, id).setSource(jsonStr, XContentType.JSON).get();
//		System.out.println(resp.getId()); // 输出id
		
//		getById(index, type, id); // 主键查询
		
//		deleteById(index, type, "vnPhxWcBWf2a5lU3dTyq"); // 主键删除
//		deleteByType(index, type); // 删除type下全部
		
//		getAll(CONSTANT.INDEX_KV, CONSTANT.TYPE_KV); // 查询全部
	
		List list = queryByKeyword("h");
		System.out.println(list.toString());
		
//		List<TableKVVO> list = list(index, type, "EUR");
//		System.out.println(list.size());
	}
	
	public void getById(String index, String type, String id) {
        GetResponse response = elasticsearchTemplate.getClient().prepareGet(index, type, id).get();
        System.out.println(response.toString());
    }
	
	public void getAll(String index, String type) {
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(index).setTypes(type).execute().actionGet();
        System.out.println(response.toString());
    }
	
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
			try {
				results.add(objectMapper.readValue(hit.getSourceAsString(), Object.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return results;
    }
	
	public void deleteById(String index, String type, String id) {
        DeleteResponse response = elasticsearchTemplate.getClient().prepareDelete(index, type, id).get();
        System.out.println(response.toString());
    }
	
	public void deleteByType(String index, String type) {
		DeleteQuery deleteQuery = new DeleteQuery();
        deleteQuery.setIndex(index);
        deleteQuery.setType(type);
        elasticsearchTemplate.delete(deleteQuery);
    }
	
	public List list(String index, String type, String keyword) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices(index)
				.withTypes(type)
				.withQuery(QueryBuilders.matchQuery("*", keyword))
                .build();

		return elasticsearchTemplate.queryForList(searchQuery, TableKVVO.class);
	}
}

