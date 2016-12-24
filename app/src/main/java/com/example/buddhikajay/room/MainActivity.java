package com.example.buddhikajay.room;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fi.vtt.nubomedia.kurentoroomclientandroid.KurentoRoomAPI;
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomError;
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomListener;
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomNotification;
import fi.vtt.nubomedia.kurentoroomclientandroid.RoomResponse;
import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor;

public class MainActivity extends AppCompatActivity implements RoomListener {

    private KurentoRoomAPI kurentoRoomAPI;
    private LooperExecutor executor;
    private String TAG = "Kurento Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executor = new LooperExecutor();
        executor.requestStart();
        String wsRoomUri = "wss://192.168.8.100:8443/room";
        kurentoRoomAPI = new KurentoRoomAPI(executor, wsRoomUri, this);
        kurentoRoomAPI.connectWebSocket();
    }

    @Override
    public void onRoomResponse(RoomResponse response) {
        Log.d(TAG, response.toString());
        if (response.getId() == 123) {
            Log.d(TAG, "Successfully connected to the room!");
            kurentoRoomAPI.sendMessage("MyRoomName", "MyUsername", "Hello room!", 125);
        } else if (response.getId() == 125) {
            Log.d(TAG, "The server received my message!");
        }

    }

    @Override
    public void onRoomError(RoomError error) {
        Log.d(TAG, error.toString());
    }

    @Override
    public void onRoomNotification(RoomNotification notification) {
        if(notification.getMethod().equals(RoomListener.METHOD_SEND_MESSAGE)) {
            final String username = notification.getParam("user").toString();
            final String message = notification.getParam("message").toString();
            Log.d(TAG, "Oh boy! " + username + " sent me a message: " + message);
        }
    }

    @Override
    public void onRoomConnected() {
        kurentoRoomAPI.sendJoinRoom("phone", "1", true, 123);
    }

    @Override
    public void onRoomDisconnected() {

    }
}
