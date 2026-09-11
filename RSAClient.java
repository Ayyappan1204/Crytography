import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class RSAClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter p : ");
        BigInteger p = sc.nextBigInteger();

        System.out.print("Enter q : ");
        BigInteger q = sc.nextBigInteger();

        System.out.print("Enter e : ");
        BigInteger e = sc.nextBigInteger();

        System.out.print("Enter message M : ");
        BigInteger M = sc.nextBigInteger();

        // Calculate n
        BigInteger n = p.multiply(q);

        // Calculate phi(n)
        BigInteger phi = p.subtract(BigInteger.ONE)
                          .multiply(q.subtract(BigInteger.ONE));

        // Calculate private key d
        BigInteger d = e.modInverse(phi);

        // RSA Encryption
        BigInteger C = M.modPow(e, n);

        System.out.println("\nn = " + n);
        System.out.println("Phi(n) = " + phi);
        System.out.println("d = " + d);
        System.out.println("Encrypted Cipher Text = " + C);

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        out.println(C);
        out.println(d);
        out.println(n);

        out.close();
        socket.close();
        sc.close();
    }
}