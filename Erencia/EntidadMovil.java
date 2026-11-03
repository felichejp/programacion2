package Erencia;

import gamelab.core.GameObject;
import gamelab.graphics.Color;

public abstract class EntidadMovil implements GameObject {
        private double x;
        private double y;
        private final Color color;
        private int vidas;
        private final String forma;

        public EntidadMovil(double x, double y){
            this.x = x;
            this.y = y;
            this.color = Color.WHITE;
            this.vidas = 1;
            this.forma = "Rectangulo";
        }

        public EntidadMovil(double x, double y, Color color){
            this.x = x;
            this.y = y;
            this.color = color;
            this.vidas = 1;
            this.forma = "Rectangulo";
        }


        //Getters
        public double getX(){
            return this.x;
        }

        public double getY(){
            return this.y;
        }

        public double getVidas(){
            return this.vidas;
        }

        public Color getColor(){
            return this.color;
        }

        public String getString(){
            return this.forma;
        }

        //Setters
        public void setX(double x){
            this.x = x;
        }

        public void setY(double y){
            this.y = y;
        }

        public void setVidas(int vidas){
            this.vidas = vidas;
        }
}
