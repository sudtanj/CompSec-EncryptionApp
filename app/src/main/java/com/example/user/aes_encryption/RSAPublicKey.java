package com.example.user.aes_encryption;

/**
 * Created by izzyengelbert on 11/30/2017.
 */

public class RSAPublicKey {

    //result of P * Q
    private byte[] n;

    //random generated number greater than 2
    private byte[] e;

    public RSAPublicKey(byte[] n, byte[] e){
        this.e = e;
        this.n = n;
    }

    public RSAPublicKey(){

    }

    //Getter and Setter
    public byte[] getN() {
        return n;
    }
    public void setN(byte[] n) {
        this.n = n;
    }
    public byte[] getE() {
        return e;
    }
    public void setE(byte[] e) {
        this.e = e;
    }
}
