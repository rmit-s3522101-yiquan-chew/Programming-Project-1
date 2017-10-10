package controller;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import model.FileTools;
import model.Menu;
import model.Player;
import model.TradingAcc;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class LeaderboardController {
	public static Route leaderboard = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		leaderboard();
		model.put("template", "/utils/leaderboard.vtl");
				
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static void leaderboard(){
		//limit map to 100
		Map<String, Integer> board = new LinkedHashMap<>();
		FileTools ft = new FileTools();
		
		//only player with transaction account will be listed on leader board
		//get player from accountdata 
		try {
			List<String[]> accountData = FileTools.readCSV(ft.USER_ACC_FILE);
			List<String[]> toCSV = new ArrayList<>();
			String[] temp = new String[3];
			
			//merge data
			for(int i=1; i<accountData.size(); i++){
				String playerId, balance;
				int playerScore = 0;
				
				playerId = accountData.get(i)[0];
				balance = accountData.get(i)[1];
				
				BigDecimal totalSharesValue = TradingAcc.showCurrStockVal(playerId);
				
//				System.out.println(totalSharesValue);
//				int tempValue = totalSharesValue.intValue();
				
//				playerScore += tempValue;
				
				int tempBalance = (int)Double.parseDouble(balance);
//				
//				tempBalance = tempBalance/1000;
//				
				playerScore += tempBalance;
				
//				System.out.println(playerId + "," + playerScore);
				
				board.put(playerId, playerScore);
			}
			
			//sort the temporary board
			board = sortByValue(board);
			
			//columes for leaderboard
			temp[0] = "Rank";
			temp[1] = "User";
			temp[2] = "Points";
			
			toCSV.add(temp);
			
			//test printing board
			int j = 1;
			Set<Map.Entry<String, Integer>> pairs = board.entrySet();
			for(Map.Entry<String, Integer> entry : board.entrySet()){
				System.out.println(entry.getKey() + ", " + entry.getValue());
				
				String[] temp2 = new String[3];
				temp2[0] = Integer.toString(j);
				temp2[1] = entry.getKey().toString();
				temp2[2] = entry.getValue().toString();
				
				toCSV.add(temp2);
				
				j++;
			}			
			
			ft.overwriteCSV(toCSV, ft.LEADERBOARD);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	/* sorting hash map by value, altered from 
	 * https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
}
