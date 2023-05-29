import java.io.*;
import java.net.*;

public class MyClient {

	// Helper Methods //

	// Method to replace repetitive send message code
	public static void sendMessage(DataOutputStream stream, String x) {
		try {
			stream.write((x + "\n").getBytes());
			stream.flush();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String readMessage(BufferedReader stream) {
		try {
			String str;
			str = stream.readLine();
			System.out.println(str);
			return str;
		} catch (Exception e) {
			System.out.println(e);
		}
		return "null";
	}

	public static void main(String[] args) {
		try {
			// Variables for communicating the server socket.
			Socket s = new Socket("localhost", 50000);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			// Other global variables used to keep track of tasks.
			String str;
			String[] jobs = new String[10];
			String[] servers = new String[10];
			String data;
			String[] DATA = new String[10];
			int nServers;

			// Send "HELO"
			sendMessage(dout, "HELO");
			// Receive "OK"
			str = readMessage(in);

			// Send "AUTH" to server to identify user
			sendMessage(dout, "AUTH yeroC");
			// Receive "OK"
			str = readMessage(in);

			while (true) {
				// Send "REDY"
				sendMessage(dout, "REDY");
				// Recieve Job and Handle different responses to "REDY"
				str = readMessage(in);
				if (str.startsWith("JCPL")) {
					while (str.startsWith("JCPL")) {
						sendMessage(dout, "REDY");
						str = readMessage(in);
					}
				}
				// Catch "NONE" and exit loop
				if (str.startsWith("NONE")) {
					break;
				}
				jobs = str.split(" ", 10);

				// Find Best-fit Server
				sendMessage(dout, "GETS Avail " + jobs[4] + " " + jobs[5] + " " + jobs[6]);
				data = readMessage(in);
				sendMessage(dout, "OK");
				// Receive Server
				str = readMessage(in);
				//Looks for busy servers if none are available
				if(str.equals(".")) {
					sendMessage(dout, "GETS Capable " + jobs[4] + " " + jobs[5] + " " + jobs[6]);
					data = readMessage(in);
					sendMessage(dout, "OK");
					str = readMessage(in);

				}
				servers = str.split(" ", 10);

				sendMessage(dout, "OK");
				// readMessage(in);
				sendMessage(dout, "SCHD " + jobs[2] + " " + servers[0] + " " + servers[1]);
				DATA = data.split(" ", 10);
				
				for(int i = 0; i < Integer.valueOf(DATA[1]) + 1; i++) {
					readMessage(in);
				}
			}

			// Terminate connection
			sendMessage(dout, "QUIT");
			s.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
