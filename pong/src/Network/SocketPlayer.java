package src.Network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by sebpouteau on 17/11/15.
 */
public class SocketPlayer {
    private Socket socket;
    private int port;
    private String address;
    private InputStream inputStream;
    private BufferedReader bufferReader;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public BufferedReader getBufferReader() {
        return bufferReader;
    }

    public void setBufferReader(BufferedReader bufferReader) {
        this.bufferReader = bufferReader;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    private OutputStream outputStream;// = socket.getOutputStream();
    private PrintStream printStream;// = new PrintStream(os, false, "utf-8");
    public SocketPlayer(Socket socket, int port) throws IOException {
        this.socket=socket;
        this.setInputStream(this.getSocket().getInputStream());
        this.setBufferReader(new BufferedReader(new InputStreamReader(this.getInputStream(), "utf-8")));
        this.setOutputStream(getSocket().getOutputStream());
        this.setPrintStream(new PrintStream(this.getOutputStream(), false, "utf-8"));
        this.port = port;
        this.address = socket.getInetAddress().getHostAddress();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String getAdress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
