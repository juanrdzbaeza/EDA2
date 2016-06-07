package PracticasUnificadas;


public class Solucion {
	
	private Recursos[] v;
	
	private int pesoTotal;
	
	public Solucion (int totalMeses) {
		
		v = new Recursos[totalMeses];
		
		for (int i=0; i < v.length; i++) {
			
			v[i] = new Recursos (Programa4.psicologosInfantiles, Programa4.autobusesEscolares, Programa4.numtrabajadoresSociales, 1);
		}
		pesoTotal = 0;
	}

	public Solucion(Solucion s) {
		
		v = new Recursos[s.v.length];
		for (int i=0; i<v.length; i++) {
			v[i] = new Recursos (s.v[i]);
		}
		pesoTotal = s.pesoTotal;
	}
		

	public boolean comprobarPropuesta(RecursosPropuestas rp, int mes) {
		
		for (int i = mes; i < mes + rp.getDuracion(); i++) {
			if ( ! v[i].comprobar(rp))
				return false;
		}
		return true;
	}

	public void insertarPropuesta(int mes, RecursosPropuestas rp) {
		for (int i = mes; i < mes + rp.getDuracion(); i++) {
			v[i].insertarPropuesta(rp);
		}
	}
	
	public String toString () {
		String cadena = "";
		for (int i=0; i < v.length; i++) {
			for (RecursosPropuestas rp : v[i].getPropuestas()) {
				
				if (rp.getMesComienzo() == i) {
					
					cadena = cadena + "Propuesta: " + rp.getNumPropuesta()
							+" Grupo: "+rp.getGrupo()
							+" Comienzo: " + rp.getMesComienzo()
							+" Fin: " + (rp.getMesComienzo() + rp.getDuracion())
							+" Recursos: (Trabajadores = " + rp.getTrabajadores()
							+", Psicologos = " + rp.getPsicologo()
							+", Transporte = " + rp.getTransporte()
							+", Centro Social = " + rp.getCentroSocial() + ")\n";
				}
			}
		}
		cadena = cadena + "peso total: "+pesoTotal+"\n";
		return cadena;
	}
	
	public int calcularPesoTotal (Solucion s) {
		
		pesoTotal = 0;
		
		
		for (int i=0; i < v.length; i++) {
			for (RecursosPropuestas rp : v[i].getPropuestas()) {
					pesoTotal = pesoTotal + rp.getTiempoEspera();
			}
		}
		return pesoTotal;
	}
	
	public int getPesoTotal () {
		return pesoTotal;
	}
}
