package juego;

import java.awt.Image;


public class Isla {
	
	  private int x, y, ancho, alto;   // Posiciones X e Y de la isla
	  private Image image;  // Imagen de la isla

	    // Constructor que recibe la imagen y las coordenadas
	    public Isla(Image image, int x, int y, int ancho, int alto ) {
	        this.image = image;
	        this.x = x;
	        this.y = y;
	        this.ancho = ancho;
	        this.alto = alto;
	        
	    }

	    // Getters para acceder a las propiedades
	    
	    public int getX() { return x; }
	    public int getY() { return y; }
	    public Image getImagen() { return image; } 
	    public int getAncho() {return ancho;}
	    public int getAlto() {return alto;}
}
