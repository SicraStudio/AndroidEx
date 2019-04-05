package com.sicrastudio.sicra.asteroides;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Juego extends Activity {

    private VistaJuego vistaJuego;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
        // Música de fondo
        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mediaPlayer.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        // Al poner la variable corriendo a false permitimos que el thread pueda acabar
        vistaJuego.setCorriendo(false);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        // ponemos el juego en suspension
        vistaJuego.setPausa(true);
        mediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // continuamos ejecutando el thread
        vistaJuego.setPausa(false);
        vistaJuego.setCorriendo(true);
        mediaPlayer.start();
    }
}
