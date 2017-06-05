package siem_agent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		
		JSONArray config = (JSONArray) parser.parse(new FileReader("config.json"));
		
	    Thread t = new Thread(new Monitor("C:\\Windows\\Logs\\DirectX.log"));
		t.start();
				
	}

}
