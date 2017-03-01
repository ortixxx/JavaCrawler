import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import mozilla.MetaTagsExtractor;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main implements Runnable{
	public static Vector<String> urls = new Vector<String>(0, 1), noticias  = new Vector<String>(0, 1);
	public static DefaultTableModel model;
	public static Semaforo agrega;
	public static int x = 1, y = 0;
	public static LocalDate mark;
	LocalDate copia;
	DateTimeFormatter format;
	String sitio, clave, texto, titulo, date;
	Vector<Object> uotro;
	BufferedImage c;
	Document doc;
	MetaTagsExtractor mte;
	ImageIcon foto = new ImageIcon("loge.png");
	
	public Main(String st,String cl){
		sitio = st;
		clave = cl;
	}
	
	public Main(){
		format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		mark = LocalDate.now().minusDays(2);
	    
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
		agrega = new Semaforo(1);
	}
	
	public void run(){
		try {
			extrae(sitio);
		}catch (Exception e){
			System.out.println("Error en "+sitio);
		}
		//System.out.println("MurioHilo: "+(y++));
		y++;
		if(y != (usuario.getTotal())*usuario.getMultiplo()){
			usuario.getBarra().setValue(y);
	    }else{
	    	usuario.getBarra().setValue(y);
	    	usuario.getBoton(0).setEnabled(true);
	    	usuario.getBoton(1).setEnabled(false);
	    	JOptionPane.showMessageDialog(null, "Busqueda finalizada\nEncontrados: "+(x-1));
	    }
		
	}
	
	public void extrae(String origen) {
		try {
			doc = Jsoup.connect(sitio).get();
			sitio = remove(sitio);
		} catch (IOException e1) {
			System.out.println("Error de conexion: "+sitio);
			return;
		}
		
		Elements questions = doc.select("a[href]");
		for(Element link: questions){
			try {
				if(link.attr("href").substring(12, link.attr("href").length()).contains(sitio)){
					continue;
				}
					
				if(acentos(link.text()).toLowerCase().contains(clave.toLowerCase()) || espacios(acentos(link.attr("href"))).toLowerCase().contains(clave.toLowerCase()) || espacios(acentos(link.toString())).toLowerCase().contains(clave.toLowerCase())){
					texto = link.text();
					processPage(link.attr("abs:href"));
				}
			}catch (Exception e){
				agrega.Libera();
				continue;
			}
		}
	}

	private String espacios(String s) {
		s = s.replace('-', ' ');
		s = s.replace('_', ' ');
		return s;
	}

	public String remove(String s) {
		return s.substring(0, s.substring(8, s.length()).indexOf("/")+8);
	}

	public void processPage(String URL){
		agrega.Espera();
		if(!urls.contains(URL)){
			mte = new MetaTagsExtractor(URL);
			date=mte.getDate();
			if(!date.equals("No date")){
				date = mte.getDate().substring(0, 10);
				copia = LocalDate.of(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7)), Integer.parseInt(date.substring(8)));
				if(copia.isBefore(mark)){
					System.out.println(mark+"\n"+copia);
					System.out.println("salto un sitio");
					agrega.Libera();
					return;
				}
			}
			
			uotro = new Vector<Object>();
			System.out.println(x+"\n************************************\n");
			uotro.add(x++);
		
			try {
				c = ImageIO.read(new URL(mte.getImage()).openStream());
				uotro.add(new ImageIcon(getScaledImage(c, (c.getWidth()*80)/c.getHeight(), 80)));
			}catch (IOException e) {
				uotro.add(foto);
			}
			
			titulo = mte.getTitle();
	    	if(titulo.equals("No title")){
	    		if(texto.length() != 0)
	    			titulo = texto;
	    		else
	    			titulo = mte.getAux();
	    	}
			
			uotro.add(titulo+"\n"+date+"\n"+mte.getDes());
			model.addRow(uotro);
			noticias.add(mte.getTitle());
			urls.add(URL);
		}
		agrega.Libera();
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
		model.setRowCount(0);
		noticias.clear();
		urls.clear();
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
}