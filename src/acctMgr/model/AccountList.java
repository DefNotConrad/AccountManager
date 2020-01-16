package acctMgr.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class AccountList extends AbstractModel {

	public List <Account> acctList = new ArrayList <Account> ();
	
	/**
	 * constructor populates from file
	 */
	public AccountList(){
		load();
	}
	/**
	 * adds account to acctList
	 * @param name
	 * @param ID
	 * @param balance
	 * @param currency
	 */
	public void addAcct(String name, String ID, BigDecimal balance, int currency) {
		Account acct = new Account(name, ID, balance, currency);
		acctList.add(acct);
	}
	/**
	 * loads accounts from file to acctList
	 */
	public void load(){
		File file = new File("src/acctMgr/model/AccountList.txt");
		try {
			Scanner scn = new Scanner(file);
			scn.useDelimiter(",");
			while (scn.hasNext()) {
				String ID = scn.next();
				String name = scn.next();
				BigDecimal balance = new BigDecimal(scn.next());
				int currency=Integer.parseInt(scn.next());
				addAcct(name, ID, balance, currency);
		}
		scn.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return acctList
	 */
	public List <Account> getList() {return acctList;}
	/**
	 * saves acctList to initial file (overwritting old data) (in progress)
	 * @throws FileNotFoundException
	 */
	public void save() throws FileNotFoundException {
		
		PrintWriter out = new PrintWriter(new FileOutputStream("acctMgrEx/src/acctMgr/model/AccountList.txt", false));
		
		for(Account it : this.acctList) {
			
			out.println(it.ID+","+it.name+","+it.balance.toString()+","+Integer.toString(it.currency)+",");
		}
		out.close();	
	}
}
