package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.util.List;
import java.util.Random;


import java.util.ArrayList;
import java.awt.Point;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Image fondo;
	private Image casita;
	private Gnomo[] gnomos;
	private Tortugas[] tortuga;
	private Isla[] islas;
	private int vidaActual=0;


	private int tickContador = 0; // Contador de ticks
	private int tiempoParaAparecer; // Ticks hasta la próxima aparición
	private Random random = new Random(); // Generador de números aleatorios

	private int direccion;

	// Variables y métodos propios de cada grupo
	// ...	
	private List<Point> listaCoordenadas = new ArrayList<>();// Creo un ArrayList para guardar las coordenadas de las islas

	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);

		// Inicializar lo que haga falta para el juego
		this.islas = new Isla[15];  // Declaramos la lista de islas
		this.gnomos = new Gnomo[5];  // Declaramos la lista de gnomos
		this.tortuga = new Tortugas[4];
	
		

		// Se llama al metodo cargar el fondo
		cargarFondo();
		// Se llama al metodo para cargar las islas   	
		cargarIslas();
		// Se llama al metodo para agregar los Gnomas
		agregarGnomo();
		// Se llama al metodo para agregar los tortugas
		agregarTortuga();
		// Se llama al metodo para cargar la casita
		cargarCasita();

		// Generar el tiempo para la primera aparición
		tiempoParaAparecer = generarTiempoAleatorio();

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
		
		actualizar();
		// Procesamiento de un instante de tiempo

		// Imprimimos el fondo en la ventana, usamos la sobreCarga del metodo dibujarImagen para ampliar la misma 

		this.entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto()/2, 0, 1.1);		
		this.entorno.dibujarImagen(casita, entorno.ancho()/ 2, 80, 0, 0.2);

		for (Isla isla : islas) {
			entorno.dibujarImagen(isla.getImagen(), isla.getX(), isla.getY(), 0, 0.3);
		}
	
		
	    // Dibujar y mover los gnomos
	    for (Gnomo gnomo : gnomos) {
	        if (gnomo != null) { // Verificar que el gnomo no sea null
	            gnomo.dibujar(this.entorno);
	            
	            if (!gnomoSobreIsla(gnomo)) {
	                // Si el gnomo no está sobre una isla, desciende
	                gnomo.setY(gnomo.getY() + 2); // Incrementa Y para simular caída
	                int numeroConSignoAleatorio = cambiarSignoAleatorio(direccion); 
	                gnomo.setDireccion(numeroConSignoAleatorio);
	            } else {
	                gnomo.mover(); // Si está sobre una isla, puede moverse
	            }
	        }
	    }
	    
	    // Dibujar y mover los gnomos
	    for (Tortugas tortuga : tortuga) {
	        if (tortuga != null) { // Verificar que el gnomo no sea null
	            tortuga.dibujar(this.entorno);
	            
	            if (!tortugaSobreIsla(tortuga)) {
	                // Si el gnomo no está sobre una isla, desciende
	                tortuga.setY(tortuga.getY() + 2); // Incrementa Y para simular caída
	                int numeroConSignoAleatorio = cambiarSignoAleatorio(direccion); 
	                tortuga.setDireccion(numeroConSignoAleatorio);
	            } else {
	                tortuga.mover(); // Si está sobre una isla, puede moverse
	            }
	        }
	    }
	    
	    
	    
	    
	}

	// Método para verificar si un gnomo está sobre alguna isla
	private boolean gnomoSobreIsla(Gnomo gnomo) {
	    for (Isla isla : islas) {
	        if (isla != null) {
	            // Verifica si el gnomo está dentro de los límites de la isla
	            boolean tocandoX = gnomo.getX() >= isla.getX() - isla.getAncho() / 2 &&
	                               gnomo.getX() <= isla.getX() + isla.getAncho() / 2;

	            boolean tocandoY = gnomo.getY() >= isla.getY() - isla.getAlto() / 2 &&
	                               gnomo.getY() <= isla.getY() + isla.getAlto() / 2;

	            if (tocandoX && tocandoY) {
	                return true; // El gnomo está sobre esta isla
	            }
	        }
	    }
	    return false; // El gnomo no está sobre ninguna isla
	}
	
	
	
	private boolean tortugaSobreIsla(Tortugas tortuga) {
	    for (Isla isla : islas) {
	        if (isla != null) {
	            // Verifica si el gnomo está dentro de los límites de la isla
	            boolean tocandoX = tortuga.getX() >= isla.getX() - isla.getAncho() / 2 &&
	                               tortuga.getX() <= isla.getX() + isla.getAncho() / 2;

	            boolean tocandoY = tortuga.getY() >= isla.getY() - isla.getAlto() / 2 &&
	                               tortuga.getY() <= isla.getY() + isla.getAlto() / 2;

	            if (tocandoX && tocandoY) {
	                return true; // El gnomo está sobre esta isla
	            }
	        }
	    }
	    return false; // El gnomo no está sobre ninguna isla
	}
			
	
	
	private int generarTiempoAleatorio() {
		return random.nextInt(180) + 60;// Genera entre 60 (1 segundo) y 240 (4 segundos)
	}

	public void actualizar() {
		tickContador++; // Incrementa el contador de ticks

		// Verifica si es el momento de aparecer un nuevo Gnomo
		if (tickContador >= tiempoParaAparecer) {
			// Intenta agregar un nuevo Gnomo si hay espacio
			agregarGnomo();
			// Resetea el contador y genera el nuevo tiempo para la próxima aparición
			tickContador = 0;
			tiempoParaAparecer = generarTiempoAleatorio();
		}
	}

private void agregarTortuga() {
	direccion = (1);
	
		for(int i=0; i<tortuga.length;i++){
			if(tortuga[i]==null){
				int x=400;
				int y=0;
				int numeroConSignoAleatorio = cambiarSignoAleatorio(direccion);
				direccion=numeroConSignoAleatorio;

				Image imagenTortuga = Herramientas.cargarImagen("Imagenes/Tortuga.png");
				tortuga[i] = new Tortugas(imagenTortuga, x, y, direccion); // Crea el nuevo Gnomo en la posición fija
				
				
			}
		}
	}


private void agregarGnomo() {

		direccion = (1);

		for (int i = 0; i < gnomos.length; i++) {
			if (gnomos[i] == null) {
				// Usar coordenadas fijas para la posición del Gnomo
				int x = entorno.ancho()/2; // Coordenada fija en X
				int y = 95; // Coordenada fija en Y
				int numeroConSignoAleatorio = cambiarSignoAleatorio(direccion);
				direccion=numeroConSignoAleatorio;
				Image imagenGnomo = Herramientas.cargarImagen("Imagenes/gnomo.png");
				gnomos[i] = new Gnomo(imagenGnomo, x, y, direccion); // Crea el nuevo Gnomo en la posición fija

				// Mensaje de verificación
				System.out.println("Gnomo creado en posición: (" + x + ", " + y + ")");
				break;  // Salimos del bucle después de agregar el Gnomo
			}
		}
	}

	private int cambiarSignoAleatorio(int numero) {
		Random random = new Random();
		// Genera un booleano aleatorio para determinar el signo
		boolean esNegativo = random.nextBoolean(); // true o false aleatoriamente

		// Si esNegativo es true, devuelve el número negativo, de lo contrario el número positivo
		return esNegativo ? -numero : numero;
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

		

		int inicioY = 120; 		// Posicion inicial para la primer isla en el eje Y
		int nivel = 5; 			// Número de niveles de islas (filas)
		int columna = 1;		// Columnas iniciales en cada nivel
		int fila=80;			// Espacio vertical entre niveles
		int distancia = 160;	// Espacio horizontal entre islas
		int indice=0;			// Índice para recorrer el array de islas
		int ancho=120;
		int alto=40;
		
		Image imagenIsla = Herramientas.cargarImagen("Imagenes/isla.jpg"); // Cargamos la Imagen

		for(int nivelActual=0; nivelActual<nivel; nivelActual++) {
			int inicioX = entorno.ancho()/2; 	// Centramos en el eje X

			for(int columnaActual=0; columnaActual<columna; columnaActual++) {
				// Crear una isla y agregarla al array
				this.islas[indice] = new Isla(imagenIsla, inicioX-(80*nivelActual), inicioY, ancho, alto);  

				listaCoordenadas.add(new Point (inicioX-fila*nivelActual,inicioY)); // guardamos en una Lista las cordenadas de las islas

				inicioX+=distancia;				// Espacio entre islas
				indice++; 						// Avanzar al siguiente índice en el array de islas

			}
			inicioY+=fila; 						// Bajar al siguiente nivel
			columna++;							// Aumentar la cantidad de columnas en el siguiente nivel

		}

		
	}

	public void cargarCasita()
	{
		this.casita = Herramientas.cargarImagen("Imagenes/casita.png");
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
