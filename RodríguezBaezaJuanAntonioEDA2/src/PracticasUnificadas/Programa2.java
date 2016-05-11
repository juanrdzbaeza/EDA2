package PracticasUnificadas;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Clase Programa2
 * @author Grupo Iluminati
 * @version 1.2
 */
public class Programa2 {

	/**
	 * Atributos:
	 * FACTOR: factor para calcular la media de absentismo.
	 * listaSuperiores: lista enlazada de las manzanas.
	 * S: tamaño del lado de una manzana (suponemos que son cuadradas).
	 * W: tamaño del ancho de las avenidas.
	 * listaGreedy: coleccion de las manzanas que será ordenada por absentismo.
	 * listaGrupos: coleccion de los grupos (uno por cada manzana que supera el absentismo).
	 */
	public static final double FACTOR = 1.7;
	public static ArrayList<Manzana> listaSuperiores;
	public static final double S = 20;
	public static final double W = 12;
	public static LinkedList<Manzana> listaGreedy;
	public static ArrayList<Grupo> listaGrupos = new ArrayList<Grupo>();

	/**
	 * ejecutar para la practica 2
	 * 
	 */
	public static void ejecutar2() {
		Scanner lectura = new Scanner(System.in);
		
		
		String directorio = System.getProperty("user.dir" + File.separator);
		
		System.out.println("      Práctica 02: GREEDY");
		System.out.println("-------------------------------------------");
		System.out.println("1. GENERAR DATOS ALEATORIAMENTE");
		System.out.println("2. ESCOGER ARCHIVO DE DATOS MANUALMENTE");
		System.out.println("3. VOLVER AL MENÚ PRINCIPAL");
		System.out.println();
		System.out.printf("Introduzca la acción deseada... ");
		
		int opcion;
		
		do{
			opcion = lectura.nextInt();
			if(opcion < 1 || opcion > 3)
				System.out.println("El numero elegido está fuera del intervalo permitido, insertelo nuevamente.");
			}
			while(opcion < 1 || opcion > 3);
		
		
		int evaluacion = 0;
		String archivoEntrada = "";
		
		switch (opcion) {
		case 1:
			// Generamos los datos aleatorios.
			Generar_Datos.generarDatos(directorio);
			System.out.println("Los datos han sido generados.");
			System.out.printf("Elija el n que desea evaluar entre 1 y 9: ");
			
			do{
			evaluacion = lectura.nextInt();
			if(evaluacion < 1 || evaluacion > 9)
				System.out.println("El numero elegido está fuera del intervalo permitido, insertelo nuevamente.");
			}
			while(evaluacion < 1 || evaluacion > 9);
			
			int caso;
			
			System.out.println();
			
			System.out.println("1- Mejor caso.\n2- Peor caso.\n3- Caso aleatorio");
			
			System.out.println();
			
			System.out.println("Seleccione el caso que desea:");
			
			do{
				caso = lectura.nextInt();
				if(caso < 0 || caso > 3)
					System.out.println("Elección no valida, inserte otra opción:");
			}while(caso < 0 || caso > 3);
			
			
			// ELECCIÓN CASO
			
			if(caso == 1)
				archivoEntrada = "datosMejor" + evaluacion + ".txt";
			else if(caso == 2)
				archivoEntrada = "datosPeor" + evaluacion + ".txt";
			else
				archivoEntrada = "datos" + evaluacion + ".txt";
			
			break;
			
			
		case 2:
			System.out.printf("Introduzca el archivo en la carpeta del programa...");
			System.out.println();
			System.out.printf("Indique el nombre del archivo:  ");
			archivoEntrada = lectura.next();
			break;

		case 3:
			ClaseCompleta.menu();
			break;
			
			
		default:
			
			System.out.println("No se ha seleccionado una opcion correcta, el programa se cerrará");
			System.exit(0);
			break;
		}
		lectura.close();

		// Inicio del proceso
			String nombreFichero = archivoEntrada;

			try{
				
				///////////////////////////////////////////////////////////////////////
				//////////////////////////// APARTADO A ///////////////////////////////
				///////////////////////////////////////////////////////////////////////
				HashMap<Integer, Integer> hash = leerDatos (archivoEntrada);
				
				comprobarDatos(hash);
				
				int n = comprobar_n(hash);

				double totalManzanas = hash.get(0) / (Math.pow(2, n) * Math.pow(2, n));
				int media = (int) (totalManzanas * FACTOR);

				listaSuperiores = new ArrayList<Manzana> ();
				
				// Pretratamiento GREEDY
				
				listaGreedy = pretratarGreedy(hash, n);
				Collections.sort(listaGreedy);
				
				// Medida del método GREEDY_A
				long tiempoAntes = System.nanoTime();
				calcularSuperiores (media);
				long tiempoDespues = System.nanoTime();
				long tiempo = tiempoDespues - tiempoAntes;
				

				nombreFichero = "datosSalidaGreedyA.txt";

				guardarSuperiores (listaSuperiores, nombreFichero, media, tiempo);
				
				
				///////////////////////////////////////////////////////////////////////
				//////////////////////////// APARTADO B ///////////////////////////////
				///////////////////////////////////////////////////////////////////////
				
				
				// Creamos una lista con todos los grupos
				crearListaGrupos();
				
				// Medida del método GREEDY_B
				tiempoAntes = System.nanoTime();
				GreedyB ();
				tiempoDespues = System.nanoTime();
				tiempo = tiempoDespues - tiempoAntes;
				System.out.println("mongolo");
				nombreFichero = "datosSalidaGreedyB.txt";			
				
				guardarGruposTXT (listaGrupos, nombreFichero, tiempo);

				Grupo.guardarGrupoObject(listaGrupos);//Guardamos los objetos para la práctica 3
				
				System.out.println("La ejecución ha finalizado satisfactoriamente.");
				System.out.println("Se ha generado un archivo con los datos calculados: datosSalidaGreedyA.txt");
				System.out.println("Se ha generado un archivo con los grupos calculados: datosSalidaGreedyB.txt");
				System.out.println("Se recomienda abrir el documento con un editor diferente al bloc de notas para una correcta visualización de los datos.");
			}
			catch (RuntimeException e) {
				System.out.println(nombreFichero+" --> "
						+e.getMessage());
			}
			
	}
	
	/**
	 * Antes de lanzar el Greedy hacemos un pretratamiento de los datos. Se trata
	 * de llenar una estructura de datos LinkedList con todas las manzanas,
	 * de paso hacemos el calculo de la dirección (calle y avenida) de cada manzana,
	 * de lo que se encarga el metodo calcularCalleAvenida(), una vez esta la lista
	 * llena, invocamos la ordenacion sort() de la coleccion, de forma que se ordenen
	 * de mayor a menor absentismo.
	 * @param HashMap<Integer, Integer> hash
	 * @param int n
	 * @return lista, colección ordenada de mayor a menor (absentismo)
	 */
	private static LinkedList<Manzana> pretratarGreedy(HashMap<Integer, Integer> hash, int n) {
		LinkedList<Manzana> lista = new LinkedList<Manzana>();
		// Nos quedaremos solo con las manzanas, descartaremos los distritos.
		int pos = (int) (hash.size() - Math.pow(4, n));
		// Agregamos las manzanas a una lista y las ordenamos por absentismo 
		for (int i = pos; i < hash.size(); i++) {
			Manzana nueva = new Manzana(i, hash.get(i));
			nueva.calcularCalleAvenida(pos, n);
			lista.add(nueva);
		}
		// Ordenamos la lista de mayor a menor
		Collections.sort(lista);// Se ordena de + a - de esta manera ya que el CompareTo de manzana 
		                        // se puso al reves a posta para esta situación.
		return lista;
	}


	/**
	 * Método para comprobar el tamaño del problema según la cantidad de posiciones
	 * leidas en el fichero, estas han sido añadidas a un hashmap, de forma que,
	 * si el tamaño del mapa es multiplo de 4, los datos son correctos y se devuelve
	 * un entero n con el tamaño del problema 
	 * @param HashMap<Integer, Integer> hash
	 * @return n tamaño de las manzanas
	 */
	private static int comprobar_n(HashMap<Integer, Integer> hash) {
		int n = 0;
		int suma = 0;
		for (int i = 0; i < hash.size(); i++) {
			suma += Math.pow(4, i);
			if(suma == hash.size())
				n = i;
		}
		return n;
	}

	/**
	 * Método para comprobar que el tamaño de manzanas es adecuado. La comprobación
	 * viene dada por la fórmula Math.pow(4, n), de manera que el número de manzanas
	 * siempre debe ser múltiplo de 4, pero nunca mayor que el tamaño delpropio mapa.
	 * Además, se comprueba tambien que el absentismo de un distrito sea la suma de
	 * sus 4 hijos
	 * @param HashMap<Integer, Integer> hash
	 */
	private static void comprobarDatos(HashMap<Integer, Integer> hash) {
		int numero = (int) Math.pow(4, 0);
		int n = 1;
		while (numero < hash.size()) {
			numero = numero + (int) Math.pow(4, n);
			n++;
		}
		if (numero > hash.size())
			throw new RuntimeException ("Numero de elementos erroneo");
		int numDistritos = numero - (int) Math.pow(4, n-1);
		for (int i=0; i<numDistritos; i++) {
			if (hash.get(i) != hash.get(i*4+1) + hash.get(i*4+2) +
					hash.get(i*4+3) + hash.get(i*4+4))
				throw new RuntimeException ("Suma de absentismo erronea");
		}
	}


	/**
	 * Método que lee los datos de absentismo de un fichero y rellena el hash.
	 * @param nombreFichero fichero del que leemos los datos
	 * @return datos datos leidos
	 */
	public static HashMap<Integer, Integer> leerDatos (String nombreFichero) {
		HashMap<Integer, Integer> datos = null;
		try {
			Scanner entrada = new Scanner (new File (nombreFichero));
			// Calculamos la posición de la primera manzana.
			int pos = 0;
			// Inicializamos el Hash.
			datos = new HashMap<Integer, Integer>();
			while(entrada.hasNextLine()){
				int abse = Integer.parseInt(entrada.nextLine());
				datos.put(pos, abse);
				pos++;
			}
			entrada.close();
		}
		catch (IOException e) {
			System.out.println("Error de lectura del fichero "+nombreFichero);
		}
		return datos;
	}
	
	/**
	 * <p><strong>Método GREEDY; núcleo de la práctica 2-A.</strong></p>
	 * <p>Con el pretratamiento realizado,
	 * el greedy se basa en ir sacando manzanas (ordenadas por absentismo) de la
	 * listaGreedy, y con ellas ir rellenando la listaSuperiores, cuando detecta
	 * que el absentismo de una manzana es menor o igual que la media (no supera
	 * la media), el algoritmo rompe la ejecución, puesto que al estar ordenada,
	 * todas las manzanas a partir de aqui tendran un abesentismo menor o igual
	 * al de aquella que rompe la ejecución.</p>
	 * <p>no necesita pasarle nada más que la media como parametro, ya que tanto la
	 * listaGreedy como la listaSuperiores son propiedades de la propia clase.</p>
	 * @param media, absentismo a superar.
	 */
	public static void calcularSuperiores (int media) {
		listaSuperiores.clear();
		boolean mayor = true;
		while (mayor) {
			if(listaGreedy.getFirst().getAbse() <= media)
				// mayor = false;
				break;
			listaSuperiores.add(listaGreedy.getFirst());
			listaGreedy.removeFirst();
		}
	}
	
	/**
	 * Método que devuelve la posición de la primera manzana, como en la estructura
	 * de datos estan ocupando las primeras posiciones los distritos, la primera
	 * manzana se encuentra en teniendo en cuenta la formula pos + (int)Math.pow(4, i)
	 * siendo i el tamaño del problema que se incrementa en cada vuelta del bucle.
	 * @param n tamaño del problema
	 * @return pos posicion de la primera manzana
	 */
	public static int posicionPrimeraManzana(int n){
		int pos = 0;
		for (int i=0; i<n; i++)
			pos = pos + (int)Math.pow(4, i);
		return pos;
	}
	
	/**
	 * Método que guarda en un fichero la lista de manzanas que superan la media 
	 * de absentismo. no devuelve nada ya que usa los atributos de la clase.
	 * @param listaSuperiores lista de manzanas
	 * @param nombreFichero fichero en el que escribimos
	 * @param media media de absentismo
	 * @param tiempo tiempo estimado para el algoritmo
	 */
	public static void guardarSuperiores (ArrayList<Manzana> listaSuperiores, String nombreFichero, int media, double tiempo) {
		try {
			// CONSOLA
			System.out.println();
			System.out.println("SALIDA PRÁCTICA 2-A");
			System.out.println("--------------------------------------------------------------------------------------------------------");
			System.out.println();
			System.out.println("Hay un total de " + listaSuperiores.size() + " manzanas que superan la media de absentismo:");
			System.out.println(" \n");
			System.out.println("La media es: " + media + ". ");
			System.out.println(" \n");
			System.out.println(" Manzana -->   Pos (Avnd, Calle) --> Absentismo");
			System.out.println(" -------------------------------------------------");
			for (Manzana man : listaSuperiores) {
				
				System.out.println(man);
			}
			System.out.println("\n\nEl tiempo estimado para el algoritmo es de " + tiempo + " nanosegundos.");
			
			// ARCHIVO
			PrintWriter f = new PrintWriter (new FileWriter (nombreFichero));
			
			f.write("Hay un total de " + listaSuperiores.size() + " manzanas que superan la media de absentismo:\n");
			f.write(" \n");
			f.write("La media es: " + media + ". \n");
			f.write(" \n");
			f.write(" Manzana -->   Pos (Avnd, Calle) --> Absentismo\n");
			f.write(" -------------------------------------------------\n");
			for (Manzana man : listaSuperiores) {
				
				f.write(man + "\n");
			}
			f.write("\n\nEl tiempo estimado para el algoritmo es de " + tiempo + " nanosegundos.");
			
			f.close();
		}
		catch (IOException e) {
			System.out.println("Error en la escritura del fichero");
		}
	}
	
	/**
	 * <p><strong>Método GREEDY; núcleo de la práctica 2-B.</strong></p>
	 * <p>Se trata de un algoritmo algo mas
	 * elaborado que el del apartado A. Inicia un bucle while mientras que la lista
	 * pretratada de manzanas no este vacía. lo primero que hace es coger el primer
	 * objeto de la coleccion en la variable m. Dentro del bucle se comprueba se el
	 * absentismo es 0 retorna el método, puesto que la coleccion esta ordenada
	 * (igual que en el A), despues define un entero muy grande "min" que ira acercando
	 * la distancia de la manzana al punto crítico. se crea un segundo bucle del
	 * tamaño de la lista de grupos, este recorre todos los grupos y va comprobando
	 * cual es el que presenta una menor distancia desde "m" al punto crítico del
	 * grupo i (ordinal que corresponde al número de vuelta), si la distancia
	 * es menor que el numero grande, asignamos la distancia al entero "min",
	 * por último "posMin" guarda el ordinal del grupo al que pertenecerá la manzana</p>
	 * <p>El método no retorna nada ya que trabaja con los atributos de la clase</p>
	 * 
	 */
	public static void GreedyB() {
		while ( ! listaGreedy.isEmpty()) {			// mientras no este vacia
			Manzana m = listaGreedy.getFirst();		// m = primer elemento (seleccion)
			//busco la minima distancia
			if(m.getAbse() == 0)					// como estan ordenadas
				return;								// fin del método
			double min = Double.MAX_VALUE;				// defino un número mu grande
			int posMin = -1;							// 
			for (int i=0; i<listaGrupos.size(); i++) {	// 
				double dis = listaGrupos.get(i).distanciaPuntoCritico (m, S, W);
				if (dis < min) {
					min = dis;
					posMin = i;
				}
			}
			listaGreedy.removeFirst();
			listaGrupos.get(posMin).addManzana (m);
		}
	}
	
	/**
	 * Método que crea un grupo con cada manzana que supera el absentismo,
	 * y la añade a la coleccion de grupos listaGrupos (atributo de la clase).
	 * 
	 */
	private static void crearListaGrupos() {
		for (int i = 0; i < listaSuperiores.size(); i++) {
			Grupo grupo = new Grupo(i+1, listaSuperiores.get(i));
			listaGrupos.add(grupo);
		}
	}


	private static void guardarGruposTXT(ArrayList<Grupo> lista, String archivo, double tiempo) {
		
		try {
			// CONSOLA
			System.out.println();
			System.out.println("SALIDA PRÁCTICA 2-B");
			System.out.println("--------------------------------------------------------------------------------------------------------");
			System.out.println();
			System.out.println("Hay un total de " + lista.size() + " grupos:");
			System.out.println();
			System.out.println();
			System.out.println("--------------------------------------------------------------------------------------------------------");
			
			for (Grupo group : lista) {
				System.out.println(group.toString());
				System.out.println("Total Manzanas --> " + (group.totalMan()+1));
				System.out.println("   Total Absentismo --> " + group.totalAbs() + "\n");
				System.out.println("--------------------------------------------------------------------------------------------------------");
				
			}
			
			//ARCHIVO
			PrintWriter f = new PrintWriter (new FileWriter (archivo));
			
			f.write("Hay un total de " + lista.size() + " grupos:\n");
			f.write(" \n");
			f.write(" \n");
			f.write("--------------------------------------------------------------------------------------------------------\n");
			
			for (Grupo group : lista) {
				f.write(group.toString() + "\n");
				f.write("Total Manzanas --> " + (group.totalMan()+1));
				f.write("   Total Absentismo --> " + group.totalAbs() + "\n\n");
				f.write("--------------------------------------------------------------------------------------------------------\n");
				
			}
			f.close();
		}
		catch (IOException e) {
			System.out.println("Error en la escritura del fichero");
		}
	}
}
