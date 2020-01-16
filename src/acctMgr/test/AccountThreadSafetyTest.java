package acctMgr.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import acctMgr.model.Account;
import acctMgr.model.OverdrawException;
import acctMgr.model.DepositAgent;
import acctMgr.model.WithdrawAgent;

public class AccountThreadSafetyTest {
	Account acc;
	WithdrawAgent withdrawAg;
	DepositAgent depAg;
	@Before
	public void setUp() throws Exception {
		acc = new Account("Alice", "58392", new BigDecimal("1000.45"), 1);
		depAg = new DepositAgent(acc, new BigDecimal("3.32"), 15);
		withdrawAg = new WithdrawAgent(acc, new BigDecimal("10.07"), 10);
	}

	@After
	public void tearDown() throws Exception {
		acc = null;
	}

	@Test
	public void testAccountThreadSafe() {
		Thread depAgT = new Thread(depAg);
		Thread withdrawAgT = new Thread(withdrawAg);
		
		depAgT.start();
		withdrawAgT.start();
		try{
			depAgT.join();
			withdrawAgT.join();
		}
		catch(InterruptedException ex){System.out.println("Agent threads interrupted");}
		System.out.println("deposit agent transferred " + depAg.getTransferred());
		System.out.println("withdraw agent transferred " + withdrawAg.getTransferred());
		assertEquals(depAg.getTransferred().toString(), "49.80");
		assertEquals(withdrawAg.getTransferred().toString(), "100.70");
		assertEquals(acc.getBalance().toString(), "949.55");
	}

}
