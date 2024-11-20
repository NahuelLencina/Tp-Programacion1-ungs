package juego;

import java.awt.Image;
import entorno.Entorno;

public class Tortugas{

	
	
	private int direccion;
	private double y , x ;
	int ancho , alto;
	private Image image;
	
	
//Constructor que recibe la imagen y las coordenadas
	public Tortugas(Image image, double x, int y, int direccion, int ancho, int alto)
	{
	    this.image = image; 
	    this.x = x;
	    this.y = y;
	    this.direccion = direccion;
	    this.ancho = ancho;  
	    this.alto = alto;   	
	}
	
	public void dibujarTortugas(Entorno entorno) {
        entorno.dibujarImagen(image, x, y, 0, 0.05);
    }
	
	// metodos 
	public double getX() {
		return x;
		}
	
	public double getY() {
		return y;
		}
	
	public Image getImage() {
		return image;
		}
	
	
	public void mover() {
		this.x = this.x + this.direccion;
		}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(getImage(),getX(), getY(), 0, 0.03);
		}
	
	public void setY(double d) {
		this.y = d;
		}
	
	public void setX(double d) {
		this.x = d;
		}
	
	public void setDireccion(int direccion) {
		this.direccion = direccion;
		}
	
	public int getDireccion() {
		return direccion;
		}
	
	public int getAlto() {
		return alto;
		}
	
	public int getAncho() {
		return ancho;
		}
}
