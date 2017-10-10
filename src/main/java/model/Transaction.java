package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
	public enum TransType {
		BUYING,
		SELLING
	}
	
	private String user_ID;
	private TransType transType;
	private String ASXcode;
	private int numShares;
	private String compName;
	private BigDecimal shareVal;	
	private String dateTime;
	
	public Transaction(String user_ID, TransType transType, String ASXcode, int numShares, String compName, BigDecimal shareVal, String dateTime){
		this.user_ID = user_ID;
		this.transType = transType;
		this.ASXcode = ASXcode;
		this.numShares = numShares;
		this.compName = compName;
		this.shareVal = shareVal;
		this.dateTime = dateTime;
	}
	
	public static Transaction loadLastTrans(String userId)
	{
		Transaction lastTrans = null;
		List<String[]> userTrans = FileTools.searchFile(userId, FileTools.USER_TRANSACTION_LOG);
		List<String[]> notUserTrans = new ArrayList<String[]>();
		String[] lastTransParams;
		
		if (userTrans != null)
		{
			//remove all rows where userId and row user_ID is not exactly the same
			for (String[] trans : userTrans)
			{
				if (!trans[0].equals(userId))
				{
					notUserTrans.add(trans);
				}
			}
			userTrans.removeAll(notUserTrans);
			
			if (!userTrans.isEmpty())
			{
				//get last transaction
				lastTransParams = userTrans.get(userTrans.size() - 1);
				lastTrans = new Transaction(lastTransParams[0], TransType.valueOf(lastTransParams[1]), lastTransParams[2], Integer.parseInt(lastTransParams[3])
						,lastTransParams[4], new BigDecimal(lastTransParams[5]), lastTransParams[6]);
			}
			
		}
		
		return lastTrans;
	}
	
	public String getID(){
		return user_ID;
	}
	
	public String getCompName(){
		return compName;
	}
    
	public TransType getTransType(){
		return transType;
	}
	
	public String getSeller(){
		String seller = null;
		
		switch(transType){
			case BUYING:
				seller = ASXcode;
				break;
			case SELLING:
				seller = user_ID;
				break;
		}
		
		return seller;
	}
	
	public String getBuyer(){
		String buyer = null;
		
		switch(transType){
		case BUYING:
			buyer = user_ID;
			break;
		case SELLING:
			buyer = ASXcode;
			break;
		}
		
		return buyer;
	}
	
	public BigDecimal getShareVal(){
		return shareVal;
	}
	
	public String getDateTime(){
		return dateTime;
	}
	
	public String getASXcode(){
		return ASXcode;
	}
	
	public BigDecimal getTotalPrice()
	{
		return shareVal.multiply(new BigDecimal(numShares));
	}
	
	public int getNumShares(){
		return numShares;
	}
	
}
