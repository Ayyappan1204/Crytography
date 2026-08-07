import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DESClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter Plain Text : ");
        String plainText = sc.nextLine();

        System.out.print("Enter 8 Character Key : ");
        String keyString = sc.nextLine();

        if (keyString.length() != 8) {
            System.out.println("DES Key must contain exactly 8 characters.");
            socket.close();
            sc.close();
            return;
        }

        // Create DES Key
        SecretKey key = new SecretKeySpec(
                keyString.getBytes("UTF-8"),
                "DES");

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // Encrypt
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted = cipher.doFinal(
                plainText.getBytes("UTF-8"));

        String cipherText = Base64.getEncoder()
                .encodeToString(encrypted);

        System.out.println("Encrypted Cipher Text : " + cipherText);

        // Send to Server
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        out.println(cipherText);
        out.println(keyString);

        out.close();
        socket.close();
        sc.close();
    }
}