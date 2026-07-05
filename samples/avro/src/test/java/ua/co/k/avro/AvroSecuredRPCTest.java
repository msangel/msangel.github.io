package ua.co.k.avro;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.ipc.RPCContext;
import org.apache.avro.ipc.RPCPlugin;
import org.apache.avro.ipc.Requestor;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.netty.NettyServer;
import org.apache.avro.ipc.netty.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.junit.jupiter.api.Test;
import ua.co.k.avro.generated.Mail;
import ua.co.k.avro.generated.Message;

public class AvroSecuredRPCTest {

    public static class SecurityPlugin extends RPCPlugin {

        @Override
        public void clientStartConnect(RPCContext context) {
            String token = "I'm a dolphin";
            ByteBuffer buf = ByteBuffer.wrap(token.getBytes(StandardCharsets.UTF_8));
            context.requestHandshakeMeta().put("Authorization", buf);
            System.out.println("Client sending authentication token: " + token);
            super.clientStartConnect(context);
        }

        @Override
        public void serverConnecting(RPCContext context) {
            ByteBuffer buf = context.requestHandshakeMeta().get("Authorization");
            if (buf == null) {
                throw new SecurityException("No authentication token!");
            }
            CharBuffer token = StandardCharsets.UTF_8.decode(buf);
            if (token.toString().contains("dolphin")) {
                System.out.println("Server received authentication token: " + token);
            } else {
                throw new SecurityException("Invalid authentication token!");
            }
        }
    }
    public static class MailImpl implements Mail {
        // in this simple example just return details of the message
        public Utf8 send(Message message) {
            System.out.println("Sending message");
            return new Utf8("Sending message to " + message.getTo().toString()
                    + " from " + message.getFrom().toString()
                    + " with body " + message.getBody().toString());
        }
    }

    @Test
    public void test() throws IOException, InterruptedException {
        String to = "to";
        String from = "from";
        String body = "body";
        System.out.println("Starting server");
        // usually this would be another app, but for simplicity
        Server server = startServer();
        System.out.println("Server started");
        try (NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(65111));
        ) {
            SpecificRequestor requestor = new SpecificRequestor(Mail.class, client);
            requestor.addRPCPlugin(new SecurityPlugin());

            // Attach headers to the request
            // client code - attach to the server and send a message
            Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, requestor);
            System.out.println("Client built, got proxy");

            // fill in the Message record and send it
            Message message = new Message();
            message.setTo(new Utf8(to));
            message.setFrom(new Utf8(from));
            message.setBody(new Utf8(body));
            System.out.println("Calling proxy.send with message:  " + message);
            System.out.println("Result: " + proxy.send(message));

            // cleanup
            System.out.println("Closing client");
            client.close(true);
            System.out.println("Closing server");

        } finally {
            server.close();
            server.join();
            System.out.println("Exiting (forcing due to Netty non-daemon thread introduced between Avro 1.9.2 and 1.10.0)");
        }
    }

    private static Server startServer() throws IOException, InterruptedException {
        SpecificResponder responder = new SpecificResponder(Mail.class, new MailImpl());
        responder.addRPCPlugin(new SecurityPlugin());
        return new NettyServer(responder, new InetSocketAddress(65111));
    }

}
