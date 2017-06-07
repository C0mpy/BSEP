package siem_agent;

import org.json.simple.JSONObject;

public class Monitor implements Runnable {
	
	protected String agentId;
	protected StateHandler state_handler;
	private Sender sender;
	
	Monitor(JSONObject cfg,Sender sender,StateHandler state_handler) {
		agentId= (String) cfg.get("id");
		this.sender = sender;
		this.state_handler=state_handler;
	}
	
	
	@Override
	public void run() {
				
	}
	
	void dispatch_log(String line){
		JSONObject json = new JSONObject();
		json.put("agentId", agentId);
		json.put("log", line);
		sender.send(line);
		//sender.sendPostRequest(json);
	}

}
