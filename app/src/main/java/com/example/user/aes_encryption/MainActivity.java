package com.example.user.aes_encryption;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* reference :
* https://en.wikipedia.org/wiki/RSA_(cryptosystem)
* https://www.youtube.com/watch?v=Z8M2BTscoD4&t=351s
* https://en.wikipedia.org/wiki/Primality_test
* https://crypto.stackexchange.com/questions/1970/how-are-primes-generated-for-rsa
* ftp://ftp.rsasecurity.com/pub/pkcs/pkcs-1/pkcs-1v2-1.pdf
* https://brilliant.org/wiki/secure-hashing-algorithms/
* https://github.com/B-Con/crypto-algorithms
* https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java/140861#140861
* https://stackoverflow.com/questions/8890174/in-java-how-do-i-convert-a-hex-string-to-a-byte
* https://www.cc.gatech.edu/~aboldyre/teaching/F11cs6260/rsaenc.pdf
*/

public class MainActivity extends AppCompatActivity {

    //views
    TextView encrypt;
    TextView decrypt;
    EditText input;
    Button eButton;
    Button dButton;
    RadioButton aes;
    RadioButton rsa;
    CheckBox oaep;


    //RSA variables for encryption
    RSAPublicKey pubKey;
    RSAPrivateKey priKey;
    RSAKeyGenerator keygen;
    RSACipher RSAcipher;

    //AES variables for encryption
    KeyGenerator kgen;
    SecretKeySpec skeySpec;
    SecretKey skey;
    Cipher cipher;

    //variables to store messages in bytes
    String message = "";
    HexConverter hc = new HexConverter();
    byte[] encryptedMessage;
    byte[] decryptedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeViews();
        initializeKeys();

        aes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aes.isChecked()){
                    rsa.setChecked(false);
                    oaep.setEnabled(false);
                }
            }
        });

        rsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rsa.isChecked()){
                    aes.setChecked(false);
                    oaep.setEnabled(true);
                }
            }
        });

        //on click listener for encryption button
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = input.getText().toString();
                if(message.equals("")){
                    Toast.makeText(getApplicationContext(),"Please input a message!", Toast.LENGTH_SHORT).show();
                }else if(aes.isChecked()){
                    encryptionWithAES();
                }else if(rsa.isChecked()){
                    if(!oaep.isChecked()){
                        RSAcipher = new RSACipher();
                        byte[] bytes = message.getBytes();
                        encryptedMessage = RSAcipher.encrypt(bytes,pubKey);
                        encrypt.setText(hc.toHex(encryptedMessage));
                    }else{
                        RSAcipher = new RSACipher(RSACipher.OAEP_PADDING);
                        byte[] bytes = message.getBytes();
                        encryptedMessage = RSAcipher.encrypt(bytes,pubKey);
                        encrypt.setText(hc.toHex(encryptedMessage));
                    }

                }
            }
        });

        //on click listener for decryption button
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(encryptedMessage==null){
                    Toast.makeText(getApplicationContext(),"Please input a message!", Toast.LENGTH_SHORT).show();
                }else if(aes.isChecked()){
                    decryptionWithAES();
                }else if(rsa.isChecked()){
                    decryptedMessage = RSAcipher.decrypt(encryptedMessage,priKey);
                    decrypt.setText(new String(decryptedMessage));
                }
            }
        });
    }

    private void encryptionWithAES(){
        message=input.getText().toString();

        // Get the KeyGenerator

        kgen = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kgen.init(128); // 192 and 256 bits may not be available


        // Generate the secret key specs.
        skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        skeySpec = new SecretKeySpec(raw, "AES");


        // Instantiate the cipher

        cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        encryptedMessage = new byte[0];
        try {
            encryptedMessage = cipher.doFinal(message.getBytes());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }



        //Toast.makeText(getApplicationContext(),"encrypted string: " + asHex(encrypted) +"\n"+"Original string: " + originalString + " " + asHex(original) , Toast.LENGTH_SHORT).show();
        encrypt.setText(hc.toHex(encryptedMessage));

    }

    private void decryptionWithAES(){
        try {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        decryptedMessage = new byte[0];
        try {
            decryptedMessage = cipher.doFinal(encryptedMessage);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String originalString = new String(decryptedMessage);
        decrypt.setText(originalString);
    }

    //initializing variables needed for encryption
    void initializeKeys(){
        //enter key size for the key generator
        keygen = new RSAKeyGenerator(1024);
        //keygen = new RSAKeyGenerator(2048);

        pubKey = keygen.generatePublicKey();
        priKey = keygen.generatePrivateKey();

    }

    //initializing views
    void initializeViews(){
        eButton = (Button) findViewById(R.id.encryptButton);
        dButton = (Button) findViewById(R.id.decryptButton);
        encrypt = (TextView) findViewById(R.id.encryptedText);
        decrypt = (TextView) findViewById(R.id.decryptedText);
        input = (EditText) findViewById(R.id.editText);
        aes = (RadioButton) findViewById(R.id.aes);
        rsa = (RadioButton) findViewById(R.id.rsa);
        oaep = (CheckBox) findViewById(R.id.oaep);
        aes.setChecked(true);
    }

}
