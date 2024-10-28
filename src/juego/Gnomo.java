package juego;

import entorno.Entorno;

import java.awt.Image;


public class Gnomo{
	 
	
	private int x, direccion, ancho, alto;
	double y;
	private Image image;
	
	

	
//Constructor que recibe la imagen y las coordenadas
	public Gnomo(Image image, int x, int y, int direccion, int ancho, int alto)
	{
		this.image = image; 
		this.x = x;
		this.y = y;
		this.direccion = direccion;	
		this.ancho = ancho;
		this.alto = alto;
	}
	
	// metodos 
	public double getX() {return x;}
	public double getY() {return y;}
	public Image getImage() {return image;}
	public int getVelocidad() {return this.direccion;}
	public void iniciar(int velocidad) {this.direccion = velocidad;}
	public void mover() {this.x = this.x + this.direccion;}
	public void dibujar(Entorno entorno) {entorno.dibujarImagen(getImage(),getX(), getY(), 0, 0.03);}
	public void setY(double d) {this.y = d;}
	public void setX(int x) {this.x = x;}
	public void setDireccion(int direccion) {this.direccion = direccion;}
	public int getAlto() {return alto;}
	public int getAncho() {return ancho;}
}
