package acctMgr.test;

import static org.junit.Assert.*;
import acctMgr.model.Account;
import acctMgr.model.OverdrawException;
import java.math.BigDecimal;
import acctMgr.model.WithdrawAgent;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AutoWithdrawBlockTest {
	Account acc;
	WithdrawAgent withdrawAg;
	final static int TIMEOUT = 500;
	private volatile boolean success = true;

	private class Taker implements Runnable {
		private Account acc;
		public  Taker(Account acc) {this.acc = acc;}
		public void run() {
			try {
				acc.autoWithdraw(new BigDecimal("100.07"), withdrawAg);
				System.out.println("failing inside taker");
				success = false;
				fail("get blocking failed");
				//assertTrue(false);
			} catch(InterruptedException success){
				System.out.println("take thread interrupted");
			}
			catch(AssertionFailedError ex) {
				System.out.println("AssertionFailedError");
			}
		}
	}
	@Before
	public void setUp() throws Exception {
		acc = new Account("Bob", "58392", new BigDecimal("10.45"), 2);
		withdrawAg = new WithdrawAgent(acc, new BigDecimal("100.07"), 1);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testWithdrawBlock() {
		Thread taker = new Thread(new Taker(acc));
		try {
			taker.start();
			Thread.sleep(TIMEOUT);
			//System.out.println("taker is alive after sleep, interrupting");
			taker.interrupt();
			taker.join(TIMEOUT);	
			if(success) assertFalse(taker.isAlive());
			else fail();
			
		}catch(Exception unexpected){
			fail("unexpected fail");
		}
	}

}
