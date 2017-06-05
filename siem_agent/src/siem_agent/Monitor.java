package siem_agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import org.json.simple.JSONObject;

public class Monitor implements Runnable {

	private File logfile;
	private long filepointer;
	private long delaytime;
	private Sender sender;
	
	Monitor(String path) throws FileNotFoundException{
		logfile=new File(path);
		if(!logfile.exists()) throw new FileNotFoundException();
		
		filepointer=0;
		delaytime=2000; //2sec
		
		sender = new Sender();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		boolean monitor=true;
		while(monitor){
			try{
				RandomAccessFile f=new RandomAccessFile(logfile,"r");
				if(logfile.length() > filepointer){
					f.seek(filepointer);
						
					String line;
				
					while((line=f.readLine())!=null){
						//ovde negde salje log
						if (line.equals("")) continue; //preskoci prazan red
						
						JSONObject json = new JSONObject();
						json.put("agentId", "1");
						json.put("log", line);
						int response = sender.sendPostRequest(json);
						
					}					
						
					filepointer=f.getFilePointer();
				}
				f.close();
				Thread.sleep(delaytime);					
			}catch(Exception e){
				e.printStackTrace();
				monitor=false;
			}
		}		
	}

}
