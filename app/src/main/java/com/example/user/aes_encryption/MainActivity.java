package com.example.user.aes_encryption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    String message;
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit=(Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message=((EditText)findViewById(R.id.editText)).getText().toString();

                // Get the KeyGenerator

                KeyGenerator kgen = null;
                try {
                    kgen = KeyGenerator.getInstance("AES");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                kgen.init(128); // 192 and 256 bits may not be available


                // Generate the secret key specs.
                SecretKey skey = kgen.generateKey();
                byte[] raw = skey.getEncoded();

                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");


                // Instantiate the cipher

                Cipher cipher = null;
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

                byte[] encrypted =
                        new byte[0];
                try {
                    encrypted = cipher.doFinal(message.getBytes());
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

                try {
                    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                byte[] original = new byte[0];
                try {
                    original = cipher.doFinal(encrypted);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                String originalString = new String(original);

                Toast.makeText(getApplicationContext(),"encrypted string: " + asHex(encrypted) +"\n"+"Original string: " +
                        originalString + " " + asHex(original) , Toast.LENGTH_SHORT).show();
            }
        });

    }

}
