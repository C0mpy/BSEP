import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Logovanje {

	public static void main(String[] args) throws IOException, InterruptedException {

		// String command = "powershell.exe your command";
		// Getting the version
		int i = 0;
		
		String command = "C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe"
				+ " New-EventLog –LogName System –Source MWSecurity";
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		String command2 = "C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe"
				+ " Write-EventLog –LogName System –Source MWSecurity"
				+ " –EntryType Information –EventID 4625"
				+ " –Message \"LogOnFailed\"";
		
		while(i<5){
			
			Process powerShellProcess2 = Runtime.getRuntime().exec(command2);
			Thread.sleep(5000);
			
			/*
			// Getting the results
			powerShellProcess2.getOutputStream().close();
			String line;
			System.out.println("Standard Output:");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess2.getInputStream()));
			while ((line = stdout.readLine()) != null) {
				System.out.println(line);
			}
			stdout.close();
			System.out.println("Standard Error:");
			BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess2.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
				System.out.println(line);
			}
			stderr.close();
			System.out.println("Done");*/
			
			
			i++;
			
			
		}
		// Executing the commands
		
		
		
		
			
		

	}

}