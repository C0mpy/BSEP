package siem_agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

public class FileMonitor extends Monitor {

	private File logfile;
	private long filepointer;
	private long delaytime;
	//private long log_line_num;
	private String regex;
	private String structure;
	private String system;
	private String log_name;
	private String type;
	private String time;
	private SimpleDateFormat time_format;


	FileMonitor(JSONObject cfg,Sender sender,StateHandler state_handler) {
		super(cfg,sender,state_handler);
		logfile= new File((String) cfg.get("url"));
		filepointer=0;
		delaytime=(long) cfg.get("delaytime");
		//log_line_num=(long) cfg.get("log_line_num");
		regex =	(String) cfg.get("regex");
		structure=(String) cfg.get("structure");
		system = (String) cfg.get("system");
		log_name = (String)	cfg.get("logName");
		time_format=new SimpleDateFormat((String)cfg.get("timeformat"));
		
		readState();

		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){

				saveState();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		boolean monitor=true;
		while(monitor){
			try{
				RandomAccessFile f=new RandomAccessFile(logfile,"r");
				long file_length=f.length();
				if(file_length > filepointer){
					f.seek(filepointer);

					byte[] b=new byte[(int) (file_length-filepointer)];
					f.readFully( b);

					filepointer=f.getFilePointer();
					System.out.println(filepointer);
					String text = new String(new String(b, StandardCharsets.US_ASCII));

					Pattern p=Pattern.compile(regex);
					Matcher m=p.matcher(text);

					while(m.find()) {
						String line=m.group();

						//get date
						final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
						Date d=new Date();
						String[] st=structure.split(" ");
						for(int i=0;i<st.length;i++){
							if(st[i].equals("date")) d=this.time_format.parse(m.group(i+1));
						}
						d.setYear(new Date().getYear());
						this.time=sdf.format(d);
						type="Informational";
						if(line.toUpperCase().contains("WARNING")||line.toUpperCase().contains("WARN")) type="Warning";
						if(line.toUpperCase().contains("ERROR") ) type="Error";

						this.dispatchLog(line);

					}

					//line by line reader
					//uses log_line_number to concatenate
					/*
					String line;
					while((line=f.readLine())!=null){
						if (line.equals("")) continue; //preskoci prazan red
						//konkateniraj linije
						for(int i=1;i<log_line_num;i++) line+=" "+f.readLine();
						this.dispatch_log(line); // samo printa na konzolu
					}
					*/

				}else if(logfile.length() < filepointer){
					filepointer=0;
				}
				f.close();
				Thread.sleep(delaytime);					
			}catch(FileNotFoundException ex){
				System.out.println("Monitor "+id+" nije pronasao fajl navedenu u config.json.\n");
				monitor=false;
			}catch(Exception e){
				e.printStackTrace();
				monitor=false;
			}
		}
	}

	//cuvanje pointera u logfajlu
	@SuppressWarnings("unchecked")
	void saveState(){
	    JSONObject state=new JSONObject();
	    state.put("filepointer",filepointer);
		state_handler.setState(id,state);
		state_handler.save();//cuvaj u temp fajl
	}

	//citanje pointera u logfajlu
    void readState(){
	   JSONObject state=  state_handler.getState(id);
	   if(state!=null) filepointer=(long) state.get("filepointer");
    }

    @SuppressWarnings("unchecked")
	void dispatchLog(String line) throws IOException {
    	sender.send(line);
    	JSONObject json=new JSONObject();
    	json.put("monitorId",id);
    	json.put("log",line);
    	json.put("regex",regex);
    	json.put("structure",structure);
    	json.put("system",system);
    	json.put("logName",log_name);
    	json.put("type",type);
    	json.put("date", time);
    	sender.sendPostRequest(json);
	}
}
