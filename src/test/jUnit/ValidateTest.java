import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Validate;

public class ValidateTest {
	
	static Validate val = null;
	
	@BeforeClass
	public static void classCall(){
		 val = new Validate();
	}
	
	//@Test
	public void validateAgetest() {
		boolean ageRange = val.validateAge(50);
		assertTrue(ageRange);
		
		ageRange = val.validateAge(105);
		assertFalse(ageRange);
	}
	
	@Test
	public void valNameTest(){
		boolean nameInput = val.valName("John");
		assertTrue(nameInput);
		
		nameInput = val.valName("jOhn");
		assertFalse(nameInput);
	}
	
	//@Test
	public void valPassword(){
		boolean passwordInput = val.valPassword("Password123!");
		assertTrue(passwordInput);
		
		passwordInput = val.valPassword("1234");
		assertFalse(passwordInput);
	}
	
	//@Test
	public void valPwConfirmation(){
		boolean comparison = val.pwConfirm("Password123!", "Password123!");
		assertTrue(comparison);
		
		comparison = val.pwConfirm("Password123!", "Password124!");
		assertFalse(comparison);
	}

}
