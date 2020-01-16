package acctMgr.test;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        DepositAgentTest.class,
        WithdrawAgentTest.class })
public class AgentTests {

}
