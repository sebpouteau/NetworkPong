import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
/**
 * Created by sebpouteau on 16/10/15.
 */
public class MultipleSelectTCP {

        public static void main(String[] args) throws IOException {

            ServerSocketChannel ssc =ServerSocketChannel.open();
            ServerSocket ecoute = ssc.socket();
            ecoute.bind(new InetSocketAddress(7777));
            Selector selector = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(selector,SelectionKey.OP_ACCEPT);
            int numberClient;
            while (true) {
                numberClient = selector.select();
                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();
                while(it.hasNext()){
                    SelectionKey key = (SelectionKey) it.next();
                    if (key.isAcceptable()){
                        Socket client = ecoute.accept();
                        SocketChannel sc = client.getChannel();
                        sc.configureBlocking(false);
                        sc.register(selector,SelectionKey.OP_READ);
                    }
                    if(key.isReadable()){
                        SocketChannel sc2 = (SocketChannel) key.channel();
                        ByteBuffer bf= ByteBuffer.allocate(30);
                        int n=sc2.read(bf);

                        if (n == 0) {
                            key.cancel();
                            sc2.close();
                        }
                        else {
                            bf.flip();
                            sc2.write(bf);
                        }
                    }

                }
                keys.clear();
            }


        }
}

