package siem_agent;

import java.io.File;
import java.io.RandomAccessFile;

import org.json.simple.JSONObject;

public class FileMonitor extends Monitor {

	private File logfile;
	private long filepointer;
	private long delaytime;
	
	FileMonitor(JSONObject cfg,Sender sender,StateHandler state_handler) {
		super(cfg,sender,state_handler);
		logfile= new File((String) cfg.get("url"));
		filepointer=0;
		delaytime=(long) cfg.get("delaytime");

		readState();

		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				saveState();
			}
		});
	}
	
	@Override
	public void run(){
		boolean monitor=true;
		while(monitor){
			try{
				RandomAccessFile f=new RandomAccessFile(logfile,"r");
				if(logfile.length() > filepointer){
					f.seek(filepointer);
						
					String line;
				
					while((line=f.readLine())!=null){
						
						if (line.equals("")) continue; //preskoci prazan red
					
						this.dispatch_log(line); // samo printa na konzolu
						line=f.readLine();
					}					
						
					filepointer=f.getFilePointer();
				}else if(logfile.length() < filepointer){
					filepointer=0;
				}
				f.close();
				Thread.sleep(delaytime);					
			}catch(Exception e){
				e.printStackTrace();
				monitor=false;
			}
		}
	}

	void saveState(){
	    JSONObject state=new JSONObject();
	    state.put("filepointer",filepointer);
		state_handler.setState(agentId,state);
		state_handler.save();
	}

    void readState(){
	   JSONObject state=  state_handler.getState(agentId);
	   if(state!=null) if(state.get("filepointer")!=null) filepointer=(long) state.get("filepointer");
    }
}
