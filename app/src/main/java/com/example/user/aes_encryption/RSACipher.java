package com.example.user.aes_encryption;

import java.math.BigInteger;

/**
 * Created by izzyengelbert on 11/30/2017.
 */

public class RSACipher {

    public static final int NO_PADDING = 0;
    public static final int OAEP_PADDING = 1;
    private int padding;

    //Empry constructor
    public RSACipher(int padding){
        this.padding = padding;
    }

    public RSACipher(){
        this.padding = NO_PADDING;
    }


    /*private static byte[] getHash(byte[] inputBytes){

        byte[] digestedBytes = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(inputBytes);
            digestedBytes = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return digestedBytes;
    }*/

    //Function to encrypt message
    public byte[] encrypt(byte[] message, RSAPublicKey RSAPublicKey){

        if(padding==OAEP_PADDING){
            try {
                OAEP.pad(message, "SHA-256 MGF1", message.length + 32 + 32 + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //transform message to big integer
        BigInteger m = new BigInteger(message);

        // m^e mod n = c
        m = m.modPow(new BigInteger(RSAPublicKey.getE()),new BigInteger(RSAPublicKey.getN()));

        return m.toByteArray();
    }

    //Function to decrypt message
    public byte[] decrypt(byte[] encodedMessage, RSAPrivateKey RSAPrivateKey){

        if(padding==OAEP_PADDING){
            try {
                OAEP.unpad(encodedMessage, "SHA-256 MGF1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //transform encoded message to big integer
        BigInteger m = new BigInteger(encodedMessage);

        // c^d mod n = m
        m = m.modPow(new BigInteger(RSAPrivateKey.getD()),new BigInteger(RSAPrivateKey.getN()));

        return m.toByteArray();
    }


}
