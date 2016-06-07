package PracticasUnificadas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class Programa3 {

	public static ArrayList<Grupo> grupos = new ArrayList<Grupo>();
	public static ArrayList<Propuesta> listaPropuestas;
	public static ArrayList<Propuesta> listaPropuestasAceptadas;
	public static double[][] B;

	public static void ejecutar3() {

		Scanner lectura = new Scanner(System.in);


		System.out.println("      Práctica 03: PROGRAMACIÓN DINÁMICA");
		System.out.println("-------------------------------------------");
		System.out.println("1. ESCOGER ARCHIVO GENERADO EN LA PRÁCTICA 2-B");
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



		switch (opcion) {
		case 1:
			try {
				grupos = Grupo.leerGrupoObject();
				
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			ejecucion(grupos);
			break;

		case 2:
			Scanner s = new Scanner(System.in);
			System.out.println("Asegurese de que el archivo está situado en la carpeta del programa Datos");
			System.out.print("Introduzca el nombre del archivo (con extension): ");
			String nombreArchivo = s.nextLine();
			String file =  "datos" + File.separator + nombreArchivo;
			grupos = leerGruposP3(file);
			ejecucion(grupos);
			break;

		default:
			break;
		}
	}
	
	private static void ejecucion (ArrayList<Grupo> grupos){
		
		
		generarTablasPropuestas(grupos);
		
		
		// Generamos el importe del estado aleatoriamente, calculamos mas o menos la mitad de las propuestas
		// que son las que nos quedaremos, con un valor de entre 1 y 100
		int importeEstado = (int)(listaPropuestas.size() / 2 * (Math.random()*100 +1));
		
		// El NADOE nos da un remanente de entre 20 y 30
		int importeNadoe = (int)(Math.random()*10+20);
		
		// Marcamos el peso de la mochila
		int mochilaSize = importeEstado + importeNadoe;
		
		// Ordenamos la lista por presupuesto, de menor a mayor		
		Collections.sort(listaPropuestas);
		
		long tiempoAntes = System.nanoTime();
		B = knapsack(listaPropuestas, mochilaSize);
		long tiempoDespues = System.nanoTime();
		
		long tiempo = tiempoDespues - tiempoAntes;
		
		listaPropuestasAceptadas = new ArrayList<Propuesta>();
		test (listaPropuestas.size(), mochilaSize);
		
		System.out.println("Estado: " + importeEstado + "    NADOE: " + importeNadoe);
		
		guardarPropuestasAceptadas(mochilaSize);
	}



	private static double[][] knapsack(ArrayList<Propuesta> lista, int mochilaSize) {
		
		// Tendra tantas filas como propuestas halla (incluyendo el 0)
		int filas = lista.size()+1;
		
		// Tendra tantas columnas como tamaños de mochila posibles (incluyendo el 0)
		int columnas = mochilaSize + 1;
		
		B = new double [filas][columnas];
		
		// Ponemos toda la primera fila a 0, ya que si no metemos ningun elemento, el beneficio es 0
		for (int w = 0; w < columnas; w++)
			B[0][w] = 0;
		
		for (int k = 1; k < filas; k++) {
			
			// Cogemos la primera propuesta de la lista (menor presupuesto)
			Propuesta prop = lista.get(k-1);
			
			// Si el presupuesto(peso) es mayor que el tamaño de la mochila (w) no insertamos el elemento en la mochila
			for (int w = 0; w < prop.getPresupuesto() && w < columnas; w++) {
				B[k][w] = B[k-1][w];
			}
			
			// Nos situamos en la mochila de peso igual al peso del elemento (presupuesto) ya que en las mochilas
			// de menor peso no entraría
			for (int w = prop.getPresupuesto(); w < columnas; w++) {
				
				// Nos quedamos con el maximo de [beneficio de la fila anterior , columna actual - peso de elemento] + benef de 
				// elemento O  [beneficio actual sin incluir el elemento]
				B[k][w] = Math.max(B[k-1][w-prop.getPresupuesto()] + prop.getBeneficio(), B[k-1][w]);
			}
		}
		
		return B;
	}
	
	private static void test(int j, int c) {
		
		if (j > 0) {
			
			Propuesta p = listaPropuestas.get(j-1);
			
			if (c < p.getPresupuesto()) {
				
				test (j-1, c);
			}
			else {
				
				if (B[j-1][c-p.getPresupuesto()] + p.getBeneficio() > B[j-1][c]) {
					
					test (j-1, c-p.getPresupuesto());
					
					listaPropuestasAceptadas.add(p);
				}
				else 
					test (j-1, c);
			}
		}
	}

	private static void guardarPropuestasAceptadas(int mochilaSize) {
		
		try {
			PrintWriter f = new PrintWriter (new FileWriter (new File ("datosSalidaProgramacionDinamica.txt")));
			
			f.println("El presupuesto total es de: " + mochilaSize + "\n");
			
			System.out.println("El presupuesto total es de: " + mochilaSize + "\n");
			
			f.println("\tNº Propuesta\tNº Grupo\tPresupuesto\tPrioridad\tImpacto\t       Beneficio");
			
			System.out.println("\tNº Propuesta\tNº Grupo\tPresupuesto\tPrioridad\tImpacto\t       Beneficio");
			
			f.println("-----------------------------------------------------------------------------------------------------------");
			
			System.out.println("-----------------------------------------------------------------------------------------------------------");
			
			for (Propuesta p : listaPropuestasAceptadas) {
				
				f.println(p);
				
				System.out.println(p);
			}
			f.close();
		}
		catch (IOException e) {	
		}
		
		
	}

	private static void generarTablasPropuestas (ArrayList<Grupo> listaTrabajo) {
		
		String file = "propuestas.txt";
		int totalSuperficie = 0;
		int numPro = 1;
		listaPropuestas = new ArrayList<Propuesta>();
		for (Grupo g : listaTrabajo) {
			totalSuperficie = totalSuperficie + g.totalMan();
		}
		for (Grupo g : listaTrabajo) {
			//numero de propuestas aleatorio entre 1 y 3
			int numPropuestas = (int) (Math.random()*3+1);
			for (int i=1; i<=numPropuestas; i++) {
				//genero aleatoriamente el presupuesto que va a ser
				//un numero entre 10 y 100
				int presupuesto = (int) (Math.random()*91 + 10);
				//la prioridad seria el numero de propuesta del 
				//grupo actual
				int prioridad = i;
				//genero el impacto como un numero entre 1 y 10
				int impacto = (int) (Math.random() * 10 + 1);
				Propuesta p = new Propuesta (numPro, g.getNumero(),
						presupuesto, prioridad, impacto, g);
				p.calcularBeneficio(g, totalSuperficie);
				listaPropuestas.add(p);
				numPro++;
			}
		}
		//luego hago un shuffle y cambio los numeros de propuestas
		//y asi no tengo todas las propuestas del mismo grupo con
		//numeros consecutivos
		Collections.shuffle(listaPropuestas);
		for (int i=0; i<listaPropuestas.size(); i++) {
			listaPropuestas.get(i).setNumPropuesta(i+1);
		}
		/////////////////////////////
		//ahora tengo que crear el archivo con los datos de las 
		//propuestas que tenemos en la lista de propuestas
		try {
			PrintWriter f = new PrintWriter (new FileWriter (file));
			for (Propuesta p : listaPropuestas) {
				f.println(p);
			}
			f.close();
		}
		catch (IOException e) {
			System.out.println("Error de escritura en fichero " + file);
		}
	}



	private static ArrayList<Grupo> leerGruposP3 (String nombreFichero) {
		ArrayList<Grupo> lista = null;
		try {
			Scanner f = new Scanner (new File (nombreFichero));
			//primera linea tiene el numero de grupos
			int numGrupos = Integer.parseInt(f.nextLine());
			//System.out.println("numgrupos = "+numGrupos);
			lista = new ArrayList<Grupo>(numGrupos);
			//la segunda linea no tiene informacion valida
			f.nextLine();
			//ahora tengo que leer el resto de lineas
			for (int i=0; i<numGrupos; i++) {
				String linea = f.nextLine();
				//System.out.println("he leido: "+linea);
				//tengo que transforar la linea en grupo de trabajo
				Grupo grupo = transformarLinea (linea);
				lista.add(grupo);
				//System.out.println("meto: "+grupo.getNumero());
			}
			f.close();
		}
		catch (IOException e) {
			System.out.println("Error de lectura del fichero "+nombreFichero);
		}
		return lista;

	}

	private static Grupo transformarLinea(String linea) {
		Scanner sc = new Scanner (linea);
		sc.useDelimiter(",");
		//primer trozo numero de grupo
		int numero = Integer.parseInt(sc.next());
		//System.out.println("numero: "+numero);
		//segundo trozo punto critico
		String cadenaCritica = sc.next();
		//System.out.println("cadena critica: "+cadenaCritica);
		Manzana puntoCritico = new Manzana (cadenaCritica);
		//System.out.println("Punto Critico: "+puntoCritico);
		Grupo grupo = new Grupo(numero, puntoCritico);
		//siguientes trozos manzanas del grupo
		while (sc.hasNext()) {
			String cadena = sc.next();
			if (cadena.startsWith("[") || cadena.startsWith(" "))
				cadena = cadena.substring(1);
			if (cadena.endsWith("]"))
				cadena = cadena.substring(0, cadena.length()-1);
			if ( ! cadena.equals("")) {
				Manzana m = new Manzana (cadena);
				grupo.addManzana(m);
			}
		}
		return grupo;
	}
}

