package PracticasUnificadas;

import java.util.ArrayList;

public class Recursos {

	private int psicolocos, autobuses, trabajadores, centro;
	public ArrayList<RecursosPropuestas> propuestas;
	
	
	public Recursos(int psicolocos, int autobuses, int trabajadores, int centro) {
		
		this.psicolocos = psicolocos;
		this.autobuses = autobuses;
		this.trabajadores = trabajadores;
		this.centro = centro;
		this.propuestas = new ArrayList<RecursosPropuestas>();
	}
	
	public Recursos(Recursos r) {
		
		this.psicolocos = r.psicolocos;
		this.autobuses = r.autobuses;
		this.trabajadores = r.trabajadores;
		this.centro = r.centro;
		this.propuestas = new ArrayList<RecursosPropuestas>(r.getPropuestas());
	}
	
	

	public int getPsicolocos() {
		return psicolocos;
	}



	public int getAutobuses() {
		return autobuses;
	}



	public int getTrabajadores() {
		return trabajadores;
	}



	public int getCentro() {
		return centro;
	}



	public ArrayList<RecursosPropuestas> getPropuestas() {
		return propuestas;
	}



	public  boolean comprobar(RecursosPropuestas rp) {
		
		if((centro - rp.getCentroSocial() >= 0) && (trabajadores - rp.getTrabajadores() >= 0 ) &&
		(psicolocos - rp.getPsicologo() >= 0 ) && (autobuses - rp.getTransporte() >= 0 )){
			
			for(RecursosPropuestas rpx : propuestas){
				if(rpx.getGrupo() == rp.getGrupo())
					return false;
			}
			return true;
		}
		
		return false;
	}
	
	public void insertarPropuesta (RecursosPropuestas rp) {
		psicolocos -= rp.getPsicologo();
		autobuses -= rp.getTransporte();
		trabajadores -= rp.getTrabajadores();
		centro -= rp.getCentroSocial();
		propuestas.add(rp);
	}
	
	public void quitarPropuesta () {
		RecursosPropuestas rp = propuestas.get(propuestas.size()-1);
		psicolocos += rp.getPsicologo();
		autobuses += rp.getTransporte();
		trabajadores += rp.getTrabajadores();
		centro += rp.getCentroSocial();
		propuestas.add(rp);
	}
	
	public String toString () {
		String cadena = psicolocos + " " + autobuses + " " + trabajadores+" " + centro + " ";
		
		for (RecursosPropuestas rp : propuestas)
			cadena = cadena + "(" + rp + ") ";
		return cadena;
	}
	
}
