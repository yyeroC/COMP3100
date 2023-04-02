    import java.io.*;  
    import java.net.*;  
    public class MyClient {  
    
    
    
    public static void main(String[] args) {  
    
    try{      
    Socket s=new Socket("localhost",50000);  
    DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));  
   
    int cores = 0;
    String[] largest = new String[10];
    int count = 0;
    int schedCount = 0;
    String name = "";
    
    dout.write(("HELO\n").getBytes());  
    dout.flush();  
    
    String str=(String)in.readLine();  
    System.out.println(str);
    
    dout.write(("AUTH yeroC\n").getBytes());
    dout.flush();
    
    str = (String)in.readLine();
    System.out.println(str);
    
    boolean found = false;
    boolean none = false; 
    
   while(none != true) {
    dout.write(("REDY\n").getBytes());
    dout.flush();
    str = (String)in.readLine();
    
    String[] jobs = str.split(" ", 10);
    System.out.println(jobs[0]);
    if(jobs[0].equals("NONE")) {
    	none = true;
    }
    
    if(found == false) {
    dout.write(("GETS All\n").getBytes());
    dout.flush();
    
    str = (String)in.readLine();
    System.out.println(str);
    
    dout.write(("OK\n").getBytes());
    dout.flush();
    
    String[] DATA = str.split(" ");
    int nServers = Integer.valueOf(DATA[1]);
    for(int i = 0; i < nServers; i++) {
    	str = (String)in.readLine();
    	System.out.println(str);
    	String[] ser = str.split(" ");
    	count++;
    	int tempCores = Integer.parseInt(String.valueOf(ser[4]));
    	if(cores < tempCores) {
    		cores = tempCores;
    		largest = ser;
    		name = largest[0];
    		count = 1;
    	}
    	if(tempCores == cores && name.equals(ser[0]) == false) {
    		count--;
    	}
    }
    	found = true;
    }
  
    	dout.write(("OK\n").getBytes());
   	dout.flush();
   	 
   	str = (String)in.readLine();
    	System.out.println(str);
    	if(!str.equals(".") || !str.equals("ERR")){
    	if(jobs[0].equals("JOBN")) {
    		if(schedCount == count) {
    			schedCount = 0;
    		}
        	dout.write(("SCHD " + jobs[2] + " " + largest[0] + " " + schedCount + "\n").getBytes());
        	dout.flush();
        	schedCount++;
        }
        }
   }
   
    dout.write(("OK\n").getBytes());
    dout.flush();
    
    dout.write(("QUIT\n").getBytes());
    dout.flush();
    s.close();  
    }catch(Exception e){System.out.println(e);}  
    }  
    }  

