package juego;

import java.awt.Image;

public class Gnomo {
	 
	// Posiciones X e Y de la isla
	// Imagen de la isla
	
	private int x, y, velocidad;
	private Image image;
	
//Constructor que recibe la imagen y las coordenadas
	public Gnomo(Image image, int x, int y, int velocidad)
	{
		this.image = image; 
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		
		
	}
	
//metodos 
	
	public int getX() {return x;}
	public int getY() {return y;}
	public Image getImage() {return image;}
	public int getVelocidad() {return velocidad;}
	
}
