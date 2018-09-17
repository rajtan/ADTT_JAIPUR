package com.rto_driving_test_rajasthan.Models;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;

/**
 * Created by Deep on 9/12/2018.
 */

public class MyService extends IntentService {


    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        /*Log.d("SERVICE", "onHandleIntent");
        final int port = 1300;
        ServerSocket listener = null;
        try {
            listener = new ServerSocket("192.168.10.115",port);
            Log.d("SERVICE", String.format("listening on port = %d", port));
            while (true) {
                Log.d("SERVICE", "waiting for client");
                Socket socket = listener.accept();
                Log.d("SERVICE", String.format("client connected from: %s", socket.getRemoteSocketAddress().toString()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream out = new PrintStream(socket.getOutputStream());
                for (String inputLine; (inputLine = in.readLine()) != null;) {
                    Log.d("RECIEVED", "received");
                    Log.d("RECIEVED", inputLine);
                    StringBuilder outputStringBuilder = new StringBuilder("");
                    char inputLineChars[] = inputLine.toCharArray();
                    for (char c : inputLineChars)
                        outputStringBuilder.append(Character.toChars(c + 1));
                    out.println(outputStringBuilder);
                }
            }
        } catch(IOException e) {
            Log.d("EXCEPTION", e.toString());
        }*/

        try {

            String params = URLEncoder.encode("param1", "UTF-8")
                    + "=" + URLEncoder.encode("value1", "UTF-8");
            params += "&" + URLEncoder.encode("param2", "UTF-8")
                    + "=" + URLEncoder.encode("value2", "UTF-8");

            String hostname = "192.168.10.115";
            int port = 1300;

            InetAddress addr = InetAddress.getByName(hostname);
            Socket socket = new Socket(addr, port);
            String path = "/simpleserver";

            // Send headers
            BufferedWriter wr =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            wr.write("POST "+path+" HTTP/1.0rn");
            wr.write("Content-Length: "+params.length()+"rn");
            wr.write("Content-Type: application/x-www-form-urlencodedrn");
            wr.write("rn");

            // Send parameters
            wr.write(params);
            wr.flush();

            // Get response
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                System.out.println("LINE"+line);
            }

            wr.close();
            rd.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    }

