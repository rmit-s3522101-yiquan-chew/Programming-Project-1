package model;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradingAcc {
	public static final BigDecimal INIT_BAL = new BigDecimal(1000000);
	private String user_ID;
	private BigDecimal currBal;
	private ArrayList<String[]> sharesOwned;
	FileTools fileTool = new FileTools();
	
	public TradingAcc(String user_ID){
		this.user_ID = user_ID;
	}
	
	public Transaction buyShares(Shares share, int amt) throws InsufficientFundsException{
		String currTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		BigDecimal newBal;
		Transaction buy;
		boolean found = false;

		//return null if player don't have enough money
		// throws exceptions because not enough funds
		if (currBal.doubleValue() < share.getShareVal().doubleValue() * amt)
		{
			double amountNeeded = share.getShareVal().doubleValue() - currBal.doubleValue();
			throw new InsufficientFundsException(amountNeeded);
		}
		else if (sharesOwned == null)
		{
			sharesOwned = new ArrayList<String[]>();
		}
		
		//set balance to (current balance - (share value * amt)), add share to list and create a transaction object
		newBal = currBal.subtract(share.getShareVal().multiply(new BigDecimal(amt)));
		setCurrBal(newBal);
		
		//find if sharesOwned contains asx code, if yes, increase number, else add new
		for (String[] ownedShare : sharesOwned)
		{
			if (ownedShare[0].equals(share.getASX_code()))
			{
				ownedShare[1] = String.valueOf(Integer.parseInt(ownedShare[1]) + amt);
				found = true;
				break;
			}
		}
		if (!found)
		{
			sharesOwned.add(new String[] {share.getASX_code(), String.valueOf(amt)});
		}
		
		buy = new Transaction(user_ID, Transaction.TransType.BUYING, share.getASX_code(), amt, share.getCompName(), share.getShareVal(), currTimeStamp);

		//save transaction to file and update user account data file
		try {
			fileTool.addToTransCSV(buy, FileTools.USER_TRANSACTION_LOG);
			fileTool.trAccToFile(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buy;
	}
	
	public Transaction sellShares(Shares share, int amt) throws NoSharesException{
		String currTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		BigDecimal newBal;
		Transaction sell;
		boolean found = false;
		
		// if no shares, return null
		if (sharesOwned == null)
		{
			throw new NoSharesException(sharesOwned);
		}
		
		//find share in sharesOwned and lower num of that share by amt
		for (String[] ownedShare : sharesOwned)
		{
			if (ownedShare[0].equals(share.getASX_code()))
			{
				if (Integer.parseInt(ownedShare[1]) < amt)
				{
					return null;
				}
				else
				{
					ownedShare[1] = String.valueOf(Integer.parseInt(ownedShare[1]) - amt);
				}
				
				found = true;
				break;
			}
		}
		
		//set balance to (current balance + share value), remove share from list and create a transaction object if share in possession, else return null
		if (found)
		{
			newBal = currBal.add(share.getShareVal().multiply(new BigDecimal(amt)));
			setCurrBal(newBal);
			sell = new Transaction(user_ID, Transaction.TransType.SELLING, share.getASX_code(), amt, share.getCompName(), share.getShareVal(), currTimeStamp);
		}
		else
		{
			return null;
		}
		
		//save transaction to file and update user account data file
		try {
			fileTool.addToTransCSV(sell, FileTools.USER_TRANSACTION_LOG);
			fileTool.trAccToFile(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sell;
	}
	
	public void updateId(Player player)
	{
		user_ID = player.getID();
	}
	
	public Transaction showLastTrans(String inputFile){
		Transaction lastTrans = null;
		return lastTrans;
	}
	
	public BigDecimal showCurrStockVal(){
		BigDecimal currStockVal = new BigDecimal(0);
		List<String[]> allShares = null;
		int priceIndex = 3;
		int allSharesCodeIndex = 1;
		int codeIndex = 0;
		int amtIndex = 1;
		
		//return 0 if no shares owned
		if (this.sharesOwned == null || this.sharesOwned.isEmpty())
		{
			return new BigDecimal(0);
		}
		
		//read asx companies list
		try {
			allShares = FileTools.readCSV(FileTools.ASX_COMPANIES_DATA_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return null if can't read asx companies list or it is empty
		if (allShares == null || allShares.isEmpty())
		{
			return null;
		}

		//calculate total stock val
		for (String[] ownedShare : this.sharesOwned)
		{
			for (String[] share : allShares)
			{
				if (share[allSharesCodeIndex].equals(ownedShare[codeIndex]))
				{
					currStockVal = currStockVal.add(new BigDecimal(share[priceIndex]).multiply(new BigDecimal(ownedShare[amtIndex])));
					break;
				}
			}
		}
		
		return currStockVal;
	}
	
	//read current stock value from file
	public static BigDecimal showCurrStockVal(String userId)
	{
		BigDecimal currStockVal = new BigDecimal(0);
		int idIndex = 0;
		int stockValIndex = 2;
		List<String[]> searchResult = FileTools.searchFile(userId, FileTools.USER_ACC_FILE);
		
		if (searchResult != null)
		{
			//calculate current stock value
			for (String[] result : searchResult)
			{
				//only set surrStockVal if userId matches and is not empty
				if (result[idIndex].equals(userId) && !result[stockValIndex].equals(""))
				{
					currStockVal = new BigDecimal(result[stockValIndex]);
					break;
				}
			}
		}
		
		return currStockVal;
	}
	
	public String getUser_ID(){
		return user_ID;
	}
	
	public BigDecimal getCurrBal(){
		return currBal;
	}
	
	public BigDecimal setCurrBal(BigDecimal newBal){
		currBal = newBal;
		return currBal;
	}
	
	public ArrayList<String[]> getSharesOwned(){
		return sharesOwned;
	}
	
	public ArrayList<String[]> setSharesOwned(ArrayList<String[]> newShares){
		sharesOwned = newShares;
		return sharesOwned;
	}
}
