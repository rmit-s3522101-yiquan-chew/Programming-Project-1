package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Admin;
import model.FileTools;
import model.Menu;
import model.Player;
import model.Transaction;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class AdminController {	
	public static Route adminPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		loadToModel(model, req);
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route searchPlayer = (req, res) -> {
		Map<String, Object> model = searchPlayer(req);
		LoginController.loadToModel(model, req);
		
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route listPlayers = (req, res) -> {
		Map<String, Object> model = listPlayers(req);
		LoginController.loadToModel(model, req);
		
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route deletePlayer = (req, res) -> {
		Map<String, Object> model = deletePlayer(req);
		LoginController.loadToModel(model, req);
		
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route lastTransaction = (req, res) -> {
		Map<String, Object> model = lastTransaction(req);
		LoginController.loadToModel(model, req);
		
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
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
	}

	public static Map<String, Object> searchPlayer(Request req)
	{
		Map<String, Object> model = new HashMap<>();
		User user = null;
		
		if((user = FileTools.LoadUser(req.queryParams("searchPlayerID"))) == null)
		{
			model.put("userNotFound", true);
			return model;
		}
		
		model.put("userUsername", user.getID());
		model.put("userFirstName", user.getFName());
		model.put("userLastName", user.getLName());
		model.put("userAge", user.getAge());
		model.put("userFound", true);
		return model;
	}
	
	public static Map<String, Object> listPlayers(Request req)
	{
		Map<String, Object> model = new HashMap<>();
		List<String[]> playerList = null;
		try {
			playerList = FileTools.readCSV(FileTools.USER_DATA_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// removes line containing field headers
		playerList.remove(0);
		
		model.put("listPlayers", true);
		model.put("playerList", playerList);
		return model;
	}
	
	public static Map<String, Object> deletePlayer(Request req)
	{
		Map<String, Object> model = new HashMap<>();
		User user = null;
		
		if((user = FileTools.LoadUser(req.queryParams("deletePlayerID"))) == null)
		{
			model.put("deletionFailed", true);
			return model;
		}
		
		try {
			Admin.delUser(user.getFName());
			model.put("deletionSucceeded", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	private static Map<String, Object> lastTransaction(Request req) {
		Map<String, Object> model = new HashMap<>();
		String userId = req.queryParams("listPlayerID");
		
		Transaction lastTransaction = Transaction.loadLastTrans(userId);
		
		model.put("userId", userId);
		model.put("transType", lastTransaction.getTransType());
		model.put("ASXcode", lastTransaction.getASXcode());
		model.put("numShares", lastTransaction.getNumShares());
		model.put("compName", lastTransaction.getCompName());
		model.put("shareVal", lastTransaction.getShareVal());
		model.put("dateTime", lastTransaction.getDateTime());
		
		model.put("lastTransaction", true);
		return model;
	}
}