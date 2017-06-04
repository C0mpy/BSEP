package siem_agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class Monitor implements Runnable {

	private File logfile;
	private long filepointer;
	private long delaytime;
	
	Monitor(String path) throws FileNotFoundException{
		logfile=new File(path);
		if(!logfile.exists()) throw new FileNotFoundException();
		
		filepointer=0;
		delaytime=2000; //2sec
	}
	
	@Override
	public void run() {
		boolean monitor=true;
		while(monitor){
			try{
				RandomAccessFile f=new RandomAccessFile(logfile,"r");
				if(logfile.length() > filepointer){
					f.seek(filepointer);
						
					String line;
					line=f.readLine();
				
					while(line!=null){
						//ovde negde salje log
						line=f.readLine();
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
