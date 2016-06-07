package PracticasUnificadas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Programa4 {

	public static ArrayList<Grupo> grupos = new ArrayList<Grupo>();
	public static ArrayList<Propuesta> listaPropuestas;
	public static ArrayList<Propuesta> listaPropuestasAceptadas;
	public static ArrayList<Integer> SOA;
	
	public static ArrayList<RecursosPropuestas> listaPropuestas_recursos;
	public static ArrayList<Solucion> SOA2;
	public static ArrayList<RecursosPropuestas> listaPlanificacion;
	
	
	public static int numtrabajadoresSociales = (int)(Math.random()*10 + 10);
	public static int psicologosInfantiles = (int)(Math.random()*10 + 10);
	public static int autobusesEscolares = (int)(Math.random()*10 + 10);
	public static int centro = 1;
	
	
	
	public static void ejecutar4(){
		
		Scanner lectura = new Scanner(System.in);


		System.out.println("      Práctica 04: BACKTRACKING");
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
			ejecucionA(grupos);
			break;

		case 2:
			Scanner s = new Scanner(System.in);
			System.out.println("Asegurese de que el archivo está situado en la carpeta del programa Datos");
			System.out.print("Introduzca el nombre del archivo (con extension): ");
			String nombreArchivo = s.nextLine();
			String file =  "datos" + File.separator + nombreArchivo;
			grupos = leerGruposP4(file);
			ejecucionA(grupos);
			break;

		default:
			break;
		}
		
		lectura.close();
	}
	
	private static void ejecucionA (ArrayList<Grupo> grupos){
		
		////////////////////////////////////////////////////////
		////////////////////  APARTADO A  //////////////////////
		////////////////////////////////////////////////////////
		
		generarTablasPropuestas(grupos);
		
		
		// Generamos el importe del estado aleatoriamente, calculamos mas o menos la mitad de las propuestas
		// que son las que nos quedaremos, con un valor de entre 1 y 100
		int importeEstado = (int)(listaPropuestas.size() / 2 * (Math.random()*100 +1));
		
		// El NADOE nos da un remanente de entre 20 y 30
		int importeNadoe = (int)(Math.random()*10+20);
		
		// Marcamos el peso de la mochila
		int mochilaSize = importeEstado + importeNadoe;
		

		
		long tiempoAntes = System.nanoTime();
		SOA = backtracking(listaPropuestas, mochilaSize);
		long tiempoDespues = System.nanoTime();
		
		long tiempo = tiempoDespues - tiempoAntes;
		
		listaPropuestasAceptadas = sacarSolucion();
		
		System.out.println("                                  APARTADO A");
		System.out.println("-------------------------------------------------------------------------------------------------");
		
		System.out.println("Estado: " + importeEstado + "    NADOE: " + importeNadoe);
		
		guardarPropuestasAceptadas(mochilaSize);
		
		System.out.println("\nEl tiempo del algoritmo es de: " + tiempo);
		
		System.out.println("\n");
		
		
		
		
		////////////////////////////////////////////////////////
		////////////////////  APARTADO B  //////////////////////
		////////////////////////////////////////////////////////
		
		
		listaPropuestas_recursos = crearCriteriosPropuestas();
		
			
		// Ordenamos la lista por tiempo de espera (num de alumnos * duracionPropuesta) de menor a mayor
		//Collections.sort(listaPropuestas_recursos);
		
		
		guardarPropuestasRecursos();
		
		SOA2 = backtracking_tareas(numeroMeses());
		
		Solucion s = calcularMejorSolucion (SOA2);
		
		guardarSolucion (s);
		
		
		System.out.println("                                  APARTADO B");
		System.out.println("-------------------------------------------------------------------------------------------------");
		
		System.out.println("Nº de trabajadores sociales: " + numtrabajadoresSociales + "\tNº psicologos infantiles: " + psicologosInfantiles 
							+ "\tNº autobuses escolares: " + autobusesEscolares + "\n");
		
		System.out.println(s);
		
		

	}
	
	private static void guardarPropuestasRecursos() {
		
		String file =  "datos" + File.separator + "propuestasRecursosTratar.txt";
		
		try {
			
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			
			pw.println("\n");
			
			for (RecursosPropuestas rp : listaPropuestas_recursos) {
				pw.println(rp.toString() + "      Tiempo espera total: " + rp.getTiempoEspera());
			}
			
			pw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static int numeroMeses() {
		
		int suma = 0;
		
		for (RecursosPropuestas rp : listaPropuestas_recursos) {
			suma += rp.getDuracion();
		}
		return suma;
	}
		


	// Metodo BackTracking apartado B
	private static ArrayList<Solucion> backtracking_tareas(int numeroMaxMeses) {

		int nivel = 0;
		
		Solucion s = new Solucion (numeroMaxMeses);
		
		SOA2 = new ArrayList<Solucion>();
		
		planificar (s, nivel, numeroMaxMeses);
		
		return SOA2;
	}
	
	
	private static void planificar(Solucion s, int nivel, int totalMeses) {
		
		if (nivel == listaPropuestas_recursos.size()) {
			SOA2.add(new Solucion (s));
			
			return;
		}
		
		RecursosPropuestas rp = listaPropuestas_recursos.get(nivel);
		
		for (int i  = 0; i <= totalMeses - rp.getDuracion(); i++) {
			
			if (s.comprobarPropuesta (rp, i)) {
				
				RecursosPropuestas copy = new RecursosPropuestas(rp);
				
				copy.setMesComienzo(i);
				
				s.insertarPropuesta(i, copy);
				
				planificar (s, nivel+1, totalMeses);
			}
		}
		
	}




	/*private static ArrayList<Recursos> iniciarSolucion(int totalMeses) {
		ArrayList<Recursos> s = new ArrayList<Recursos>(totalMeses);
		for (int i=0; i < totalMeses; i++) {
			s.set(i, new Recursos (psicologosInfantiles, autobusesEscolares, numtrabajadoresSociales, 1));
		}
		return s;
	}*/
	
	/*private static ArrayList<Recursos> copiarSolucion(ArrayList<Recursos> s) {
		
		ArrayList<Recursos> r = (ArrayList<Recursos>) s.clone();
		
		return r;
	}*/
	

	private static Solucion calcularMejorSolucion (ArrayList<Solucion> soa) {
		
		int min = Integer.MAX_VALUE;
		
		int posMin = -1;
		
		//calculo el peso de cada solucion y lo comparo con el minimo
		for (int i=0; i<soa.size(); i++) {
			
			Solucion s = soa.get(i);
			
			s.calcularPesoTotal(s);
			
			if (s.getPesoTotal() < min) {
				min = s.getPesoTotal();
				posMin = i;
			}
		}
		return soa.get(posMin);
	}

	private static void guardarSolucion (Solucion s) {
		
		try {
			PrintWriter f = new PrintWriter (new FileWriter(new File ("datosSalidaBacktracking_B.txt")));
			f.println(s);
			f.close();
		}
		catch (IOException e) {
			
		}
	}
	
	
	

	private static ArrayList<RecursosPropuestas> crearCriteriosPropuestas() {
		
		ArrayList<RecursosPropuestas> lista = new ArrayList<RecursosPropuestas>();
		
		for (Propuesta propuesta : listaPropuestasAceptadas) {
			RecursosPropuestas rp = new RecursosPropuestas(propuesta);
			lista.add(rp);
		}
			
		return lista;
	}

	private static ArrayList<Propuesta> sacarSolucion() {
		
		ArrayList<Propuesta> propuestasAceptdas = new ArrayList<Propuesta>();
		
		for(int i = 0 ; i < SOA.size(); i++){
			if(SOA.get(i) == 1){
				propuestasAceptdas.add(listaPropuestas.get(i));
			}
		}
		
		return propuestasAceptdas;
	}

	private static ArrayList<Integer> backtracking(ArrayList<Propuesta> listaPropDisp, int mochilaSize) {
		
		int nivel = 0;
		
		// Array solucion actual inicializado con sus elementos a -1
		ArrayList<Integer> s = new ArrayList<Integer>();
		for(int i = 0; i < listaPropDisp.size(); i++){
			s.add(-1);
		}
		
		// Solucion actual ¡¡¡¡FINAL!!!!
		ArrayList<Integer> soa = new ArrayList<Integer>();
		
		// Beneficion optimo actual
		double voa = Double.NEGATIVE_INFINITY; 
		
		// Peso acumulado
		int pact = 0;
		
		// Beneficio acumulado
		double bact = 0;
		
		do{
			// Generar Nivel
			
			s.set(nivel, s.get(nivel)+1);
			
			// Si el cogemos el elemento...
			if(s.get(nivel) == 1){
				
				// Peso acumulado es igual al peso acumulado + el peso del elemento
				pact += listaPropDisp.get(nivel).getPresupuesto();
				
				// Beneficio acumulado es igual al beneficio acumulado + el beneficio del elemento
				bact += listaPropDisp.get(nivel).getBeneficio();
				
			}// Fin Generar
			
			//  |                SI SOLUCION                         |  Y | ben acum > voa|
			if((nivel == listaPropDisp.size()-1 && pact <= mochilaSize) && bact > voa){
				voa = bact;
				soa = new ArrayList<Integer>(s);
			}
			
				// CRITERIO
			else if(nivel < listaPropDisp.size()-1 && pact <= mochilaSize){
				nivel++;
			}
			
			else{
				
				// Mientras NO mas hermanos
				while(s.get(nivel) >= 1){
					
					// Retroceder
					pact -= listaPropDisp.get(nivel).getPresupuesto()*s.get(nivel);
					bact -= listaPropDisp.get(nivel).getBeneficio()*s.get(nivel);
					s.set(nivel, -1);
					nivel -= 1;
					// Fin retroceder	
					
					if(nivel == -1)
						break;
				}
			}
						
		}while(nivel != -1);
		
		
		return soa;
	}


	private static void generarTablasPropuestas (ArrayList<Grupo> listaTrabajo) {
		
		String file = "datos" + File.separator + "propuestas.txt";
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



	private static ArrayList<Grupo> leerGruposP4 (String nombreFichero) {
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
		sc.close();
		return grupo;
	}
	
	private static void guardarPropuestasAceptadas(int mochilaSize) {
		
		try {
			PrintWriter f = new PrintWriter (new FileWriter (new File ("datosSalidaBacktracking_A.txt")));
			
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
	
}
