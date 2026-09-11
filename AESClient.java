import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        System.out.print("Enter Plain Text : ");
        String plainText = sc.nextLine();

        System.out.print("Enter 16 Character Key : ");
        String keyString = sc.nextLine();

        if (keyString.length() != 16) {

            System.out.println(
                    "AES key must contain exactly 16 characters.");

            socket.close();
            sc.close();
            return;
        }

        // Create AES Key
        SecretKey key = new SecretKeySpec(
                keyString.getBytes("UTF-8"),
                "AES");

        // Create Cipher
        Cipher cipher = Cipher.getInstance(
                "AES/ECB/PKCS5Padding");

        // Encryption
        cipher.init(
                Cipher.ENCRYPT_MODE,
                key);

        byte[] encrypted = cipher.doFinal(
                plainText.getBytes("UTF-8"));

        // Convert encrypted bytes to Base64
        String cipherText = Base64.getEncoder()
                .encodeToString(encrypted);

        System.out.println(
                "Encrypted Cipher Text : " + cipherText);

        // Send data to Server
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(),
                true);

        out.println(cipherText);
        out.println(keyString);

        out.close();
        socket.close();
        sc.close();
    }
}