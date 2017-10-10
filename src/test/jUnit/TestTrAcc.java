import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import model.FileTools;
import model.InsufficientFundsException;
import model.NoSharesException;
import model.Player;
import model.Shares;
import model.TradingAcc;
import model.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestTrAcc {

	TradingAcc trAcc;
	Shares testShare;
	
	@Before
	public void Before() throws Exception {
		trAcc = new TradingAcc("testing");
		trAcc.setCurrBal(TradingAcc.INIT_BAL);
		
		//testShare with asx code "TST" no longer usable tests containing it needs to be updated
		//testShare = new Shares("TST", "testCompany", "test group", new BigDecimal(5.5));
		
		testShare = FileTools.loadShare("BHP");
	}
	
	//@Test
	public void testBuy() throws InsufficientFundsException {
		Transaction trans = trAcc.buyShares(testShare, 3);
		assertNotNull(trans);
		assertEquals(trans.getID(), "testing");
		assertEquals(trans.getASXcode(), "BHP");
		assertEquals(trans.getCompName(), "BHP BILLITON LIMITED");
		assertEquals(trans.getTransType(), Transaction.TransType.BUYING);
	}
	
	//@Test
	public void testBuyFail() throws InsufficientFundsException {
		trAcc.setCurrBal(new BigDecimal(2));
		Transaction trans = trAcc.buyShares(testShare, 1);
		assertNull(trans);
	}
	
	//@Test
	public void testSell() throws NoSharesException {
		try {
			trAcc.buyShares(testShare, 1);
		} catch (InsufficientFundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Transaction trans = trAcc.sellShares(testShare, 1);
		assertNotNull(trans);
		assertEquals(trans.getID(), "testing");
		assertEquals(trans.getASXcode(), "BHP");
		assertEquals(trans.getCompName(), "BHP BILLITON LIMITED");
		assertEquals(trans.getTransType(), Transaction.TransType.SELLING);
	}
	
	//@Test
	public void testSellFail() throws NoSharesException {
		Transaction trans = trAcc.sellShares(testShare, 1);
		assertNull(trans);
	}
	
	//@Test 
	public void testShowCurrStockVal()
	{
		ArrayList<String[]> testSharesOwned = new ArrayList<String[]>();
		testSharesOwned.add(new String[]{"BHP", "2"});
		testSharesOwned.add(new String[]{"ASX", "1"});
		trAcc.setSharesOwned(testSharesOwned);
		System.out.println(trAcc.showCurrStockVal());
		assertFalse(trAcc.showCurrStockVal() == new BigDecimal(0));
	}
	
	@Test 
	public void testLoadTrAcc()
	{
		Player player = (Player) FileTools.LoadUser("bobby123");
		System.out.println(player.getFName());
		System.out.println(player.getTradingAcc().getCurrBal());
		System.out.println(player.getTradingAcc().getSharesOwned().get(0)[0]);
	}

	@After
	public void After() throws Exception {
	}

	

}
