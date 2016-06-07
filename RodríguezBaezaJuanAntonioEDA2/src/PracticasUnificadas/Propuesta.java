package PracticasUnificadas;

public class Propuesta implements Comparable{

	private int numPropuesta;
	private int numGrupo;
	private int presupuesto;
	private int prioridad;
	private int impacto;
	private Grupo gr;
	private double beneficio;
	
	public Propuesta(int numPropuesta, int numGrupo, int presupuesto, int prioridad, int impacto, Grupo g) {
		this.numPropuesta = numPropuesta;
		this.numGrupo = numGrupo;
		this.presupuesto = presupuesto;
		this.prioridad = prioridad;
		this.impacto = impacto;
		gr = g;
		beneficio = 0;
	}

	public Grupo getGrupo() {
		return gr;
	}
	
	public int getNumPropuesta() {
		return numPropuesta;
	}

	public void setNumPropuesta(int numPropuesta) {
		this.numPropuesta = numPropuesta;
	}

	public int getNumGrupo() {
		return numGrupo;
	}

	public void setNumGrupo(int numGrupo) {
		this.numGrupo = numGrupo;
	}

	public int getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(int presupuesto) {
		this.presupuesto = presupuesto;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public int getImpacto() {
		return impacto;
	}

	public void setImpacto(int impacto) {
		this.impacto = impacto;
	}
	
	public String toString () {
		return "\t     " + numPropuesta+"\t          "+numGrupo+"\t           "+presupuesto+"\t            "+prioridad+"\t           "+impacto+ "\t         "+ beneficio;
	}
	
	public void calcularBeneficio (Grupo g, int totalSuperficie) {	
		beneficio = impacto * g.totalAbs() * 
				((double)g.totalMan() / totalSuperficie) * 
				(1.0 / prioridad);  
	}
	
	public double getBeneficio () {
		return beneficio;
	}
	
	public void setBeneficio(double beneficio){
		this.beneficio = beneficio;
	}
	
	
	public int compareTo(Object obj) {
		Propuesta p = (Propuesta) obj;
		if (presupuesto < p.presupuesto)
			return -1;
		if (presupuesto > p.presupuesto)
			return +1;
		return 0;
	}
}
