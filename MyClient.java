    import java.io.*;  
    import java.net.*;  
    public class Test {  
    
    
    
    public static void main(String[] args) {  
    
    try{      
    Socket s=new Socket("localhost",50000);  
    DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));  
    
    dout.write(("HELO\n").getBytes());  
    dout.flush();  
    
    String str=(String)in.readLine();  
    System.out.println(str);
    
    dout.write(("AUTH yeroC\n").getBytes());
    dout.flush();
    
    str = (String)in.readLine();
    System.out.println(str);
    
    dout.write(("REDY\n").getBytes());
    dout.flush();
    str = (String)in.readLine();
    System.out.println(str);
    
    String[] jobs = str.split(" ", 10);
    
    dout.write(("GETS All\n").getBytes());
    dout.flush();
    
    str = (String)in.readLine();
    System.out.println(str);
    
    dout.write(("OK\n").getBytes());
    dout.flush();
    
    String[] DATA = str.split(" ");
    int nServers = Integer.valueOf(DATA[1]);
    int j = 0;
    String[] largest = new String[10];
    int count = 0;
    for(int i = 0; i < nServers; i++) {
    	str = (String)in.readLine();
    	System.out.println(str);
    	String[] ser = str.split(" ");
    	int cores = Integer.parseInt(String.valueOf(ser[4]));
    	count++;
    	if(j < cores) {
    		j = cores;
    		largest = ser;
    	}
    }
    dout.write(("OK\n").getBytes());
    dout.flush();
    
    dout.write(("SCHD " + jobs[2] + " " + largest[0] + " " + largest[1] + "\n").getBytes());
    dout.flush();
    
    dout.write(("OK\n").getBytes());
    dout.flush();
    
    dout.write(("QUIT\n").getBytes());
    dout.flush();
    s.close();  
    }catch(Exception e){System.out.println(e);}  
    }  
    }  

