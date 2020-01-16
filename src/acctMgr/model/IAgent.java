package acctMgr.model;
import java.math.BigDecimal;
public interface IAgent extends Model {
	
	public BigDecimal getTransferred();
	public void onPause();
	public void onResume();
	public void setName(String name);
	public String getName();
	public Account getAccount();
	public void setStatus(AgentStatus agSt);
	public AgentStatus getStatus();
	public void finish();
	
}
