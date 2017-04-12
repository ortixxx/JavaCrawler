package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;
import bdex.sqlite;
import org.apache.commons.validator.routines.UrlValidator;
import todos.estado;
import todos.palabra;

public class interfaz extends JFrame implements ActionListener, MouseListener, WindowListener{
	private static final long serialVersionUID = 1L;
	static JDialog ad;
	static JMenuBar menuBar;
	static JMenu sistem, search, rules;
	static JMenuItem consultar, agregar, agregarMas, borrar, importar, exportar, salir, start, stop, nivelDos, omisas, newomisas;
	static Vector<String> paginas = new Vector<String>(0, 1);
	static Vector<Object> agregaAbc;
	static boolean bandera=false, iniciado=false, iniciadoTodos=false;
	static int multiplo=0, pags=0, frame = 0, reglon, error=0, returnVal;
	static JTextField clave, nuevoLink;
	static JTextArea area;
	static JButton buscar, insert, delete;
	static barra barra;
	static omitir omitidos = new omitir();
	static JComboBox<String> caja, cajaABC;
	static String[] estadosABC = new String[]{"Aguascalientes","Baja California","Baja California Sur","Campeche","Coahuila","Colima","Chiapas","Chihuahua","CDMX","Durango","Guanajuato","Guerrero","Hidalgo","Jalisco","Edo de México","Michoacán","Morelos","Nayarit","Nuevo León","Oaxaca","Puebla","Querétaro","Quintana Roo","San Luis Potosí","Sinaloa","Sonora","Tabasco","Tamaulipas","Tlaxcala","Veracruz","Yucatán","Zacatecas"};
	static sqlite con = new sqlite();
	static String sql;
	static StringTokenizer token;
	static UrlValidator validar = new UrlValidator();
	static JFileChooser abrir, guardar;
	static File archivoGuardar;
	Thread [] hilos, aux, hiloNewMeta;
	crawl [] inicio;
	palabra [] newMeta;
	crawl maind = new crawl();
	Vector<String> otras = new Vector<String>(), otrasUrls = new Vector<String>(), dos = new Vector<String>(0, 1);
	JTable Tabla, tablaAbc;
	JScrollPane Consulta, scrollAbc, scrollInsert;
	JPanel PanelTabla, panelAbc, panelInsert;
	DefaultTableModel modelAbc;
	
	public interfaz(){
		super("Gallbo: Motor de busqueda");		
		hazInterfaz();
	}
	
	private void hazInterfaz(){
	    sistem = new JMenu("Sistema");
	    agregar = new JMenuItem("Agregar Pagina");
	    agregarMas = new JMenuItem("Agregar mas...");
	    borrar = new JMenuItem("Borrar Pagina");
	    consultar = new JMenuItem("Consultar");
	    importar = new JMenuItem("Importar", new ImageIcon("icon/import-icon.png"));
	    exportar = new JMenuItem("Exportar", new ImageIcon("icon/export-icon.png"));
	    salir = new JMenuItem("Salir", new ImageIcon("icon/delete-icon.png"));
	    
	    search = new JMenu("Busqueda");
	    start = new JMenuItem("Iniciar", new ImageIcon("icon/Start-2-icon.png"));
	    stop  = new JMenuItem("Detener", new ImageIcon("icon/Stop-2-icon.png"));
	    nivelDos = new JCheckBoxMenuItem("Nivel Dos", false);
	    
	    rules = new JMenu("Reglas");
	    newomisas = new JMenuItem("Agregar omision");
	    omisas = new JMenuItem("Omisiones");
	    	    
	    sistem.setMnemonic(KeyEvent.VK_S);
	    	agregar.setMnemonic(KeyEvent.VK_A);
	    	agregarMas.setMnemonic(KeyEvent.VK_M);
	    	borrar.setMnemonic(KeyEvent.VK_B);
	    	consultar.setMnemonic(KeyEvent.VK_C);
	    	importar.setMnemonic(KeyEvent.VK_I);
	    	exportar.setMnemonic(KeyEvent.VK_E);
	    search.setMnemonic(KeyEvent.VK_D);
	    	start.setMnemonic(KeyEvent.VK_N);
	    	stop.setMnemonic(KeyEvent.VK_T);
	    rules.setMnemonic(KeyEvent.VK_G);
	    	newomisas.setMnemonic(KeyEvent.VK_I);
	    	omisas.setMnemonic(KeyEvent.VK_O);
	    
	    menuBar = new JMenuBar();
	    menuBar.add(sistem);
	    	sistem.add(agregar);
	    	sistem.add(agregarMas);
	    	sistem.add(borrar);
	    	sistem.add(consultar);
	    	sistem.addSeparator();
	    	sistem.add(importar);
	    	sistem.add(exportar);
	    	sistem.addSeparator();
	    	sistem.add(salir);
	    menuBar.add(search);
	    	search.add(start);
	    	search.add(stop);
	    	search.addSeparator();
	    	search.add(nivelDos);
	    menuBar.add(rules);
	    	rules.add(newomisas);
	    	rules.add(omisas);
	    	
	    setJMenuBar(menuBar);
	    
		clave = new JTextField("siniestro");
		clave.setBounds(5, 15, 350, 25);
		clave.selectAll();
		add(clave);
		
		buscar = new JButton("Buscar");
		buscar.setBounds(475, 14, 80, 27);
		add(buscar);
		
		box();
		barra = new barra();
		add(barra);
		
		Tabla = new JTable(crawl.getModel());
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
		
		nuevoLink = new JTextField();
		nuevoLink.setLocation(10, 10);
		nuevoLink.addFocusListener(new FocusListener(){
			@Override
		    public void focusGained(FocusEvent e) {
		        if (nuevoLink.getText().equals("http://...")){
		        	nuevoLink.setText("");
		            nuevoLink.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e){
		        if (nuevoLink.getText().isEmpty() && frame==1) {
		        	nuevoLink.setForeground(Color.GRAY);
		        	nuevoLink.setText("http://...");
		        }
		    }
		});
		
		cajaABC = new JComboBox<String>(estadosABC);
		cajaABC.insertItemAt("Periodicos", 0);
		cajaABC.setSelectedIndex(0);
		cajaABC.setMaximumRowCount(13);
		cajaABC.setSize(120, 30);
		
		modelAbc = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
	        public Class<?> getColumnClass(int col) {
	            switch(col) {
	            	case 0: return int.class;
	            	default: return String.class;
	            }
	        }
			
			@Override
			public boolean isCellEditable(int row, int col) {
	             return false;
	        }
	    };
	    
	    modelAbc.addColumn("#");
	    modelAbc.addColumn("Descripcion");
	    
	    tablaAbc = new JTable(modelAbc);
	    tablaAbc.setRowHeight(25);
	    tablaAbc.getColumnModel().getColumn(0).setMaxWidth(35);
		tablaAbc.getColumnModel().getColumn(0).setResizable(false);
	    tablaAbc.getColumnModel().getColumn(1).setMaxWidth(445);
		tablaAbc.getColumnModel().getColumn(1).setResizable(false);
		tablaAbc.getTableHeader().setReorderingAllowed(false);
		
		scrollAbc = new JScrollPane(tablaAbc);
		
		panelAbc = new JPanel();
		panelAbc.setLayout(new BorderLayout());
		panelAbc.setSize(470, 277);
		panelAbc.add(scrollAbc, BorderLayout.CENTER);
		
		area = new JTextArea();
		area.setFont(nuevoLink.getFont());
		
		scrollInsert = new JScrollPane(area);
			
		panelInsert = new JPanel();
		panelInsert.setLayout(new BorderLayout());
		panelInsert.setBounds(10, 10, 340, 240);
		panelInsert.add(scrollInsert, BorderLayout.CENTER);
		
		insert = new JButton("Agregar");
		insert.setBounds(490, 9, 120, 32);
		
		delete = new JButton("Borrar");
		delete.setBounds(10, 258, 120, 30);
		
		ad = new JDialog(this, "ABC periodicos", true);
		ad.setLayout(null);
		ad.setSize(625, 80);
		ad.setLocationRelativeTo(null);
		ad.setLocation( ad.getY()+17, ad.getX()-170);
		ad.setResizable(false);
		
		abrir = new JFileChooser();
		abrir.setDialogTitle("Importar");
		abrir.setAcceptAllFileFilterUsed(false);
		abrir.addChoosableFileFilter(new FileNameExtensionFilter("Texto CSV (*.csv)", "csv"));
		abrir.addChoosableFileFilter(new FileNameExtensionFilter("SQL (*.sql)", "sql"));
		SwingUtilities.updateComponentTreeUI(abrir);
		
		guardar = new JFileChooser();
		guardar.setDialogTitle("Exportar");
		guardar.setFileSelectionMode(1);		
		
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
		agregarMas.addActionListener(this);
	    borrar.addActionListener(this);
	    consultar.addActionListener(this);
	    importar.addActionListener(this);
	    exportar.addActionListener(this);
	    salir.addActionListener(this);
	    
	    start.addActionListener(this);
		stop.addActionListener(this);
		
		newomisas.addActionListener(this);
		omisas.addActionListener(this);
		
		cajaABC.addActionListener(this);
		insert.addActionListener(this);
		delete.addActionListener(this);
		nuevoLink.addActionListener(this);
		
		Tabla.addMouseListener(this);
		tablaAbc.addMouseListener(this);
		
		addWindowListener(this);
		ad.addWindowListener(this);
	}

	private void box(){
		caja=new JComboBox<String>(estadosABC);
		caja.insertItemAt("Todos", 0);
		caja.setSelectedIndex(0);
		caja.setMaximumRowCount(10);
		caja.setBounds(360, 14, 110, 27);
		add(caja);
	}	
	
	private void paginas(int estado){
		paginas.clear();
		if(estado==0){
			sql = "select nombre from periodicos";
		}else{
			sql = "select nombre from periodicos where id_estado = "+estado;
		}
		paginas = con.getQuery(sql);
	}

	private void buscar(){
		prep();
		
		if(!dos.isEmpty()){
			multiplo=dos.size();
			pags = paginas.size();
			hilos = new Thread[pags*multiplo];
			inicio = new crawl[pags*multiplo];
			barra.setMax(hilos.length);
			crawl.setZ(hilos.length);
			for(int i=0;i<dos.size();i++){
				System.out.println(dos.elementAt(i));
				for(int j=0;j<pags;j++){
					inicio[j+(i*pags)] = new crawl(paginas.elementAt(j), dos.elementAt(i), 0);
					hilos[j+(i*pags)] = new Thread(inicio[j+(i*pags)]);
					hilos[j+(i*pags)].start();
				}
			}
		}else{
			buscar.setEnabled(true);
			barra.setMax(barra.getMaximum());
			JOptionPane.showMessageDialog(null, "Busqueda finalizada\nEncontrados: 0");
		}
	}
	
	private void prep(){
		buscar.setEnabled(false);
		barra.setValue(0);
		crawl.clearVector();
		dos.clear();
		procesa(clave.getText());
	}
	
	private void procesa(String s){
		token = new StringTokenizer(s);
		String nuevo = "", comillas = "";
		boolean coma = false;
		while(token.hasMoreTokens()){
			nuevo = token.nextToken();
			if(nuevo.charAt(0)=='"'){
				comillas = nuevo.substring(1, nuevo.length());
				coma = true;
			}else{
				if(coma){
					comillas = comillas+" "+nuevo;
					if(comillas.charAt(comillas.length()-1)=='"'){
						comillas = comillas.substring(0, comillas.length()-1);
						if(comillas.length()>=2 && !omitidos.getVector().contains(maind.acentos(comillas)) && !dos.contains(comillas))
							dos.add(comillas);
						
						coma = false;
					}
				}else{
					if(nuevo.length()>=2 && !omitidos.getVector().contains(maind.acentos(nuevo)) && !dos.contains(nuevo))
						dos.add(nuevo);
				}								
			}			
		}
	}

	private void removeAd(){
		if(frame > 4)
			ad.setTitle("Reglas");
		else
			ad.setTitle("ABC periodicos");
		
		ad.remove(delete);
		ad.remove(cajaABC);
		ad.remove(insert);
		ad.remove(nuevoLink);
		ad.remove(panelAbc);
		ad.remove(panelInsert);
	}

	private void ordenaCaja(){
		paginas(cajaABC.getSelectedIndex());
		modelAbc.setRowCount(0);
		for(int i=0;i<paginas.size();i++){
			agregaAbc = new Vector<Object>(0, 1);
			agregaAbc.add(i+1);
			agregaAbc.add(paginas.elementAt(i));
			modelAbc.addRow(agregaAbc);
		}
	}
	
	private void ordenaOmitidos(){
		paginas.clear();
		modelAbc.setRowCount(0);
		sql = "select nombre from ignorar";
		paginas = con.getQuery(sql);
		for(int i=0;i<paginas.size();i++){
			agregaAbc = new Vector<Object>(0, 1);
			agregaAbc.add(i+1);
			agregaAbc.add(paginas.elementAt(i));
			modelAbc.addRow(agregaAbc);
		}
	}

	private void borraRegistro(){
		int res = JOptionPane.showConfirmDialog(null, "Realmente deseas eliminar\n"+paginas.elementAt(reglon)+" ?", "Advertencia", JOptionPane.YES_NO_OPTION);
		if (res == 0){
			sql = "DELETE FROM periodicos WHERE nombre = '"+paginas.elementAt(reglon)+"'";
			try{
				con.setQuery(sql);
				ordenaCaja();
				bandera=false;
				JOptionPane.showMessageDialog(null, "Pagina eliminada");
				System.out.println("Total de periodicos: "+con.getTotal());
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "Error al eliminar\n"+paginas.elementAt(reglon));
			}
		}		
	}
	
	private boolean agregarUrl(String s){
		if(validar.isValid(s)){
			int res = JOptionPane.showConfirmDialog(null, "Se agregara: \n"+s+" -> "+cajaABC.getSelectedItem()+"\nes correcto?", "Mensaje de confirmacion", JOptionPane.YES_NO_OPTION);
			if (res == 0){
				sql = "INSERT OR IGNORE INTO periodicos VALUES ('"+s+"', "+cajaABC.getSelectedIndex()+")";
				try {
					con.setQuery(sql);					
					System.out.println("Total de periodicos: "+con.getTotal());
				} catch (SQLException ex) {
					error=2;
					return false;
				}
				return true;
			}
		}else{
			error=1;
		}		
		return false;				
	}
	
	public static JMenuItem getNivelDos(){
		return nivelDos;
	}
		
	public static sqlite getSql(){
		return con;
	}
	
	public static barra getBarra(){
		return barra;
	}
	
	public static void reactivar(){
		buscar.setEnabled(true);
		if(((JCheckBoxMenuItem)nivelDos).getState()){
			((JCheckBoxMenuItem)nivelDos).setSelected(false);;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==buscar || e.getSource()==clave || e.getSource()==start){
			if(caja.getSelectedIndex()==0){
				int o = JOptionPane.showConfirmDialog(null, "Se buscara en TODOS los estados\nRealmente deseas continuar?", "Precaución", JOptionPane.YES_NO_OPTION);
				if (o == 0){
					prep();
					estado.reset();
					iniciadoTodos = true;					
					if(!dos.isEmpty()){
						if(!((JCheckBoxMenuItem)nivelDos).getState())
							((JCheckBoxMenuItem)nivelDos).setSelected(true);
						
						newMeta = new palabra[dos.size()];
						hiloNewMeta = new Thread[dos.size()];
						for(int i=0;i<dos.size();i++){
							newMeta[i] = new palabra(dos.elementAt(i));
							hiloNewMeta[i] = new Thread(newMeta[i]);
							hiloNewMeta[i].start();
						}
					}else{
						
					}
				}				
				return;
			}
			iniciado=true;
			paginas(caja.getSelectedIndex());
			buscar();			
			return;
		}
		if(e.getSource()==stop && iniciado){
			barra.setValue(barra.getMaximum());
			for(int i=0; i<hilos.length;i++){
				if(hilos[i].isAlive())
					hilos[i].stop();
			}
			iniciado=false;
			buscar.setEnabled(true);
			return;
		}
		if(e.getSource()==stop && iniciadoTodos){
			barra.setValue(barra.getMaximum());
			for(int i=0; i<hiloNewMeta.length;i++){
				newMeta[i].stopChild();
				if(hiloNewMeta[i].isAlive())
					hiloNewMeta[i].stop();
			}
			iniciadoTodos=false;
			buscar.setEnabled(true);
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
			removeAd();
			nuevoLink.setSize(340, 30);
			ad.add(nuevoLink);
			cajaABC.setLocation(360, 10);
			ad.add(cajaABC);
			ad.add(insert);
			ad.setSize(625, 78);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==agregarMas){
			frame=4;
			removeAd();
			cajaABC.setLocation(360, 10);
			ad.add(cajaABC);
			ad.add(insert);
			ad.add(panelInsert);
			ad.setSize(625, 288);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==borrar){
			frame=2;
			removeAd();			
			cajaABC.setLocation(10, 10);
			ad.add(cajaABC);
			panelAbc.setLocation(140, 10);
			ad.add(panelAbc);
			ad.add(delete);
			ordenaCaja();
			ad.setSize(625, 325);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==consultar){
			frame=3;
			/*try{
				System.out.println("Total de periodicos: "+con.getTotal());
			}catch(SQLException ex){
				
			}	*/
			removeAd();			
			cajaABC.setLocation(10, 10);
			ad.add(cajaABC);
			panelAbc.setLocation(140, 10);
			ad.add(panelAbc);
			ordenaCaja();
			ad.setSize(625, 325);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==newomisas){
			frame=5;
			removeAd();
			nuevoLink.setSize(470, 30);
			ad.add(nuevoLink);
			ad.add(insert);
			ad.setSize(625, 78);
			ad.setVisible(true);
			return;
		}
		if(e.getSource()==omisas){
			frame=6;
			removeAd();
			panelAbc.setLocation(10, 10);
			ad.add(panelAbc);
			ordenaOmitidos();
			ad.setSize(495, 325);
			ad.setVisible(true);
			return;
		}
		if((e.getSource()==insert || e.getSource()==nuevoLink ) && frame==1 && cajaABC.getSelectedIndex()!=0){
			error=0;
			if(agregarUrl(nuevoLink.getText())){
				nuevoLink.setText("");
				cajaABC.setSelectedIndex(0);
				JOptionPane.showMessageDialog(null, "Pagina agregada");
			}else{
				if(error==1)
					JOptionPane.showMessageDialog(null, "URL no válida");
				else
					if (error==2)
						JOptionPane.showMessageDialog(null, "Eror al agregar\n"+nuevoLink.getText());
			}
			return;
		}
		if((e.getSource()==insert || e.getSource()==nuevoLink ) && frame==1 && cajaABC.getSelectedIndex()==0){
			cajaABC.grabFocus();
			JOptionPane.showMessageDialog(null, "Seleccione un estado");
			return;
		}			
		if(e.getSource()==nuevoLink && cajaABC.getSelectedIndex()==0){
			cajaABC.grabFocus();
			return;
		}
		if(e.getSource()==insert && frame==4){
			paginas.clear();
			String erro;
			token = new StringTokenizer(area.getText());
			while(token.hasMoreTokens()){
				if(!agregarUrl(erro = token.nextToken())){
					paginas.add(erro);
				}
		    }
			if(paginas.size() != 0){
				JOptionPane.showMessageDialog(null, "Paginas agregadas, errores: "+ paginas.size());			
				for(int i=0;i<paginas.size(); i++){
					area.append(paginas.elementAt(i)+"\n");
				}				
			}else{
				JOptionPane.showMessageDialog(null, "Paginas agregadas");
				area.setText("");
			}
			cajaABC.setSelectedIndex(0);
		}
		if((e.getSource()==insert || e.getSource()==nuevoLink ) && frame==5){
			if(nuevoLink.getText().length()==0){
				JOptionPane.showMessageDialog(null, "Rellene el campo de texto");
				nuevoLink.grabFocus();
				nuevoLink.selectAll();
				return;
			}
			int res = JOptionPane.showConfirmDialog(null, "La palabra >>"+nuevoLink.getText()+"<< sera omitida\nes correcto?", "Mensaje de confirmacion", JOptionPane.YES_NO_OPTION);
			if (res == 0){
				sql = "INSERT OR IGNORE INTO ignorar VALUES ('"+nuevoLink.getText()+"')";
				try {
					con.setQuery(sql);
					nuevoLink.setText("");
					JOptionPane.showMessageDialog(null, "Palabra agregada");
				} catch (SQLException ex) {
					System.out.println("Error al agregar");
				}
			}
			return;
		}
		if(e.getSource()==cajaABC && (frame==2 || frame==3)){
			bandera=false;
			ordenaCaja();			
			return;
		}
		if(e.getSource()==delete){
			if(cajaABC.getSelectedIndex()==0){
				JOptionPane.showMessageDialog(null, "Seleccione un estado");
				return;
			}			
			
			if(!bandera){
				JOptionPane.showMessageDialog(null, "Seleccione un link");
				return;
			}
			
			borraRegistro();
			return;
		}
		if(e.getSource()==importar){			
			returnVal = abrir.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
				archivoGuardar = abrir.getSelectedFile();
			else
				return;
			//importExport . . . import(archivoGuardar.getPath());
		}
		if(e.getSource()==exportar){			
			returnVal =  guardar.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
				archivoGuardar = guardar.getSelectedFile();
			else
				return;
			//importExport . . . import(archivoGuardar.getPath());
			//Sera con el boton (. . .) para que seleccione la ruta en el dialog y se puede agregar condiciones de guardado
			//importar no usara dialog , pero tendra mas validaciones ya que al leer, y procesar el transact debe ser valido
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2){
			if (e.getSource() == Tabla) {
				otrasUrls = crawl.getVector(1);
				try{
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + otrasUrls.elementAt(Tabla.getSelectedRow()));
				}catch(Exception err){
					JOptionPane.showMessageDialog(null,"Error: "+err);
				}
				return;
			}
			if(e.getSource()==tablaAbc && frame==2){
				reglon = tablaAbc.getSelectedRow();
				bandera=true;
				borraRegistro();
				return;
			}
			if(e.getSource()==tablaAbc && frame==3){
				try{
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + paginas.elementAt(tablaAbc.getSelectedRow()));
				}catch(Exception err){
					JOptionPane.showMessageDialog(null,"Error: "+err);
				}
				return;
			}
			if(e.getSource()==tablaAbc && frame==6){
				reglon = tablaAbc.getSelectedRow();
				int res = JOptionPane.showConfirmDialog(null, "Realmente deseas eliminar >>"+paginas.elementAt(reglon)+"<< ?", "Advertencia", JOptionPane.YES_NO_OPTION);
				if (res == 0){
					sql = "DELETE FROM ignorar WHERE nombre = '"+paginas.elementAt(reglon)+"'";
					try{
						con.setQuery(sql);
						ordenaOmitidos();
						JOptionPane.showMessageDialog(null, "Registro eliminado");
					}catch(Exception ex){
						JOptionPane.showMessageDialog(null, "Error al eliminar\n"+paginas.elementAt(reglon));
					}
				}
				return;
			}
			return;
		}
		if(e.getSource()==tablaAbc && frame==2){
			reglon = tablaAbc.getSelectedRow();
			bandera=true;
			return;
		}
	}
	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0){}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}
	
	public void windowActivated(WindowEvent arg0){}
	public void windowClosed(WindowEvent arg0){}
	public void windowClosing(WindowEvent arg0){
		if(arg0.getSource()==this){
			int o = JOptionPane.showConfirmDialog(null, "Realmente desea salir?", "Advertencia", JOptionPane.YES_NO_OPTION);
			if (o == 0){
				con.dbClose();
				System.exit(0);
			}
		}
		if(arg0.getSource()==ad){
			nuevoLink.setText("");
			area.setText("");
			cajaABC.setSelectedIndex(0);
			modelAbc.setRowCount(0);
			bandera=false;
		}
	}
	public void windowDeactivated(WindowEvent arg0){}
	public void windowDeiconified(WindowEvent arg0){}
	public void windowIconified(WindowEvent arg0){}
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
		new interfaz();
	}
	
	public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {
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
}