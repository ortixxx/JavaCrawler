package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import bdex.sqlite;
import org.apache.commons.validator.routines.UrlValidator;
import todos.estado;
import todos.palabra;

public class interfaz extends JFrame implements ActionListener, MouseListener, WindowListener{
	private static final long serialVersionUID = 1L;
	static dialog ad;
	static JMenuBar menuBar;
	static JMenu sistem, search, rules;
	static JMenuItem consultar, agregar, agregarMas, borrar, importar, exportar, salir, start, stop, nivelDos, omisas, newomisas;
	static Vector<String> paginas = new Vector<String>(0, 1);
	static Vector<Object> agregaAbc;
	static boolean bandera=false, iniciado=false, iniciadoTodos=false;
	static int multiplo=0, pags=0, frame = 0, reglon, error=0;
	static JTextField clave, nuevoLink, sorter;
	static JTextArea area, Output;
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
	static importExport user = new importExport();
	static Desktop desktop = java.awt.Desktop.getDesktop();
	Thread [] hilos, aux, hiloNewMeta;
	crawl [] inicio;
	palabra [] newMeta;
	crawl maind = new crawl();
	Vector<String> otras = new Vector<String>(), otrasUrls = new Vector<String>(), dos = new Vector<String>(0, 1);
	JTable Tabla, tablaAbc;
	JScrollPane Consulta, scrollAbc, scrollInsert;
	static JScrollPane JspOutput;
	static JPanel PanelTabla, panelAbc, panelInsert;
	DefaultTableModel modelAbc;
	MultiLineCellRenderer aiuda;
	
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
	    importar = new JMenuItem("Importar", new ImageIcon("img/import-icon.png"));
	    exportar = new JMenuItem("Exportar", new ImageIcon("img/export-icon.png"));
	    salir = new JMenuItem("Salir", new ImageIcon("img/delete-icon.png"));
	    
	    search = new JMenu("Busqueda");
	    start = new JMenuItem("Iniciar", new ImageIcon("img/Start-icon.png"));
	    stop  = new JMenuItem("Detener", new ImageIcon("img/Stop-icon.png"));
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
		Tabla.setDefaultRenderer(String.class, aiuda = new MultiLineCellRenderer());
		
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
		panelAbc.setSize(470, 237);
		panelAbc.add(scrollAbc, BorderLayout.CENTER);
		
		area = new JTextArea();
		area.setFont(nuevoLink.getFont());
		
		scrollInsert = new JScrollPane(area);
			
		panelInsert = new JPanel();
		panelInsert.setLayout(new BorderLayout());
		panelInsert.setBounds(10, 10, 470, 277);
		panelInsert.add(scrollInsert, BorderLayout.CENTER);
		
		insert = new JButton("Agregar");
		insert.setSize(120, 30);
		
		delete = new JButton("Borrar");
		delete.setBounds(490, 258, 120, 30);
		
		sorter = new JTextField();
		sorter.setBounds(10, 10, 470, 30);
		
		ad = new dialog();		
		
		abrir = new JFileChooser();
		abrir.setDialogTitle("Importar");
		abrir.setAcceptAllFileFilterUsed(false);
		abrir.addChoosableFileFilter(new FileNameExtensionFilter("SQL (*.sql)", "sql"));
		//abrir.addChoosableFileFilter(new FileNameExtensionFilter("Texto CSV (*.csv)", "csv"));		
		SwingUtilities.updateComponentTreeUI(abrir);
		
		guardar = new JFileChooser();
		guardar.setDialogTitle("Exportar");
		guardar.setSelectedFile(new File("periodicos.sql"));
		
		Output = new JTextArea();
		Output.setFont(nuevoLink.getFont());
		DefaultCaret caret = (DefaultCaret)Output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	 	Output.setEditable(false);
	 	
	 	JspOutput = new JScrollPane(Output);
	 	JspOutput.setBounds(5, 600, 785, 60);
	 	add(JspOutput);
		
		listeners();
		setIconImage(new ImageIcon("loge.png").getImage());
		setSize(800,715);
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
		
		sorter.addActionListener(this);
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
			if(dos.size()>6){
				int res = JOptionPane.showConfirmDialog(null, "La cantidad de palabras es: "+dos.size()+", \nSe recomienda maximo 6\nDesea continuar?", "Advertencia", JOptionPane.YES_NO_OPTION);
				if (res == 1){
					reactivar();
					barra.setMax(barra.getMaximum());
					JOptionPane.showMessageDialog(null, "Busqueda finalizada\nEncontrados: 0");
					return;
				}
			}
			multiplo=dos.size();
			pags = paginas.size();
			hilos = new Thread[pags*multiplo];
			inicio = new crawl[pags*multiplo];
			barra.setMax(hilos.length);
			Output.append("Busqueda inicial: "+hilos.length+"\n");
			crawl.setZ(hilos.length);
			for(int i=0;i<dos.size();i++){
				for(int j=0;j<pags;j++){
					inicio[j+(i*pags)] = new crawl(paginas.elementAt(j), dos.elementAt(i), 0);
					hilos[j+(i*pags)] = new Thread(inicio[j+(i*pags)]);
					hilos[j+(i*pags)].start();
				}
			}
		}else{
			reactivar();
			barra.setMax(barra.getMaximum());
			JOptionPane.showMessageDialog(null, "Busqueda finalizada\nEncontrados: 0");
		}
	}
	
	private void prep(){
		buscar.setEnabled(false);
		start.setEnabled(false);
		barra.setValue(0);
		crawl.clearVector();
		aiuda.emptyPoint();
		dos.clear();
		Output.setText("");
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
	
	private void busqueda(String like, int estado){
		paginas.clear();
		modelAbc.setRowCount(0);
		if(estado==0){
			sql = "select nombre from periodicos where nombre like '%"+like+"%'";
		}else{
			sql = "select nombre from periodicos where nombre like '%"+like+"%' and id_estado = "+estado;
		}
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
				Output.append("Total de periodicos: "+con.getTotal()+"\n");
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
					Output.append("Total de periodicos: "+con.getTotal()+"\n");
				} catch (SQLException ex) {
					importExport.logErrores.error(ex.toString());
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
	
	public static boolean getTodos(){
		return iniciadoTodos;
	}
	
	public static JTextArea getOut(){
		return Output;
	}
	
	public static String[] getEstados(){
		return estadosABC;
	}
	
	public static void reactivar(){
		buscar.setEnabled(true);
		start.setEnabled(true);
		importExport.logErrores.saveLog();
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
					if(dos.size()>6){
						int res = JOptionPane.showConfirmDialog(null, "La cantidad de palabras es: "+dos.size()+", \nSe recomienda maximo 6\nDesea continuar?", "Advertencia", JOptionPane.YES_NO_OPTION);
						if (res == 1){
							reactivar();
							barra.setMax(barra.getMaximum());
							JOptionPane.showMessageDialog(null, "Busqueda finalizada\nEncontrados: 0");
							return;
						}
					}
					estado.reset();
					iniciadoTodos = true;					
					if(!dos.isEmpty()){						
						if(!((JCheckBoxMenuItem)nivelDos).getState())
							((JCheckBoxMenuItem)nivelDos).setSelected(true);
						
						newMeta = new palabra[dos.size()];
						hiloNewMeta = new Thread[dos.size()];
						try {
							Output.append("Busqueda inicial: "+con.getTotal()*dos.size()+"\n");							
						} catch (SQLException e1) {}
						
						for(int i=0;i<dos.size();i++){
							newMeta[i] = new palabra(dos.elementAt(i));
							hiloNewMeta[i] = new Thread(newMeta[i]);
							hiloNewMeta[i].start();
						}
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
			reactivar();
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
			reactivar();
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
			ad.removeAd(frame=1);
			return;
		}		
		if(e.getSource()==borrar){					
			ordenaCaja();
			ad.removeAd(frame=2);	
			return;
		}
		if(e.getSource()==consultar){			
			try{
				Output.append("Total de periodicos: "+con.getTotal()+"\n");
			}catch(SQLException ex){}
			ordenaCaja();
			ad.removeAd(frame=3);			
			return;
		}
		if(e.getSource()==agregarMas){
			ad.removeAd(frame=4);
			return;
		}
		if(e.getSource()==newomisas){
			ad.removeAd(frame=5);
			return;
		}
		if(e.getSource()==omisas){
			ordenaOmitidos();
			ad.removeAd(frame=6);			
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
					Output.append("Error al agregar"+"\n");
				}
			}
			return;
		}
		if(e.getSource()==cajaABC && (frame==2 || frame==3)){
			if(sorter.getText().length()>0){
				busqueda(sorter.getText(), cajaABC.getSelectedIndex());
			}else{
				bandera=false;
				ordenaCaja();	
			}					
			return;
		}
		if(e.getSource()==sorter){
			busqueda(sorter.getText(), cajaABC.getSelectedIndex());
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
			abrir.showOpenDialog(this);
			if ((archivoGuardar = abrir.getSelectedFile()) != null){
				user.importar(archivoGuardar.getPath());
			}
			return;
		}
		if(e.getSource()==exportar){	
			guardar.showSaveDialog(null);
			if ((archivoGuardar = guardar.getSelectedFile()) != null){
				user.exportar(archivoGuardar.getPath());
			}
			return;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2){
			if (e.getSource() == Tabla) {
				otrasUrls = crawl.getVector(1);
				try{					
					desktop.browse(java.net.URI.create(otrasUrls.elementAt(Tabla.getSelectedRow())));
					aiuda.setHighlighted(Tabla.getSelectedRow(), 2, true);
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
					desktop.browse(java.net.URI.create(paginas.elementAt(tablaAbc.getSelectedRow())));
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
				reactivar();
				System.exit(0);
			}
		}
		if(arg0.getSource()==ad){
			nuevoLink.setText("");
			area.setText("");
			sorter.setText("");
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
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
		
		public Set<Point> highlightedCells = new HashSet<Point>();

	    void setHighlighted(int r, int c, boolean highlighted)
	    {
	        if (highlighted)
	        {
	            highlightedCells.add(new Point(r,c));
	        }
	        else
	        {
	            highlightedCells.remove(new Point(r,c));
	        }
	    }

	    private boolean isHighlighted(int r, int c){
	        return highlightedCells.contains(new Point(r,c));
	    }
	    
	    public void emptyPoint(){
	    	highlightedCells.clear();
	    }
	    
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			if (isSelected) {
			      setForeground(table.getSelectionForeground());
			      setBackground(table.getSelectionBackground());
			} else {
			      setForeground(table.getForeground());
			      setBackground(table.getBackground());
			}			
		    
		    if(isHighlighted(row,  column)){
	            setForeground(table.getSelectionForeground());
	            setBackground(Color.LIGHT_GRAY);
	        }
		    
		    setFont(table.getFont());
			setText((value == null) ? "" : value.toString());
	        return this;
		}
	}
}