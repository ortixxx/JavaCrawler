import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import bdex.sqlite;

class usuario extends JFrame implements ActionListener, MouseListener, WindowListener{
	private static final long serialVersionUID = 1L;
	static JMenuBar menuBar;
	static JMenu fileMenu;
	static JMenuItem newMenuItem;
	static Vector<String> paginas = new Vector<String>(0, 1), estados = new Vector<String>(0, 1);
	static int multiplo=0, pags=0;
	static Bar barra;
	static JButton buscar, detener;
	static JComboBox<String> caja;
	static sqlite queryPeriodicos = new sqlite(), queryEstados = new sqlite();
	Thread [] hilos;
	Main [] inicio;
	Main model = new Main();
	Vector<String> otras = new Vector<String>(), otrasUrls = new Vector<String>(), dos = new Vector<String>(0, 1);
	
	JTextField clave;
	String actual = " ";
	JTable Tabla;
	JScrollPane Consulta;
	JPanel PanelTabla;
	
	public usuario(){
		super("Gallbo: Motor de busqueda");
		interfaz();
	}
	
	public void interfaz(){
		menuBar = new JMenuBar();
	    fileMenu = new JMenu("File");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);
	    
	    newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
	    fileMenu.add(newMenuItem);
	    
	    setJMenuBar(menuBar);
	    
		clave = new JTextField("siniestro");
		clave.setBounds(5, 15, 350, 25);
		clave.selectAll();
		add(clave);
		clave.addActionListener(this);
		
		buscar = new JButton("Buscar");
		buscar.setBounds(475, 14, 80, 27);
		add(buscar);
		buscar.addActionListener(this);
		
		detener = new JButton("Detener");
		detener.setBounds(475, 14, 80, 27);
		detener.setEnabled(false);
		//add(detener);
		detener.addActionListener(this);
		
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
		Tabla.addMouseListener(this);
		Consulta = new JScrollPane(Tabla);
		
		PanelTabla = new JPanel();
		PanelTabla.setLayout(new BorderLayout());
		PanelTabla.setBounds(5, 50, 785, 546);
		PanelTabla.add(Consulta, BorderLayout.CENTER);
		add(PanelTabla);
		
		setIconImage(new ImageIcon("loge.png").getImage());
		setSize(800,650);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	public void box(){
		String sql = "select nombre from estados";
		
		estados = queryEstados.getQuery(sql);
		
		caja=new JComboBox<String>(estados);
		caja.insertItemAt("Estados", 0);
		caja.setSelectedIndex(0);
		caja.setMaximumRowCount(10);
		caja.setBounds(360, 14, 110, 27);
		add(caja);
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		try{
			if(e.getSource()==buscar || e.getSource()==clave){
				if(caja.getSelectedIndex()==0){
					JOptionPane.showMessageDialog(null, "Seleccione un estado");
					return;
				}
				paginas(caja.getSelectedIndex());
				buscar();			
				return;
			}
		}catch(Exception exe){
		}
		if(e.getSource()==detener){
			buscar.setEnabled(true);
			//detener.setEnabled(false);
			barra.setValue(barra.getMaximum());
			for(int i=0; i<hilos.length;i++){
				if(hilos[i].isAlive())
					hilos[i].stop();
			}
		}
	}
	
	public void paginas(int estado){
		paginas.clear();
		String sql = "select nombre from periodicos where id_estado = "+estado;
		paginas = queryPeriodicos.getQuery(sql);
	}

	public void buscar(){
		//detener.setEnabled(true);
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
				//System.out.println(dos.elementAt(i));
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

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {		
	}

	public void mousePressed(MouseEvent arg0) {		
	}

	public void mouseReleased(MouseEvent arg0) {		
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
	
	public static JButton getBoton(int i){
		if(i==0)
			return buscar;
		else
			return detener;
	}
	
	public static int getMultiplo(){
		return multiplo;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		int opcion = JOptionPane.showConfirmDialog(null, "Realmente deseas salir?", "Advertencia", JOptionPane.YES_NO_OPTION);
		if (opcion == 0){
			queryPeriodicos.dbClose();
			queryEstados.dbClose();
			System.exit(0);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
	
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