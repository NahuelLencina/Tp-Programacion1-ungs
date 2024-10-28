package juego;

import java.awt.Image;

import entorno.Entorno;

public class Tortugas{

	
	
	private double direccion;
	double y , x ;
	int ancho , alto;
	private Image image;
	
	
//Constructor que recibe la imagen y las coordenadas
	public Tortugas(Image image, double x, int y, double direccion, int ancho, int alto)
	{
		this.image = image; 
		this.x = x;
		this.y = y;
		this.direccion = direccion;	
		
	}
	
	// metodos 
	public double getX() {return x;}
	public double getY() {return y;}
	public Image getImage() {return image;}
	public void iniciar(int velocidad) {this.direccion = velocidad;}
	public void mover() {this.x = this.x + this.direccion;}
	public void dibujar(Entorno entorno) {entorno.dibujarImagen(getImage(),getX(), getY(), 0, 0.03);}
	public void setY(double d) {this.y = d;}
	public void setX(double d) {this.x = d;}
	public void setDireccion(double direccion) {this.direccion = direccion;}
	public double getDireccion() {return direccion;}
	public int getAlto() {return alto;}
	public int getAncho() {return ancho;}
}
