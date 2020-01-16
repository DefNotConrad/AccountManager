package acctMgr.view;

import acctMgr.controller.AgentController;
import acctMgr.controller.Controller;
import acctMgr.controller.AccountController;
import acctMgr.model.Agent;
import acctMgr.model.Model;
import acctMgr.model.Account;
import acctMgr.model.AccountList;
import acctMgr.model.ModelEvent;
import acctMgr.util.AgentManager;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.JTextField;
//import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

/**
 * 
 *
 * @author Britain Mackenzie
 *
 */

public class AccountView extends JFrameView {
	public final static String Deposit = "Deposit";
	public final static String Withdraw = "Withdraw";
	public final static String StartDepAgent = "StartDepAgent";
	public final static String StartWithdrawAgent = "StartWithdrawAgent";
	public final static String Accounts = "Accounts";
	public final static String[] currencies = {"$","€","¥"};
	private JPanel topPanel;
	private JPanel textPanel;
	private JPanel buttonPanel;
	private JLabel balanceLabel;
	private JLabel amountLabel;
	private JTextPane contactField;
	private JTextPane balanceField;
	private JTextField amountField;
	private JButton depButton;
	private JButton withdrawButton;
	private JButton startDepAgentButton;
	private JButton startWithdrawAgentButton;
	public static JComboBox <String[]> currencyDrop;
	public static JComboBox <String> accountList;
	public int index=-1;
	
	private Handler handler = new Handler();
	private List<AgentController> agentContrs = new ArrayList<AgentController>(10);
	
	private String initAmountS;
	
	/**
	 * Constructor sets save on close (still working on that)
	 * @param model
	 * @param controller
	 */
	private AccountView(Model model, Controller controller){
		super(model, controller);
		initAmountS = "0.00";
		this.getContentPane().add(getContent());
		
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent evt) {
		    	for(AgentController agContr : agentContrs) agContr.operation(AgentView.Dismiss);
		    	((AccountController)getController()).saveList(index);
		    	AgentManager.finishThreads();
		        dispose();
				
		        System.exit(0);
		    }
		});
		
		setLocation(400, 300);
		pack();
	}
	/**
	 * Populates Jpanel with buttonPanel, textFieldPanel and ComboBoxes
	 * @return
	 */
	private JPanel getContent() {
		if (topPanel == null) {
			topPanel = new JPanel();
			GridLayout layout = new GridLayout(2, 3);
			topPanel.setLayout(layout);
			//topPanel.setPreferredSize(new Dimension(300, 100));
			GridBagConstraints ps = new GridBagConstraints();
			ps.gridx = 0;
			ps.gridy = 0;
			ps.fill = GridBagConstraints.HORIZONTAL;
			topPanel.add(getAccountList(),null);
			
			GridBagConstraints bs = new GridBagConstraints();
			bs.gridx = 0;
			bs.gridy = 2;
			bs.fill = GridBagConstraints.VERTICAL;
			topPanel.add(getTextFieldPanel(), null);
			topPanel.add(getButtonPanel(), null);
			
		}
		return topPanel;
	}
	/**
	 * populates Deposit button
	 * @return depButton
	 */
	private JButton getDepButton(){
		if(depButton == null){
			depButton = new JButton(Deposit);
			depButton.addActionListener(handler);
		}
		return depButton;
	}
	/**
	 * populates Withdraw button
	 * @return withdrawButton
	 */
	private JButton getWithdrawButton(){
		if(withdrawButton == null){
			withdrawButton = new JButton(Withdraw);
			withdrawButton.addActionListener(handler);
		}
		return withdrawButton;
	}
	/**
	 * populates DepositAgent button
	 * @return startDepAgentButton
	 */
	private JButton getDepAgentButton(){
		if(startDepAgentButton == null){
			startDepAgentButton = new JButton(StartDepAgent);
			startDepAgentButton.addActionListener(handler);
		}
		return startDepAgentButton;
	}
	/**
	 * populates WithdrawAgent button
	 * @return startWithdrawAgentButton
	 */
	private JButton getWithdrawAgentButton(){
		if(startWithdrawAgentButton == null){
			startWithdrawAgentButton = new JButton(StartWithdrawAgent);
			startWithdrawAgentButton.addActionListener(handler);
		}
		return startWithdrawAgentButton;
	}
	/**
	 * Creates buttonPanel and populates buttonPanel
	 * @return buttonPanel 
	 */
	private JPanel getButtonPanel()
	{
		if(buttonPanel == null){
			GridBagConstraints depButtonCtr = new GridBagConstraints();
			depButtonCtr.gridx = 0;
			depButtonCtr.gridy = 0;
			
			GridBagConstraints wButtonCtr = new GridBagConstraints();
			wButtonCtr.gridx = 1;
			wButtonCtr.gridy = 0;
			
			GridBagConstraints depAgButtonCtr = new GridBagConstraints();
			depAgButtonCtr.gridx = 2;
			depAgButtonCtr.gridy = 0;
			
			GridBagConstraints wAgButtonCtr = new GridBagConstraints();
			wAgButtonCtr.gridx = 3;
			wAgButtonCtr.gridy = 0;
			
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDepButton(), depButtonCtr);
			buttonPanel.add(getWithdrawButton(), wButtonCtr);
			buttonPanel.add(getDepAgentButton(), depAgButtonCtr);
			buttonPanel.add(getWithdrawAgentButton(), wAgButtonCtr);
		}
		
		return buttonPanel;
	}
	/**
	 * 
	 * Creates and populates textFieldPanel with balance and amount areas and currency ComboBox
	 * @return textPanel
	 */
	private JPanel getTextFieldPanel()
	{
		if(textPanel == null){
			GridBagConstraints bl = new GridBagConstraints();
			bl.gridx = 0;
			bl.gridy = 0;
			
			GridBagConstraints bf = new GridBagConstraints();
			bf.gridx = 1;
			bf.gridy = 0;
			
			GridBagConstraints aml = new GridBagConstraints();
			aml.gridx = 0;
			aml.gridy = 1;
			aml.gridwidth =1;
			
			GridBagConstraints db  = new GridBagConstraints();
			db.gridx  = 1;
			db.gridy  = 1;
			db.gridwidth = 1;
			db.gridheight = 1;
			db.anchor = GridBagConstraints.EAST;
			
			GridBagConstraints amf = new GridBagConstraints();
			amf.gridx = 2;
			amf.gridy = 1;
			amf.anchor = GridBagConstraints.WEST; 
			
			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			textPanel.add(getBalanceLabel(), bl);
			textPanel.add(getBalanceField(), bf);;
			textPanel.add(getAmountLabel(), aml);
			textPanel.add(getCurrencyDrop(),db);
			textPanel.add(getAmountField(), amf);
		}
		return textPanel;
	}
	/**
	 * populates balanceLabel
	 * @return balanceLabel
	 */
	private JLabel getBalanceLabel(){
		if(balanceLabel == null){
			balanceLabel = new JLabel();
			balanceLabel.setText("Balance:");
			balanceLabel.setPreferredSize(new Dimension(200, 20));
		}
		return balanceLabel;
	}
	/**
	 * If null populates balance field, if not null populates balance field based on selected currency from ComboBox
	 * @return balanceField
	 */
	public JTextPane getBalanceField(){
		if(balanceField == null){
			balanceField = new JTextPane();
			balanceField.setSize(200, 25);
			balanceField.setEditable(false);
		}
		int currency = (((Account)getModel()).getCurrency());
		BigDecimal amount= BigDecimal.ZERO;
		BigDecimal euro = new BigDecimal(.79);
		BigDecimal yen = new BigDecimal(94.1);
		
		amount=((Account)getModel()).getBalance();
		if (currency ==1) {
			amount = amount.multiply(euro);
			balanceField.setText("€"+(amount.setScale(2, RoundingMode.HALF_UP)).toString());}
		else if (currency == 2) {
			amount = amount.multiply(yen);
			balanceField.setText("¥"+(amount.setScale(2, RoundingMode.HALF_UP)).toString());}
		else {balanceField.setText("$"+(((Account)getModel()).getBalance().setScale(2, RoundingMode.HALF_UP)).toString());}
		
		return balanceField;
	}
	/**
	 * populates amountField
	 * @return amountLabel
	 */
	private JLabel getAmountLabel(){
		if(amountLabel == null){
			amountLabel = new JLabel();
			amountLabel.setText("Amount:");
			amountLabel.setPreferredSize(new Dimension(200, 20));
		}
		return amountLabel;
	}
	/**
	 * returns user entered amount
	 * @return amountField
	 */
	private JTextField getAmountField(){
		if(amountField == null){
			amountField = new JTextField("0.00");
			amountField.setSize(200, 25);
			amountField.setEditable(true);
			amountField.addActionListener(handler);
		}
		return amountField;
	}
	/**
	 * populates currency ComboBox, and returns selected currency type for conversion and storage in each account
	 * @return currencyDrop
	 */
	public JComboBox getCurrencyDrop(){
		if(currencyDrop == null) {
			currencyDrop = new JComboBox(currencies);
			currencyDrop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((AccountController)getController()).setCurrency(currencyDrop.getSelectedIndex());
					}
			});
			currencyDrop.setSelectedItem(-1);
		}else {	
		currencyDrop.setSelectedIndex(((Account)getModel()).getCurrency());}
		return currencyDrop;
	}
	/**
	 * populates accountList ComboBox
	 * @return accountList
	 */
	private JComboBox getAccountList() {
		if(accountList == null) {
			accountList = new JComboBox();
			for(Account foo : (((Account)getModel()).accountList.getList())) {
				accountList.addItem(foo.ID +" "+ foo.name);
			}
			accountList.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
				((AccountController)getController()).getAccountIndex(accountList.getSelectedIndex(),index);
				index=accountList.getSelectedIndex();
				}
			});
			accountList.setSelectedItem(0);
		}
		return accountList;
	}
	/**
	 * gets user entered amount from amount field and stores is in BigDecimal format, also verifies user input
	 * @return amount
	 */
	public BigDecimal getAmount() {
		// no checking for parsing errors
		BigDecimal amount = BigDecimal.ZERO;
		try {
			amount = new BigDecimal(amountField.getText());
			
		}
		catch(NumberFormatException ex) {
			amountField.setText(initAmountS);
			showError("Amount field only accepts decimals");
		}
		return amount;
	}
	
	/**
	 * shows error message
	 * @param msg
	 */
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
	/**
	 * Alerts listeners of balance updates and updates balanceField with new amounts in correct currency
	 */
	public void modelChanged(ModelEvent me){
		if(me.getKind() == ModelEvent.EventKind.BalanceUpdate) {
			//System.out.println("Balance field to " + me.getBalance());
			getBalanceField();
		}
	}
	/**
	 * AgentView constructor
	 * @param ag
	 * @param agContr
	 * @return
	 */
	public AgentView createAgentView(Agent ag, AgentController agContr){
		AgentView app = new AgentView(ag, agContr);
  	  	agContr.setView(app);
  	  	agentContrs.add(agContr);
  	  	app.setVisible(true);
  	  	return app;
	}
	
	/**
	 * handler for listeners on buttons and amountfield
	 *
	 */
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			((AccountController)getController()).operation(evt.getActionCommand());
		}
	}
	
	public static void main(String[] args) {
		final Account account = new Account();
		account.acctList();
		final AccountController contr = new AccountController();
		contr.setModel(account);
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	    	  AccountView app = new AccountView(account, contr);
	    	  contr.setView(app);
	    	  app.setVisible(true);
	      }
	    });
	  }
}
