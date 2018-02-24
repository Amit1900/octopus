package com.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.model.DataModel;

public class CSVDataParserTest {
	List<DataModel> dataList;
	CSVDataParser parser;
	String fileName = null;
	DataModel dm = null;

	@Before
	public void setUp() throws Exception {
		dataList = new ArrayList<>();
		parser = new CSVDataParser();
		fileName = "./TestData.csv";
		dm = new DataModel();

	}

	@After
	public void tearDown() throws Exception {
		dataList.clear();
		parser = null;
		dm = null;
	}

	@Test
	public void getFileTest() throws Exception {
		assertTrue(parser.getFile(fileName).exists());
	}

	@Test
	public void grossProfitCountryInUSDTest() throws Exception {

		List<DataModel> al = new ArrayList();
		Map<String, List<DataModel>> groupByCountry = new HashMap();

		dm = new DataModel();
		dm.setCurrency("USD");
		dm.setGrossProfit(new BigDecimal(1234));
		al.add(dm);

		dm = new DataModel();
		dm.setCurrency("USD");
		dm.setGrossProfit(new BigDecimal(1234));

		al.add(dm);
		groupByCountry.put("USA", al);
		al = new ArrayList<>();
		dm = new DataModel();
		dm.setCurrency("INR");
		dm.setGrossProfit(new BigDecimal(1983453236));
		al.add(dm);
		groupByCountry.put("India", al);

		dm = parser.grossProfitCountryInUSD(groupByCountry, dm);
		assertEquals(new BigDecimal(2468), dm.getGrossProfitMap().get("USA"));
	}

	@Test
	public void netProfitRegionInUSDTest() throws Exception {

		List<DataModel> al = new ArrayList();
		Map<String, List<DataModel>> groupByRegion = new HashMap();

		dm = new DataModel();
		dm.setCurrency("USD");
		dm.setTaxRate(new BigDecimal(10));
		dm.setGrossProfit(new BigDecimal(1234));
		al.add(dm);

		dm = new DataModel();
		dm.setCurrency("USD");
		dm.setTaxRate(new BigDecimal(10));
		dm.setGrossProfit(new BigDecimal(1234));

		al.add(dm);
		groupByRegion.put("NA", al);
		al = new ArrayList<>();
		dm = new DataModel();
		dm.setCurrency("INR");
		dm.setTaxRate(new BigDecimal(10));
		dm.setGrossProfit(new BigDecimal(1983453236));
		al.add(dm);
		groupByRegion.put("APAC", al);

		dm = parser.netProfitRegionInUSD(groupByRegion, dm);
		BigDecimal taxDeduct = new BigDecimal(2468)
				.subtract((new BigDecimal(10).divide(new BigDecimal(100)).multiply(new BigDecimal(2468))));
		assertEquals(taxDeduct, dm.getNetProfitMap().get("NA"));

	}

	@Test
	public void mostProfitableRegionTest() throws Exception {

		Map<String, BigDecimal> profitMap = new HashMap();
		String max = "1";

		String[] keyA = { "A", "B", "C" };
		BigDecimal[] valA = { BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE };

		for (int i = 0; i < 3; i++) {
			String key = keyA[i];
			BigDecimal val = valA[i];

			max = parser.mostProfitableRegion(profitMap, key, val, max);

		}
		assertEquals("B", max);

	}
}