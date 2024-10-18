package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Image fondo;
	private Image casita;
	private Isla[] islas;
		
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);

		// Inicializar lo que haga falta para el juego
	
	
		
		// Se llama al metodo cargar el fondo
		cargarFondo();
		
		// Se llama al metodo para cargar las islas   	
		cargarIslas();
		this.casita = Herramientas.cargarImagen("Imagenes/Casita.png");
	
		// Inicia el juego!
		this.entorno.iniciar();
	}
	
	
	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// Imprimimos el fondo en la ventana, usamos la sobreCarga del metodo dibujarImagen para ampliar la misma 
		
		this.entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto()/2, 0, 1.1);
		
		this.entorno.dibujarImagen(casita, entorno.ancho()/ 2, 80, 0, 0.2);
		
		for (Isla isla : islas) {
				entorno.dibujarImagen(isla.getImagen(), isla.getX(), isla.getY(), 0, 0.3);
		}
		
		
		// Aca debemos declarar un if con las vidas
		
		
		
	}
	

	// Este metodo carga el fondo del juego
	public void cargarFondo(){
		//Cargamos el fondo
		try {
			this.fondo = Herramientas.cargarImagen("Imagenes/fondo.jpg");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al cargar la imagen de respaldo: " + e.getMessage());
		}
	}
	
	
	// Este metodo se encarga de cargar las cordenadas de las Islas en forma de piramide
	public void cargarIslas() {
		this.islas = new Isla[15]; 
		
		int inicioY = 120; 		// Posicion inicial para la primer isla en el eje Y
		int nivel = 5; 			// Número de niveles de islas (filas)
		int columna = 1;		// Columnas iniciales en cada nivel
		int fila=80;			// Espacio vertical entre niveles
		int distancia = 160;	// Espacio horizontal entre islas
		int indice=0;			// Índice para recorrer el array de islas
		
		Image imagenIsla = Herramientas.cargarImagen("Imagenes/isla.jpg"); // Cargamos la Imagen
		
		for(int nivelActual=0; nivelActual<nivel; nivelActual++) {
			int inicioX = entorno.ancho()/2; 	// Centramos en el eje X
			
			for(int columnaActual=0; columnaActual<columna; columnaActual++) {
												// Crear una isla y agregarla al array
					this.islas[indice] = new Isla(imagenIsla, inicioX-fila*nivelActual, inicioY);  
				inicioX+=distancia;				// Espacio entre islas
				indice++; 						// Avanzar al siguiente índice en el array de islas

			}
			inicioY+=fila; 						// Bajar al siguiente nivel
			columna++;							// Aumentar la cantidad de columnas en el siguiente nivel
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
