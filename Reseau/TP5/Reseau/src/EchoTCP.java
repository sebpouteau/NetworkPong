import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class EchoTCP {
public static void main(String []args) throws IOException{
	ServerSocket socket = new ServerSocket(7777);
	while(true){
		Socket  socket2 = socket.accept();
		OutputStream os = socket2.getOutputStream();
		InputStream is = socket2.getInputStream();
		while(true){
			int lu = is.read();
			if (lu == -1)
				break;
			os.write(lu);	
		}
		socket2.close();
	}
	
}
}
