package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class RegisterController {
	public static Route registerPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "/mainpage/register.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Route regSuccess = (req, res) -> {
		Map<String, Object> model = register(req);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Map<String, Object> register(Request req) throws IOException{
		Map<String, Object> model = new HashMap<>();
		
		String userName = req.queryParams("username");
		String firstName = req.queryParams("firstname");
		String lastName = req.queryParams("lastname");
		int age = Integer.parseInt(req.queryParams("age"));
		String password = req.queryParams("password");
		String confirmPassword = req.queryParams("confirmpassword");
		
		if(app.Application.menu.register(userName, firstName, lastName, age, password, confirmPassword))
		{	
			model.put("template", "/mainpage/regSuccess.vtl");
		}
		else
			model.put("template", "/mainpage/register.vtl");
		
		return model;	
	}
}