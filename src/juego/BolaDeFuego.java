package juego;

import entorno.Entorno;
import java.awt.Image;

public class BolaDeFuego {
	
	private Image imagen;
	private int y, x;
	private int velocidadX;
	private int direccion;

	
	
	
	public BolaDeFuego(Image imagen, int y, int x, int direccion) {
		
		this.imagen = imagen;
		this.y = y;
		this.x = x;
		this.velocidadX =  0;
		this.direccion = direccion;
	}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(imagen, x, y, 0, 0.05);
	}
	
	
	public void movimientoBola() {
		this.x = this.x + this.direccion;		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
