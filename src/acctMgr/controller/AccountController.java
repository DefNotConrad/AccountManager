package acctMgr.controller;

import acctMgr.model.Account;
import acctMgr.model.Agent;
import acctMgr.util.AgentManager;
import acctMgr.model.OverdrawException;
import acctMgr.view.AccountView;
import acctMgr.view.AgentView;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.SwingUtilities;

public class AccountController extends AbstractController {
	BigDecimal euro = new BigDecimal(0.79);
	BigDecimal yen = new BigDecimal(94.1);
	
	/**
	 * takes actionevent and translates it to deposit, withdraw, and agent button functions. Converts amounts based on currency dropBox selection everything is stored in dollars.
	 * @param opt string passed based on button pressed
	 */
	public void operation(String opt) {
		
		if(opt == AccountView.Deposit) {
			BigDecimal amount = ((AccountView)getView()).getAmount();
			int currency = ((Account)getModel()).getCurrency();
			if(currency == 1) {amount= amount.divide(euro, 6, RoundingMode.HALF_UP);}
			else if(currency == 2 ) {amount = amount.divide(yen, 6, RoundingMode.HALF_UP);}
			((Account)getModel()).deposit(amount);
		} else if(opt == AccountView.Withdraw) {
			BigDecimal amount = ((AccountView)getView()).getAmount();
			int currency = ((Account)getModel()).getCurrency();
			if(currency == 1) {amount= amount.divide(euro, 6, RoundingMode.HALF_UP);}
			else if(currency == 2 ) {amount = amount.divide(yen, 6, RoundingMode.HALF_UP);}
			try {
				((Account)getModel()).withdraw(amount);
			}
			catch(OverdrawException ex) {
				final String msg = ex.getMessage();
				SwingUtilities.invokeLater(new Runnable() {
				      public void run() {
				    	  ((AccountView)getView()).showError(msg);
				      }
				    });
			}
			((AccountView)getView()).getBalanceField();
		} else if(opt == AccountView.StartDepAgent) {
			final AccountView acView = (AccountView)getView();
			int currency = ((Account)getModel()).getCurrency();
			BigDecimal amount = acView.getAmount();
			if(currency == 1) {amount= amount.divide(euro, 6, RoundingMode.HALF_UP);}
			else if(currency == 2 ) {amount = amount.divide(yen, 6, RoundingMode.HALF_UP);}
			if(amount.signum() > 0) {
				final Agent ag = AgentManager.createDepAgent(((Account)getModel()), amount);
				final AgentController agContr = new AgentController();
				agContr.setModel(ag);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						acView.createAgentView(ag, agContr);
					}
			    });
			}
			((AccountView)getView()).getBalanceField();
		} else if(opt == AccountView.StartWithdrawAgent) {
			final AccountView acView = (AccountView)getView();
			BigDecimal amount = acView.getAmount();
			int currency = ((Account)getModel()).getCurrency();
			if(currency == 1) {amount= amount.divide(euro, 6, RoundingMode.HALF_UP);}
			else if(currency == 2 ) {amount = amount.divide(yen, 6, RoundingMode.HALF_UP);}
			if(amount.signum() > 0) {
				final Account accnt = (Account)getModel();
				final Agent ag = AgentManager.createWithdrawAgent(((Account)getModel()), amount);
				final AgentController agContr = new AgentController();
				agContr.setModel(ag);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
			    	  AgentView app = acView.createAgentView(ag, agContr);
			    	  accnt.addModelListener(app);
			      }
			    });
			}
		}
	}
	/**
	 * sets account currency selection based on user pick from ComboBox and refreshes balance field based on each selection
	 * @param pick current currency combobox selection
	 */
	public void setCurrency(int pick){
		((Account)getModel()).setCurrency(pick);
		((AccountView)getView()).getBalanceField();
	}
	/**
	 * saves accountList
	 * @param index current selected combobox account
	 */
	public void saveList(int index) {
		((Account)getModel()).saveList(index);
	}
	/**
	 * selects account based on user selection from Combobox, and refreshes balance field and currency ComboBox
	 * @param index current selection index
	 * @param previous previous index amount
	 */
	public void getAccountIndex(int index, int previous) {
		if(previous>0) {saveList(previous);}
		((Account)getModel()).setAccount(index);
		((AccountView)getView()).getCurrencyDrop();
		((AccountView)getView()).getBalanceField();
	}
	
}
