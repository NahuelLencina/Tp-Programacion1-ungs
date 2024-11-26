package juego;
import java.awt.Image;
import entorno.Entorno;


public class Pep {
	
	private Image imagen;
	private int x, y;
	private int direccion; // 1 = derecha, -1 = izquierda
	private boolean saltando;
	private int velocidadY;
	private int velocidadX;
	private int alturaSaltoMaxima = 100; // Altura máxima de salto en unidades
	private int yInicial;
	private int direccionUltima;

	public Pep(Image imagen, int x, int y) {
		this.imagen = imagen;
		this.x = x;
		this.y = y;
		this.direccion = 1;
		this.saltando = false;
		this.velocidadY = 0;
		this.velocidadX = 0;
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(imagen, x, y, 0, 0.05);
	}

	public void rebotar() {
		this.direccion = this.direccion * (-1);
	}

	public void moverIzquierda() {
		this.x -= 2;
		this.direccion = -1;
		this.direccionUltima = -1;
	}

	public void moverDerecha() {
		this.x += 2;
		this.direccion = 1;
		this.direccionUltima = 1;
	}

	public void moverArriba(Isla[] islas) {
		for (Isla isla : islas) {
			if (isla != null && y > isla.getY()) {
				this.y = isla.getY() - isla.getAlto() / 2; // Posiciona a Pep en el borde superior de la isla
				break;
			}
		}
	}

	public void moverAbajo(Isla[] islas) {
		for (Isla isla : islas) {
			if (isla != null && y < isla.getY()) {
				this.y = isla.getY() - isla.getAlto() / 2; // Posiciona a Pep en el borde superior de la isla
				break;
			}
		}
	}

	public void saltar() {
		if (!saltando) {
			this.velocidadY = -5; // Velocidad inicial de salto (hacia arriba)
			this.saltando = true;
			this.yInicial = this.y; // Guarda la posición Y inicial del salto
		}
	}


	public void actualizar(Isla[] islas) {
		if (saltando) {
			this.y += velocidadY;

			// Cambia la dirección de la velocidadY si ha alcanzado la altura máxima de 10 unidades
			if (this.yInicial - this.y >= alturaSaltoMaxima) {
				this.velocidadY = 10; // Comienza a caer
			}

			// Verifica si Pep está sobre una isla para detener el salto
			if (estaSobreIsla(islas)) {
				saltando = false;
				velocidadY = 0;
			}
		}
	}

	private boolean estaSobreIsla(Isla[] islas) {
		for (Isla isla : islas) {
			boolean enBordeX = x >= isla.getX() - isla.getAncho() / 2 && x <= isla.getX() + isla.getAncho() / 2;
			boolean enBordeY = y >= isla.getY() - isla.getAlto() / 2 - 1 && y <= isla.getY() + isla.getAlto() / 2;

			if (enBordeX && enBordeY) {
				this.y = isla.getY() - isla.getAlto() / 2; // Ajusta la posición Y de Pep
				return true;
			}
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public void setX(int x) {
		this.x = x;
	}

	public boolean estaSaltando() {
		return saltando;
	}
}
