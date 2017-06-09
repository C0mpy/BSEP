package siem_agent;

import org.json.simple.JSONObject;

import java.io.IOException;

public class Monitor implements Runnable {
	
	protected String id;
	protected StateHandler state_handler;
	protected Sender sender;
	
	Monitor(JSONObject cfg,Sender sender,StateHandler state_handler) {
		id= (String) cfg.get("id");
		this.sender = sender;
		this.state_handler=state_handler;
	}
	
	
	@Override
	public void run() {
				
	}
	


}
