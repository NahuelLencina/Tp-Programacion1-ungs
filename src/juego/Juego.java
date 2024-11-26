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
//=========================================================================================
// 		Objeto
	
	 	private Entorno entorno;
	    private Image fondo;
	    private Image casita;
	    private BolaDeFuego[] bolas;	    
	    private Gnomo[] gnomos;
	    private Tortugas[] tortugas;
	    private Isla[] islas;
	    private Random random = new Random(); // Generador de números aleatorios
	    private Pep pep; 
	    
//=========================================================================================
//		Variables de instancia
	    
	    private int vidaActual = 5;
	    private boolean vidaPep = true;
	    private int tickContador = 0; // Contador de ticks
	    private int tiempoParaAparecer; // Ticks hasta la próxima aparición
	    private int direccion = 1;
	    private boolean enMovimiento = false;
	    private int velocidadBola = -3;
	    private int tiempoEnSegundos = 0;
	    private int contadorDeTicks = 0; // Contador auxiliar de ticks
	    private int contadorGnomo=0;



//=========================================================================================
// 		Constantes
	    private int TICK_POR_SEGUNDO = 60; // Suponiendo que 60 ticks equivalen a 1 segundo
	    private int VELOCIDAD_AL_CAER = 3; 
	    private int LIMITE_PARA_SALVAR_GNOMOS = 280;
	    private int TIEMPO_MAXIMO_ALEATORIO = 400; 
	    private int MITAD_EJEX;
	    private int LIMITE_INFERIOR_PANTALLA;

//=========================================================================================
//      Métodos
	    
	    Juego() {
	        // Inicializa el objeto entorno
	        this.entorno = new Entorno(this, "Al Rescate de los Gnomos", 800, 600);
	        MITAD_EJEX = entorno.ancho() / 2;
	        LIMITE_INFERIOR_PANTALLA = entorno.alto();
	        inicializaCargarObjetos();
	        

	    }
//====================================================================================	    
// 		En este metodo se procesa un instante de tiempo
	    public void tick() {	 
	    	
	        if (entorno.sePresiono(entorno.TECLA_ENTER) || enMovimiento) {
	            if (vidaActual > 0 && vidaPep==true) {
	                actualizar();
	                enMovimiento = true;   
	            }
	            else 
	            	vidaActual=0;
	            if(entorno.sePresiono(entorno.TECLA_ESCAPE)) {
	            	inicializaCargarObjetos();
		        }
	        }
//====================================================================================        
// 			los siguientes metodos se encargan de actualizar los objeetos en pantalla
	        
	        imprimirFondo();
	        imprimirCasita();
	        dibujarIslas();
	        actualizarGnomos();
	        actualizarTortugas();
	        actualizarPep();
	        actualizarBola();
	        mostrarDatosPantalla();
	        
	        // Mostramos cartel para inicio de juego presione ENTER 
	        if (enMovimiento!=true){
	        	entorno.cambiarFont("Tahoma", 25,Color.BLACK, entorno.NEGRITA);
	        	entorno.escribirTexto("Presione Enter para comenzar...", 210, 320); 
	        }
	    }
	    
	      
	    public void inicializaCargarObjetos() {
	    	
	    	   // Inicializar lo que haga falta para el juego
	        vidaActual = 5;
	        vidaPep = true;
	        tickContador = 0;  // Reiniciar contador de ticks
	        tiempoParaAparecer = generarTiempoAleatorio();  // Reiniciar el tiempo de aparición
	        direccion = 1;
	        enMovimiento = false;
	        velocidadBola = -3;
	        tiempoEnSegundos = 0;
	        contadorDeTicks = 0; // Reiniciar el contador auxiliar de ticks
	        contadorGnomo = 0;
	       
		   
		    

	        this.islas = new Isla[15]; // Declaramos el array de islas
	        this.gnomos = new Gnomo[5]; // Declaramos el array de gnomos
	        this.tortugas = new Tortugas[4];
	        this.pep = new Pep(Herramientas.cargarImagen("Imagenes/Pep.png"), MITAD_EJEX, 400); // Posición inicial de Pep
	        this.bolas = new BolaDeFuego[6];

	        // Se llama al método cargar el fondo
	        cargarFondo();
	        // Se llama al método para cargar las islas
	        cargarIslas();
	        // Se llama al método para cargar la casita
	        cargarCasita();
	        // Inicia el juego!
	        this.entorno.iniciar();
	    }
	    
//======================================================================================================== 
// 		Método que muestra los datos en pantalla
	    
	    private void mostrarDatosPantalla() {
	        // Mostrar tiempo en pantalla
	        if(enMovimiento==true&&vidaActual>0) {	
	        	revisarMovimientosPep();
	        	// Incrementar contador de ticks para el tiempo
		    	contadorDeTicks++;
		    	if (contadorDeTicks >= TICK_POR_SEGUNDO) {
		    		tiempoEnSegundos++;
		    		contadorDeTicks = 0;
		    	}   	
	        }
	        entorno.cambiarFont("ITALIC", 20, Color.BLACK);

	        if(vidaActual==0 || vidaPep==false) {
	        	entorno.cambiarFont("Tahoma", 78,Color.BLACK, entorno.NEGRITA);
       		 	entorno.escribirTexto("Game Over", 190, 300);
       			entorno.cambiarFont("Tahoma", 25,Color.BLACK, entorno.NEGRITA);
	        	entorno.escribirTexto("Presione Escape...", 120, 90); 
	        }
	        
	        	entorno.cambiarFont("ITALIC", 20, Color.BLACK);
	        	mostrarTiempoEnPantalla();
	        	String vidaActualString = String.format("Vidas:  " + vidaActual);
	        	entorno.escribirTexto(vidaActualString, 200, 40);
	        	entorno.cambiarFont("ITALIC", 20, Color.BLACK);
	        	String gnomoSalvadoString = String.format("Gnomos Salvados: " + contadorGnomo);
	        	entorno.escribirTexto(gnomoSalvadoString, 600, 40);
	    }
	    
//======================================================================================================== 
// 		Método encargado de la deteccion de teclas y movimientos de pep
	    
	    private void revisarMovimientosPep() {
	    	if(vidaPep==true) { 
	    		// Control de teclas para Pep
	    		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('a')) {	
	    			if(pep.getX()<10) {
	    				pep.rebotar();		
	    			}
	    			else{
	    				pep.moverIzquierda();
	    				velocidadBola = -3;
	    			
	    			}
	    		} else if (entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('d')) {
	    			if(pep.getX()>entorno.ancho()-10)
	    				pep.rebotar();
	    				
	    			else{
	    				pep.moverDerecha();
	    				velocidadBola = 3;
	    			
	    			}
	    		}
	    		// Iniciar el salto
	    		if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) {
	    			pep.saltar();
	    		
	    			// Herramientas.play("Sonidos/mario-bros-woo-hoo.wav");
	    		}

	    		if (entorno.sePresiono('c') && enMovimiento) {
//					Ejecuta sonido al saltar
	    			Herramientas.play("Sonidos/pew-pew.wav");
	    			agregarBola(pep.getX(),pep.getY(),velocidadBola);  	
	    		}
	    	}
	    	
	    } 


//======================================================================================================== 
// 		Método encargado de mostrar el tiempo de juego en pantalla
			
		private void mostrarTiempoEnPantalla() {
	        int minutos = tiempoEnSegundos / 60;
	        int segundos = tiempoEnSegundos % 60;
	        String tiempoFormateado = String.format("Tiempo: %02d:%02d", minutos, segundos);

	        // Mostrar en la esquina superior izquierda, ajusta las coordenadas según prefieras
	        entorno.escribirTexto(tiempoFormateado, 10, 40);
	    }

//======================================================================================================== 
// 		Método encargado de monitorear el comportamiento de Pep
		
		public void actualizarPep() {
			// Actualizamos a Pep
			if(vidaPep==true && !colisionPepTortuga(pep) ) {
				pep.dibujar(this.entorno);
				pep.actualizar(islas);
				// Aplica gravedad si Pep no estÃ¡ sobre una isla y no estÃ¡ saltando
				if (!pepSobreIsla(pep) && !pep.estaSaltando()) {
					gravedadPep(); 
					if(pep.getY()>LIMITE_INFERIOR_PANTALLA) {
						vidaPep=false;
					}
				}
				else
					if(pep.getY()>LIMITE_INFERIOR_PANTALLA)
					{
						vidaPep=false;
					}
			}
			else { 
				pep=null;
				vidaPep=false;
			}
		}
//======================================================================================================== 
// 		Método encargado de aplicar gravedad
	
		public void gravedadPep(){
			if(!pepSobreIsla(pep)) {
				pep.setY(pep.getY() + VELOCIDAD_AL_CAER); //Aplica gravedad
			}
		}
		
	
		
//======================================================================================================== 
// 		Método encargado de demorar de forma aleatoria el nacimiento de un Gnomo y tortuga
	    
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

//======================================================================================================== 
// 		Método encargado de monitorear el comportamiento de los Gnomos

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
						if(colisionPepGnomo(pep) && this.gnomos[i]!=null) {
							if(pep.getY()>LIMITE_PARA_SALVAR_GNOMOS){
	    						gnomos[i] = null;  // Eliminar gnomo
	    						contadorGnomo++;        // Incrementar contador solo una vez
	    					}
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    
//======================================================================================================== 
// 		Método encargado de monitorear el comportamiento de las tortugas
	    
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

//======================================================================================================== 
// 		Método encargado de actualizar la posicion de la Bola de fuego
	    
		private void actualizarBola() {
			for (int i = 0; i < bolas.length; i++) {
				BolaDeFuego bola = bolas[i];
				if (bola != null) {
					bola.dibujar(this.entorno);
				if(bola.getX()>0 && bola.getX()<entorno.ancho()) {
					bola.movimientoBola(); // si la bola de fuego esta dentro de la pantalla vive.	
					if(disparoColision(bola)) {
						this.bolas[i] = null; // al colisionar matamos el objeto bola de fuego
					}
				}
				else
					this.bolas[i] = null; // de lo contrario matamos el objeto bola de fuego
				}
				
			}			
		}

		
//======================================================================================================== 
// 		Método encargado de controlar el comportamiento de los Gnomos
// 		Controla la caida de los Gnomos del las Ultimas islas 
		
	    private void moverGnomo(Gnomo gnomo, int index) {
	    	int naceY = 95;
	    	int naceX = entorno.ancho()/2;
	    	
	        gnomo.setY(gnomo.getY() + 1);
	        if (gnomo.getY() > 550) {
	        	gnomo.setX(naceX);
	        	gnomo.setY(naceY);
	        	return;
	        }
	        		
	        if(colisionConTortuga(gnomo)) {
	            this.gnomos[index] = null; // Eliminar el gnomo si cae o colisiona
	        } else {
	            int nuevaDireccion = direccionAleatorio(direccion);
	            gnomo.setDireccion(nuevaDireccion);
	        }
	    }
		
//======================================================================================================== 
// 		Método para detectar colisiones entre la bola de fuego y las Tortugas	
		
		private boolean disparoColision(BolaDeFuego bola) {
		        int margen = 20; // Margen de colisión
		        for (int i=0; i<tortugas.length;i++) {
		        	Tortugas tortuga = tortugas[i];
		        	if (bola != null && tortuga != null) {
		                // Verificar si las posiciones están dentro del margen
		                boolean colisionX = Math.abs(tortuga.getX() - bola.getX()) < margen;
		                boolean colisionY = Math.abs(tortuga.getY() - bola.getY()) < margen;
		                 
		                if (colisionX && colisionY) {
		                	this.tortugas[i] = null;
		                    return true;
		                }
		            }
		        }
		        return false;
		    }
		
//======================================================================================================== 
// 		Método encargado de monitorear las colisiones entre tortugas y Gnomos 	
	    
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

//======================================================================================================== 
// 		Método para detectar colisiones entre Pep-Tortugas		
		
		private boolean colisionPepTortuga(Pep pep) {
	        int margen = 10; // Margen de colisión
	        for (int i=0; i<tortugas.length;i++) {
	        	Tortugas tortuga = tortugas[i];
	        	
	        	
	        	if (pep!= null && tortuga != null) {
	                // Verificar si las posiciones están dentro del margen
	                boolean colisionX = Math.abs(tortuga.getX() - pep.getX()) < margen;
	                boolean colisionY = Math.abs(tortuga.getY() - pep.getY()) < margen;
	                 
	                if (colisionX && colisionY) {
	                	this.pep = null;
	                	vidaPep=false;
	                    return true;
	                }
	            }
	        }
	        return false;
	    }
		
//======================================================================================================== 
// 		Método para detectar colisiones entre Pep-Gnomos
		
		private boolean colisionPepGnomo(Pep pep) {
	        int margen = 10; // Margen de colisión
	        for (int i=0; i<gnomos.length;i++) {
	            Gnomo gnomo = gnomos[i];
	        	if (pep!= null && gnomo!= null) {
	                // Verificar si las posiciones están dentro del margen
	                boolean colisionX = Math.abs(gnomo.getX() - pep.getX()) < margen;
	                boolean colisionY = Math.abs(gnomo.getY() - pep.getY()) < margen;              
	                if (colisionX && colisionY) {	  
	                    return true;
	                }
	            }
	        }
	        return false;
	    }

//======================================================================================================== 
// 		Método para detectar los bordes de las islas, para que las tortugas se mantengan en la misma isla
		
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
		

//=================================================================================================== 
// 		Método para verificar si un Gnomo está sobre alguna isla

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

    
//=================================================================================================== 
// 		Metodo para verificar si Pep estÃ¡ sobre alguna isla
	    
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
//=================================================================================================== 
// 		Método para verificar si un tortuga está sobre alguna isla
	    
		private boolean tortugaSobreIsla(Tortugas tortuga) {
		    for (Isla isla : islas) {
		            // Verifica si el gnomo está dentro de los límites de la isla
		            boolean tocandoX = tortuga.getX() >= isla.getX() - isla.getAncho() / 2 &&
		                               tortuga.getX() <= isla.getX() + isla.getAncho() / 2;
		            boolean tocandoY = tortuga.getY()>180 &&	tortuga.getY() >= isla.getY() - isla.getAlto() / 2 &&
		            				   tortuga.getY() <= isla.getY() + isla.getAlto() / 2;
		            if (tocandoX && tocandoY) {
		                return true; // El gnomo está sobre esta isla
		            }
		    }
		    return false; // El gnomo no está sobre ninguna isla
		}
		
		
		private int generarTiempoAleatorio() {
			return random.nextInt(TIEMPO_MAXIMO_ALEATORIO);// Genera tiempo aleatorio hasta 400 tick
		}

//======================================================================================================
//		Este metodo agrega las bola dentro del Array si el mismo tiene una posicion Null
		
		private void agregarBola(int x, int y, int direccion) {
			for(int i=0; i<bolas.length;i++){
				if(bolas[i]==null){
					Image imagenBola = Herramientas.cargarImagen("Imagenes/fireball.png");
					bolas[i] = new BolaDeFuego(imagenBola,y, x, direccion ); // Crea el nuevo Gnomo en la posición fija
					break;// Salimos del bucle después de agregar la tortuga
				}
			}
		}

//======================================================================================================
//		Este metodo agrega las tortugas dentro del Array si el mismo tiene una posicion Null

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

		
//======================================================================================================
//		Este metodo agregalos gnomos dentro del Array si el mismo tiene una posicion Null
		
		private void agregarGnomo() {
	    	int naceY = 95;
	    	int naceX = entorno.ancho()/2;
	    	
			for (int i = 0; i < gnomos.length; i++) {
				if (gnomos[i] == null) {
					// Usar coordenadas fijas para la posición del Gnomo
					int x = naceX; // Coordenada fija en X
					int y = naceY; // Coordenada fija en Y
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
//==================================================================================================
//		Este metodo devuelve o numeros negativos o positivos de forma aleatoria para que los objetos 
//      tomen una direccion aleatoria		
		
		private int direccionAleatorio(int numero) {
			Random random = new Random();
			// Genera un booleano aleatorio para determinar el signo
			boolean esNegativo = random.nextBoolean(); // true o false aleatoriamente
			// Si esNegativo es true, devuelve el número negativo, de lo contrario el número positivo
			return esNegativo ? -numero : numero;
		}

//=====================================================================================================
//		Metodo para dibujar los objetos en el entorno grafico.
		
	    private void imprimirFondo() {
	        // Imprimimos el fondo en la ventana
	        this.entorno.dibujarImagen(fondo, MITAD_EJEX, LIMITE_INFERIOR_PANTALLA / 2, 0, 1.1);
	    }

	    private void imprimirCasita() {
	        this.entorno.dibujarImagen(casita, MITAD_EJEX, 80, 0, 0.2);
	    }

	    private void dibujarIslas() {
	        for (Isla isla : islas) {
	            if (isla != null) {
	                entorno.dibujarImagen(isla.getImagen(), isla.getX(), isla.getY(), 0, 0.3);
	            }
	        }
	    }
		
	
//=================================================================================================	    
//		Metodos necesarios para cargar las imagenes del juego
		
	    public void cargarFondo(){
			//Cargamos el fondo
			try {
				this.fondo = Herramientas.cargarImagen("Imagenes/fondo.jpg");
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error al cargar la imagen de respaldo: " + e.getMessage());
			}
		}

// 		Este metodo se encarga de cargar las cordenadas de las Islas en forma de piramide
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

