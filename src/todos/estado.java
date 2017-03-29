package todos;

import java.util.Vector;
import main.Main;
import main.usuario;

public class estado implements Runnable{
	Main m[];
	Vector<String> paginas = new Vector<String>(0, 1);
	int estado;
	String palabra;
	
	public estado(int estado, String palabra){
		this.estado=estado;
		this.palabra=palabra;
		Vector<String> aux = new Vector<String>(0, 1);
		aux = usuario.getSql().getQuery("select nombre from periodicos where id_estado = "+estado);
		for(int i = 0;i<aux.size();i++){
			paginas.add(aux.elementAt(i));
		}
		m = new Main[paginas.size()];
	}

	public void run() {		
		for(int i = 0;i<paginas.size();i++){
			m[i] = new Main(paginas.elementAt(i), palabra, 0);
			m[i].extrae(paginas.elementAt(i));
		}			
	}
}
