package PracticasUnificadas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase Programa2
 * @author Grupo Iluminati
 * @version 1.2
 */
public class Grupo implements Serializable {

	/**
	 * Atributos:
	 * numero: Ordinal del grupo
	 * puntoCritico: manzana epicentro del grupo
	 * manzanas: arrayList de manzanas del grupo
	 */
	private int numero;
	private Manzana puntoCritico;
	private ArrayList<Manzana> manzanas;
	
	/**
	 * Constructor que, una vez generado el grupo en el construcotr
	 * por defecto, rellena el arrayList con las manzanas del grupo
	 * que se le pasa como parametro.
	 * @param g grupo
	 */
	public Grupo (Grupo g){
		numero = g.numero;
		puntoCritico = g.puntoCritico;
		manzanas = new ArrayList<>(g.manzanas);
	}
	
	/**
	 * Constructor por defecto que acepta el ordinal y el punto
	 * crítico como parametro.
	 * @param numero ordinal del grupo
	 * @param puntoCritico epicentro del grupo
	 */
	public Grupo (int numero, Manzana puntoCritico) {
		this.numero = numero;
		this.puntoCritico = puntoCritico;
		manzanas = new ArrayList<Manzana>();
	}
	
	/**
	 * Método para calcular la distancia de una manzana con el punto crítico
	 * de su grupo
	 * @param m manzana
	 * @param s calle
	 * @param w avenida
	 * @return double distanciaPuntoCritico
	 */
	public double distanciaPuntoCritico (Manzana m, double s, double w) {
		return (s+w) * (Math.abs(m.getAvda()-puntoCritico.getAvda())
					+ Math.abs(m.getCalle()-puntoCritico.getCalle()));
	}

	/**
	 * Añade una manzana al arraList manzanas
	 * @param m manzana para añadir
	 */
	public void addManzana(Manzana m) {
		manzanas.add(m);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String salida = "Grupo\t" + numero + "\t|\tPuntoCritico\t" + puntoCritico + "\t|\tManzanas\t" + manzanas.get(0);
		
		for (int i = 1; i < manzanas.size(); i++) {
			salida += "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + manzanas.get(i);
		}
		
		return salida;
	}

	/**
	 *  Método que devuelve el tamaño del arrayList manzanas
	 * @return manzanas.size()
	 */
	public int totalMan() {
		return manzanas.size();
	}

	/**
	 * Método que devuelve la suma total de todo el absentismo
	 * de la ciudad
	 * @return s suma de absentismo total
	 */
	public int totalAbs() {
		int s = puntoCritico.getAbse();
		for (int i = 0; i < manzanas.size(); i++) {
			s += manzanas.get(i).getAbse();
		}
		return s;
	}
	
	/**
	 * Método para guardar la informacion en un fichero binario para la siguiente
	 * práctica 
	 * @param g grupo para añadir
	 */
	public static void guardarGrupoObject(ArrayList<Grupo> g){
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("grupos.obj"));
			for (int i = 0; i < g.size(); i++) {
				os.writeObject(g.get(i));
			}
			os.flush();
			os.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	/**
	 * Método que lee el fichero binario para la siguiente práctica
	 * @return a lista de grupos
	 * @throws ClassNotFoundException excepcion de la clase
	 */
	public static ArrayList<Grupo> leerGrupoObject() throws ClassNotFoundException{
		ArrayList<Grupo> a = new ArrayList<Grupo>();
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream("grupos.obj"));
			
			Object aux = is.readObject();
			
			while(aux != null){
				if(aux instanceof Grupo){
					a.add((Grupo) aux);
				}
				else
					System.out.println("No es un objeto");
				aux = is.readObject();
			}
			is.close();
		} catch (IOException e) {
			e.getMessage();
		}
		return a;
	}
}
