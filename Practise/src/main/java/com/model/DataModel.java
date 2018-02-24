package com.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DataModel {

    public String region;
    public String country;
    public String account;
    public String currency;
    public BigDecimal grossProfit;
    public BigDecimal taxRate;
    public String mostGrossProfitableRegion;
    public String mostNetProfitableRegion;
    public String mostProfitableRegionBeforeTax;

    public Map<String,BigDecimal> grossProfitMap = new HashMap();
    public Map<String,BigDecimal> netProfitMap = new HashMap();
    public Map<String,BigDecimal> netProfitMapBeforeTax = new HashMap();

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getAccount() {
        return account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getMostGrossProfitableRegion() {
        return mostGrossProfitableRegion;
    }

    public void setMostGrossProfitableRegion(String mostGrossProfitableRegion) {
        this.mostGrossProfitableRegion = mostGrossProfitableRegion;
    }

    public String getMostNetProfitableRegion() {
        return mostNetProfitableRegion;
    }

    public void setMostNetProfitableRegion(String mostNetProfitableRegion) {
        this.mostNetProfitableRegion = mostNetProfitableRegion;
    }

    public Map getGrossProfitMap() {
        return grossProfitMap;
    }

    public void setGrossProfitMap(Map grossProfitMap) {
        this.grossProfitMap = grossProfitMap;
    }

    public Map getNetProfitMap() {
        return netProfitMap;
    }

    public void setNetProfitMap(Map netProfitMap) {
        this.netProfitMap = netProfitMap;
    }

   
	public Map<String, BigDecimal> getNetProfitMapBeforeTax() {
		return netProfitMapBeforeTax;
	}

	public void setNetProfitMapBeforeTax(Map<String, BigDecimal> netProfitMapBeforeTax) {
		this.netProfitMapBeforeTax = netProfitMapBeforeTax;
	}

	public String getMostProfitableRegionBeforeTax() {
		return mostProfitableRegionBeforeTax;
	}

	public void setMostProfitableRegionBeforeTax(String mostProfitableRegionBeforeTax) {
		this.mostProfitableRegionBeforeTax = mostProfitableRegionBeforeTax;
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}