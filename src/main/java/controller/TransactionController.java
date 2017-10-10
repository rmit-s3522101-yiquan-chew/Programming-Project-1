package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.FileTools;
import model.InsufficientFundsException;
import model.Menu;
import model.NoSharesException;
import model.Player;
import model.Shares;
import model.TradingAcc;
import model.Transaction;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class TransactionController {
	
	//Initialise transactionAccount page and details
	public static Route transactionAccount = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		Player player = req.session().attribute("playerObj");
		if (player != null)
		{
			TradingAcc trAcc = player.getTradingAcc();
			Transaction lastTrans = req.session().attribute("lastTrans");
			
			if (trAcc == null)
			{
				player.loadTrAcc();
			}
			model.put("username", trAcc.getUser_ID());
			model.put("currBal", trAcc.getCurrBal());
			model.put("stockVal", trAcc.showCurrStockVal());
			model.put("shareList", trAcc.getSharesOwned());
			
			//show player topshares details
			model.put("topShare", trAcc.getSharesOwned().get(0));
			
			//show all company to buy
			model.put("table", "utils/companyTable.vtl");
			
			//load last trans if attribute is null
			if (lastTrans == null)
			{
				lastTrans = Transaction.loadLastTrans(trAcc.getUser_ID());
				if (lastTrans != null)
				{
					req.session().attribute("lastTrans", lastTrans);
				}
			}
			putTransToModel(model, lastTrans);
			
			//transaction history
			ArrayList<String[]> transList = FileTools.getTransactionLog(trAcc.getUser_ID());
			model.put("transList", transList);
		}
		
		model.put("userTemplate", "/users/TransactionAccount.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	//page to show user transaction right after buying/selling
	public static Route ConfirmTransaction = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		Player player = req.session().attribute("playerObj");
		Transaction.TransType transType = Transaction.TransType.valueOf(req.queryParams("transType"));
		int amtShares;
		Shares share;
		Transaction transaction = null;
		
		if (req.queryParams("amtShares").isEmpty())
		{
			amtShares = 1;
		}
		else
		{
			amtShares = Integer.parseInt(req.queryParams("amtShares"));
		}
		
		try {			
			//do buy/sell transaction depending on transType
			if (transType == Transaction.TransType.BUYING)
			{
				share = FileTools.loadShare(req.queryParams("ASXCode"));
				transaction = player.getTradingAcc().buyShares(share, amtShares);
			}
			else //get sell details
			{
				share = FileTools.loadShare(req.queryParams("ASXCode"));
				transaction = player.getTradingAcc().sellShares(share, amtShares);
			}
			
			//to load into latest transaction
			req.session().attribute("lastTrans", transaction);
			model.put("currBal", player.getTradingAcc().getCurrBal());
			model.put("stockVal", player.getTradingAcc().showCurrStockVal());
			
			putTransToModel(model, transaction);
		} catch (InsufficientFundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.put("username", player.getID());
		model.put("userTemplate", "/utils/ConfirmTransaction.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	//loads transaction in model, used by both buy and sell in controller
	public static void putTransToModel(Map<String, Object> model, Transaction transaction)
	{
		if (transaction != null)
		{
			model.put("transUserId", transaction.getID());
			model.put("ASXcode", transaction.getASXcode());
			model.put("compName", transaction.getCompName());
			model.put("shareVal", transaction.getShareVal());
			model.put("numShares", transaction.getNumShares());
			model.put("totalPrice", transaction.getTotalPrice());
			model.put("transType", transaction.getTransType());
			model.put("transDate", transaction.getDateTime());
		}
	}
}