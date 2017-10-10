package app;

import static spark.Spark.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import model.FileTools;
import model.Menu;
import model.Player;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.servlet.SparkFilter;
import spark.template.velocity.VelocityTemplateEngine;

public class Application {
	public static Menu menu;
	
	//this class is used to test for Velocity template
	public static void main(String[] args){	    
		staticFiles.location("/public");
		port(getHerokuAssignedPort());
		menu = new Menu();
		
		// login page paths
		get("/",						controller.LoginController.mainPage);
		get("/login",					controller.LoginController.loginPage);
		get("/redirectUser",			controller.LoginController.redirectUser);
		post("/logout",                 controller.LoginController.logout);
		
		// register page paths
		get("/register",				controller.RegisterController.registerPage);
		get("/regSuccess",				controller.RegisterController.regSuccess);
		
		// user page paths
		get("/userPage",				controller.UserController.userPage);
		get("/confirmEditProfile",		controller.UserController.confirmEditProfile);
		get("/openTradingAccount",		controller.UserController.openTradingAcc);
		get("/deleteAccount",	        controller.UserController.deleteAccount);
		get("/TransactionAccount",		controller.TransactionController.transactionAccount);
		get("/ConfirmTransaction",		controller.TransactionController.ConfirmTransaction);
		
		// admin page paths
		get("/adminPage",				controller.AdminController.adminPage);
		post("/searchPlayer",			controller.AdminController.searchPlayer);
		get("/listPlayers",				controller.AdminController.listPlayers);
		get("/deletePlayer",			controller.AdminController.deletePlayer);
		get("/userTransactions",		controller.AdminController.userTransactions);
		get("/allTransactions",			controller.AdminController.allTransactions);
		get("/CompanyPage", 			controller.CompanyPageController.companyPage);
		get("/Leaderboard", 			controller.LeaderboardController.leaderboard);
		
		get("/ERROR_PAGE",				controller.ErrorPageController.NOT_FOUND_PAGE);
		
		// For all pages not defined by the application
		notFound(controller.ErrorPageController.NOT_FOUND_PAGE);
		internalServerError(controller.ErrorPageController.INTERNAL_SERVER_ERROR_PAGE);
		
		//start fetching data every hour
		startDataFetching();
	}
	
	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if(processBuilder.environment().get("PORT") != null)
		{
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567;
	}
	
	//starts fetching data and repeat every our
	private static void startDataFetching()
	{
		FileTools fileTool = new FileTools();
		
		//fetch date from url every hour in another thread while program is running
		new Thread(){
			public void run()
			{
				while (true)
				{
					fileTool.fetchAllShareData();
					
					try {
						TimeUnit.HOURS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
