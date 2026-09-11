import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class RSAServer {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5000);

        System.out.println("Server waiting for client...");

        Socket socket = server.accept();

        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

        BigInteger C = new BigInteger(in.readLine());
        BigInteger d = new BigInteger(in.readLine());
        BigInteger n = new BigInteger(in.readLine());

        System.out.println("Received Cipher Text : " + C);

        // RSA Decryption
        BigInteger M = C.modPow(d, n);

        System.out.println("Decrypted Message : " + M);

        in.close();
        socket.close();
        server.close();
    }
}