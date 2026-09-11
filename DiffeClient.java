import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class DiffeClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter q : ");
        BigInteger q = sc.nextBigInteger();

        System.out.print("Enter alpha : ");
        BigInteger alpha = sc.nextBigInteger();

        System.out.print("Enter private key Xa : ");
        BigInteger Xa = sc.nextBigInteger();

        System.out.print("Enter private key Xb : ");
        BigInteger Xb = sc.nextBigInteger();

        // Calculate Client public key
        BigInteger Ya = alpha.modPow(Xa, q);

        // Calculate Server public key
        BigInteger Yb = alpha.modPow(Xb, q);

        // Calculate shared secret key
        BigInteger Ka = Yb.modPow(Xa, q);

        System.out.println("\nClient Public Key Ya : " + Ya);
        System.out.println("Server Public Key Yb : " + Yb);
        System.out.println("Shared Secret Key : " + Ka);

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        out.println(q);
        out.println(alpha);
        out.println(Ya);
        out.println(Yb);

        out.close();
        socket.close();
        sc.close();
    }
}