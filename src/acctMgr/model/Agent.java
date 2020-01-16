package acctMgr.model;

import java.math.BigDecimal;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Agent extends AbstractModel implements IAgent {
	
	protected Object pauseLock;
	protected volatile boolean paused;
	public volatile boolean active;
	protected Account account;
	protected BigDecimal amount;
	protected BigDecimal transferred;
	protected int iters = 0;
	protected String name = new String("Default");
	protected AgentStatus status;
	protected volatile boolean wasBlocked;

	public BigDecimal getTransferred(){return transferred;}
	public void onPause() {
		synchronized (pauseLock) {
			paused = true;
			setStatus(AgentStatus.Paused);
		}
	}

	public void onResume() {
		synchronized (pauseLock) {
			if(wasBlocked) setStatus(AgentStatus.Blocked);
			else setStatus(AgentStatus.Running);
			paused = false;
			pauseLock.notify();
		}
	}
	public void setStatus(AgentStatus agSt) {
		status = agSt;
		if(status == AgentStatus.Blocked) wasBlocked = true;
		if(status == AgentStatus.Running) wasBlocked = false;
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AgentStatusUpdate, BigDecimal.ZERO, agSt);
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	public AgentStatus getStatus(){return status;}
	public void setName(String name) {this.name = name;}
	public String getName(){return name;}
	public Account getAccount(){return account;}
	public void finish(){
		active = false;
	}
	
	public Boolean checkAgent(Integer agentID) {
		Boolean validID=false;
		Scanner scn= new Scanner("Agents.txt");
		while (scn.hasNext()) {
			if (agentID==Integer.parseInt(scn.next())){
				validID=true;
			}
		}
		scn.close();
		return validID;
	}
}