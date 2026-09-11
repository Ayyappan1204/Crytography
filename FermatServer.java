import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class FermatServer {

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
        BigInteger result = new BigInteger(in.readLine());
        String message = in.readLine();

        System.out.println("Received n : " + n);
        System.out.println("Received a : " + a);
        System.out.println("a^(n-1) mod n : " + result);
        System.out.println("Result : " + message);

        in.close();
        socket.close();
        server.close();
    }
}