package juego;

import java.awt.Image;

import entorno.Entorno;

public class Tortugas {
	private int x, y, direccion;
	private Image image;
	

	
//Constructor que recibe la imagen y las coordenadas
	public Tortugas(Image image, int x, int y, int direccion)
	{
		this.image = image; 
		this.x = x;
		this.y = y;
		this.direccion = direccion;	
	
	}
	
	// metodos 
	public int getX() {return x;}
	public int getY() {return y;}
	public Image getImage() {return image;}
	public int getVelocidad() {return this.direccion;}
	public void iniciar(int velocidad) {this.direccion = velocidad;}
	public void mover() {this.x = this.x + this.direccion;}
	public void dibujar(Entorno entorno) {entorno.dibujarImagen(getImage(),getX(), getY(), 0, 0.03);}
	public void setY(int y) {this.y = y;}
	public void setX(int x) {this.x = x;}
	public void setDireccion(int direccion) {this.direccion = direccion;}
}
