package acctMgr.model;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Account extends AbstractModel {
	public BigDecimal balance;
	public String name;
	public String ID;
	public int currency;
	public AccountList accountList;
	
	public Account(){
		name = "Name";
		ID = "ID";
		balance= new BigDecimal(0000);
		balance.setScale(2, RoundingMode.HALF_UP);
		currency=0;
	}
	public Account(String name, String ID, BigDecimal balance, int currency){
		this.name = name;
		this.ID = ID;
		this.balance = balance;
		this.balance.setScale(2, RoundingMode.HALF_UP);
		this.currency=currency;
	}
	
	/**
	 * populates accountList
	 */
	public void acctList() {accountList = new AccountList();}
	/**
	 * sets currency for exchange rates/account preferences
	 * @param currencies
	 */
	public void setCurrency(int currencies) {currency=currencies;}
	/**
	 * gets currency
	 * @return currency
	 */
	public Integer getCurrency() {return currency;}
	/**
	 * gets account balance
	 * @return balance
	 */
	public BigDecimal getBalance(){return balance;}
	/**
	 * deposits amount into account
	 * @param amount
	 */
	public synchronized void deposit(BigDecimal amount) {
		balance = balance.add(amount);
		
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		//notifyChanged(me);
		
		
		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
		notifyAll();
	}
	/**
	 * withdraws amount from account
	 * @param amount
	 * @throws OverdrawException
	 */
	public synchronized void withdraw(BigDecimal amount) throws OverdrawException {
		BigDecimal newB = balance.add(BigDecimal.ZERO);
		newB = newB.subtract(amount);
		if(newB.signum() <= 0) throw new OverdrawException(newB);
		balance = newB;
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		
		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
	}
	/**
	 * autowithdraw from agent window of selected amount
	 * @param amount
	 * @param ag
	 * @throws InterruptedException
	 */
	public synchronized void autoWithdraw(BigDecimal amount, Agent ag) throws InterruptedException {
		//try {
			//System.out.println("Trying to withdraw " + amount + " from balance " + balance);
			
			//if(balance - amount < 0.0) {
				//System.out.println("Sending blocked");
				//ag.setStatus(AgentStatus.Blocked);		
			//}
			BigDecimal aux = balance.add(BigDecimal.ZERO);
			System.out.println("autoWithdraw ; aux = " + aux);
			aux = aux.subtract(amount);
			System.out.println("autoWithdraw ; subtracted aux = " + aux);
			System.out.println("autoWithdraw ; signum = " + aux.signum());
			while(aux.signum() < 0) {
				ag.setStatus(AgentStatus.Blocked);
				System.out.println("autoWithdraw blocking");
				wait();
			}
			if(ag.getStatus() == AgentStatus.Paused) return;
			ag.setStatus(AgentStatus.Running);
					
			balance = balance.subtract(amount);
			final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, this.balance, AgentStatus.Running);
			SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
		//}
		//catch(InterruptedException ex){
		//	System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
		//}
		/*
		catch(InvocationTargetException ex){
			System.out.println("Thread " + Thread.currentThread().getName() + " " + ex.getMessage());
			ex.printStackTrace();
		}
		*/
	}
	public AccountList getAccountList() {return accountList;}
	/**
	 * populates model account with account from accountList
	 * @param index
	 */
	public void setAccount(int index) {
		this.name =(accountList.acctList.get(index)).name;
		this.ID = (accountList.acctList.get(index)).ID;
		this.balance = (accountList.acctList.get(index)).balance;
		this.currency = (accountList.acctList.get(index)).currency;
	}
	/**
	 * saves accountList
	 * @param index
	 */
	public void saveList(int index) {
	accountList.acctList.get(index).balance = this.balance;	
	accountList.acctList.get(index).currency = this.currency;
	}
}


