package PracticasUnificadas;

import java.io.File;
import java.util.Scanner;

/**
 * Clase ClaseCompleta
 * @author Grupo Iluminati
 * @version 1.2
 */
public class ClaseCompleta {
	
	public static void main(String[] args) {
		
		File directorio = new File("datos");
		directorio.mkdirs();
		menu();
	}
	
	
	public static void menu(){
		
		Scanner menu = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------");
		System.out.println("                   MEN� PRINCIPAL");
		System.out.println("------------------------------------------------------");
		System.out.println("1. PR�CTICA 01 - DvY");
		System.out.println("2. PR�CTICA 02 - Greedy");
		System.out.println("3. PR�CTICA 03 - Programaci�n Din�mica");
		System.out.println("4. PR�CTICA 04 - BackTracking");
		System.out.println("5. SALIR");
		
		
		int opcion;
		
		do{
			opcion = menu.nextInt();
			if(opcion < 1 || opcion > 5)
				System.out.println("El numero elegido est� fuera del intervalo permitido, insertelo nuevamente.");
			}
			while(opcion < 1 || opcion > 5);

		
		switch (opcion) {
		case 1:
			Programa.ejecutar1();
			break;
			
		case 2:
			Programa2.ejecutar2();
			break;
			
		case 3:		
			Programa3.ejecutar3();
			break;
			
		case 4:
			Programa4.ejecutar4();
			break;
			
		case 5:
			System.exit(0);
			break;

		default:
			break;
		}
	}
}
