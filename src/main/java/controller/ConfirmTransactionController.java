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

public class ConfirmTransactionController {
	public static Route companyTransaction = (req, res) -> {
		System.out.println("company Transaction Page reached");
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "utils/ConfirmTransaction.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "utils/ConfirmTransaction.vtl"));
	};
}
