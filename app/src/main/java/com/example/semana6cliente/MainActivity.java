package com.example.semana6cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText user, idUser;
    private Button button;
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.user);
        idUser = findViewById(R.id.idUser);
        button = findViewById(R.id.button);

        serverInit();
        button.setOnClickListener(
                (view)->{
                    Gson gson = new Gson();
                    String usertxt= user.getText().toString();
                    String idtxt = idUser.getText().toString();
                    User currentUser = new User(usertxt,idtxt);
                    Log.e(">>>USERCURRENT",currentUser.getName());

                    String json = gson.toJson(currentUser);
                    Log.e("miJASOOOOOn",json);
                    sendMessage( json);

                }
        );

        Gson gson = new Gson();
    }

    public void serverInit() {

        new Thread(
                ()->{
                    try {
                        socket = new Socket("192.168.0.10",5000);
                        //esperando

                        Log.e("INIT>>>","me conecte");

                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);
                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while(true) {
                            String line = reader.readLine();

                           if(line.equals("ok"))  {
                               Intent i = new Intent(this,wellcome.class);
                               startActivity(i);
                           }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

        ).start();;

    }
    public void sendMessage(String ms) {
        new Thread(
                ()->{
                    try {
                        writer.write(ms+"\n");
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

        ).start();
    }
}