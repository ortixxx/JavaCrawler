package todos;

public class palabra implements Runnable{
	static final int cont = 32;
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
		}
	}
	
	@SuppressWarnings("deprecation")
	public void stopChild(){
		for(int i=0; i<hilo.length;i++){
			if(hilo[i].isAlive())
				hilo[i].stop();
		}
	}
}
