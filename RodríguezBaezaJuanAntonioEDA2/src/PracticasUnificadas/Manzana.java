package PracticasUnificadas;

import java.io.Serializable;

public class Manzana implements Comparable<Manzana>, Serializable{

	private int calle;
	private int avda;
	private int abse;
	private int posicion;
	
	public Manzana(int calle, int avda, int abse) {
		this.calle = calle;
		this.avda = avda;
		this.abse = abse;
		posicion = 0;
	}
	
	public Manzana (int posicion, int abse) {
		calle = 0;
		avda = 0;
		this.abse = abse;
		this.posicion = posicion;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	public int getCalle() {
		return calle;
	}

	public void setCalle(int calle) {
		this.calle = calle;
	}

	public int getAvda() {
		return avda;
	}

	public void setAvda(int avda) {
		this.avda = avda;
	}

	public int getAbse() {
		return abse;
	}

	public void setAbse(int abse) {
		this.abse = abse;
	}
	
	public String toString () {
		return "   ("+posicion+")\t-->\t("+avda+","+calle+")\t-->\t"+abse;
	}

	@Override
	public int compareTo(Manzana o) {
		if(this.getAbse() > o.getAbse()) return -1;
		if(this.getAbse() < o.getAbse()) return 1;
		return 0;
	}
	
	/**
	 * Método que calcula a que en que avenida y calle se encuentra la manzana
	 * @param pos
	 * @param n
	 */
	
	public void calcularCalleAvenida (int pos, int n) {
		// Calculamos la posición real
		int posReal = posicion - pos;
		
		int filas_columnas = (int) Math.pow(2, n);
		int fila_inicial = 0;
		int fila_final = filas_columnas -1;
		int columna_inicial = 0; 
		int columna_final = filas_columnas -1;
		int potencia = (int) Math.pow(4, n-1);
		while (potencia >= 1) {
			
			int x = posReal / potencia;
			switch (x) {
			case 0:// Distrito 1
				
				//fila_inicial, columna_inicial son iguales
				fila_final = fila_inicial + filas_columnas/2 - 1;
				columna_final = columna_inicial + filas_columnas/2 - 1;
				break;
				
			case 1:// Distrito 2
				
				//columna_inicial, columna_final son iguales
				fila_inicial = fila_inicial + filas_columnas/2;
				fila_final = fila_inicial + filas_columnas/2 - 1;
				break;
				
			case 2:// Distrito 3
				
				//cambian los 4
				fila_inicial = fila_inicial + filas_columnas/2;
				fila_final = fila_inicial + filas_columnas/2 - 1;
				columna_inicial = columna_inicial + filas_columnas/2;
				columna_final = columna_inicial + filas_columnas/2 - 1;
				break;
				
			case 3:// Distrito 4
				
				//fila_inicial, fila_final son iguales
				columna_inicial = columna_inicial + filas_columnas/2;
				columna_final = columna_inicial + filas_columnas/2 - 1;
				break;
			}
			
			filas_columnas = filas_columnas / 2;// Reducimos a la mitad.
			posReal = posReal - x*potencia;
			potencia = potencia / 4;
		}
		calle = fila_inicial + 1;
		avda = columna_inicial + 1;
	}
	
	
}
