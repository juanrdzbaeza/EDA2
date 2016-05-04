package Practica_02;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Grupo implements Serializable {

	private int numero;
	private Manzana puntoCritico;
	private ArrayList<Manzana> manzanas;
	
	public Grupo (Grupo g){
		numero = g.numero;
		puntoCritico = g.puntoCritico;
		manzanas = new ArrayList<>(g.manzanas);
	}
	
	public Grupo (int numero, Manzana puntoCritico) {
		this.numero = numero;
		this.puntoCritico = puntoCritico;
		manzanas = new ArrayList<Manzana>();
	}
	
	public double distanciaPuntoCritico (Manzana m, double s, double w) {
		return (s+w) * (Math.abs(m.getAvda()-puntoCritico.getAvda())
					+ Math.abs(m.getCalle()-puntoCritico.getCalle()));
	}

	public void addManzana(Manzana m) {
		manzanas.add(m);
	}

	@Override
	public String toString() {
		String salida = "Grupo\t" + numero + "\t|\tPuntoCritico\t" + puntoCritico + "\t|\tManzanas\t" + manzanas.get(0);
		
		for (int i = 1; i < manzanas.size(); i++) {
			salida += "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + manzanas.get(i);
		}
		
		return salida;
	}

	public int totalMan() {
		
		return manzanas.size();
	}

	public int totalAbs() {
		int s = puntoCritico.getAbse();
		for (int i = 0; i < manzanas.size(); i++) {
			s += manzanas.get(i).getAbse();
		}
		return s;
	}
	
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
