package siem_agent;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class StateHandler {

    private JSONObject states;
    private JSONParser parser;

    StateHandler() throws IOException, ParseException {
        File temp = new File("temp.json");
        parser= new JSONParser();

        if(temp.exists()) {
            states = (JSONObject) parser.parse(new FileReader(temp));
        }else{
            states=new JSONObject();
        }

    }

    public synchronized void save(){
        FileWriter fw;

        try {
            fw =new FileWriter("temp.json");
            fw.write(states.toJSONString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
	public synchronized void setState(String monitor_id,JSONObject state){
        this.states.put(monitor_id,state);
    }

    public JSONObject getState(String monitor_id){
        return (JSONObject) this.states.get(monitor_id);
    }


}
