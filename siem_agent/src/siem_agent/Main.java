package siem_agent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		JSONObject cfg = (JSONObject) parser.parse(new FileReader("config.json"));

		StateHandler state_handler= new StateHandler();

		Sender sender=new Sender((JSONObject)cfg.get("sender"));

        //if(true) {
		if(sender.authenticate()) {
            for (Object m : (JSONArray) cfg.get("monitors")) {
                JSONObject monitor_cfg = (JSONObject) m;
                if (((String) monitor_cfg.get("type")).equals("filemonitor")) {
                    	Thread t = new Thread(new FileMonitor(monitor_cfg,
                                sender,
                                state_handler));
                        t.start();           
                }else if(((String) monitor_cfg.get("type")).equals("windowsmonitor")){

                		Thread t = new Thread(new WindowsMonitor(monitor_cfg,
                                sender,
                                state_handler));
                        t.start();
                }
            }
        }else{
		    System.out.println("Agent failed to authenticate");
        }
	}

}
