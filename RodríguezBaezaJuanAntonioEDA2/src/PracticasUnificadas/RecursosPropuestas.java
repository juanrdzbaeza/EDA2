package PracticasUnificadas;

public class RecursosPropuestas implements Comparable<RecursosPropuestas> {
	
	private Propuesta prop;
	private int numPropuesta, grupo, duracionProp, necTrabajador, necTransporte, necPsicologos, mesComienzo, mesFin, necCentroSocial, tiempoEspera;
	
	
	public RecursosPropuestas(Propuesta p){
		
		prop = p;
		numPropuesta = p.getNumPropuesta();
		grupo = p.getNumGrupo();
		duracionProp = (int)(Math.random()*5 +1); // numero aleatorio del 1 al 10
		necTrabajador = (int)(Math.random()*5 +1); // numero aleatorio del 1 al 5
		necTransporte = (int)(Math.random()*6); // numero aleatorio del 0 al 5
		necPsicologos = (int)(Math.random()*5 +1); // numero aleatorio del 1 al 5
		necCentroSocial = (int)(Math.random()*2);
		mesComienzo = -1;
		tiempoEspera = getTiempoEspera();
	}
	
	
	
	public RecursosPropuestas(RecursosPropuestas rp) {
		prop = rp.prop;
		numPropuesta = rp.getNumPropuesta();
		grupo = rp.getGrupo();
		duracionProp = rp.duracionProp;
		necTrabajador = rp.necTrabajador;
		necTransporte = rp.necTransporte;
		necPsicologos = rp.necPsicologos;
		necCentroSocial = rp.necCentroSocial;
		mesComienzo = rp.mesComienzo;
		mesFin = rp.mesFin;
		tiempoEspera = rp.tiempoEspera;

	}



	public String toString(){
		return numPropuesta + "\t" + grupo + "\t" + mesComienzo + "\t" + mesFin + "\t" + duracionProp + "\t" + necTrabajador + 
				"\t" + necTransporte + "\t" + necCentroSocial + "\t" + necPsicologos;
	}
	
	public Propuesta getPropuesta(){
		return prop;
	}
	
	public int getTiempoEspera(){
		return prop.getGrupo().totalAbs() * duracionProp;
	}
	
	
	public int getDuracion(){
		return duracionProp;
	}

	public int getGrupo(){
		return grupo;
	}
	
	public int getTrabajadores(){
		return necTrabajador;
	}
	
	public int getTransporte(){
		return necTransporte;
	}
	
	public int getPsicologo(){
		return necPsicologos;
	}
	
	public int getMesComienzo() {
		return mesComienzo;
	}
	
	
	public int getCentroSocial(){
		return necCentroSocial;
	}
	
	public int getNumPropuesta(){
		return numPropuesta;
	}

	
	public void setMesComienzo(int mesComienzo) {
		this.mesComienzo = mesComienzo;
	}
	

	@Override
	public int compareTo(RecursosPropuestas o) {
		if(this.getTiempoEspera() < o.getTiempoEspera()) return -1;
		if(this.getTiempoEspera() > o.getTiempoEspera()) return 1;
		return 0;
	}

}
