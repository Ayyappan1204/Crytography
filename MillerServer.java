import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class MillerServer {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5000);

        System.out.println("Server waiting for client...");

        Socket socket = server.accept();

        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

        BigInteger n = new BigInteger(in.readLine());
        BigInteger a = new BigInteger(in.readLine());
        int s = Integer.parseInt(in.readLine());
        BigInteger d = new BigInteger(in.readLine());
        String message = in.readLine();

        System.out.println("Received n : " + n);
        System.out.println("Received a : " + a);
        System.out.println("s : " + s);
        System.out.println("d : " + d);
        System.out.println("Result : " + message);

        in.close();
        socket.close();
        server.close();
    }
}