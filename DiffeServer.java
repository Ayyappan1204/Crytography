import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class DiffeServer {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5000);

        System.out.println("Server waiting for client...");

        Socket socket = server.accept();

        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

        BigInteger q = new BigInteger(in.readLine());
        BigInteger alpha = new BigInteger(in.readLine());
        BigInteger Ya = new BigInteger(in.readLine());
        BigInteger Yb = new BigInteger(in.readLine());

        System.out.println("Received q : " + q);
        System.out.println("Received alpha : " + alpha);
        System.out.println("Received Client Public Key Ya : " + Ya);
        System.out.println("Received Server Public Key Yb : " + Yb);

        // For demonstration, ask server's private key
        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("Enter private key Xb : ");
        BigInteger Xb =
                new BigInteger(keyboard.readLine());

        // Calculate shared secret key
        BigInteger Kb = Ya.modPow(Xb, q);

        // Calculate server public key
        BigInteger calculatedYb =
                alpha.modPow(Xb, q);

        System.out.println("\nCalculated Server Public Key : "
                + calculatedYb);

        System.out.println("Shared Secret Key : " + Kb);

        in.close();
        socket.close();
        server.close();
    }
}