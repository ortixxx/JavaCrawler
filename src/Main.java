import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import mozilla.MetaTagsExtractor;

public class Main implements Runnable{
	public static Vector<String> urls = new Vector<String>(0, 1), noticias  = new Vector<String>(0, 1);
	public static DefaultTableModel model;
	public static Semaforo luz;
	public static int x = 1, y = 0, z = 0, bandera=0;
	public static LocalDate mark;
	Thread [] hilos;
	Main [] inicio;
	int nivel;
	LocalDate copia;
	String sitio, clave, texto, titulo, date;
	Vector<Object> uotro;
	Vector<String> nuevaPags;
	BufferedImage c;
	Document doc;
	MetaTagsExtractor mte;
	ImageIcon foto = new ImageIcon("loge.png");
	
	public Main(String st,String cl, int n){
		nivel = n;
		sitio = st;
		clave = acentos(cl);
	}
	
	public Main(){
		mark = LocalDate.now().minusDays(2);
	    luz = new Semaforo(1);
		model = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
	        public Class<?> getColumnClass(int col) {
	            switch(col) {
	            	case 0: return int.class;
	                case 1: return ImageIcon.class;
	                default: return String.class;
	            }
	        }
			
			@Override
			public boolean isCellEditable(int row, int col) {
	             return false;
	        }
	    };
	    
	    model.addColumn("#");
		model.addColumn("Img");
		model.addColumn("Noticias");
	}
	
	public void run(){
		if(nivel==0 && bandera==0){
			bandera++;
			z = usuario.getTotal()*usuario.getMultiplo();
		}
			
		extrae();
		//System.out.println("MurioHilo: "+(y++));
		y++;		
		if((y != z && nivel == 0) || (y != z-1 && nivel==1)){
			usuario.getBarra().setValue(y);
	    }else{
	    	usuario.getBarra().setValue(y);
	    	usuario.getBoton().setEnabled(true);
	    	//Bandera de terminacion para la busqueda TODOS
	    	JOptionPane.showMessageDialog(null, "Busqueda finalizada\nEncontrados: "+(x-1));
	    }
	}
	
	public void extrae(){
		try {
			doc = Jsoup.connect(sitio).get();
			sitio = remove(sitio);
		} catch (IOException e1) {
			System.out.println("Error de conexion: "+sitio);
			return;
		}
		
		Elements questions = doc.select("a[href]");
		for(Element link: questions){
			try{
				if(acentos(link.text()).toLowerCase().contains(clave.toLowerCase()) || espacios(acentos(link.attr("href"))).toLowerCase().contains(clave.toLowerCase()) || espacios(acentos(link.toString())).toLowerCase().contains(clave.toLowerCase())){					
					texto = link.text();
					processPage(link.attr("abs:href"));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void processPage(String URL){
		luz.Rojo();
		if(URL.length()>12)
			if(URL.substring(12, URL.length()-1).contains(sitio)){
				luz.Verde();
				return;
			}
				
			
		if(URL.contains("facebook") || URL.contains("google") || URL.contains("twitter") || URL.contains("youtube") || URL.contains("pinterest") || URL.contains("instagram") || URL.contains("whatsapp")){
			luz.Verde();
			return;
		}
			
		
		if(!urls.contains(URL)){
			mte = new MetaTagsExtractor(URL);
			date=mte.getDate();
			
			if(!date.equals("No date")){
				date = mte.getDate().substring(0, 10);
				copia = LocalDate.of(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7)), Integer.parseInt(date.substring(8)));
				if(copia.isBefore(mark)){
					mte.closeBd();
					luz.Verde();
					return;
				}
			}
			
			uotro = new Vector<Object>();
			//System.out.println("***************************************************");
			uotro.add(x++);
			
			try {
				c = ImageIO.read(new URL(mte.getImage()).openStream());
				uotro.add(new ImageIcon(getScaledImage(c, (c.getWidth()*80)/c.getHeight(), 80)));
			}catch (Exception e) {
				uotro.add(foto);
			}
			
			titulo = mte.getTitle();
	    	if(titulo.equals("No title")){
	    		if(texto.length() != 0 && texto!=null)
	    			titulo = texto;
	    		else
	    			titulo = mte.getAux();
	    	}
	    	
	    	if(titulo==null && date.equals("No date") && mte.getDes().equals("No description") && nivel==1){
	    		mte.closeBd();
	    		luz.Verde();
	    		return;	    		
	    	}
	    		
			uotro.add(titulo+"\n"+date+"\n"+mte.getDes());
			model.addRow(uotro);
			noticias.add(mte.getTitle());
			urls.add(URL);	
			
			nuevaPags = mte.getHref();
			
			if(nivel == 0 && (z+nuevaPags.size())<300 && ((JCheckBoxMenuItem)usuario.getNivelDos()).getState()){
				luz.Verde();
				z += nuevaPags.size();
				hilos = new Thread[nuevaPags.size()];
				inicio = new Main[nuevaPags.size()];
				usuario.getBarra().setMax(z);
				for(int i=0;i<nuevaPags.size();i++){					
					inicio[i] = new Main(gato(nuevaPags.elementAt(i)), clave, 1);
					hilos[i] = new Thread(inicio[i]);
					hilos[i].start();
				}
			}else{
				luz.Verde();				
			}
			mte.closeBd();
		}else{
			luz.Verde();
		}
	}

	public static Vector<String> getVector(int in){
		if(in==0)
			return noticias;
		else
			return urls;
	}
	
	public static void clearVector(){
		x = 1;
		y = 0;
		bandera = 0;
		model.setRowCount(0);
		noticias.clear();
		urls.clear();
		//Reset bandera de terminacion para la busqueda TODOS
	}
	
	public String acentos(String s){
		s = s.replace('á', 'a');
		s = s.replace('é', 'e');
		s = s.replace('í', 'i');
		s = s.replace('ó', 'o');
		s = s.replace('ú', 'u');
		
		s = s.replace('Á', 'A');
		s = s.replace('É', 'E');
		s = s.replace('Í', 'I');
		s = s.replace('Ó', 'O');
		s = s.replace('Ú', 'U');
		return s;
	}
	
	public String espacios(String s) {
		s = s.replace('-', ' ');
		s = s.replace('_', ' ');
		return s;
	}
	
	public String gato(String s){
		if(s.charAt(s.length()-1)=='#'){
			s = s.substring(0, s.length()-1);
		}
		return s;
	}

	public String remove(String s) {
		return s.substring(0, s.substring(8, s.length()).indexOf("/")+8);
	}
	
	public static DefaultTableModel getModel(){
		return model;
	}
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = resizedImg.createGraphics();

	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(srcImg, 0, 0, w, h, null);
	    g.dispose();

	    return resizedImg;
	}

	public static int getY(){
		return y;
	}
	
	public static int getX() {
		return x;
	}
	
	public Thread[] getHilos(){
		return hilos;
	}
}