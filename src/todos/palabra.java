package todos;

public class palabra implements Runnable{
	static int cont = 32;
	String palabra;
	estado [] inicio = new estado[32];
	Thread [] hilo = new Thread[32];	
	
	public palabra(String s){
		palabra=s;
	}
	
	public void run() {
		for(int i=0;i<cont;i++){
			inicio[i] = new estado(i+1, palabra);
			hilo[i] = new Thread(inicio[i]);
			hilo[i].start();
			//hilo[i].join();
		}
		//SOP.Terminado
	}
}
