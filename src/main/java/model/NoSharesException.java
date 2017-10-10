package model;

import java.util.ArrayList;

public class NoSharesException extends Exception{
	private ArrayList<String[]> sharesOwned;
	
	public NoSharesException (ArrayList<String[]> sharesOwned2){
		this.sharesOwned = sharesOwned2;
	}
	
	public ArrayList<String[]> getSharesOwned(){
		return sharesOwned;
	}
}
