package com.example.user.aes_encryption;

/**
 * Created by izzyengelbert on 11/30/2017.
 */

public class RSAPrivateKey {


    //result of P * Q
    private byte[] n;

    //inverse of e mod euler's totient function (phi)
    private byte[] d;

    public RSAPrivateKey(byte[] n, byte[] d){
        this.d = d;
        this.n = n;
    }

    public RSAPrivateKey(){

    }

    //Getter and Setter
    public byte[] getD() {
        return d;
    }
    public void setD(byte[] d) {
        this.d = d;
    }
    public byte[] getN() {
        return n;
    }
    public void setN(byte[] n) {
        this.n = n;
    }

}
