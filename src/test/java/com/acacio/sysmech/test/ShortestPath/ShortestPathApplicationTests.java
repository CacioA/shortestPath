package com.acacio.sysmech.test.ShortestPath;

import com.google.gson.Gson;

import org.json.JSONObject;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.HashMap;
import java.util.LinkedHashMap;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@AutoConfigureMockMvc
class ShortestPathApplicationTests {


	@Autowired
	private MockMvc mockMvc;

	private Gson gson = new Gson();

	@Test
	public void whenGet_WithValidParams_ExpectListOfNodesAndLength_And200() throws Exception {
//		 this tests checks:
//		 					1) function returns expected nodes with classification
//	   						2) function returns correct and minimum length
//							3) endpoint which requires filePath
		HashMap<String,String> expectedNodes = new HashMap<>();
		expectedNodes.put("T/2345","Transceiver");
		expectedNodes.put("Bartrum-X5","Link");
		expectedNodes.put("T/0031","Transceiver");

		MvcResult result = this.mockMvc.perform(get("/shortestDistanceWithPath")
				.param("startNode","T/2345")
				.param("finishNode","T/0031")
				.param("fileName","/data.xml"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		JSONObject jsonResult =new JSONObject(result.getResponse().getContentAsString());
		Assert.assertEquals(jsonResult.get("totalLengthInNodes"),3);
		JSONObject hashmap = (JSONObject) jsonResult.get("nodesWithClassification");

		LinkedHashMap<String,String> actualNodes = gson.fromJson(String.valueOf(hashmap), LinkedHashMap.class);
		Assert.assertEquals(expectedNodes,actualNodes);

	}

	@Test
	public void whenGet_WithInvalidTarget_ExpectNoNodes_And200() throws Exception {
//		this test checks:
//						1)when given an invalid parameter, the search will not be attempted
//						returning empty arraylist of paths with length = 0
//						2)second endpoint which uses default filePath name as data.xml
		HashMap<String,String> expectedNodes = new HashMap<>();
		MvcResult result = this.mockMvc.perform(get("/shortestDistance")
						.param("startNode","M60")
						.param("finishNode","NotValid")
						)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		JSONObject jsonResult =new JSONObject(result.getResponse().getContentAsString());

		JSONObject hashmap = (JSONObject) jsonResult.get("nodesWithClassification");
		LinkedHashMap<String,String> actualNodes = gson.fromJson(String.valueOf(hashmap),LinkedHashMap.class);

		Assert.assertEquals(jsonResult.get("totalLengthInNodes"),0);
		Assert.assertEquals(jsonResult.get("nodesWithClassification").toString(),new JSONObject().toString());
	}

}
