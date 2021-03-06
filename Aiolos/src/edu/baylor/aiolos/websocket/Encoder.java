package edu.baylor.aiolos.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.logging.Logger;

import edu.baylor.aiolos.ClientSession;
import edu.baylor.aiolos.Server;

public abstract class Encoder implements
        CompletionHandler<Integer, ClientSession> {
    public static final Logger log = Logger.getLogger("aiolos.networking");
    
    protected final AsynchronousSocketChannel channel;
    protected ClientSession attachment;
    protected final ByteBuffer writeBuf;
    
    public Encoder(AsynchronousSocketChannel channel, ClientSession attachment){
        this.channel = channel;
        this.attachment = attachment;
        this.writeBuf = ByteBuffer.allocateDirect(Server.BUFF_SIZE);
    }
    
    @Override
    public void completed(Integer result, ClientSession attachment) {
        if (result == -1) {
            try {
                log.info("Client " + channel.getRemoteAddress() + " disconnected abruptly.");
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        this.attachment = attachment;
    }
    
    @Override
    public void failed(Throwable exc, ClientSession attachment) {
        log.warning(exc.getMessage());
    }
}
