package com.sicrastudio.sicra.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Vector;

/**
 * Created by sicra on 18/01/15.
 */
public class VistaJuego extends View implements SensorEventListener {

    // ASTEROIDES //
    private Vector<Grafico> aste;
    private int numAste = 5;
    private int numFrag = 3;

    // NAVE //
    private Grafico nave;
    private int     giroNave;
    private float   aceleracionNave;
    private int     despNave;
    private static final int   PASO_GIRO_NAVE        = 5;
    private static final float PASO_ACELERACION_NAVE = 1.5f;
    private static final int   DESPLAZA              = 5;

    // MISIL //
    private Grafico misil;
    private static float VELOCIDAD_MISIL = 12;
    private boolean misilActivo;
    private int     distanciaMisil;

    // THREAD Y TIEMPO //
    private HiloJuego hiloJuego;               // Hilo encargado de procesar el tiempo
    private static int  PERIODO_PROCESO = 50;  // Tiempo que ha de transcurrir para procesar cambios (ms)
    private        long ultimoProceso   = 0;   // Momento en el que se realizo el ultimo proceso
    private boolean corriendo = false;         // Controlar si la aplicacion esta en segundo plano
    private boolean pausa;                     // Controlar si la aplicacion esta en pausa

    // PANTALLA TACTIL //
    // Las variables mX y mY se utilizaran para recordar las coordenadas del ultimo evento
    private float mX = 0, mY = 0;
    private boolean disparo = false;

    // Variables para manejo por sensor de orientacion
    private boolean hayValorInicial = false;
    private float valorInicial;

    // Sonido
    private MediaPlayer mediaPlayer;


    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.explosion);

        Drawable graficoNave, graficoAste, graficoMisil;
        // Obtenemos la imagen/recurso del asteroide
        graficoAste = context.getResources().getDrawable(R.drawable.asteroide_01);
        // Obtenemos la imagen/recurso de la nave
        graficoNave = context.getResources().getDrawable(R.drawable.nave2);
        // Obtenemos la imagen/recurso del misil
        graficoMisil = context.getResources().getDrawable(R.drawable.disparo);


        // Registramos -> REGISTRO DE SENSORES
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (!listaSensores.isEmpty()) {
            Sensor sensorOrientacion = listaSensores.get(0);
            sensorManager.registerListener(this, sensorOrientacion, SensorManager.SENSOR_DELAY_UI);
        }

        // Creamos un vector para contener todos los asteroides que iran por la pantalla
        // y lo rellenamos con graficos de asteroides con valores aleatorios para su velocidad
        // y rotacion
        aste = new Vector<Grafico>();
        for (int i = 0; i < numAste; i++) {
            Grafico aste = new Grafico(this, graficoAste);
            aste.setIncX(Math.random() * 4 - 2);
            aste.setIncY(Math.random() * 4 - 2);
            aste.setAngulo((int) (Math.random() * 360));
            aste.setRotacion((int) (Math.random() * 8 - 4));
            this.aste.add(aste);
        }

        // Inicializamos el grafico de la nave
        nave = new Grafico(this, graficoNave);
        // Inicializamos el misil y el estado del disparo
        misil = new Grafico(this, graficoMisil);
        misilActivo = false;

        corriendo = true;
    }

    // Al comenzar y dibujar por primera vez la pantalla del juego
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Dibujamos los asteroides en posiciones aleatorias
        for (Grafico aste : this.aste) {
            do {
                aste.setPosX(Math.random() * (w - aste.getAncho()));
                aste.setPosY(Math.random() * (h - aste.getAlto()));
            } while (aste.distancia(nave) < (w + h) / 5);
        }

        // Dibujamos la nave abajo en el centro
        nave.setPosX((w / 2) - (nave.getAncho() / 2));
        nave.setPosY(h - nave.getAlto());

        // HILO QUE CONTROLA EL JUEGO
        hiloJuego = new HiloJuego();
        hiloJuego.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Dibujamos cada uno de los asteroides
        for (Grafico aste : this.aste) {
            aste.dibujaGrafico(canvas);
        }
        // Dibujamos la nave
        nave.dibujaGrafico(canvas);
        // dibujamos el misil
        if (misilActivo) {
            misil.dibujaGrafico(canvas);
        }
    }

    // Movimiento de la nave MANEJADOR DE EVENTOS DE TECLADO
    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
        super.onKeyDown(codigoTecla, evento);
        // procesamos la pulsacion
        boolean pulsacion = true;

        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave += PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                aceleracionNave = 0;
                despNave = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                despNave = -DESPLAZA;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                despNave = DESPLAZA;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                lanzarMisil();
                break;
            case KeyEvent.KEYCODE_Q:
                //terminarJuego();
                break;
            default:
                // si llegamos aqui no hemos pulsado nada que nos interese
                pulsacion = false;
                break;
        }
        return pulsacion;
    }

    // MANEJADOR DE LA PANTALLA TACTIL
    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        super.onTouchEvent(evento);

        // Obtenemos la posicion de la pulsacion
        float x = evento.getX();
        float y = evento.getY();

        switch (evento.getAction()) {
            // Si comienza una pulsacion (ACTION_DOWN) activamos la variable deisparo
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;
            // Comprobamos si la pulsacion es contunuada con un desplazamiento horizontal
            // o vertical. En caso de ser asi, desactivamos disparo porque se trara de un movimiento
            // en lugar de un disparo.
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);

                if (dy < 6 && dx > 6) {
                    // Un desplazamiento del dedo horizontal
                    if ((x - mX) > 0)
                        despNave = DESPLAZA;
                    else
                        despNave = -DESPLAZA;
                    disparo = false;
                } else if (dx < 6 && dy > 6) {
                    // Un desplazamiento vertical que produce una aceleracion o una parada
                    if((x - mX) > 0)
                        aceleracionNave += PASO_ACELERACION_NAVE;
                    else
                        aceleracionNave -= PASO_ACELERACION_NAVE;

                    if ((y - mY) > 0) {
                        aceleracionNave = 0;
                        despNave = 0;
                    }
                    disparo = false;
                }

                break;
            // Si se levanta el dedo (ACTION_UP) sin haberse producido desplazamiento horizontal
            // o vertical si disparo esta activo -> disparar
            case MotionEvent.ACTION_UP:
                if (disparo) {
                    lanzarMisil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }

    protected synchronized void actualizaMovimiento() {
        long ahora = System.currentTimeMillis();
        double retardo, nIncX, nIncY;

        // No hacemos nada si el periodo de proceso no se ha cumplido
        if (ultimoProceso + PERIODO_PROCESO <= ahora) {
            // Para una ejecucion en tiempo real calculamos retardo
            retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;

            // Movemos los asteroides
            for (Grafico asteroides : aste) {
                asteroides.incrementaPos();
            }
            // Movemos el misil
            if (misilActivo) {
                misil.incrementaPos();
                distanciaMisil--;
                if (distanciaMisil < 0) {
                    misilActivo = false;
                } else {
                    for (int i = 0; i < aste.size(); i++) {
                        if (misil.verificaColision(aste.elementAt(i))) {
                            mediaPlayer.start();
                            destruyeAste(i);
                        }
                    }
                }
            }

            ultimoProceso = ahora;

            // Movemos la nave
            nave.setIncX(despNave + aceleracionNave * retardo);
            nave.incrementaPos();

        }

    }

    private void destruyeAste(int i) {
        aste.remove(i);
        misilActivo = false;
    }

    private void lanzarMisil() {
        misil.setPosX(nave.getPosX() + nave.getAncho()/2 - misil.getAncho()/2);
        misil.setPosY(nave.getPosY() + nave.getAlto()/2 - misil.getAlto()/2);

        misil.setAngulo(nave.getAngulo()+270);
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * VELOCIDAD_MISIL);

        distanciaMisil = (int) Math.min(
                this.getWidth() / Math.abs(misil.getIncX()),
                this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[1];
        if (!hayValorInicial) {
            valorInicial = valor;
            hayValorInicial = true;
        }
        if (valor > valorInicial) {
            despNave = DESPLAZA;
        } else {
            despNave = -DESPLAZA;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /* GET y SET */
    public HiloJuego getHiloJuego() {
        return hiloJuego;
    }

    public void setCorriendo(boolean corriendo) {
        this.corriendo = corriendo;
    }

    public void setPausa(boolean pausa) {
        this.pausa = pausa;
    }

    /* CLASES Internas */
    private class HiloJuego extends Thread {
        @Override
        public void run() {
            while (true) {
                while (corriendo && !pausa) {
                    actualizaMovimiento();
                }
            }
        }
    }


}
