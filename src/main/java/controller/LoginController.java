package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import app.Application;
import model.Admin;
import model.FileTools;
import model.Menu;
import model.Player;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class LoginController {
	public static Route mainPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		// put "value" inside "cardholder"
		model.put("template", "/mainpage/mainpage.vtl");
		model.put("username", req.session().attribute("username"));
		model.put("table", "utils/companyTable.vtl");
		model.put("leaderboard", "utils/leaderboard.vtl");
		model.put("admin", req.session().attribute("adminObj"));
		
		// The vtl files are located under the resources directory
		// The line below are required to make the page works
        return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Route loginPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "/mainpage/login.vtl");
		model.put("username", req.session().attribute("username"));
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Route logout = (req, res) -> {
		logoutUser(req);
		
		res.redirect("/login");
		return null;
	};
	
	public static Route redirectUser = (req, res) -> {
		Map<String, Object> model = login(req);

		//check for admin by username
		String username	= req.queryParams("username");
		
		if(model.containsKey("authenticationFailed")){
			//authenticationFailed
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		}
		else if(username.contains("admin")){
			//redirect to adminPage
			res.redirect("/adminPage");
			return null;
		}
		else{
			//redirect to user page
			res.redirect("/userPage");
			return null;
		}			
	};
	
	public static Map<String, Object> login(Request req){
		Map<String, Object> model = new HashMap<>();
		
		Admin admin = null;
		Player player = null;
		
		String username	= req.queryParams("username");
		String password	= req.queryParams("password");
				
		if(username.contains("admin"))
		{
			admin = (Admin)FileTools.LoadUser(username);
			
			if(admin != null)
			{
				String firstName = admin.getFName();
				String lastName = admin.getLName();
				int age = admin.getAge();
				
				//need to check if return val is true or false later or else any pw will log you in
				if(app.Application.menu.login(username, password))
				{		
					req.session().attribute("adminObj", admin);
					req.session().attribute("username", username);
					req.session().attribute("password", password);
					req.session().attribute("firstname", firstName);
					req.session().attribute("lastname", lastName);
					req.session().attribute("age", age);
					
					AdminController.loadToModel(model, req);
					model.put("userTemplate", "/users/admin.vtl");
				}
				else
				{
					model.put("authenticationFailed", true);
					model.put("template", "/mainpage/login.vtl");
				}
			}
			else
			{
				model.put("authenticationFailed", true);
				model.put("template", "/mainpage/login.vtl");
			}
		}
		else
		{
			player = (Player)FileTools.LoadUser(username);
			
			if(player != null)
			{
				String firstName = player.getFName();
				String lastName = player.getLName();
				int age = player.getAge();
				
				//need to check if return val is true or false later or else any pw will log you in
				if(app.Application.menu.login(username, password))
				{		
					req.session().attribute("playerObj", player);
					req.session().attribute("username", username);
					req.session().attribute("password", password);
					req.session().attribute("firstname", firstName);
					req.session().attribute("lastname", lastName);
					req.session().attribute("age", age);
				
					player.loadTrAcc();
					if(player.getTradingAcc() != null)
					{
						//returns error when player hasn't opened a trading account yet
						UserController.loadTradingAccToSession(req);
					}
				}
				else
				{
					model.put("authenticationFailed", true);
					model.put("template", "/mainpage/login.vtl");
				}
			}
			else
			{
				model.put("authenticationFailed", true);
				model.put("template", "/mainpage/login.vtl");
			}
		}
		
		return model;		
	}
	
	public static void logoutUser(Request req)
	{
		String username = req.session().attribute("username");
		if(username.contains("admin"))
			req.session().removeAttribute("adminObj");
		else
		{
			UserController.removeTradAccfromSession(req);
			req.session().removeAttribute("playerObj");
		}
		
		req.session().removeAttribute("username");
		req.session().removeAttribute("password");
		req.session().removeAttribute("firstname");
		req.session().removeAttribute("lastname");
		req.session().removeAttribute("age");
	}
	
	public static void loadToModel(Map<String, Object> model, Request req)
	{		
		if (req.session().attribute("username") != null && req.session().attribute("firstname") != null)
		{
			model.put("username", req.session().attribute("username"));
			model.put("firstname", req.session().attribute("firstname"));
			model.put("lastname", req.session().attribute("lastname"));
			model.put("age", req.session().attribute("age"));
			model.put("password", req.session().attribute("password"));
		}
		//TODO returns null always
		if (req.session().attribute("currBal") != null)
		{
			model.put("tradingAcc", true);
			model.put("currBal", req.session().attribute("currBal"));
			model.put("sharesOwned", req.session().attribute("sharesOwned"));
		}
		else
			model.put("tradingAcc", false);
	}
}