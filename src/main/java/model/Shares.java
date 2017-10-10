package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Shares {
	private String ASX_code;
	private String compName;
	private String industryGroup;
	private BigDecimal shareVal;
	
	ArrayList<BigDecimal> priceHistory = new ArrayList<BigDecimal>();
	
	public Shares(String ASX_code, String compName, String industryGroup, BigDecimal shareVal){
		this.ASX_code = ASX_code;
		this.compName = compName;
		this.industryGroup = industryGroup;
		this.shareVal = shareVal;
	}
	
	public String getASX_code(){
		return ASX_code;
	}
	
	public String getIndustryGroup(){
		return industryGroup;
	}
	
	public String getCompName(){
		return compName;
	}
	
	public BigDecimal getShareVal(){
		return shareVal;
	}

}
