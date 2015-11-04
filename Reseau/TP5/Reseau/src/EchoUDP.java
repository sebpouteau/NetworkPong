
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class EchoUDP {
	
	public static void main(String []args) throws IOException{
		DatagramSocket socket  = new DatagramSocket(7777);
		byte tampon[] = new byte[1500];
		DatagramPacket p = new DatagramPacket(tampon, 1500);
		while(true){
			socket.receive(p);
			socket.send(p);
		}


		
	}
	
}
