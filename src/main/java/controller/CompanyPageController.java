package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class CompanyPageController {
	
	public static Route companyPage = (req, res) -> {		
		Map<String, Object> model = new HashMap<>();
		CompanyPageController controller = new CompanyPageController();
		Player player;
		List<String[]> ownedShareList;
		int numShareOwned = 0;
		
		model.put("template", "/utils/CompanyDetails.vtl");
		model.put("chart", "/utils/chart.vtl");
		
		//if player exist
		if(req.session().attribute("username") != null){
			String username = req.session().attribute("username");
			model.put("username", req.session().attribute("username"));
//			controller.userDetails(model, username);
			
			//calculate how many of the share player currently has and put it into the model
			player = req.session().attribute("playerObj");
			ownedShareList = player.getTradingAcc().getSharesOwned();
			
			if (ownedShareList != null && !ownedShareList.isEmpty())
			{
				for (String[] ownedShare : ownedShareList)
				{
					if (req.queryParams("code").equals(ownedShare[0]))
					{
						numShareOwned = Integer.parseInt(ownedShare[1]);
						break;
					}
				}
			}
			model.put("ownedShareAmt", numShareOwned);
		}
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	/* Check for available shares from user 
	 * So user know shares they owned
	 */
	public void userDetails(Map<String, Object> model, String username){
		TradingAcc ta = FileTools.loadTrAcc(username);
		
		ArrayList<String[]> shareList = ta.getSharesOwned();

		System.out.println(ta.getSharesOwned() + ": shareList before if");
		
		//list user shares
		if(!shareList.isEmpty()){
			System.out.println(shareList.toString());
			model.put("shareList", shareList);
		}		
	}
}
