package PracticasUnificadas;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Programa {

	public static final double FACTOR = 1.7;
	public static LinkedList<Manzana> listaSuperiores;


	/**
	 * Método main
	 * @param args
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
		String archivoEntrada = "";
		
		
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
		

		// Inicio del proceso
		                        //Introducir nombre del fichero aquí
			String nombreFichero = archivoEntrada;
			String nombreCompleto = directorio + nombreFichero;

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




	private static void calcularAvndaCalle(int n) {
		
		int pos = posicionPrimeraManzana(n);
		for (Manzana manzana : listaSuperiores) {
			manzana.calcularCalleAvenida(pos, n);
		}
		
	}




	/**
	 * Método que lee los datos de absentismo de un fichero y rellena el hash.
	 * @param nombreFichero
	 * @return HashMap<Integer, Integer> datos
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
	 * Método DIVIDE Y VENCERÁS, calcula las manzanas con un absentismo superios a la media
	 * y las guarda en una lista.
	 * @param hash
	 * @param posicion
	 * @param media
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
		// variable, para posteriomente no tener que repetir la posicion
	}






	/**
	 * Método que devuelve la posición de la primera manzana.
	 * @param n
	 * @return int pos
	 */
	public static int posicionPrimeraManzana(int n){

		int pos = 0;
		for (int i=0; i<n; i++)
			pos = pos + (int)Math.pow(4, i);
		return pos;
	}


	/**
	 * Método que guarda en un fichero la lista de manzanas que superan la media de absentismo.
	 * @param listaSuperiores
	 * @param nombreFichero
	 */
	public static void guardarSuperiores (String nombreFichero, int media, long tiempo) {

		
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
