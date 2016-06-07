package PracticasUnificadas;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
//import java.util.List;
import java.util.Scanner;

/**
 * Clase Programa
 * @author Grupo Iluminati
 * @version 1.2
 */
public class Programa {
	
	/**
	 * FACTOR: factor para calcular la media de absentismo
	 * listaSuperiores: lista enlazada de las manzanas
	 */
	public static final double FACTOR = 1.7;
	public static LinkedList<Manzana> listaSuperiores;


	/*
	 * ejecutar para la practica 1
	 */
	public static void ejecutar1() {
		
		Scanner lectura = new Scanner(System.in);
		
		
		String directorio = System.getProperty("user.dir" + File.separator);
		
		System.out.println("      Práctica 01: DIVIDE Y VENCERÁS");
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
		String archivoEntrada = "datos" + File.separator;
		
		
		// MENU CONSOLA
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
				archivoEntrada += "datosMejor" + evaluacion + ".txt";
			else if(caso == 2)
				archivoEntrada += "datosPeor" + evaluacion + ".txt";
			else
				archivoEntrada += "datos" + evaluacion + ".txt";
			
			break;
			
			
		case 2:
			System.out.printf("Introduzca el archivo en la carpeta del programa DATOS");
			System.out.println();
			System.out.printf("Indique el nombre del archivo:  ");
			archivoEntrada = "datos" + File.separator + lectura.next();
			break;

		case 3:
			ClaseCompleta.menu();
			break;
			
			
		default:
			
			System.out.println("No se ha seleccionado una opcion correcta, el programa se cerrará");
			System.exit(0);
			break;
		}
		

		// Inicio del proceso
		                        
			String nombreFichero = archivoEntrada;

			try{
				HashMap<Integer, Integer> hash = leerDatos (nombreFichero);

				comprobarDatos(hash);

				int n = comprobar_n(hash);

				double totalManzanas = hash.get(0) / (Math.pow(2, n) * Math.pow(2, n));
				int media = (int) (totalManzanas * FACTOR);

				listaSuperiores = new LinkedList<Manzana> ();

				// Medida del método DIVIDE Y VENCERÁS
				long tiempoAntes = System.nanoTime();
				calcularSuperiores (hash, 0, media);
				long tiempoDespues = System.nanoTime();
				long tiempo = tiempoDespues - tiempoAntes;

				nombreFichero = "datosSalida.txt";
				
				Collections.sort(listaSuperiores);
				
				calcularAvndaCalle(n);
				
				guardarSuperiores (nombreFichero, media, tiempo);
				
				System.out.println("La ejecución ha finalizado satisfactoriamente.");
				System.out.println("Se ha generado un archivo con los datos calculados: " + nombreFichero);
				System.out.println("Se recomienda abrir el documento con un editor diferente al bloc de notas para una correcta visualización de los datos.");

			}
			catch (RuntimeException e) {
				System.out.println(nombreFichero+" --> "
						+e.getMessage());
			}
	}
	
	/**
	 * Método para la direccion de cada manzana, llama al método 
	 * calcularCalleAvenida(pos, n) de la clase manzana que es el 
	 * que realmente realiza el calculo
	 * @param n
	 */
	private static void calcularAvndaCalle(int n) {
		
		int pos = posicionPrimeraManzana(n);
		for (Manzana manzana : listaSuperiores) {
			manzana.calcularCalleAvenida(pos, n);
		}
		
	}

	/**
	 * Método que lee los datos de absentismo de un fichero y rellena el hash.
	 * @param nombreFichero fichero desde el cual leemos los datos
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
	 * <p><strong>Método DIVIDE Y VENCERÁS; núcleo de la primera práctica 1.</strong></p>
	 * <p>Contiene dos casos bases:</p>
	 *	<p>	1) 	Si el absentismo no supera la media el return nos envía
	 *			al final del método.</p>
	 *	<p>	2)	Si la posicion en la que estamos no tiene hijos, significa
	 *			que estamos en una manzana (último nivel), y que no haya
	 *			entrado en el caso 1 nos asegura que se trata de una manzana
	 *			que supera el media de absentismo, por lo que la añadimos
	 *			a listaSuperiores, la lista de manzanas que superan la
	 *			media. El return nos saca del método.</p>
	 * <p>Posteriormente, si no pasamos por ninguno de los casos base, significa
	 * estamos en un distrito divisible, y que supera la media de absentismo
	 * (la suma de los absentismos de sus hijos) por lo que realizamos 4
	 * llamadas recursivas a los hijos de la posicion actual.</p>
	 * @param hash estructura con todas las manzanas.
	 * @param posicion, posición actual de división.
	 * @param media, media de absentismo a superar.
	 */
	public static void calcularSuperiores (HashMap<Integer, Integer>hash, int posicion, double media) {

		// Caso base 1, si el absentismo no supera la media, no entramos.
		if (hash.get(posicion) <= media)//
			return;

		// Caso base 2, si no existe hijo de la posición en la que estamos, quiere
		// decir que estamos en una manzana y el absentismo es mayor que la media.
		if (hash.get(posicion*4 + 1) == null) {

			// Añadimos la manzana a la lista de superiores.
			Manzana nueva = new Manzana (posicion, hash.get(posicion));
			listaSuperiores.add(nueva);
			return;
		}

		// Llamamos recursivamente a los hijos de la posicion actual.
		calcularSuperiores (hash, posicion*4+1, media);
		calcularSuperiores (hash, posicion*4+2, media);
		calcularSuperiores (hash, posicion*4+3, media);
		calcularSuperiores (hash, posicion*4+4, media);

		// Una posible mejora seria calcular la operacion "posicion*4" y asignarsela a una
		// variable, para posteriomente no tener que repetir la operación
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
	 * de absentismo. no devuelve nada ya que usa los atributos de la calse.
	 * @param nombreFichero fichero en el que escribimos
	 * @param media media de absentismo
	 * @param tiempo tiempo estimado para el algoritmo
	 */
	public static void guardarSuperiores (String nombreFichero, int media, long tiempo) {

		
		System.out.println("Hay un total de " + listaSuperiores.size() + " manzanas que superan la media de absentismo:\n");
		System.out.println(" \n");
		System.out.println("La media es: " + media + ". \n");
		System.out.println(" \n");
		System.out.println(" Manzana    Pos (Avnd, Calle) --> Absentismo\n");
		System.out.println(" -------------------------------------------------\n");
		for (Manzana man : listaSuperiores) {

			System.out.println(man + "\n");
		}
		System.out.println("\n\nEl tiempo estimado para el algoritmo es de " + tiempo + " nanosegundos.");
		
		
		
		try {
			
			
			PrintWriter f = new PrintWriter (new FileWriter (nombreFichero));
			

			f.write("Hay un total de " + listaSuperiores.size() + " manzanas que superan la media de absentismo:\n");
			f.write(" \n");
			f.write("La media es: " + media + ". \n");
			f.write(" \n");
			f.write(" Manzana    Pos (Avnd, Calle) --> Absentismo\n");
			f.write(" -------------------------------------------------\n");
			for (Manzana man : listaSuperiores) {

				f.write(man + "\n");
			}
			f.write("\n\nEl tiempo estimado para el algoritmo es de " + tiempo + " nanosegundos.");

			f.close();
		}
		catch (IOException e) {
			System.out.println("Error en la escritura del fichero.");
		}
	}

	/**
	 * Método para comprobar el tamaño del problema según la cantidad de posiciones
	 * leidas en el fichero, estas han sido añadidas a un hashmap, de forma que,
	 * si el tamaño del mapa es multiplo de 4, los datos son correctos y se devuelve
	 * un entero n con el tamaño del problema 
	 * @param hash
	 * @return n tamaño de las manzanas
	 */
	private static int comprobar_n(HashMap<Integer, Integer> hash) {
		// TODO Auto-generated method stub

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
	 * @param hash
	 */
	private static void comprobarDatos(HashMap<Integer, Integer> hash) {
		int numero = (int) Math.pow(4, 0);
		int n = 1;
		while (numero < hash.size()) {
			numero = numero + (int) Math.pow(4, n);
			n++;
		}
		if (numero > hash.size())
			throw new RuntimeException ("Numero de elementos erroneo.");
		int numDistritos = numero - (int) Math.pow(4, n-1);
		for (int i=0; i<numDistritos; i++) {
			if (hash.get(i) != hash.get(i*4+1) + hash.get(i*4+2) +
					hash.get(i*4+3) + hash.get(i*4+4))
				throw new RuntimeException ("Suma de absentismo erronea.");
		}
	}
}
