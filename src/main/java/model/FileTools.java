package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import org.json.JSONObject;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class FileTools {
	public static final String CSV_RESOURCE_FOLDER = "src/main/resources/public/csv";
	public static final String USER_DATA_FILE = CSV_RESOURCE_FOLDER + "/UserData.csv";
	public static final String USER_ACC_FILE = CSV_RESOURCE_FOLDER + "/AccountData.csv";
	public static final String USER_SHARES_OWNED_FILE = CSV_RESOURCE_FOLDER + "/sharesOwned.csv";
	public static final String USER_TRANSACTION_LOG = CSV_RESOURCE_FOLDER + "/TransactionLog.csv";
	public static final String ASX_COMPANIES_DATA_FILE = CSV_RESOURCE_FOLDER + "/ASXListedCompanies.csv";
	public static final String LEADERBOARD = CSV_RESOURCE_FOLDER + "/leaderboard.csv";
	public static final String ALPHA_ADVANTAGE_API_KEY = "MP9H93RQEUUFGX07";
    public static final String URL_JSON_PATH_P1_INTRADAY = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=";
    public static final String URL_JSON_PATH_P2_INTRADAY = ".AX&interval=60min&apikey=" + ALPHA_ADVANTAGE_API_KEY;
    public static final String URL_JSON_PATH_P1_DAILY = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";
    public static final String URL_JSON_PATH_P2_DAILY = ".AX&apikey=" + ALPHA_ADVANTAGE_API_KEY;
    public static final String HOURLY_TIME_SERIES_STRING = "Time Series (60min)";
    public static final String DAILY_TIME_SERIES_STRING = "Time Series (Daily)";
   
    Util util = new Util();
	
    //fetch data from a JSON in a url using its ASX code, if ASX code is not a key, then return null
	public String fetchShareData(String ASXcode, String TimeSeriesString) throws IOException
	{
		String jsonString = null;
		URL url = null;
		
		//returned data can be intraday(hourly) or daily
		switch (TimeSeriesString){
			case HOURLY_TIME_SERIES_STRING: 
				url = new URL(URL_JSON_PATH_P1_INTRADAY + ASXcode + URL_JSON_PATH_P2_INTRADAY);
				break;
			case DAILY_TIME_SERIES_STRING:
				url = new URL(URL_JSON_PATH_P1_DAILY + ASXcode + URL_JSON_PATH_P2_DAILY);
				break;
			default:
				return null;
		}
		
		InputStream is;
		BufferedReader br;
		
		is = url.openConnection().getInputStream();
		br = new BufferedReader(new InputStreamReader(is));
		
		StringBuilder jsonStringBuilder = new StringBuilder();
		String line;
		
		while ((line = br.readLine()) != null)
		{
			jsonStringBuilder.append(line);
		}
		
		jsonString = jsonStringBuilder.toString();	
		
		if (!jsonString.contains(TimeSeriesString))
		{
			return null;
		}
		
		br.close();
		is.close();
		
		return jsonString;
	}
	
	//Separate function for writing changes to csv file after fetching data
	public synchronized void overwriteCSV(List<String[]> content, String filePath) throws IOException
	{
		CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
		csvWriter.writeAll(content);
		csvWriter.flush();
		csvWriter.close();
	}
	
	//reads entire csv file and return its contents as a List of string arrays
	public static List<String[]> readCSV(String filePath)throws IOException
	{
		CSVReader csvReader = new CSVReader(new FileReader(filePath));
		List<String[]> csvContents = csvReader.readAll();
		csvReader.close();
		return csvContents;
	}
	
	//fetch all data from from JSON url and save them to file
	public void fetchAllShareData()
	{
		try {
			JSONObject json = null;
			String jsonString = null;
			List<String[]> companiesCSVlist;
			Object[] jsonArray;
			//read from csv and add edit vals
			companiesCSVlist = readCSV(ASX_COMPANIES_DATA_FILE);
			
			for (int i = 1; i < companiesCSVlist.size(); i++)
			{
				jsonString = fetchShareData(companiesCSVlist.get(i)[1], HOURLY_TIME_SERIES_STRING);
				if (jsonString != null)
				{
					json = new JSONObject(jsonString);
					jsonArray = json.getJSONObject("Time Series (60min)").keySet().toArray();
					Arrays.sort(jsonArray, Collections.reverseOrder());
					JSONObject priceDataList = json.getJSONObject("Time Series (60min)");
					JSONObject latestData = (JSONObject) priceDataList.get(jsonArray[0].toString());
					
					companiesCSVlist.get(i)[3] = latestData.get("1. open").toString();
					companiesCSVlist.get(i)[4] = latestData.getString("2. high").toString();
					companiesCSVlist.get(i)[5] = latestData.getString("3. low").toString();
					companiesCSVlist.get(i)[7] = latestData.getString("5. volume").toString();
					
					//some shares only have data for 1 time (don't have historical data)
					if (priceDataList.length() > 1)
					{
						String lastPrice = ((JSONObject) priceDataList.get(jsonArray[1].toString())).get("1. open").toString();
						companiesCSVlist.get(i)[6] = util.calculateChange(new BigDecimal(lastPrice), new BigDecimal(latestData.get("1. open").toString())).toString();
					}
				}
				else
				{
					companiesCSVlist.get(i)[3] = null;
				}
				//print for debugging
				//System.out.println(i);
			}
						
			//write to csv (rewrite everything)
			overwriteCSV(companiesCSVlist, ASX_COMPANIES_DATA_FILE);
			updateAllPlayerStockVal();
			System.out.println("updated csv");
		}
		catch (IOException e){
			System.out.println("something happened");
		}
	}

	//use user ID to find player in a file and return it
	public static User LoadUser(String user_ID)
	{
		User user = null;
		ArrayList<String[]> searchedPlayers = searchFile(user_ID, USER_DATA_FILE);
		String[] parameters = null;
		String password;
		String firstName;
		String lastName;
		int age;
		
		//return null if searched players empty
		if (searchedPlayers == null)
		{
			return null;
		}
		
		for (String[] player : searchedPlayers)
		{
			if (player[0].equals(user_ID))
			{
				parameters = player;
				break;
			}
		}
		
		//return null if can't find user_ID in file
		if (parameters == null)
		{
			return null;
		}
		
		//use these variables to make it easier to read
		password = parameters[1];
		firstName = parameters[2];
		lastName = parameters[3];
		age = Integer.parseInt(parameters[4]);
		
		if (user_ID.equals("admin"))
		{
			user = new Admin(user_ID, password, firstName, lastName, age);
		}
		else
		{
			user = new Player(user_ID, password, firstName, lastName, age);
		}
		
		return user;
	}
	
	//search a file for a specified id string and return row as a string array
	public static ArrayList<String[]> searchFile(String id, String filepath)
	{		
		ArrayList<String[]> matching = new ArrayList<String[]>();
		List<String[]> list = null;
		
		try {
			list = readCSV(filepath);
			
			if (list == null || list.isEmpty())
			{
				return null;
			}
			
			for (String[] item : list)
			{
				if (item[0].contains(id) && !item[0].equals("user_ID"))
				{
					matching.add(item);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//error handling of for null 0 elements matching
		if(matching.size() == 0){
			return null;
		}
		return matching;
	}
	
	//convert to CVS format
	public String toCSV(User player){
		String cvsString = null;
		cvsString = player.getID() + "," + 
					player.getPassword() + "," + 
					player.getFName() + "," + 
					player.getLName() + "," +
					player.getAge();
		return cvsString;
	}
	//write to end of file (Append to File)
	public void playerToFile(User player) throws IOException{
		FileWriter fw =  new FileWriter(USER_DATA_FILE,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(toCSV(player));
		bw.close();
	}
	
	// Putting the trading account into a string format
	public String trAccToString(TradingAcc tr){
		String trAccString = null;
		//getting player's shares
		ArrayList<String[]> sharesOwned = tr.getSharesOwned();
		
		trAccString = tr.getUser_ID() + "," + tr.getCurrBal() + ",";
		//Listing all shares under player's possession
		int i = 0;
		while(i <= sharesOwned.size()-1){
			trAccString = trAccString + sharesOwned.get(i);
			if (i != sharesOwned.size()-1)
			{
				trAccString = trAccString + ";";
			}
			i ++;
		}

		return trAccString;
	}
	
	//writing Trading account to file, need to do it differently because it has variable columns(not anymore)
	public void trAccToFile(TradingAcc trAcc) throws IOException{
		List<String[]> trAccFileContent = readCSV(USER_ACC_FILE);
		List<String[]> sharesOwnedFileContent = readCSV(USER_SHARES_OWNED_FILE);
		boolean found = false;
		
		//update tradingaccount file Content
		for (String[] trAccInFile : trAccFileContent)
		{
			if (trAccInFile[0].equals(trAcc.getUser_ID()))
			{
				trAccInFile[1] = trAcc.getCurrBal().toString();
				trAccInFile[2] = trAcc.showCurrStockVal().toString();
				found = true;
				break;
			}
		}
		if (!found)
		{
			trAccFileContent.add(new String[]{trAcc.getUser_ID(), trAcc.getCurrBal().toString()});
		}
		
		//update sharesOwned File Content
		for (String[] shareOwned : trAcc.getSharesOwned())
		{
			found = false;
			for (String[] shareOwnedInFile : sharesOwnedFileContent)
			{
				if (shareOwnedInFile[0].equals(trAcc.getUser_ID()) && shareOwnedInFile[1].equals(shareOwned[0]))
				{
					//if no player no more of a share, delete it, else change numShares
					if (shareOwned[1].equals(String.valueOf(0)))
					{
						sharesOwnedFileContent.remove(shareOwnedInFile);
					}
					else
					{
						shareOwnedInFile[2] = shareOwned[1];
					}
					
					found = true;
					break;
				}
			}
			if (!found)
			{
				sharesOwnedFileContent.add(new String[]{trAcc.getUser_ID(), shareOwned[0], shareOwned[1]});
			}
		}
		
		//update files
		overwriteCSV(trAccFileContent, USER_ACC_FILE);
		overwriteCSV(sharesOwnedFileContent, USER_SHARES_OWNED_FILE);
	}
	
	//adds a transaction
	public void addToTransCSV(Transaction transaction, String filePath) throws IOException
	{
		List<String[]>transList = readCSV(USER_TRANSACTION_LOG);
		String[] newTrans = {transaction.getID(), transaction.getTransType().toString(), transaction.getASXcode(),
				String.valueOf(transaction.getNumShares()), transaction.getCompName(), 
				transaction.getShareVal().toString(), transaction.getDateTime()};
		transList.add(newTrans);
		overwriteCSV(transList, USER_TRANSACTION_LOG);
	}
	
	//updates transaction csv if username is changed
	public void updateIdInCSV( String oldId, String newId, String filePath) throws IOException
	{
		List<String[]> fileContents = readCSV(filePath);
		
		for (String[] row : fileContents)
		{
			if (row[0].equals(oldId))
			{
				row[0] = newId;
				
				//only transaction log will contain multiple of the same userID
				if (!filePath.equals(USER_TRANSACTION_LOG) || !filePath.equals(USER_SHARES_OWNED_FILE))
				{
					break;
				}
			}
		}
		
		overwriteCSV(fileContents, filePath);
	}
	
	//got content of csv and make it a simple json string
	public String csvToJsonString(String filePath)
	{
		String jsonString = "";
		
		try {
			List<String[]> csvContent = readCSV(filePath);
			StringBuilder sb = new StringBuilder();
			String[] headers = csvContent.get(0);
			sb.append("{");
			
			for (int row = 1; row < csvContent.size(); row++)
			{
				sb.append("\"");
				sb.append(csvContent.get(row)[0]);
				sb.append("\":{");
				
				for (int col = 1; col < csvContent.get(row).length; col++)
				{
					sb.append("\"");
					sb.append(headers[col]);
					sb.append("\":\"");
					sb.append(csvContent.get(row)[col]);
					sb.append("\"");
					if (col != csvContent.get(row).length-1)
					{
						sb.append(",");
					}
				}
				
				sb.append("}");
				
				if (row != csvContent.size()-1)
				{
					sb.append(",");
				}
			}
			
			sb.append("}");
			jsonString = sb.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	//loading trading account
	public static TradingAcc loadTrAcc(String userID)
	{
		TradingAcc trAcc = null;
		ArrayList<String[]> sharesOwned = new ArrayList<String[]>();
		
		ArrayList<String[]> trAccList = searchFile(userID, USER_ACC_FILE);
		ArrayList<String[]> sharesOwnedList = searchFile(userID, USER_SHARES_OWNED_FILE);
		
		if (trAccList == null)
		{
			return null;
		}
		
		//load current balance
		for (String[] trAccParams : trAccList)
		{
			if (trAccParams[0].equals(userID))
			{
				trAcc = new TradingAcc(userID);
				trAcc.setCurrBal(new BigDecimal(trAccParams[1]));
				break;
			}
		}
		
		//load sharesOwned
		if (sharesOwnedList == null)
		{
			return trAcc;
		}
		for (String[] ownedShareInFile : sharesOwnedList)
		{
			if (ownedShareInFile[0].equals(trAcc.getUser_ID()))
			{
				sharesOwned.add(new String[]{ownedShareInFile[1], ownedShareInFile[2]});
			}
		}
		trAcc.setSharesOwned(sharesOwned);
		
		return trAcc;
	}
	
	//function to load a specific share from file
	public static Shares loadShare(String ASXCode) throws IOException
	{
		Shares share = null;
		int asxCodeIndex = 1;
		List<String[]> fileContent = readCSV(ASX_COMPANIES_DATA_FILE);
		String compName;
		String industryGroup;
		BigDecimal shareVal;
		
		if (fileContent != null)
		{
			for (String[] searchItem : fileContent)
			{
				if (searchItem[asxCodeIndex].equals(ASXCode))
				{
					compName = searchItem[0];
					industryGroup = searchItem[2];
					shareVal = new BigDecimal(searchItem[3]);
					share = new Shares(searchItem[asxCodeIndex], compName, industryGroup, shareVal);
					break;
				}
			}
		}
		
		return share;
	}
	
	//function to update player stock val after fetching data
	public void updateAllPlayerStockVal() throws IOException
	{
		List<String[]> trAccList = readCSV(USER_ACC_FILE);
		Player player;
		BigDecimal newStockVal;
		
		if (trAccList != null && !trAccList.isEmpty())
		{
			for (String[] trAccDetails : trAccList)
			{
				if (trAccDetails != trAccList.get(0))
				{
					player = (Player) LoadUser(trAccDetails[0]);
					newStockVal = player.getTradingAcc().showCurrStockVal();
					trAccDetails[2] = newStockVal.toString();
				}
			}
			
			overwriteCSV(trAccList, USER_ACC_FILE);
		}
		
	}

	/* 
	 * For getting Player log based on
	 */
	public static ArrayList<String[]> getTransactionLog(String id) throws IOException{
		//read from csv
		ArrayList<String[]> transactionLog = new ArrayList<String []>();
		List<String[]> temp = readCSV(USER_TRANSACTION_LOG);
		
		//from admin, ask for all
		if(id.equals("ALL")){
			for(String[] data : temp){
				transactionLog.add(data);
			}
		}
		else{
			for(String[] data : temp){
				if(data[0].equals(id)){
					transactionLog.add(data);
				}
			}			
		}
		
		//error handling of for null 0 elements matching
		if(transactionLog.size() == 0){
			return null;
		}
		
		return transactionLog;
	}
}
