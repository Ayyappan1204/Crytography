import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class MillerClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter n : ");
        BigInteger n = sc.nextBigInteger();

        System.out.print("Enter a : ");
        BigInteger a = sc.nextBigInteger();

        // n - 1 = 2^s * d
        BigInteger d = n.subtract(BigInteger.ONE);
        int s = 0;

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            s++;
        }

        System.out.println("s = " + s);
        System.out.println("d = " + d);

        // Calculate x = a^d mod n
        BigInteger x = a.modPow(d, n);

        System.out.println("a^d mod n = " + x);

        boolean probablyPrime = false;

        if (x.equals(BigInteger.ONE) ||
            x.equals(n.subtract(BigInteger.ONE))) {

            probablyPrime = true;
        } 
        else {

            for (int r = 1; r < s; r++) {

                x = x.multiply(x).mod(n);

                System.out.println(
                        "x after squaring = " + x);

                if (x.equals(n.subtract(BigInteger.ONE))) {
                    probablyPrime = true;
                    break;
                }
            }
        }

        String message;

        if (probablyPrime) {
            message = n + " is probably prime";
        } 
        else {
            message = n + " is composite";
        }

        System.out.println("Result : " + message);

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        out.println(n);
        out.println(a);
        out.println(s);
        out.println(d);
        out.println(message);

        out.close();
        socket.close();
        sc.close();
    }
}