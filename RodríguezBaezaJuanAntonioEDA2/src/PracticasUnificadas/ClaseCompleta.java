package PracticasUnificadas;

import java.util.Scanner;

public class ClaseCompleta {
	
	public static void main(String[] args) {
		
		menu();
	}
	
	
	public static void menu(){
		
		Scanner menu = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------");
		System.out.println("                   MEN� PRINCIPAL");
		System.out.println("------------------------------------------------------");
		System.out.println("1. PR�CTICA 01 - DvY");
		System.out.println("2. PR�CTICA 02 - Greedy");
		System.out.println("3. SALIR");
		
		
		int opcion;
		
		do{
			opcion = menu.nextInt();
			if(opcion < 1 || opcion > 3)
				System.out.println("El numero elegido est� fuera del intervalo permitido, insertelo nuevamente.");
			}
			while(opcion < 1 || opcion > 3);

		
		switch (opcion) {
		case 1:
			Programa.ejecutar1();
			break;
			
		case 2:
			Programa2.ejecutar2();
			break;
			
		case 3:
			System.exit(0);

		default:
			break;
		}
	}
}
