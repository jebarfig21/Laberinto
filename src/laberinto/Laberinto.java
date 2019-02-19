/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laberinto;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.lang.Math;
/**
 *
 * @author yisus
 */
public class Laberinto extends  PApplet {
    
    PFont fuente;  // Fuente para mostrar texto en pantalla
    
    // Propiedades del modelo de termitas.
    int alto = 30;         // Altura (numero de celdas) de la cuadricula.
    int ancho = 50;        // Anchura (numero de celdas) de la cuadricula.
    int celda = 20;
    //boolean visitado= false;// Tamanio de cada celda cuadrada (en pixeles).
    //int termitas = 200;      // Cantidad de termitas dentro del modelo.
    //float densidad = 0.20f;   // Proporcion de astilla en el modelo (con probabilidad de 0 a 1).
    ModeloLaberinto modelo;  // El objeto que representa el modelo de termitas.

    
@Override
    public void setup() {
        //frameRate(60);
        size( ancho*celda, (alto*celda)+32);
        background(50);
        fuente = createFont("Arial",12,true);
        modelo = new ModeloLaberinto(ancho, alto, celda);
        // Preprocesamiento
        //for(int i = 0; i < 15000; i++)
        //modelo.evolucion2();
    }
    
    
    /**
     * Pintar el mundo del modelo (la cuadricula y las astillas).
     */
    @Override
    public void draw() {
        // Las astillas se representan por el valor True del estado de cada Celda.
        
        for(int i = 0; i < alto; i++)
          for(int j = 0; j < ancho; j++){
            if(modelo.mundo[i][j].estado)
              fill(255, 210, 0);//amarillo
            else
             
             stroke(20);
             fill(200);
             rect(j*modelo.tamanio, i*modelo.tamanio, modelo.tamanio, modelo.tamanio);
            
          }

        
        
    //Borrar pared    
        stroke(200);
        line(x1,y1,x2,y2);
    //
        
        // Dibujar las termitas.
        // Cada termita puede ser de color verde, si no carga astilla o roja en caso contrario.
        //for(Termita t : modelo.termitas){
          //if(t.cargando){
            //fill(255, 0, 0);
            //rect(t.posX*modelo.tamanio, t.posY*modelo.tamanio, modelo.tamanio, modelo.tamanio);
          //}
          //else{  
            //fill(0, 255, 0);
           // rect(t.posX*modelo.tamanio, t.posY*modelo.tamanio, modelo.tamanio, modelo.tamanio);
          //}
        //}

        // Pintar informacion del modelo en la parte inferior de la ventana.
        fill(50);
        rect(0, alto*celda, (ancho*celda), 32);
        fill(255);
        textFont(fuente,10);
        //text("Generacion " + modelo.generacion, 128, (alto*celda)+12);
        text("Tiempo: " + millis()/1000, 5, (alto*celda)+24);
        //text("Proporcion de astillas: " + densidad, 128, (alto*celda)+24);

        // Actualizar el modelo a la siguiente generacion.
        //modelo.evolucion1();
        //modelo.evolucion2();
        //modelo.evolucion3();
    }
    
    class Celda{
      int celdaX, celdaY;
      boolean estado;
      boolean paredIzq;
      boolean paredDer;
      boolean paredArr;
      boolean paredPis;
      //boolean visitado;

      /** Constructor de una celda
        @param celdaX Coordenada en x
        @param celdaY Coordenada en y
        * estado True para recorrida false para virgen.
        * los 4 boolean referentes a las paredes inicializan siempre con valor true
        * 
        * 
      */
      Celda(int celdaX, int celdaY){
        this.celdaX = celdaX;
        this.celdaY = celdaY;
        this.estado = false;//si ya se visito o no
        this.paredIzq=true;
        this.paredDer=true;
        this.paredArr=true;
        this.paredPis=true;
        //this.visitado=false;
      }
    }
    
  /**
   * 
   * 
   */
    
    class Creador{
      int posX, posY;  // Coordenadas de la posicion de la termita
      int direccion;   // Valor entre 0 y 7 para indicar dirección de movimiento
      //boolean cargando;  // True si está cargando una astilla, false en caso contrario.

      /** Constructor de una termita
        @param posX Indica su posicion en el eje X
        @param posX Indica su posicion en el eje Y
        @param direccion Indica la direccion en la que mira.
            -----------
           | 0 | 1 | 2 |
           |-----------|
           | 7 |   | 3 |
           |-----------|
           | 6 | 5 | 4 |
            -----------
      */
      Creador(int posX, int posY, int direccion){
        this.posX = posX;
        this.posY = posY;
        this.direccion = direccion;
        //this.cargando = false;
      }
    }

    // --- Clase ModeloTermitas ---
    /**
     * Representa el automata celular que genera autorganizacion
     * en una colonia de termitas.
     */
    class ModeloLaberinto{
      int ancho, alto;  // Tamaño de celdas a lo largo y ancho de la cuadrícula.
      int tamanio;  // Tamaño en pixeles de cada celda.
      int tam;  // Numero de bloque que ah avanzado el creador
      Celda[][] mundo;  // Mundo de celdas donde habitan las astillas.
      //ArrayList<Termita> termitas;  // Todas las termitas del modelo.
      Random rnd = new Random();  // Auxiliar para decisiones aleatorias.

      /** Constructor del modelo
        @param ancho Cantidad de celdas a lo ancho en la cuadricula.
        @param ancho Cantidad de celdas a lo largo en la cuadricula.
        @param tamanio Tamaño (en pixeles) de cada celda cuadrada que compone la cuadricula.
        @param cantidad Numero de termitas dentro de la cuadricula.
      */
      
      ModeloLaberinto(int ancho, int alto, int tamanio){
        this.ancho = ancho;
        this.alto = alto;
        this.tamanio = tamanio;
        //this.generacion = 0;
        
        //Inicializar mundo (usar densidad)
        mundo = new Celda[alto][ancho];
        for(int i = 0; i < alto; i++)
          for(int j = 0; j < ancho; j++)
            mundo[i][j] = new Celda(i,j);
        //Inicializar termitas (usar cantidad)
        //termitas = new ArrayList<Termita>();
        //for(int i = 0; i < cantidad; i++)
          //termitas.add(new Termita(rnd.nextInt(ancho), rnd.nextInt(alto), rnd.nextInt(8)) );
      }

      /** Mueve una termita segun la direccion dada.
        Considerando que las fronteras son periodicas.
        @param t La termita a mover en el modelo.
        @param direccion La direccion en la que se desea mover la termita (con valor entre 0 y 7).
      */
      void moverCreador(Creador t, int direccion){
        switch(direccion) {
          case 0:  t.posX = (t.posX-1)%ancho;
                   if(t.posX < 0) t.posX += ancho;
                   t.posY = (t.posY-1)%alto;
                   if(t.posY < 0) t.posY += alto;
                   t.direccion = direccion;
                   break;
          case 1:  t.posY = (t.posY-1)%alto;
                   if(t.posY < 0) t.posY += alto;
                   t.direccion = direccion;
                   break;
          case 2:  t.posX = (t.posX+1)%ancho;
                   if(t.posX < 0) t.posX += ancho;
                   t.posY = (t.posY-1)%alto;
                   if(t.posY < 0) t.posY += alto;
                   t.direccion = direccion;
                   break;
          case 3:  t.posX = (t.posX+1)%ancho;
                   if(t.posX < 0) t.posX += ancho;
                   t.direccion = direccion;
                   break;
          case 4:  t.posX = (t.posX+1)%ancho;
                   if(t.posX < 0) t.posX += ancho;
                   t.posY = (t.posY+1)%alto;
                   if(t.posY < 0) t.posY += alto;
                   t.direccion = direccion;
                   break;
          case 5:  t.posY = (t.posY+1)%alto;
                   if(t.posY < 0) t.posY += alto;
                   t.direccion = direccion;
                   break;
          case 6:  t.posX = (t.posX-1)%ancho;
                   if(t.posX < 0) t.posX += ancho;
                   t.posY = (t.posY+1)%alto;
                   if(t.posY < 0) t.posY += alto;
                   t.direccion = direccion;
                   break;
          case 7:  t.posX = (t.posX-1)%ancho;
                   if(t.posX < 0) t.posX += ancho;
                   t.direccion = direccion;
                   break;
        }
      }
      /**
       * 
       * @param C 
       */
      void mueveAleatorio(Creador C){
        Random rnd = new Random();
        moverCreador(C,rnd.nextInt(8));
        //int siguiente=rnd.nextInt(8);
      }
    }
      
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here}
         PApplet.main(new String[] { "laberinto.Laberinto" });
    }
    

}
