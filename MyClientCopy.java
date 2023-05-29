import java.io.*;  
import java.net.*;  

/* Fixes: 
 * DEAL WITH JCPL: If JCPL send REDY
 * 
*/
public class MyClientCopy {   
	public static void main(String[] args) {  
		try{
			//Variables for communicating the server socket.      
			Socket s=new Socket("localhost",50000);  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));  
			
			//Other global variables used to keep track of tasks.
			String[] jobs = new String[10];


			//Send "HELO"
			dout.write(("HELO\n").getBytes());  
			dout.flush();  
			//Receive "OK"
			String str=(String)in.readLine();  
			
			//Send "AUTH" to server to identify user
			dout.write(("AUTH yeroC\n").getBytes());
			dout.flush();
			//Receive "OK"
			str = (String)in.readLine();
			
			//Keep looping while there are still jobs that need to be scheduled.
			while(str != "NONE\n") {
				dout.write(("REDY\n").getBytes()); //Ready to receive a job.
				dout.flush();
				str = (String)in.readLine(); //Receive the "JOBN" containing job data.
				if(str.equals("NONE")) break; // Exit loop if no jobs.
				
				jobs = str.split(" ", 10); //Split the command string by each space.
				// Only entered if largest server type hasn't been found yet.
				dout.write(("GETS Capable " + jobs[4] + " " + jobs[5] + " " + jobs[6] + "\n").getBytes()); // Request resource information.
				dout.flush();
					
				str = (String)in.readLine(); // Read "DATA"
					
					
					dout.write(("OK\n").getBytes()); // Receive server information.
					dout.flush();
					
					String[] DATA = str.split(" "); // Split the server information string by spaces.		
					
				
				
				dout.write(("OK\n").getBytes());
				dout.flush();
				String[] server = str.split(" ");
				
				str = (String)in.readLine();
				
				//If "JOBN" was received at start of loop, schedule the job.
				if(jobs[0].equals("JOBN")) {
					//If previous job was scheduled to the final server reset the counter.
					dout.write(("SCHD " + server[0] + " " + server[1] + "\n").getBytes());
					dout.flush();
				}
			}
			
			str = (String)in.readLine();
			
			//Send quit message
			dout.write(("QUIT\n").getBytes());
			dout.flush();
			
			//Receive quit message
			str = (String)in.readLine();
			
			//Close socket
			s.close();  
		}	catch(Exception e){System.out.println(e);}
		
	}  
}  

