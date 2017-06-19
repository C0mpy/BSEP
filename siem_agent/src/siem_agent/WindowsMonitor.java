package siem_agent;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.simple.JSONObject;

import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator;
import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;
public class WindowsMonitor extends Monitor {

	private long delaytime;
	private String structure;
	private String logName;
	private String time;
	private String logType;
	private long recordNumber;
	private String source;
	private String system;
	private String regex;
	private String computerName;
	private String message;
	private long eventId;
	
	private long logPointer;
	
	WindowsMonitor(JSONObject cfg, Sender sender, StateHandler state_handler) {
		super(cfg, sender, state_handler);
		
		delaytime=(long) cfg.get("delaytime");
		system = (String) cfg.get("system");
		structure=(String) cfg.get("structure");
		logName=(String) cfg.get("logName");
		regex=(String) cfg.get("regex");
		logPointer = -1;
		
		readState();

	}
	
	@Override
	public void run(){
		boolean monitor = true;
		while(monitor){
			try{
				EventLogIterator iter = new EventLogIterator(logName);
				
				if(logPointer!=-1){
					while(iter.next().getRecordNumber()!=logPointer){}
				}
				
				while (iter.hasNext()) {
					EventLogRecord record = iter.next();
					
					//konverzija vremena prispeca loga(iz unix time-a u normalno vreme)
					final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
					sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
					Timestamp t = new Timestamp(record.getRecord().TimeWritten.intValue());
					Date date = new Date();
					date.setTime(t.getTime()*1000L);
					
					recordNumber = record.getRecordNumber();
					logType = record.getType().toString();
					time = sdf.format(date);
					source = record.getSource();
					eventId = record.getEventId();
					
					String result = jWMI.getWMIValue("Select * from Win32_NTLogEvent where "
							+ "Logfile='"+logName+"' and RecordNumber=" + record.getRecordNumber(), "ComputerName,Message");
					
					int computerIdx = result.indexOf("\r\n");
					
					if(computerIdx!=-1){
						computerName = result.substring(0, computerIdx);
						message = result.substring(computerIdx+2, result.length());
					}else{
						computerName = result;
						message="";
					}
					
					String log = eventId+" "+recordNumber+" "+time+" "+computerName+" "+source+": "+message;
					this.dispatchLog(log);
					logPointer = recordNumber;
					saveState();
				}
				
				Thread.sleep(delaytime);
			}catch(Exception e){
				e.printStackTrace();
				//System.out.println("Monitor "+id+" nije podrzan na ovom operativnom sistemu");
				monitor=false;
			}
		}
		
	}
	
	//cuvanje pointera u logfajlu
	@SuppressWarnings("unchecked")
	void saveState(){
		JSONObject state=new JSONObject();
		state.put("logPointer",logPointer);
		state_handler.setState(id,state);
		state_handler.save();//cuvaj u temp fajl
	}

	//citanje pointera u logfajlu
	void readState(){
		JSONObject state = state_handler.getState(id);
		if(state!=null) logPointer=(long) state.get("logPointer");
	}
	
	@SuppressWarnings("unchecked")
	void dispatchLog(String line) throws IOException {
    	sender.send(line);
    	JSONObject json=new JSONObject();
    	json.put("monitorId", id);
    	json.put("log",line);
    	json.put("regex",regex);
    	json.put("system",system);
    	json.put("logName",logName);
    	json.put("structure",structure);
    	json.put("type",logType);
    	json.put("date",time);
    	sender.sendPostRequest(json);
	}

}
