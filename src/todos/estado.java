package todos;

import java.util.Vector;
import main.crawl;
import main.interfaz;

public class estado implements Runnable{
	crawl m;
	Vector<String> paginas = new Vector<String>(0, 1);
	int estado;
	static int contador = 0, cont = 0;
	String palabra;
	
	public estado(int estado, String palabra){
		this.estado=estado;
		this.palabra=palabra;
		Vector<String> aux = new Vector<String>(0, 1);
		aux = interfaz.getSql().getQuery("select nombre from periodicos where id_estado = "+estado);
		for(int i = 0;i<aux.size();i++){
			paginas.add(aux.elementAt(i));
		}
		cont += aux.size();
		interfaz.getBarra().setMax(cont);
	}

	public void run() {
		m = new crawl(paginas.firstElement(), palabra, 0);
		
		for(int i = 0;i<paginas.size();i++){
			m.setEstado(estado);
			m.extrae(paginas.elementAt(i));
			interfaz.getOut().append("Padre: "+(contador++)+"\n");
			interfaz.getBarra().setValue(contador);
		}
		if(contador==cont){
			interfaz.reactivar();
			interfaz.getBarra().setValue(contador);
			interfaz.getOut().append("Busqueda Finalizada!"+"\n");
		}			
	}
	
	public static void reset(){
		cont=0;
		contador=0;
	}
}