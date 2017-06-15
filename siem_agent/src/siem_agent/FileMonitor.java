package siem_agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
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


	FileMonitor(JSONObject cfg,Sender sender,StateHandler state_handler) {
		super(cfg,sender,state_handler);
		logfile= new File((String) cfg.get("url"));
		filepointer=0;
		delaytime=(long) cfg.get("delaytime");
		//log_line_num=(long) cfg.get("log_line_num");
		regex =	(String) cfg.get("regex");
		structure=(String) cfg.get("structure");

		//readState();

		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
			//	saveState();
			}
		});
	}
	
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
					String text = new String(new String(b, StandardCharsets.US_ASCII));

					Pattern p=Pattern.compile(regex);
					Matcher m=p.matcher(text);

					while(m.find()) {
						this.dispatch_log(m.group());
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

					filepointer=f.getFilePointer();
				}else if(logfile.length() < filepointer){
					filepointer=0;
				}
				f.close();
				Thread.sleep(delaytime);					
			}catch(FileNotFoundException ex){
				System.out.println("Monitor "+id+" nije podrzan na ovom operativnom sistemu.\n");
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
	void dispatch_log(String line) throws IOException {
    	sender.send(line);
    	JSONObject json=new JSONObject();
    	json.put("monitorId",id);
    	json.put("log",line);
    	json.put("regex",regex);
    	json.put("structure",structure);
    	sender.sendPostRequest(json);
	}
}
