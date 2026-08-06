import java.io.*;
import java.net.*;

class HillCipher {

    int key[][] = new int[2][2];
    int inverse[][] = new int[2][2];

    // Find Modular Inverse
    int modInverse(int a) {

        a = ((a % 26) + 26) % 26;

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

        result += (char)(p1 + 'A');
        result += (char)(p2 + 'A');
    }

    return result;
}
}
public class HillServer {

    public static void main(String args[]) throws Exception {

        ServerSocket ss = new ServerSocket(5000);

        System.out.println("Server Waiting...");

        Socket socket = ss.accept();

        System.out.println("Client Connected");

        DataInputStream dis =
                new DataInputStream(socket.getInputStream());

        HillCipher hc = new HillCipher();

        // Receive Key Matrix
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                hc.key[i][j] = dis.readInt();
            }
        }

        // Receive Cipher Text
        String cipher = dis.readUTF();

        System.out.println("Received Cipher Text : " + cipher);

        String plain = hc.decrypt(cipher);

        System.out.println("Decrypted Text : " + plain);

        dis.close();
        socket.close();
        ss.close();
    }
}