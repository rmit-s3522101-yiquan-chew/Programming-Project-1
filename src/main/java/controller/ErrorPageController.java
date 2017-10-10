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

public class ErrorPageController {
	public static Route NOT_FOUND_PAGE = (req, res) -> {
		
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "/ERROR/404_PAGE.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Route INTERNAL_SERVER_ERROR_PAGE = (req, res) -> {
		
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "/ERROR/ISE_PAGE.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
}