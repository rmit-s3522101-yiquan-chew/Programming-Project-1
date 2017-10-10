import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import model.FileTools;
import model.Player;
import model.Shares;
import model.TradingAcc;

public class PlayerTest {
	static Player player = null;
	
	@BeforeClass
	public static void Before(){
		FileTools ft = new FileTools();
		player = (Player) ft.LoadUser("bobby123");
	}

	@Test
	public void editProfiletest() throws IOException {
		
		Player editedPlayer = player.editProfile("bobby123", "bobby123", "Asdfgh34!", "Dave", "Smith", 20);
		
		System.out.println(editedPlayer.getPassword());
		System.out.println(editedPlayer.getFName());
		
		assertEquals(editedPlayer.getPassword(), "Asdfgh34!");
	}
	
	@AfterClass
	public static void openTrAccTest() throws IOException{
		TradingAcc tr;
		tr = player.openTradeAcc(player.getID());
		
		//current balanced is set to the initial balance of $1,000,000
		assertEquals(tr.getCurrBal(), tr.INIT_BAL);
		
		//new TradingAcc does not contain any shares
		ArrayList<String[]> sharesOwned = tr.getSharesOwned();
		assertEquals(sharesOwned.size(), 0);
	}

}
