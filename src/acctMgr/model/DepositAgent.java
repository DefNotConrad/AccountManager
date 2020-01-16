package acctMgr.model;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DepositAgent extends Agent implements Runnable{
	
	public DepositAgent(Account account, BigDecimal amount){
		this.account = account;
		this.amount = amount;
		this.amount.setScale(2, RoundingMode.HALF_UP);
		this.transferred = BigDecimal.ZERO;
		this.transferred.setScale(2, RoundingMode.HALF_UP);
		
		this.status = AgentStatus.Running;
		this.active = true;
		this.paused = false;
		this.pauseLock = new Object();
	}
	public DepositAgent(Account account, BigDecimal amount, int iters){
		this(account, amount);
		this.iters = iters;
		this.active = false;
	}
	public void run() {
			while(active || iters > 0) {
				synchronized (pauseLock) {
					while (paused) {
						try {
							pauseLock.wait();
						} catch (InterruptedException e) {
							System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
						}
					}
				}
				account.deposit(amount);
				transferred = transferred.add(amount);
				iters--;
				final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AmountTransferredUpdate, transferred, AgentStatus.NA);
				SwingUtilities.invokeLater(
						new Runnable() {
							public void run() {
								notifyChanged(me);
							}
						});
				try {
					Thread.sleep(50);
				}
				catch(InterruptedException ex){
					System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
				}
			}
		
	}
}
