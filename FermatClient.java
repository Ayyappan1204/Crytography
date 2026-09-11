import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class FermatClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter n : ");
        BigInteger n = sc.nextBigInteger();

        System.out.print("Enter a : ");
        BigInteger a = sc.nextBigInteger();

        // Fermat's Little Theorem
        // a^(n-1) mod n should be 1 if n is probably prime

        BigInteger result =
                a.modPow(n.subtract(BigInteger.ONE), n);

        System.out.println("a^(n-1) mod n = " + result);

        String message;

        if (result.equals(BigInteger.ONE)) {
            message = n + " is probably prime";
        } else {
            message = n + " is composite";
        }

        System.out.println("Result : " + message);

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        out.println(n);
        out.println(a);
        out.println(result);
        out.println(message);

        out.close();
        socket.close();
        sc.close();
    }
}