class Semaforo {

	private int Recursos;

	public Semaforo(int Recursos) {
		this.Recursos=Recursos;
	}

	public synchronized void Espera() {
		while (Recursos<=0) {
			try {

				wait();


			} catch( InterruptedException e  ) {return;}
		}
		Recursos--;
	}

	public synchronized void Libera() {
		Recursos++;
		notify();
	}


}