package PracticasUnificadas;

import java.io.Serializable;

/**
 * Clase Manzana
 * @author Grupo Iluminati
 * @version 1.2
 */
public class Manzana implements Comparable<Manzana>, Serializable{

	/**
	 * Atributos:
	 * calle: calle de la manzana
	 * avda: avenida de la manzana
	 * abse: absentismo de la manzana
	 * posicion: posicion de la manzana
	 */
	private int calle;
	private int avda;
	private int abse;
	private int posicion;
	
	/**
	 * Constructor de la clase
	 * @param calle calle de la manzana
	 * @param avda avenida de la manzana
	 * @param abse absentismo de la manzana
	 */
	public Manzana(int calle, int avda, int abse) {
		this.calle = calle;
		this.avda = avda;
		this.abse = abse;
		posicion = 0;
	}
	
	/**
	 * Constructor de la clase
	 * @param posicion posicion de la manzana
	 * @param abse absentismo de la manzana
	 */
	public Manzana (int posicion, int abse) {
		calle = 0;
		avda = 0;
		this.abse = abse;
		this.posicion = posicion;
	}

	/**
	 * getPosicion()
	 * @return posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * setPosicion(int posicion)
	 * @param posicion posicion de la manzana
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * getCalle()
	 * @return calle
	 */
	public int getCalle() {
		return calle;
	}

	/**
	 * setCalle(int calle)
	 * @param calle calle de la manzana
	 */
	public void setCalle(int calle) {
		this.calle = calle;
	}

	/**
	 * getAvda()
	 * @return avda
	 */
	public int getAvda() {
		return avda;
	}

	/**
	 * setAvda(int avda)
	 * @param avda avenida de la manzana
	 */
	public void setAvda(int avda) {
		this.avda = avda;
	}

	/**
	 * getAbse()
	 * @return abse
	 */
	public int getAbse() {
		return abse;
	}

	/**
	 * setAbse(int abse)
	 * @param abse absentismo de la manzana
	 */
	public void setAbse(int abse) {
		this.abse = abse;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString () {
		return "   ("+posicion+")\t-->\t("+avda+","+calle+")\t-->\t"+abse;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Manzana o) {
		if(this.getAbse() > o.getAbse()) return -1;
		if(this.getAbse() < o.getAbse()) return 1;
		return 0;
	}
	
	/**
	 * Método que calcula a que en que avenida y calle se encuentra la manzana
	 * @param pos posicion de la manzana
	 * @param n tamaño del problema
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
