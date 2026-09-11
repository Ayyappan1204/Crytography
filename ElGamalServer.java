import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class ElGamalServer {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5000);

        System.out.println("Server waiting for client...");

        Socket socket = server.accept();

        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

        BigInteger q = new BigInteger(in.readLine());
        BigInteger C1 = new BigInteger(in.readLine());
        BigInteger C2 = new BigInteger(in.readLine());
        BigInteger Xa = new BigInteger(in.readLine());

        System.out.println("Received C1 : " + C1);
        System.out.println("Received C2 : " + C2);

        // Calculate shared secret
        BigInteger K = C1.modPow(Xa, q);

        // Calculate inverse of K
        BigInteger KInverse = K.modInverse(q);

        // Decryption
        BigInteger M = C2.multiply(KInverse).mod(q);

        System.out.println("Shared Secret Key : " + K);
        System.out.println("Decrypted Message : " + M);

        in.close();
        socket.close();
        server.close();
    }
}