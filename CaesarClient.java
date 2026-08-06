package Crytography;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CaesarClient {
   
    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                result.append((char) ((ch - 'A' + key) % 26 + 'A'));
            } else if (Character.isLowerCase(ch)) {
                result.append((char) ((ch - 'a' + key) % 26 + 'a'));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 6666;
       

        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket(host, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
           
            System.out.print("Enter message to encrypt and send: ");
            String plaintext = sc.nextLine();
           
            System.out.println("Enter the key");
            int key = sc.nextInt();
           
            String ciphertext = encrypt(plaintext, key);
            System.out.println("Encrypted Ciphertext: " + ciphertext);
           
            out.writeUTF(ciphertext);
            out.flush();
            System.out.println("Message sent successfully!");
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
