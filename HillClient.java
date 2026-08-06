import java.io.*;
import java.net.*;
import java.util.*;

class HillCipher {

    int key[][] = new int[2][2];
    int inverse[][] = new int[2][2];

    // Read Key Matrix
    void readKey(Scanner sc) {
        System.out.println("Enter 2x2 Key Matrix:");
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                key[i][j] = sc.nextInt();
    }

    // Find Modular Inverse
    int modInverse(int a) {
        a = a % 26;
        if (a < 0)
            a += 26;

        for (int i = 1; i < 26; i++) {
            if ((a * i) % 26 == 1)
                return i;
        }
        return -1;
    }

    // Generate Inverse Matrix
    void generateInverse() {

    int det = key[0][0] * key[1][1] - key[0][1] * key[1][0];
    det = ((det % 26) + 26) % 26;

    int invDet = modInverse(det);

    if (invDet == -1) {
        System.out.println("Invalid Key Matrix");
        System.exit(0);
    }

    inverse[0][0] = ( key[1][1] * invDet) % 26;
    inverse[0][1] = (-key[0][1] * invDet) % 26;
    inverse[1][0] = (-key[1][0] * invDet) % 26;
    inverse[1][1] = ( key[0][0] * invDet) % 26;

    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            if (inverse[i][j] < 0)
                inverse[i][j] += 26;
        }
    }
}

    // Prepare Plaintext
    String prepare(String text) {

        text = text.toUpperCase().replaceAll("[^A-Z]", "");

        if (text.length() % 2 != 0)
            text += "X";

        return text;
    }

    // Encrypt
    String encrypt(String text) {

        text = prepare(text);

        String result = "";

        for (int i = 0; i < text.length(); i += 2) {

            int a = text.charAt(i) - 'A';
            int b = text.charAt(i + 1) - 'A';

            int c1 = (key[0][0] * a + key[0][1] * b) % 26;
            int c2 = (key[1][0] * a + key[1][1] * b) % 26;

            result += (char) (c1 + 'A');
            result += (char) (c2 + 'A');
        }

        return result;
    }

    // Decrypt
    String decrypt(String text) {

        generateInverse();

        String result = "";

        for (int i = 0; i < text.length(); i += 2) {

            int a = text.charAt(i) - 'A';
            int b = text.charAt(i + 1) - 'A';

            int p1 = (inverse[0][0] * a + inverse[0][1] * b) % 26;
            int p2 = (inverse[1][0] * a + inverse[1][1] * b) % 26;

            p1 = (p1 + 26) % 26;
            p2 = (p2 + 26) % 26;

            result += (char) (p1 + 'A');
            result += (char) (p2 + 'A');
        }

        return result;
    }
}

public class HillClient {

    public static void main(String args[]) throws Exception {

        try (Scanner sc = new Scanner(System.in)) {
            HillCipher hc = new HillCipher();
            
            hc.readKey(sc);
            
            sc.nextLine();
            
            System.out.print("Enter Plain Text : ");
            String plain = sc.nextLine();
            
            String cipher = hc.encrypt(plain);
            
            System.out.println("Encrypted Text : " + cipher);
            
            try (Socket socket = new Socket("localhost", 5000)) {
                DataOutputStream dos =
                        new DataOutputStream(socket.getOutputStream());
                
                // Send Key Matrix
                for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 2; j++)
                        dos.writeInt(hc.key[i][j]);
                
                // Send Cipher Text
                dos.writeUTF(cipher);
                
                dos.close();
            }
        }
    }
}