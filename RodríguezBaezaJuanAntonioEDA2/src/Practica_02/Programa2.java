package Practica_02;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;



public class Programa2 {

	public static final double FACTOR = 1.7;
	public static ArrayList<Manzana> listaSuperiores;
	public static final double S = 20;
	public static final double W = 12;
	public static LinkedList<Manzana> listaGreedy;
	public static ArrayList<Grupo> listaGrupos = new ArrayList<Grupo>();

	
	/**
	 * Método main
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner lectura = new Scanner(System.in);
		
		
		String directorio = System.getProperty("user.dir" + File.separator);
		
		System.out.println("      Práctica 02: GREEDY");
		System.out.println("-------------------------------------------");
		System.out.println("1. GENERAR DATOS ALEATORIAMENTE");
		System.out.println("2. ESCOGER ARCHIVO DE DATOS MANUALMENTE");
		System.out.println("3. SALIR");
		System.out.println();
		System.out.printf("Introduzca la acción deseada... ");
		
		int opcion = lectura.nextInt();
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
			System.out.println("Adios.");
			System.exit(0);
			break;
			
			
		default:
			
			System.out.println("No se ha seleccionado una opcion correcta, el programa se cerrará");
			System.exit(0);
			break;
		}

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
				
				//System.out.println(listaSuperiores.toString());

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
				
				nombreFichero = "datosSalidaGreedyB.txt";			
				
				guardarGruposTXT (listaGrupos, nombreFichero, tiempo);

				Grupo.guardarGrupoObject(listaGrupos);//Guardamos los objetos para la práctica 3
				
				
				System.out.println("La ejecución ha finalizado satisfactoriamente.");
				System.out.println("Se ha generado un archivo con los datos calculados: " + nombreFichero);
				
				System.out.println("Se ha generado un archivo con los grupos calculados: " + nombreFichero);
				System.out.println("Se recomienda abrir el documento con un editor diferente al bloc de notas para una correcta visualización de los datos.");
			}
			catch (RuntimeException e) {
				System.out.println(nombreFichero+" --> "
						+e.getMessage());
			}
	}
	

	/**
	 * Método que pretrata los candidatos antes de usar el algoritmo Greedy
	 * @param hash
	 * @param n
	 * @return
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
	 * Método GREEDY, calcula las manzanas con un absentismo superios a la media
	 * y las guarda en una lista.
	 * @param lista
	 * @param media
	 */
	public static void calcularSuperiores (int media) {
		
		listaSuperiores.clear();
		boolean mayor = true;
		
		
		System.out.println(listaGreedy.toString());
		
		while (mayor) {
			if(listaGreedy.getFirst().getAbse() <= media)
				break;
			
			listaSuperiores.add(listaGreedy.getFirst());
			listaGreedy.removeFirst();
		}
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
	public static void guardarSuperiores (ArrayList<Manzana> listaSuperiores, String nombreFichero, int media, double tiempo) {

		try {
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


	private static void GreedyB() {
		
		while ( ! listaGreedy.isEmpty()) {
			Manzana m = listaGreedy.getFirst();
			//busco la minima distancia
			if(m.getAbse() == 0)
				return;
			double min = Double.MAX_VALUE;
			int posMin = -1;
			for (int i=0; i<listaGrupos.size(); i++) {
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


	private static void crearListaGrupos() {
	
		for (int i = 0; i < listaSuperiores.size(); i++) {
			Grupo grupo = new Grupo(i+1, listaSuperiores.get(i));
			listaGrupos.add(grupo);
		}
	}


	private static void guardarGruposTXT(ArrayList<Grupo> lista, String archivo, double tiempo) {
		
		try {
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
