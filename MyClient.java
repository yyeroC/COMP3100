import java.io.*;  
import java.net.*;  

public class MyClient {   
	public static void main(String[] args) {  
		try{
			//Variables for communicating the server socket.      
			Socket s=new Socket("localhost",50000);  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));  
			
			//Other global variables used to keep track of tasks.
			int cores = 0;
			String[] largest = new String[10];
			int count = 0;
			int schedCount = 0;
			String name = "";
			boolean found = false;
			boolean none = false; 
			String[] jobs = new String[10];
			jobs[0] = "1";
			
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
			while(str != "NONE") {
				dout.write(("REDY\n").getBytes()); //Ready to receive a job.
				dout.flush();
				str = (String)in.readLine(); //Receive the "JOBN" containing job data.
				if(str.equals("NONE")) break; // Exit loop if no jobs.
				
				jobs = str.split(" ", 10); //Split the command string by each space.
				// Only entered if largest server type hasn't been found yet.
				if(found == false) {
					dout.write(("GETS All\n").getBytes()); // Request resource information.
					dout.flush();
					
					str = (String)in.readLine(); // Read "DATA"
					
					
					dout.write(("OK\n").getBytes()); // Receive server information.
					dout.flush();
					
					String[] DATA = str.split(" "); // Split the server information string by spaces.
					int nServers = Integer.valueOf(DATA[1]); // Case nRecs string to integer.
					
					//For each server check if bigger than previous biggest server type.
					for(int i = 0; i < nServers; i++) {
						str = (String)in.readLine(); //Read data of the current server
						
						
						String[] ser = str.split(" "); //Split current server data into string array.
						count++; // Keep count of each server type.
						int tempCores = Integer.parseInt(String.valueOf(ser[4])); //Number of cores of current server.
						
						//If the current largest server type is smaller than the current server then set largest server type to the current server.
						if(cores < tempCores) { 
							cores = tempCores;
							largest = ser;
							name = largest[0];
							count = 1; //Reset counter of largest server type to 1.
						}
						//Makes sure servers of different type but same core count do not increment server count.
						if(tempCores == cores && name.equals(ser[0]) == false) {
							count--;
						}
					}
					found = true; // Makes sure the largest server type won't be found again
				}	
				
				dout.write(("OK\n").getBytes());
				dout.flush();
				
				str = (String)in.readLine();
				
				//If "JOBN" was received at start of loop, schedule the job.
				if(jobs[0].equals("JOBN")) {
					//If previous job was scheduled to the final server reset the counter.
					if(schedCount == count) {
						schedCount = 0;
					}
					dout.write(("SCHD " + jobs[2] + " " + largest[0] + " " + schedCount + "\n").getBytes());
					dout.flush();
					schedCount++;
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

