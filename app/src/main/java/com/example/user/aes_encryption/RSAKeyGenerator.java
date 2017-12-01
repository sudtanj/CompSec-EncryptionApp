package com.example.user.aes_encryption;

import android.util.Log;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by izzyengelbert on 11/30/2017.
 */

public class RSAKeyGenerator {

    private Random random;

    //Two random generated Large Prime Number
    private BigInteger p;
    private BigInteger q;

    //RSA private exponent
    private BigInteger d;

    //euler's totient function or phi
    private BigInteger ETF;

    //RSA public exponent
    private static final BigInteger e = BigInteger.valueOf(3);

    //result of P * Q
    private BigInteger n;

    //Key Size for RSA Public Key and Private Key
    private int keySize;

    // constructor with key size parameter
    public RSAKeyGenerator(int keySize){
        random = new Random();
        this.keySize = keySize;
        generateValues();
    }

    //empty constructor with default key size of 1024 bits (128 bytes)
    public RSAKeyGenerator(){
        random = new Random();
        this.keySize = 1024;
        generateValues();
    }

    private void generateValues(){

        //Generating P and Q
        Log.d("Generate P and Q","Generating...");
        do{
            p = new BigInteger(keySize/2,100, random);
            q = new BigInteger(keySize/2,100, random);
        }while (p==q);//making sure the generated prime numbers are not the same
        Log.d("Generate P and Q","Successful");

        //Generating Euler's Totient Function
        Log.d("Generate ETF(PHI)","Generating...");
        // (p-1)*(q-1)
        ETF = (p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1)));
        Log.d("Generate ETF(PHI)","Successful");

        //Generating E
        /*Log.d("Generate E","Generating...");
        int temp;
        do{
            e = new BigInteger(24,100,random);
            temp = e.compareTo(BigInteger.valueOf(2));
        }while(temp==-1||(ETF.mod(e).equals(BigInteger.valueOf(0))));//making sure e is greater than 2 and e is not a factor of ETF
        Log.d("Generate E","Successful");*/

        //Generating D
        Log.d("Generate D","Generating...");
        d = euclidianAlgorithm(ETF,ETF,e, BigInteger.valueOf(1),ETF);//Also known as the Extended Greatest Common Divisor
        Log.d("Generate D","Successful");

        //Generating N
        Log.d("Generate N","Generating...");
        // P * Q
        n = p.multiply(q);
        Log.d("Generate N","Successful");

    }

    //Recursive function to find the inverse of e mod phi/euler's totient function(etf), see references to understand how the algorithm works
    private BigInteger euclidianAlgorithm(BigInteger upOne, BigInteger upTwo, BigInteger downOne, BigInteger downTwo, BigInteger etf){
        if(downOne.equals(BigInteger.valueOf(1))){
            if(downTwo.compareTo(BigInteger.valueOf(0))==-1){
                downTwo = downTwo.mod(etf);
            }
            return downTwo;
        }else{
            BigInteger tempDownOne = downOne;
            BigInteger tempDownTwo = downTwo;
            BigInteger temp = upOne.divide(downOne);

            downOne = upOne.subtract(downOne.multiply(temp));
            downTwo = upTwo.subtract(downTwo.multiply(temp));

            upOne = tempDownOne;
            upTwo = tempDownTwo;

            if(downTwo.compareTo(BigInteger.valueOf(0))==-1){
                downTwo = downTwo.mod(etf);
            }
            return euclidianAlgorithm(upOne,upTwo,downOne,downTwo,etf);
        }
    }

    //Method to generate public key from generated values
    public RSAPublicKey generatePublicKey(){
        byte[] nInput = n.toByteArray();
        byte[] eInput = e.toByteArray();
        return new RSAPublicKey(nInput,eInput);
    }

    //Method to generate private key from generated values
    public RSAPrivateKey generatePrivateKey(){
        byte[] nInput = n.toByteArray();
        byte[] dInput = d.toByteArray();
        return new RSAPrivateKey(nInput,dInput);
    }

    //Getter and Setter
    public byte[] getN() {
        return n.toByteArray();
    }
    public void setN(byte[] n) {
        this.n = new BigInteger(n);
    }
    public byte[] getP() {
        return p.toByteArray();
    }
    public void setP(byte[] p) {
        this.p = new BigInteger(p);
    }
    public byte[] getQ() {
        return q.toByteArray();
    }
    public void setQ(byte[] q) {
        this.q = new BigInteger(q);
    }
    public byte[] getD() {
        return d.toByteArray();
    }
    public void setD(byte[] d) {
        this.d = new BigInteger(d);
    }
    /*public byte[] getE() {
        return e.toByteArray();
    }
    public void setE(byte[] e) {
        this.e = new BigInteger(e);
    }*/
}
