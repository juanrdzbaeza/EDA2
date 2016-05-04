package Practica_03;

import java.util.ArrayList;

import Practica_02.Grupo;


public class Programa3 {
	
	public static ArrayList<Grupo> grupos;

	public static void main(String[] args) throws ClassNotFoundException {
		grupos = Grupo.leerGrupoObject();
		
		System.out.println((grupos.get(0).totalAbs()));

	}

}
