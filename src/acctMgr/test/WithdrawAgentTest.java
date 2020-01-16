package acctMgr.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import acctMgr.model.Account;
import acctMgr.model.WithdrawAgent;

public class WithdrawAgentTest {

	
	Account acc;
	WithdrawAgent withdrawAg;
	
	
	@Before
	public void setUp() throws Exception {
		acc = new Account("Bob", "58392", new BigDecimal("180.45"),2);
		withdrawAg = new WithdrawAgent(acc, new BigDecimal("10.07"), 15);
	}

	@After
	public void tearDown() throws Exception {
		acc = null;
		withdrawAg = null;
	}

	@Test
	public void testWithdrawAgent() {
		withdrawAg.run();
		System.out.println("Balance " + acc.getBalance());
		assertEquals(acc.getBalance().toString(), "29.40");
	}

}
