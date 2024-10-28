package juego;
import java.awt.Image;
import entorno.Entorno;


public class Pep {
	
	    private Image imagen;
	    private int x, y;
	    private int direccion; // 1 = derecha, -1 = izquierda
	    private boolean saltando;
	    private int velocidadY;
	    private int direccionUltima;

	    public Pep(Image imagen, int x, int y) {
	        this.imagen = imagen;
	        this.x = x;
	        this.y = y;
	        this.direccion = 1;
	        this.saltando = false;
	        this.velocidadY = 0;
	    }

	    public void dibujar(Entorno entorno) {
	        entorno.dibujarImagen(imagen, x, y, 0, 0.03);
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

	    public void saltar() {
	        if (!saltando) {
	            this.velocidadY = -10;
	            this.saltando = true;
	        }
	    }

	    public void actualizar() {
	        if (saltando) {
	            this.y += velocidadY;
	            this.velocidadY += 1; // Gravedad

	            if (this.y > 300) { // Simulamos que Pep está de nuevo sobre una isla
	                this.y = 300;
	                this.saltando = false;
	                this.velocidadY = 0;
	            }
	        }
	    }

	    public void disparar() {
	        // Implementación para disparar una bola de fuego
	        System.out.println("Pep dispara una bola de fuego en dirección " + direccionUltima);
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

	    public boolean estaSaltando() {
	        return saltando;
	    }
	
}
