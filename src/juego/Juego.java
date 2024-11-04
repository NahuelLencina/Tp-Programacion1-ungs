package juego;


import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.util.Random;


public class Juego extends InterfaceJuego
{
	 private Entorno entorno;
	    private Image fondo;
	    private Image casita;
	    private Gnomo[] gnomos;
	    private Tortugas[] tortugas;
	    private Isla[] islas;
	    private int vidaActual = 3;
	    private int tickContador = 0; // Contador de ticks
	    private int tiempoParaAparecer; // Ticks hasta la próxima aparición
	    private Random random = new Random(); // Generador de números aleatorios
	    private Pep pep; // Agregamos a Pep
	    private int direccion = 1;
	    private boolean enMovimiento = false;
	    
	    private int tiempoEnSegundos = 0;
	    private int ticksPorSegundo = 60; // Suponiendo que 60 ticks equivalen a 1 segundo
	    private int contadorDeTicks = 0; // Contador auxiliar de ticks

	    // Métodos
	    Juego() {
	        // Inicializa el objeto entorno
	        this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);

	        // Inicializar lo que haga falta para el juego
	        this.islas = new Isla[15]; // Declaramos la lista de islas
	        this.gnomos = new Gnomo[5]; // Declaramos la lista de gnomos
	        this.tortugas = new Tortugas[4];
	        this.pep = new Pep(Herramientas.cargarImagen("Imagenes/Pep.png"), entorno.ancho() / 2, 300); // Posición inicial de Pep

	        // Se llama al método cargar el fondo
	        cargarFondo();
	        // Se llama al método para cargar las islas
	        cargarIslas();
	        // Se llama al método para cargar la casita
	        cargarCasita();
	        // Inicia el juego!
	        this.entorno.iniciar();
	    }

	    public void tick() {
	
	        if (entorno.sePresiono(entorno.TECLA_ENTER) || enMovimiento) {
	            if (vidaActual > 0) {
	                actualizar();
	               // Herramientas.play("Sonidos/");
	                enMovimiento = true;
	            }
	        }
	        
	    

	        // Procesamiento de un instante de tiempo
	        imprimirFondo();
	        imprimirCasita();
	        dibujarIslas();
	        actualizarGnomos();
	        actualizarTortugas();
	        actualizarPep();
	        // Mostrar tiempo en pantalla
	       
	        if(enMovimiento==true&&vidaActual>0) {	
	        	// Incrementar contador de ticks para el tiempo
		    	contadorDeTicks++;
		    	if (contadorDeTicks >= ticksPorSegundo) {
		    		tiempoEnSegundos++;
		    		contadorDeTicks = 0;
		    	}
	         	
	        }
	        entorno.cambiarFont("ITALIC", 20, Color.BLACK);

	        if(vidaActual==0) {
	        	entorno.cambiarFont("Tahoma", 78,Color.BLACK, entorno.NEGRITA);
       		 	entorno.escribirTexto("Game Over", 190, 300);
	        }
	        
	        	entorno.cambiarFont("ITALIC", 20, Color.BLACK);
	        	mostrarTiempoEnPantalla();
	        	String vidaActualString = String.format("Vidas:  " + vidaActual);
	        	entorno.escribirTexto(vidaActualString, 200, 40);
	        
	        
	        
	        
	        // Control de teclas para Pep
	        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.sePresiono('a')) {
	            pep.moverIzquierda();
	        } else if (entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.sePresiono('d')) {
	            pep.moverDerecha();
	        }

	        // Iniciar el salto
	        if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	            pep.saltar();
	        }

	        if (entorno.sePresiono(entorno.TECLA_ABAJO) || entorno.sePresiono('s')) {
	            pep.moverAbajo(islas);
	        }

	        if (this.entorno.sePresiono('c')) {
	            pep.disparar();
	        }
	    }
	    
	    private void mostrarTiempoEnPantalla() {
	        int minutos = tiempoEnSegundos / 60;
	        int segundos = tiempoEnSegundos % 60;
	        String tiempoFormateado = String.format("Tiempo: %02d:%02d", minutos, segundos);

	        // Mostrar en la esquina superior izquierda, ajusta las coordenadas según prefieras
	        entorno.escribirTexto(tiempoFormateado, 10, 40);
	    }

	    public void actualizarPep() {
	        // Actualizamos a Pep
	        pep.dibujar(this.entorno);
	        pep.actualizar(islas);

	        // Aplica gravedad si Pep no está sobre una isla y no está saltando
	        if (!pepSobreIsla(pep) && !pep.estaSaltando()) {
	            pep.setY(pep.getY() + 1); // Controlar la velocidad de caída
	        }
	    }

	    private boolean pepSobreIsla(Pep pep) {
	        for (Isla isla : islas) {
	            if (isla != null) {
	                boolean tocandoX = pep.getX() >= isla.getX() - isla.getAncho() / 2 &&
	                        pep.getX() <= isla.getX() + isla.getAncho() / 2;
	                boolean tocandoY = pep.getY() >= isla.getY() - isla.getAlto() / 2 &&
	                        pep.getY() <= isla.getY() + isla.getAlto() / 2;
	                if (tocandoX && tocandoY) {
	                    pep.setY(isla.getY() - isla.getAlto() / 2); // Ajustar a Pep en el borde superior de la isla
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

	    private void imprimirFondo() {
	        // Imprimimos el fondo en la ventana
	        this.entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0, 1.1);
	    }

	    private void imprimirCasita() {
	        this.entorno.dibujarImagen(casita, entorno.ancho() / 2, 80, 0, 0.2);
	    }

	    private void dibujarIslas() {
	        for (Isla isla : islas) {
	            if (isla != null) {
	                entorno.dibujarImagen(isla.getImagen(), isla.getX(), isla.getY(), 0, 0.3);
	            }
	        }
	    }

	    private void moverGnomo(Gnomo gnomo, int index) {
	        gnomo.setY(gnomo.getY() + 1);
	        if (gnomo.getY() > 550 || colisionConTortuga(gnomo)) {
	            this.gnomos[index] = null; // Eliminar el gnomo si cae o colisiona
	        } else {
	            int nuevaDireccion = direccionAleatorio(direccion);
	            gnomo.setDireccion(nuevaDireccion);
	        }
	    }

	    private void actualizarGnomos() {
	        for (int i = 0; i < gnomos.length; i++) {
	            Gnomo gnomo = gnomos[i];
	            if (gnomo != null) {
	                gnomo.dibujar(this.entorno);
	                if (!gnomoSobreIsla(gnomo)) {
	                    moverGnomo(gnomo, i);
	                } else {
	                    gnomo.mover();
	                    if (colisionConTortuga(gnomo)) {
	                        this.gnomos[i] = null;
	                    }
	                }
	            }
	        }
	    }

	    private void actualizarTortugas() {
	        for (Tortugas tortuga : tortugas) {
	            if (tortuga != null) {
	                tortuga.dibujar(this.entorno);
	                if (tortugaSobreIsla(tortuga)) {
	                    tortuga.mover();
	                    if (colisionBordeIsla(tortuga)) {
	                        tortuga.setDireccion(-tortuga.getDireccion());
	                    }
	                } else {
	                    tortuga.setY(tortuga.getY() + 2);
	                }
	            }
	        }
	    }

	    private boolean gnomoSobreIsla(Gnomo gnomo) {
	        for (Isla isla : islas) {
	            if (isla != null) {
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

	    private boolean colisionConTortuga(Gnomo gnomo) {
	        int margen = 20; // Margen de colisión

	        for (Tortugas tortuga : tortugas) {
	            if (gnomo != null && tortuga != null) {
	                // Verificar si las posiciones están dentro del margen
	                boolean colisionX = Math.abs(tortuga.getX() - gnomo.getX()) < margen;
	                boolean colisionY = Math.abs(tortuga.getY() - gnomo.getY()) < margen;

	                if (colisionX && colisionY) {
	                    if(vidaActual>0)
	                    	vidaActual--;
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

		private boolean colisionBordeIsla(Tortugas tortuga) {
		    for (Isla isla : islas) {
		        // Calcula los límites de la isla
		        int limiteIzquierdo = isla.getX() - isla.getAncho() / 2;
		        int limiteDerecho = isla.getX() + isla.getAncho() / 2;
		        int limiteSuperior = isla.getY() - isla.getAlto() / 2;
		        int limiteInferior = isla.getY() + isla.getAlto() / 2;

		        // Verifica si la tortuga está dentro de los límites de la isla (con margen de 10)
		        if( (tortuga.getX()-10) == limiteIzquierdo && tortuga.getY() >= limiteSuperior && tortuga.getY() <= limiteInferior) {
		        	tortuga.setX(tortuga.getX()+5);
		        	return true;
		        }
		        if(tortuga.getX()+10 == limiteDerecho && tortuga.getY() >= limiteSuperior && tortuga.getY() <= limiteInferior){
		        	tortuga.setX(tortuga.getX()-5);
		        	return true;
		        }     
		    }
		    return false; // La tortuga no está sobre ninguna isla
		}	
		// Método para verificar si un tortuga está sobre alguna isla
		private boolean tortugaSobreIsla(Tortugas tortuga) {
		    for (Isla isla : islas) {
		        if (isla != null) {
		            // Verifica si el gnomo está dentro de los límites de la isla
		            boolean tocandoX = tortuga.getX() >= isla.getX() - isla.getAncho() / 2 &&
		                               tortuga.getX() <= isla.getX() + isla.getAncho() / 2;
		            boolean tocandoY = tortuga.getY()>180 &&	tortuga.getY() >= isla.getY() - isla.getAlto() / 2 &&
		            				   tortuga.getY() <= isla.getY() + isla.getAlto() / 2;
		            if (tocandoX && tocandoY) {
		                return true; // El gnomo está sobre esta isla
		            }
		        }
		    }
		    return false; // El gnomo no está sobre ninguna isla
		}
		
		
		private int generarTiempoAleatorio() {
			return random.nextInt(400);// Genera tiempo aleatorio hasta 400 tick
		}

		public void actualizar() {
			tickContador++; // Incrementa el contador de ticks

			// Verifica si es el momento de aparecer un nuevo Gnomo
			if (tickContador >= tiempoParaAparecer) {
				// Intenta agregar un nuevo Gnomo si hay espacio
				agregarGnomo();
				agregarTortuga();
				
				// Resetea el contador y genera el nuevo tiempo para la próxima aparición
				tickContador = 0;
				tiempoParaAparecer = generarTiempoAleatorio();
			}
		}
		
	private void agregarTortuga() {
		//direccion = 1;
		
			for(int i=0; i<tortugas.length;i++){
				if(tortugas[i]==null){
					int x= random.nextInt(80,720);
					int y=0;
					int ancho = 5;
					int alto = 10;
					Image imagenTortuga = Herramientas.cargarImagen("Imagenes/Tortuga.png");
					tortugas[i] = new Tortugas(imagenTortuga, x, y, direccion,ancho, alto); // Crea el nuevo Gnomo en la posición fija
					break;// Salimos del bucle después de agregar la tortuga
				}
			}
		}

	private void agregarGnomo() {
			for (int i = 0; i < gnomos.length; i++) {
				if (gnomos[i] == null) {
					// Usar coordenadas fijas para la posición del Gnomo
					int x = entorno.ancho()/2; // Coordenada fija en X
					int y = 95; // Coordenada fija en Y
					int ancho = 5;
					int alto = 10;
					int newDireccion = direccionAleatorio(direccion);
					direccion=newDireccion;
					Image imagenGnomo = Herramientas.cargarImagen("Imagenes/gnomo.png");
					gnomos[i] = new Gnomo(imagenGnomo, x, y, direccion, ancho, alto); // Crea el nuevo Gnomo en la posición fija				
					break;  // Salimos del bucle después de agregar el Gnomo
				}
			}
		}

		private int direccionAleatorio(int numero) {
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

