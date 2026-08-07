import java.net.*;
import java.io.*;
import java.util.*;

public class SDESClient {

    static int[] P10 = {3,5,2,7,4,10,1,9,8,6};
    static int[] P8 = {6,3,7,4,8,5,10,9};
    static int[] IP = {2,6,3,1,4,8,5,7};
    static int[] IPI = {4,1,3,5,7,2,8,6};
    static int[] EP = {4,1,2,3,2,3,4,1};
    static int[] P4 = {2,4,3,1};

    static int[][] S0 = {
        {1,0,3,2},
        {3,2,1,0},
        {0,2,1,3},
        {3,1,3,2}
    };

    static int[][] S1 = {
        {0,1,2,3},
        {2,0,1,3},
        {3,0,1,0},
        {2,1,0,3}
    };

    static int k1,k2;

    static int permute(int input,int table[],int len){
        int result=0;

        for(int i=0;i<table.length;i++){
            result<<=1;
            result|=(input>>(len-table[i]))&1;
        }

        return result;
    }

    static int leftShift(int value,int len,int shifts){
        return ((value<<shifts)|(value>>(len-shifts)))
                &((1<<len)-1);
    }

    static void generateKeys(int key){

        int p10=permute(key,P10,10);

        int left=(p10>>5)&0x1F;
        int right=p10&0x1F;

        left=leftShift(left,5,1);
        right=leftShift(right,5,1);

        k1=permute((left<<5)|right,P8,10);

        // SAME METHOD AS YOUR ORIGINAL CODE

        left=leftShift(left,5,1);
        right=leftShift(right,5,1);

        k2=permute((left<<5)|right,P8,10);

    }

    static int fk(int input,int key){

        int left=(input>>4)&0x0F;
        int right=input&0x0F;

        int ep=permute(right,EP,4);

        ep^=key;

        int leftEP=(ep>>4)&0x0F;
        int rightEP=ep&0x0F;

        int row0=((leftEP&8)>>2)|(leftEP&1);
        int col0=(leftEP>>1)&3;

        int row1=((rightEP&8)>>2)|(rightEP&1);
        int col1=(rightEP>>1)&3;

        int s0=S0[row0][col0];
        int s1=S1[row1][col1];

        int p4=permute((s0<<2)|s1,P4,4);

        return ((left^p4)<<4)|right;
    }

    static int swap(int value){

        return ((value&0x0F)<<4)|((value>>4)&0x0F);

    }

    static int encrypt(int plaintext){

        int ip=permute(plaintext,IP,8);

        int round1=fk(ip,k1);

        int swapped=swap(round1);

        int round2=fk(swapped,k2);

        return permute(round2,IPI,8);

    }
        public static void main(String args[]) {

        try {

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter 10-bit Key : ");
            String keyStr = sc.next();

            System.out.print("Enter 8-bit Plaintext : ");
            String plainStr = sc.next();

            int key = Integer.parseInt(keyStr,2);
            int plaintext = Integer.parseInt(plainStr,2);

            generateKeys(key);

            System.out.println("K1 = " +
                    String.format("%8s",
                    Integer.toBinaryString(k1))
                    .replace(' ','0'));

            System.out.println("K2 = " +
                    String.format("%8s",
                    Integer.toBinaryString(k2))
                    .replace(' ','0'));

            int cipher = encrypt(plaintext);

            System.out.println("Ciphertext = " +
                    String.format("%8s",
                    Integer.toBinaryString(cipher))
                    .replace(' ','0'));

            Socket socket = new Socket("localhost",5000);

            DataOutputStream dos =
                    new DataOutputStream(socket.getOutputStream());

            dos.writeInt(cipher);
            dos.writeInt(key);

            dos.flush();

            dos.close();
            socket.close();
            sc.close();

        }
        catch(Exception e){

            e.printStackTrace();

        }

    }

}