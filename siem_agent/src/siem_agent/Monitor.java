package siem_agent;

import org.json.simple.JSONObject;

public class Monitor implements Runnable {
	
	private String agentId;
	private Sender sender;
	
	Monitor(JSONObject cfg,Sender sender) {	
		agentId= (String) cfg.get("id");
		this.sender = sender;
	}
	
	
	@Override
	public void run() {
				
	}
	
	protected void dispatch_log(String line){
		JSONObject json = new JSONObject();
		json.put("agentId", agentId);
		json.put("log", line);
		sender.send(line);
		//sender.sendPostRequest(json);
	}

}
