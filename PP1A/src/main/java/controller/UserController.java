package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
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

public class UserController {
	public static Route userPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/user.vtl");
		model.put("username", req.session().attribute("username"));
		model.put("firstname", req.session().attribute("firstname"));
		model.put("lastname", req.session().attribute("lastname"));
		model.put("age", req.session().attribute("age"));
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route editPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/editProfile.vtl");
		model.put("username", req.session().attribute("username"));
		model.put("firstname", req.session().attribute("firstname"));
		model.put("lastname", req.session().attribute("lastname"));
		model.put("age", req.session().attribute("age"));
		model.put("password", req.session().attribute("password"));
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route confirmEditProfile = (req, res) -> {
		Map<String, Object> model = editProfile(req);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Map<String, Object> editProfile(Request req) throws IOException{
		Map<String, Object> model = new HashMap<>();
		
		String oldId = req.session().attribute("username");
		String newId = req.queryParams("username");
		String firstName = req.queryParams("firstname");
		String lastName = req.queryParams("lastname");
		int age = Integer.parseInt(req.queryParams("age"));
		String password = req.queryParams("password");
		
		Player player = req.session().attribute("playerObj");
		
		if(player.editProfile(oldId, newId, password, firstName, lastName, age) != null)
		{
			req.session().attribute("playerObj", player);
			req.session().attribute("username", newId);
			req.session().attribute("firstname", firstName);
			req.session().attribute("lastname", lastName);
			req.session().attribute("age", age);
			req.session().attribute("password", password);
			
			model.put("userTemplate", "/users/ConfirmEditProfile.vtl");
		}
		else
			model.put("userTemplate", "/users/editProfile.vtl");
		
		return model;	
	}
	
	public void loadToModel(Map<String, Object> model, Request req)
	{		
		if (req.session().attribute("username") != null && req.session().attribute("firstname") != null)
		{
			model.put("username", req.session().attribute("username"));
			model.put("firstname", req.session().attribute("firstname"));
			model.put("lastname", req.session().attribute("lastname"));
			model.put("age", req.session().attribute("age"));
			model.put("password", req.session().attribute("password"));
		}
		
		if (req.session().attribute("currBal") != null)
		{
			model.put("currBal", req.session().attribute("currBal"));
			model.put("sharesOwned", req.session().attribute("sharesOwned"));
		}
	}
	
	public void loadTradingAccToSession(Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		
		if (player.getPassword().equals(req.session().attribute("password")) && player.getTradingAcc() != null)
		{
			req.session().attribute("currBal", player.getTradingAcc().getCurrBal());
			req.session().attribute("sharesOwned", player.getTradingAcc().getSharesOwned());
		}
	}
	
	public void deleteAccount(String username, Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		
		try {
			player.deleteAcc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openTradingAcc(Map<String, Object> model, Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		TradingAcc trAcc = null;
		
		if (player.getTradingAcc() == null)
		{
			try {
				trAcc = Player.openTradeAcc(player.getID());
				
				model.put("currBal", player.getTradingAcc().getCurrBal());
				model.put("sharesOwned", player.getTradingAcc().getSharesOwned());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}