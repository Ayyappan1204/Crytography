import java.net.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DESServer {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server waiting for client...");

        Socket socket = server.accept();
        System.out.println("Client connected");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        // Receive Cipher Text
        String cipherText = in.readLine();

        // Receive Key
        String keyString = in.readLine();

        System.out.println("Received Cipher Text : " + cipherText);
        System.out.println("Received Key : " + keyString);

        // Create DES Key
        SecretKey key = new SecretKeySpec(
                keyString.getBytes("UTF-8"),
                "DES");

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // Decrypt
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decrypted = cipher.doFinal(
                Base64.getDecoder().decode(cipherText));

        String plainText = new String(decrypted, "UTF-8");

        System.out.println("Decrypted Plain Text : " + plainText);

        in.close();
        socket.close();
        server.close();
    }
}