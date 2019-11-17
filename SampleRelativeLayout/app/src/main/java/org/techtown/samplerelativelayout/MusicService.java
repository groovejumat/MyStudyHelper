package org.techtown.samplerelativelayout;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;
    private Intent intent;
    private String Music;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        Music=(String) intent.getExtras().get("Key");
//        System.out.println(Music);
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //보내준 music파일을 받아오기

        //String Music = intent.getStringExtra("Key");

        //System.out.println(Music);

//        if(Music=="asgard") {
//            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
//            mediaPlayer.setLooping(true);
//        }
//
//        if(Music=="asgard") {
//            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
//            mediaPlayer.setLooping(true);
//        }
//
//        if(Music=="asgard") {
//            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
//            mediaPlayer.setLooping(true);
//        }
//
//        if(Music=="wind") {
//            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
//            mediaPlayer.setLooping(true);
//        }
//
//        if(Music=="asgard") {
//            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
//            mediaPlayer.setLooping(true);
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Music=(String) intent.getExtras().get("Key");
        System.out.println(Music);

        if(mediaPlayer!=null){
            mediaPlayer=null;
        }

        if(Music=="asgard") {
            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
            mediaPlayer.setLooping(true);
        }

        if(Music.equals("coffeeshop")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.coffeeshop);
            mediaPlayer.setLooping(true);
        }

        if(Music.equals("rain")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.rain);
            mediaPlayer.setLooping(true);
        }

        if(Music.equals("wind")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.asgard);
            mediaPlayer.setLooping(true);
        }

        if(Music.equals("fire")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.fire);
            mediaPlayer.setLooping(true);
        }

        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
