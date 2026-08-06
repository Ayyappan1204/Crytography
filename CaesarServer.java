import java.io.*;
import java.net.*;

public class CaesarServer {
    public static String decrypt(String cipher, int shift) {
        StringBuilder result = new StringBuilder();
        for (char ch : cipher.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                result.append((char) ((ch - 'A' - shift + 26) % 26 + 'A'));
            } else if (Character.isLowerCase(ch)) {
                result.append((char) ((ch - 'a' - shift + 26) % 26 + 'a'));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        int port = 6666;
        int shift = 3;
       
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running. Waiting for a client...");
           
            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {
               
                System.out.println("Client connected.");
                String ciphertext = in.readUTF();
                System.out.println("Received Ciphertext: " + ciphertext);
               
                String plaintext = decrypt(ciphertext, shift);
                System.out.println("Decrypted Plaintext: " + plaintext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

