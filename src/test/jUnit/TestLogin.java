

import static org.junit.Assert.*;
import model.Menu;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLogin {

	static Menu menu;
	
	@BeforeClass
	public static void Before() {
		menu = new Menu();
	}
	
	@Test
	public void loginPassAdmin() {
		assertTrue(menu.login("admin", "#Admin123"));
	}
	
	@Test
	public void loginPassPlayer() {
		assertTrue(menu.login("bobby123", "Qwerty12!"));
	}
	
	@Test
	public void loginFail() {
		assertFalse(menu.login("notInDB", "FakePW!"));
	}
	
	@Test
	public void loginwrongPW() {
		assertFalse(menu.login("admin", "WrongPW!"));
	}

	@AfterClass
	public static void After() {
		
	}
}
