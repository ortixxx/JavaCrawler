import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Vector;
import bdex.sqlite;
import org.apache.commons.validator.routines.UrlValidator;

class usuario extends JFrame implements ActionListener, MouseListener, WindowListener{
	private static final long serialVersionUID = 1L;
	static JDialog ad;
	static JMenuBar menuBar;
	static JMenu sistem, search;
	static JMenuItem consultar, agregar, borrar, importar, exportar, salir, start, stop, pause, nivelDos;
	static Vector<String> paginas = new Vector<String>(0, 1);
	static int multiplo=0, pags=0, frame = 0;
	static JTextField clave, nuevoLink;
	static JButton buscar, delete, insert;
	static Bar barra;
	static JComboBox<String> caja, cajaABC, cajaLinks;
	static String[] estadosABC = new String[]{"Aguascalientes","Baja California","Baja California Sur","Campeche","Coahuila","Colima","Chiapas","Chihuahua","CDMX","Durango","Guanajuato","Guerrero","Hidalgo","Jalisco","Edo de México","Michoacán","Morelos","Nayarit","Nuevo León","Oaxaca","Puebla","Querétaro","Quintana Roo","San Luis Potosí","Sinaloa","Sonora","Tabasco","Tamaulipas","Tlaxcala","Veracruz","Yucatán","Zacatecas"};
	static sqlite con = new sqlite();
	static String sql, actual = " ";
	static UrlValidator validar = new UrlValidator();
	Thread [] hilos, aux;
	Main [] inicio;
	Main model = new Main();
	Vector<String> otras = new Vector<String>(), otrasUrls = new Vector<String>(), dos = new Vector<String>(0, 1);
	JTable Tabla;
	JScrollPane Consulta;
	JPanel PanelTabla;
	
	public usuario(){
		super("Gallbo: Motor de busqueda");
		interfaz();
	}
	
	public void interfaz(){
	    sistem = new JMenu("Sistema");
	    agregar = new JMenuItem("Agregar Pagina");
	    borrar = new JMenuItem("Borrar Pagina");
	    consultar = new JMenuItem("Consultar");
	    importar = new JMenuItem("Importar", new ImageIcon("icon/import-icon.png"));
	    exportar = new JMenuItem("Exportar", new ImageIcon("icon/export-icon.png"));
	    salir = new JMenuItem("Salir", new ImageIcon("icon/delete-icon.png"));
	    
	    search = new JMenu("Busqueda");
	    start = new JMenuItem("Iniciar", new ImageIcon("icon/Start-2-icon.png"));
	    stop  = new JMenuItem("Detener", new ImageIcon("icon/Stop-2-icon.png"));
	    pause = new JMenuItem("Pausar", new ImageIcon("icon/Pause-icon.png"));
	    nivelDos = new JCheckBoxMenuItem("Nivel Dos", false);
	    
	    sistem.setMnemonic(KeyEvent.VK_S);
	    	agregar.setMnemonic(KeyEvent.VK_A);
	    	borrar.setMnemonic(KeyEvent.VK_B);
	    	consultar.setMnemonic(KeyEvent.VK_C);
	    	importar.setMnemonic(KeyEvent.VK_I);
	    	exportar.setMnemonic(KeyEvent.VK_E);
	    search.setMnemonic(KeyEvent.VK_D);
	    	start.setMnemonic(KeyEvent.VK_N);
	    	stop.setMnemonic(KeyEvent.VK_T);
	    	pause.setMnemonic(KeyEvent.VK_P);	    
	    
	    menuBar = new JMenuBar();
	    menuBar.add(sistem);
	    	sistem.add(agregar);
	    	sistem.add(borrar);
	    	sistem.add(consultar);
	    	sistem.addSeparator();
	    	sistem.add(importar);
	    	sistem.add(exportar);
	    	sistem.addSeparator();
	    	sistem.add(salir);
	    menuBar.add(search);
	    	search.add(start);
	    	search.add(pause);
	    	search.add(stop);
	    	search.addSeparator();
	    	search.add(nivelDos);
	    	
	    setJMenuBar(menuBar);
	    
		clave = new JTextField("siniestro");
		clave.setBounds(5, 15, 350, 25);
		clave.selectAll();
		add(clave);
		
		buscar = new JButton("Buscar");
		buscar.setBounds(475, 14, 80, 27);
		add(buscar);
		
		box();
		
		barra = new Bar();
		add(barra);
		
		Tabla = new JTable(Main.getModel());
		Tabla.setRowHeight(80);
		Tabla.getColumnModel().getColumn(0).setMaxWidth(30);
		Tabla.getColumnModel().getColumn(0).setResizable(false);
		Tabla.getColumnModel().getColumn(1).setMaxWidth(150);
		Tabla.getColumnModel().getColumn(1).setResizable(false);
		Tabla.getColumnModel().getColumn(2).setMaxWidth(630);
		Tabla.getColumnModel().getColumn(2).setResizable(false);
		Tabla.getTableHeader().setReorderingAllowed(false);
		Tabla.setDefaultRenderer(String.class, new MultiLineCellRenderer());
		
		Consulta = new JScrollPane(Tabla);
		
		PanelTabla = new JPanel();
		PanelTabla.setLayout(new BorderLayout());
		PanelTabla.setBounds(5, 50, 785, 546);
		PanelTabla.add(Consulta, BorderLayout.CENTER);
		add(PanelTabla);
		
		nuevoLink = new JTextField("http://...");
		nuevoLink.setBounds(10, 10, 340, 30);
		nuevoLink.selectAll();
		
		cajaABC = new JComboBox<String>(estadosABC);
		cajaABC.insertItemAt("Estados", 0);
		cajaABC.setSelectedIndex(0);
		cajaABC.setMaximumRowCount(10);
		cajaABC.setSize(120, 30);
		
		cajaLinks = new JComboBox<String>();
		cajaLinks.insertItemAt("Links", 0);
		cajaLinks.setSelectedIndex(0);
		cajaLinks.setMaximumRowCount(10);
		cajaLinks.setLocation(140, 10);
		
		delete = new JButton("Borrar");
		delete.setBounds(490, 9, 120, 32);
		
		insert = new JButton("Agregar");
		insert.setBounds(490, 9, 120, 32);
		
		ad = new JDialog(this, "ABC periodicos", true);
		ad.setLayout(null);
		ad.setSize(625, 80);
		ad.setLocationRelativeTo(null);
		ad.setResizable(false);
		
		listeners();
		setIconImage(new ImageIcon("loge.png").getImage());
		setSize(800,650);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	private void listeners() {
		clave.addActionListener(this);
		buscar.addActionListener(this);
		
		agregar.addActionListener(this);
	    borrar.addActionListener(this);
	    consultar.addActionListener(this);
	    salir.addActionListener(this);
	    
	    start.addActionListener(this);
		stop.addActionListener(this);
		
		cajaABC.addActionListener(this);
		delete.addActionListener(this);
		insert.addActionListener(this);
		nuevoLink.addActionListener(this);
		
		Tabla.addMouseListener(this);
		
		addWindowListener(this);
		ad.addWindowListener(this);
	}

	public void box(){		
		caja=new JComboBox<String>(estadosABC);
		caja.insertItemAt("Estados", 0);
		caja.setSelectedIndex(0);
		caja.setMaximumRowCount(10);
		caja.setBounds(360, 14, 110, 27);
		add(caja);
	}

	
	
	public void paginas(int estado){
		paginas.clear();
		sql = "select nombre from periodicos where id_estado = "+estado;
		paginas = con.getQuery(sql);
	}

	public void buscar(){
		buscar.setEnabled(false);
		actual = clave.getText();
		Main.clearVector();
		dos.clear();
		procesa(actual);
		
		if(!dos.isEmpty()){
			multiplo=dos.size();
			pags = paginas.size();
			hilos = new Thread[pags*multiplo];
			inicio = new Main[pags*multiplo];
			barra.setValue(0);
			barra.setMax(pags*multiplo);
			for(int i=0;i<dos.size();i++){
				for(int j=0;j<pags;j++){
					inicio[j+(i*pags)] = new Main(paginas.elementAt(j), dos.elementAt(i), 0);
					hilos[j+(i*pags)] = new Thread(inicio[j+(i*pags)]);
					hilos[j+(i*pags)].start();
				}
			}
		}
	}

	public void procesa(String s) {
		int bandera = 0, comillas = 0;
		String nuevo = "";
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)==' ' && bandera == 0){
				if(nuevo.length()!=0){
					dos.add(nuevo);
				}
				nuevo="";
				continue;
			}
			if(Character.isAlphabetic(s.charAt(i))){
				nuevo += s.charAt(i);
				continue;
			}else{
				if(s.charAt(i)=='"' && comillas<1){
					if(nuevo.length()!=0){
						dos.add(nuevo);
					}
					nuevo="";
					comillas++;
					bandera = 1;
					continue;
				}else{
					if(s.charAt(i)!='"' && comillas>0){
						nuevo += s.charAt(i);
						continue;
					}
					comillas=0;
					bandera=0;
					if(nuevo.length()!=0){
						dos.add(nuevo);
					}
					nuevo="";
				}
			}
		}
		if(nuevo.length()!=0){
			dos.add(nuevo);
		}
	}
	
	class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public MultiLineCellRenderer() {
			setLineWrap(true);
		    setWrapStyleWord(true);
		    setOpaque(true);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			if (isSelected) {
			      setForeground(table.getSelectionForeground());
			      setBackground(table.getSelectionBackground());
			} else {
			      setForeground(table.getForeground());
			      setBackground(table.getBackground());
			}
			setFont(table.getFont());
			setText((value == null) ? "" : value.toString());
		    return this;
		}
	}
	
	public static int getTotal(){
		return paginas.size();
	}
	
	public static Bar getBarra(){
		return barra;
	}
	
	public static JButton getBoton(){
		return buscar;
	}
	
	public static int getMultiplo(){
		return multiplo;
	}
	
	public static JMenuItem getNivelDos(){
		return nivelDos;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==buscar || e.getSource()==clave || e.getSource()==start){
			if(caja.getSelectedIndex()==0){
				JOptionPane.showMessageDialog(null, "Seleccione un estado");
				return;
			}
			paginas(caja.getSelectedIndex());
			buscar();			
			return;
		}
		if(e.getSource()==stop){
			buscar.setEnabled(true);
			barra.setValue(barra.getMaximum());
			for(int i=0; i<hilos.length;i++){
				if(hilos[i].isAlive())
					hilos[i].stop();
				
				try{
					aux = new Thread[inicio[i].getHilos().length];
					aux = inicio[i].getHilos();
					for(int j=0;j<aux.length;j++)
						if(aux[j].isAlive())
							aux[j].stop();
				}catch(Exception ex){
					continue;
				}
			}
			return;
		}
		if(e.getSource()==salir){
			int o = JOptionPane.showConfirmDialog(null, "Realmente deseas salir?", "Advertencia", JOptionPane.YES_NO_OPTION);
			if (o == 0){
				con.dbClose();
				System.exit(0);
			}
			return;
		}
		if(e.getSource()==agregar){
			frame=1;
			ad.remove(cajaABC);
			ad.remove(delete);
			ad.remove(cajaLinks);
			ad.add(nuevoLink);
			cajaABC.setLocation(360, 10);
			ad.add(cajaABC);
			ad.add(insert);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==borrar){
			frame=2;
			ad.remove(cajaABC);
			ad.remove(insert);
			ad.remove(nuevoLink);
			ad.remove(cajaLinks);
			cajaABC.setLocation(10, 10);
			ad.add(cajaABC);
			cajaLinks.setSize(340, 30);
			ad.add(cajaLinks);
			ad.add(delete);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==consultar){
			frame=2;
			ad.remove(nuevoLink);
			ad.remove(delete);
			ad.remove(insert);
			ad.remove(cajaABC);
			ad.remove(cajaLinks);
			cajaABC.setLocation(10, 10);
			ad.add(cajaABC);
			cajaLinks.setSize(470, 30);
			ad.add(cajaLinks);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==insert || (e.getSource()==nuevoLink && cajaABC.getSelectedIndex()!=0)){
			if(validar.isValid(nuevoLink.getText())){
				int res = JOptionPane.showConfirmDialog(null, "Se agregara: \n"+nuevoLink.getText()+" -> "+cajaABC.getSelectedItem()+"\nes correcto?", "Mensaje de confirmacion", JOptionPane.YES_NO_OPTION);
				if (res == 0){
					sql = "INSERT OR IGNORE INTO periodicos VALUES ('"+nuevoLink.getText()+"', "+cajaABC.getSelectedIndex()+")";
					try {
						con.setQuery(sql);
						nuevoLink.setText("http://...");
						cajaABC.setSelectedIndex(0);
						JOptionPane.showMessageDialog(null, "Pagina agregada");
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Eror al agregar\n"+nuevoLink.getText());
					}
				}				
			}else{
				JOptionPane.showMessageDialog(null, "URL no válida");
			}
			return;
		}
		if(e.getSource()==nuevoLink && cajaABC.getSelectedIndex()==0){
			cajaABC.grabFocus();
			return;
		}
		if(e.getSource()==cajaABC && frame==2){
			if(cajaABC.getSelectedIndex()==0){
				cajaLinks.removeAllItems();
				cajaLinks.insertItemAt("Links", 0);
				cajaLinks.setSelectedIndex(0);
				return;
			}
				
			ordenaCaja();			
			return;
		}
		if(e.getSource()==delete){
			if(cajaABC.getSelectedIndex()==0){
				JOptionPane.showMessageDialog(null, "Seleccione un estado");
				return;
			}				
			
			int res = JOptionPane.showConfirmDialog(null, "Realmente deseas eliminar\n"+cajaLinks.getSelectedItem()+" ?", "Advertencia", JOptionPane.YES_NO_OPTION);
			if (res == 0){
				sql = "DELETE FROM periodicos WHERE nombre = '"+cajaLinks.getSelectedItem()+"' AND id_estado = "+cajaABC.getSelectedIndex();
				try{
					con.setQuery(sql);
					ordenaCaja();
					JOptionPane.showMessageDialog(null, "Pagina eliminada");
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Error al eliminar\n"+cajaLinks.getSelectedItem());
				}
			}
			return;
		}
	}
	
	private void ordenaCaja() {
		paginas(cajaABC.getSelectedIndex());
		cajaLinks.removeAllItems();
		for(int i=0;i<paginas.size();i++){
			cajaLinks.addItem(paginas.elementAt(i));
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2){
			if (e.getSource() == Tabla) {
				otrasUrls = Main.getVector(1);
				try{
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + otrasUrls.elementAt(Tabla.getSelectedRow()));
				}catch(Exception err){
					JOptionPane.showMessageDialog(null,"Error: "+err);
				}
			}	
		}	
	}
	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0){}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}
	@Override
	public void windowActivated(WindowEvent arg0){}
	@Override
	public void windowClosed(WindowEvent arg0){}
	@Override
	public void windowClosing(WindowEvent arg0){
		if(arg0.getSource()==this){
			int o = JOptionPane.showConfirmDialog(null, "Realmente deseas salir?", "Advertencia", JOptionPane.YES_NO_OPTION);
			if (o == 0){
				con.dbClose();
				System.exit(0);
			}
		}
		if(arg0.getSource()==ad){
			cajaABC.setSelectedIndex(0);
			cajaLinks.removeAllItems();
			cajaLinks.insertItemAt("Links", 0);
			cajaLinks.setSelectedIndex(0);
		}
	}
	@Override
	public void windowDeactivated(WindowEvent arg0){}
	@Override
	public void windowDeiconified(WindowEvent arg0){}
	@Override
	public void windowIconified(WindowEvent arg0){}
	@Override
	public void windowOpened(WindowEvent arg0){}	
	public static void main(String[]args) {
		String os = System.getProperty("os.name").toLowerCase();
		String name = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		if (os.indexOf("win") >= 0) {
		     try {
		          UIManager.setLookAndFeel(name);
		     }
		     catch (Exception e) {}
		}   
		new usuario();
	}
}