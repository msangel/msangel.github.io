package ua.co.k.avro;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.netty.NettyServer;
import org.apache.avro.ipc.netty.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;
import org.junit.jupiter.api.Test;
import ua.co.k.avro.generated.Mail;
import ua.co.k.avro.generated.Message;

public class AvroRPCTest {
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
            // client code - attach to the server and send a message
            Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);
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
        return new NettyServer(new SpecificResponder(Mail.class, new MailImpl()), new InetSocketAddress(65111));
    }

}
