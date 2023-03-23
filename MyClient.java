    import java.io.*;  
    import java.net.*;  
    public class MyClient {  
    
    
    
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
    
    dout.write(("QUIT\n").getBytes());
    dout.flush();
    
    str = (String)in.readLine();
    System.out.println(str);
    
    s.close();  
    }catch(Exception e){System.out.println(e);}  
    }  
    }  
