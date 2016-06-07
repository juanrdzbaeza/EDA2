package PracticasUnificadas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase Generar_Datos
 * @author Grupo Iluminati
 * @version 1.2
 */
public class Generar_Datos {

	public static void generarDatos(String directorio) {

		// Generamos los casos medios.
		for (int n = 1; n <= 9; n++) {
			int[] datos = calcularDatos(n);
			String nombreFichero = "datos" + File.separator + "datos" + n + ".txt";
			try {
				PrintWriter f = new PrintWriter(new FileWriter(nombreFichero));

				for (int i = 0; i < datos.length; i++) {
					f.write(datos[i] + "\n");
				}
				f.close();
			} catch (IOException e) {
				System.out.println("Error de escritura en fichero: " + nombreFichero);
			}
		}



		// Generamos los peores casos.
		// Suponemos que es cuando el 50% de las manzanas tienen una cantidad de absentismo
		// alto y el otro 50% tiene una cantidad de absentismo bajo.
		for (int n = 1; n <= 9; n++) {
			int[] datos = calcularDatosPeor(n);
			String nombreFichero = "datos" + File.separator + "datosPeor" + n + ".txt";
			try {
				PrintWriter f = new PrintWriter(new FileWriter(nombreFichero));

				for (int i = 0; i < datos.length; i++) {
					f.write(datos[i] + "\n");
				}
				f.close();
			} catch (IOException e) {
				System.out.println("Error de escritura en fichero: " + nombreFichero);
			}
		}


		// Generamos los mejores casos.
		// Suponemos que es cuando una manzana concentra casi todo el absentismo y las demas manzanas tienen su
		// absentismo a 1.

		for (int n = 1; n <= 9; n++) {
			int[] datos = calcularDatosMejor(n);
			String nombreFichero = "datos" + File.separator + "datosMejor" + n + ".txt";
			try {
				PrintWriter f = new PrintWriter(new FileWriter(nombreFichero));

				for (int i = 0; i < datos.length; i++) {
					f.write(datos[i] + "\n");
				}
				f.close();
			} catch (IOException e) {
				System.out.println("Error de escritura en fichero: " + nombreFichero);
			}
		}
	}





	private static int[] calcularDatos(int n) {
		int []a = null;
		int tamanio = 0;
		for (int i=0; i<=n; i++)
			tamanio = tamanio + (int) Math.pow(4, i);
		a = new int [tamanio];
		int pos = tamanio - (int) Math.pow(4, n);
		
		for (int i = 0; i < Math.pow(4, n); i++) {
				a[pos] = (int) (Math.random() * 25);
				pos++;
		}
		
		// Calculamos los distritos
		pos = tamanio - (int) Math.pow(4, n) - 1;
		for (int i=pos; i>=0; i--) {
			a[i] = a[i*4+1] + a[i*4+2] + a[i*4+3] + a[i*4+4];
		}
		return a;
	}
	
	

	private static int[] calcularDatosPeor(int n) {
		int []a = null;
		int tamanio = 0;
		for (int i=0; i<=n; i++)
			tamanio = tamanio + (int) Math.pow(4, i);
		a = new int [tamanio];
		int pos = tamanio - (int) Math.pow(4, n);
		for (int i = 0; i < Math.pow(2, n); i++) {
			for (int j = 0; j < Math.pow(2, n); j++) {
				if (Math.random() < 0.5)
					a[pos] = (int) (Math.random() * 5);
				else
					a[pos] = (int) (Math.random() * 10 + 15);
				pos++;
			}
		}
		
		// Calculamos los distritos
		pos = tamanio - (int) Math.pow(4, n) - 1;
		for (int i=pos; i>=0; i--) {
			a[i] = a[i*4+1] + a[i*4+2] + a[i*4+3] + a[i*4+4];
		}
		return a;
	}

	private static int[] calcularDatosMejor(int n) {
		int []a = null;
		int tamanio = 0;
		for (int i=0; i<=n; i++)
			tamanio = tamanio + (int) Math.pow(4, i);
		a = new int [tamanio];
		int pos = tamanio - (int) Math.pow(4, n);
		
		a[pos] = (int) (Math.random() * 250000);
		pos++;
			for (int i = 1; i < Math.pow(4, n); i++) {
				a[pos] = 1;
				pos++;
			}
		
		// Calculamos los distritos
		pos = tamanio - (int) Math.pow(4, n) - 1;
		for (int i=pos; i>=0; i--) {
			a[i] = a[i*4+1] + a[i*4+2] + a[i*4+3] + a[i*4+4];
		}
		return a;
	}
}
