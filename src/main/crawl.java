package main;

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
import mozilla.metaTags;

public class crawl implements Runnable{
	public static Vector<String> urls = new Vector<String>(0, 1), noticias  = new Vector<String>(0, 1);
	public static DefaultTableModel model;
	public static semaforo luz;
	public static int x = 1, y = 0, z = 0;
	public static LocalDate mark;
	crawl rec;
	int nivel = 0, estado;
	LocalDate copia;
	String sitio, clave, texto, titulo, date, des;
	Vector<Object> uotro;
	Vector<String> nuevaPags, omitir;
	BufferedImage c;
	Document doc;
	metaTags mte;
	ImageIcon foto = new ImageIcon("loge.png");
	
	public crawl(String st,String cl, int n){		
		sitio = st;
		clave = acentos(cl);
		nivel = n;
	}
	
	public crawl(){
		mark = LocalDate.now().minusDays(2);
	    luz = new semaforo(1);
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
		extrae(sitio);
		interfaz.getOut().append("Hilo: "+(y++)+"\n");
		//y++;
		if(y!=z){
			interfaz.getBarra().setValue(y);
		}else{
			interfaz.reactivar();
			interfaz.getBarra().setValue(y);
			interfaz.getOut().append("Busqueda Finalizada!"+"\n");
		}
	}
	
	public void extrae(String s){
		try {
			doc = Jsoup.connect(s).get();
			s = remove(s);
		} catch (IOException e1) {
			importExport.logErrores.error("Error de conexion: "+s);
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
				importExport.logErrores.error("Error: "+e);
			}
		}
	}

	public void processPage(String URL){
		luz.Rojo();
		if(URL.length()>12)
			if(URL.substring(12, URL.length()-1).contains(URL)){
				luz.Verde();
				return;
			}
		
		omitir = interfaz.getSql().getQuery("select nombre from ignorar");
		for(int i=0;i<omitir.size();i++)
			if(URL.contains(omitir.elementAt(i))){
				luz.Verde();
				return;
			}
			
		
		if(!urls.contains(URL)){
			mte = new metaTags(URL);
			date=mte.getDate();
			
			try{
				if(!date.equals("No date")){
					date = mte.getDate().substring(0, 10);
					copia = LocalDate.of(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7)), Integer.parseInt(date.substring(8)));
					if(copia.isBefore(mark)){
						mte.closeBd();
						luz.Verde();
						return;
					}
				}
			}catch(Exception w){
				date="No date";
			}
			
			uotro = new Vector<Object>();
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
	    	
	    	des=mte.getDes();
	    	if(des.equals("No description"))
	    		des=URL;
	    	
	    	if(interfaz.getTodos())		    		
	    		uotro.add(interfaz.getEstados()[(estado-1)]+"\n"+titulo+"\n"+date+"\n"+mte.getDes());
	    	else
	    		uotro.add(titulo+"\n"+date+"\n"+mte.getDes());
			
			model.addRow(uotro);
			noticias.add(mte.getTitle());
			urls.add(URL);	
						
			if(nivel == 0 && ((JCheckBoxMenuItem)interfaz.getNivelDos()).getState()){
				luz.Verde();
				nuevaPags = mte.getHref();
				rec = new crawl(URL, clave, 1);
				for(int i=0;i<nuevaPags.size();i++){
					rec.extrae(nuevaPags.elementAt(i));
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
		z = 0;
		model.setRowCount(0);
		noticias.clear();
		urls.clear();
	}
	
	public String acentos(String s){
		s = s.replace('�', 'a');
		s = s.replace('�', 'e');
		s = s.replace('�', 'i');
		s = s.replace('�', 'o');
		s = s.replace('�', 'u');
		
		s = s.replace('�', 'A');
		s = s.replace('�', 'E');
		s = s.replace('�', 'I');
		s = s.replace('�', 'O');
		s = s.replace('�', 'U');
		return s;
	}
	
	public String espacios(String s) {
		s = s.replace('-', ' ');
		s = s.replace('_', ' ');
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

	public static void setZ(int i) {
		z = i;
	}
	
	public void setEstado(int e){
		estado=e;
	}
}