package acctMgr.test;

import acctMgr.model.Account;
import acctMgr.model.OverdrawException;

import static org.junit.Assert.*;
import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountTest {
	Account accountW;
	@Before
	public void setUp() throws Exception {
		accountW = new Account("Joe", "474832", new BigDecimal("200.45"),2);
	}

	@After
	public void tearDown() throws Exception {
		accountW = null;
	}
	
	@Test
	public void testWithdraw() throws Exception {
		accountW.withdraw(new BigDecimal("110.55"));
		System.out.println("withdraw, new balance = " + accountW.getBalance());
		assertEquals(accountW.getBalance().toString(), "89.90");
	}
	
	@Test
	public void testDeposit() throws Exception {
		accountW.deposit(new BigDecimal("100.4"));
		System.out.println("deposit, new balance = " + accountW.getBalance());
		assertEquals(accountW.getBalance().toString(), "300.85");
	}
	
	@Test
	public void testOverdrawException(){
		try{
			accountW.withdraw(new BigDecimal("210"));
			fail("Overdraw exception should be thrown");
		}
		catch(OverdrawException ex) {
			System.out.println(ex.getMessage());
			assertEquals(ex.getMessage(), "Overdraw by -9.55");
			assertTrue(true);
		}
	}
}
