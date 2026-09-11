import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class ElGamalClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter q : ");
        BigInteger q = sc.nextBigInteger();

        System.out.print("Enter alpha : ");
        BigInteger alpha = sc.nextBigInteger();

        System.out.print("Enter private key Xa : ");
        BigInteger Xa = sc.nextBigInteger();

        System.out.print("Enter message M : ");
        BigInteger M = sc.nextBigInteger();

        System.out.print("Enter random key k : ");
        BigInteger k = sc.nextBigInteger();

        // Calculate public key
        BigInteger Ya = alpha.modPow(Xa, q);

        // Calculate C1
        BigInteger C1 = alpha.modPow(k, q);

        // Calculate shared secret
        BigInteger K = Ya.modPow(k, q);

        // Calculate C2
        BigInteger C2 = M.multiply(K).mod(q);

        System.out.println("\nPublic Key Ya : " + Ya);
        System.out.println("C1 : " + C1);
        System.out.println("C2 : " + C2);

        System.out.println("Cipher Text : (" + C1 + ", " + C2 + ")");

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        out.println(q);
        out.println(C1);
        out.println(C2);
        out.println(Xa);

        out.close();
        socket.close();
        sc.close();
    }
}