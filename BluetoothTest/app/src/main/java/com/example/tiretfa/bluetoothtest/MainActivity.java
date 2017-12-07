package com.example.tiretfa.bluetoothtest;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {


    static private String ip = "192.168.1.68";
    private EditText e1;
    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.textView);
        Thread myThread = new Thread(new MyServerThread());
        myThread.start();
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("ok")){
                    Toast.makeText(getApplicationContext(),editable.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void sendData(View v){
        String message = e1.getText().toString();
        BackgroundTask b1 = new BackgroundTask();
        b1.execute(message);
    }

    class MyServerThread implements Runnable{

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        String message;
        Handler h = new Handler();

        @Override
        public void run() {
            try {
                ss = new ServerSocket(7801);
                while (true) {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            test = message;
                            e1.setText(message);
                            //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask extends AsyncTask<String,Void, Void>{
        Socket s;
        PrintWriter writer;


        @Override
        protected Void doInBackground(String... voids) {
            try {
                String message = voids[0];
                s = new Socket(ip, 6000);

                writer = new PrintWriter(s.getOutputStream());
                writer.write(message);
                writer.flush();
                writer.close();

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

}
