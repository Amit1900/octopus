package com.parser;

import com.model.DataModel;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class CSVDataParser extends DataParser {

	List<DataModel> dataList = null;
	private String GROSS = "GROSS";
	private String NET = "NET";

	/**
	 * @throws IOException
	 */
	public void readData() {
		BufferedReader br;
		FileReader fr;
		dataList = new ArrayList<>();
		String fileName = "./TestData.csv";
		File file = getFile(fileName);
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			// Read to skip the header
			br.readLine();
			// Reading from the second line
			dataList = populateDataList(br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void processData() {
		DataModel dataModel = new DataModel();
		Map<String, List<DataModel>> groupByCountry = dataList.stream()
				.collect(Collectors.groupingBy(w -> w.getCountry()));
		Map<String, List<DataModel>> groupByRegion = dataList.stream()
				.collect(Collectors.groupingBy(w -> w.getRegion()));
		grossProfitCountryInUSD(groupByCountry, dataModel);
		netProfitRegionInUSD(groupByRegion, dataModel);

		consoleOutput(dataModel);
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public File getFile(String fileName) {
		ClassLoader classLoader = CSVDataParser.class.getClassLoader();
		return new File(classLoader.getResource(fileName).getFile());
	}

	/**
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private List<DataModel> populateDataList(BufferedReader br) throws IOException {
		String line;
		DataModel model;
		while ((line = br.readLine()) != null) {
			String[] dataDetails = line.split(",");

			if (dataDetails.length > 0) {
				model = new DataModel();
				model.setRegion(dataDetails[0]);
				model.setCountry(dataDetails[1]);
				model.setAccount(dataDetails[2]);
				model.setCurrency(dataDetails[3]);
				model.setGrossProfit(new BigDecimal(dataDetails[4]));
				model.setTaxRate(new BigDecimal(dataDetails[5]));
				dataList.add(model);

			}
		}
		return dataList;
	}

	/**
	 * @param groupByCountry
	 * @return
	 */
	public DataModel grossProfitCountryInUSD(Map<String, List<DataModel>> groupByCountry, DataModel dataModel) {
	    Map<String,BigDecimal> gpmap = new HashMap();
		for (Map.Entry<String, List<DataModel>> entry : groupByCountry.entrySet()) {
			String key = entry.getKey();
			List<DataModel> value = entry.getValue();
			BigDecimal gp = BigDecimal.ZERO;
			for (DataModel dm : value) {
				BigDecimal gprofit = getNetAndGrossProfit(dm, GROSS);
				gp = gp.add(gprofit);

			}
            gpmap.put(key, gp);
		}
        dataModel.setGrossProfitMap(gpmap);
		return dataModel;
	}

	/**
	 * @param groupByRegion
	 * @return
	 */
	public DataModel netProfitRegionInUSD(Map<String, List<DataModel>> groupByRegion, DataModel dataModel) {
		Map<String, BigDecimal> profitMap = new HashMap<>();
		String max = "";
		Map<String, BigDecimal> profitMapBeforeTax = new HashMap<>();
		String maxTax = "";
		for (Map.Entry<String, List<DataModel>> entry : groupByRegion.entrySet()) {
			String key = entry.getKey();
			List<DataModel> value = entry.getValue();
			BigDecimal np = BigDecimal.ZERO;
			BigDecimal npbeforeTax = BigDecimal.ZERO;
			for (DataModel dm : value) {
				BigDecimal netProfit = getNetAndGrossProfit(dm, NET);
				BigDecimal netProfitBeforeTax = getNetAndGrossProfit(dm, GROSS);

				np = np.add(netProfit);
				npbeforeTax = npbeforeTax.add(netProfitBeforeTax);
			}
			dataModel.getNetProfitMap().put(key, np);

			max = mostProfitableRegion(profitMap, key, np, max);
			maxTax = mostProfitableRegion(profitMapBeforeTax, key, npbeforeTax, maxTax);
			dataModel.setMostNetProfitableRegion(max);
			dataModel.setMostProfitableRegionBeforeTax(maxTax);

		}
		return dataModel;
	}

	public BigDecimal getNetAndGrossProfit(DataModel dm, String type) {
		BigDecimal profit = null;
		BigDecimal gross = dm.getGrossProfit().multiply(new BigDecimal(getCurrencyMap().get(dm.getCurrency().trim())));
		if (type.equals(NET)) {
			profit = gross.subtract((dm.getTaxRate().divide(new BigDecimal(100)).multiply(gross)));
		} else if (type.equalsIgnoreCase(GROSS)) {
			profit = dm.getGrossProfit().multiply(new BigDecimal(getCurrencyMap().get(dm.getCurrency().trim())));

		}
		return profit;

	}

	/**
	 * @param profitMap
	 * @param key
	 * @param val
	 * @return
	 */
	public String mostProfitableRegion(Map<String, BigDecimal> profitMap, String key, BigDecimal val, String max) {

		if (profitMap.isEmpty()) {
			profitMap.put(key, val);
			max = key;
		} else if (val.compareTo(profitMap.get(max)) == 1) {
			max = key;
		}

		profitMap.put(key, val);

		return max;

	}

	/**
	 * @return
	 */
	private Map<String, String> getCurrencyMap() {
		Map<String, String> currencyMap = new HashMap<>();
		currencyMap.put("USD", "1");
		currencyMap.put("CAN", "0.81080");
		currencyMap.put("HKD", "0.128983");
		currencyMap.put("SAR", "0.266645");
		currencyMap.put("GBP", "1.57217");
		currencyMap.put("EUR", "1.11598");
		currencyMap.put("INR", "0.0157311");
		currencyMap.put("AUD", "0.772744");
		currencyMap.put("NZD", "0.684621");
		currencyMap.put("MXN", "0.0648290");
		return currencyMap;

	}

	/**
	 * 
	 * @param dataModel
	 */
	private void consoleOutput(DataModel dataModel) {
		System.out.println("GROSS PROFIT TOTAL BY COUNTRY[USD] ");
		System.out.println(dataModel.getGrossProfitMap());

		System.out.println("______________________________________________________");
		System.out.println("Total NET PROFIT (Tax deducted) BY REGION[USD]");
		System.out.println(dataModel.getNetProfitMap());
		System.out.println("______________________________________________________");

		System.out
				.println("Most profitable region (BEFORE tax)[USD] = " + dataModel.getMostProfitableRegionBeforeTax());

		System.out.println("Most profitable region (AFTER tax)[USD]=" + dataModel.getMostNetProfitableRegion());

	}

}