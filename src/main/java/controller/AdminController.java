package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class AdminController {
	public static Route adminPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route searchPlayer = (req, res) -> {
		Map<String, Object> model = searchPlayer(req);
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
	
//	public void loadToModel(Map<String, Object> model, Request req)
//	{
//		if (req.session().attribute("username") != null && req.session().attribute("firstname") != null)
//		{
//			model.put("username", req.session().attribute("username"));
//			model.put("firstname", req.session().attribute("firstname"));
//			model.put("lastname", req.session().attribute("lastname"));
//			model.put("age", req.session().attribute("age"));
//			model.put("password", req.session().attribute("password"));
//		}
//	}
	
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
}