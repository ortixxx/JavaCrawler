import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

class usuario extends JFrame implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	static String[] paginas;
	static int multiplo=0;
	static Bar barra;
	Thread [] hilos;
	Main [] inicio;
	Main model = new Main();
	Vector<String> otras = new Vector<String>(), otrasUrls = new Vector<String>(), dos = new Vector<String>(0, 1);
	static JButton buscar, detener;
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
		paginas = new String []{
				"https://www.sintesis.mx/plaza/Tlaxcala",
				"https://www.elsoldetlaxcala.com.mx/",
				"http://www.masnoticias.net/",
				"http://expreso.com.mx/",
				"https://www.noticiasdelsoldelalaguna.com.mx/",
				"http://eldigital.com.mx/"};
		
		clave = new JTextField("siniestro");
		clave.setBounds(5, 5, 380, 25);
		clave.selectAll();
		add(clave);
		clave.addActionListener(this);
		
		buscar = new JButton("Buscar");
		buscar.setBounds(390, 4, 80, 27);
		add(buscar);
		buscar.addActionListener(this);
		
		detener = new JButton("Detener");
		detener.setBounds(475, 4, 80, 27);
		detener.setEnabled(false);
		add(detener);
		detener.addActionListener(this);
		
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
		PanelTabla.setBounds(5, 40, 785, 426);
		PanelTabla.add(Consulta, BorderLayout.CENTER);
		add(PanelTabla);
		
		setIconImage(new ImageIcon("loge.png").getImage());
		setSize(800,500);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		try{
			if(e.getSource()==buscar || e.getSource()==clave){
				buscar();			
				return;
			}
		}catch(Exception exe){
		}
		if(e.getSource()==detener){
			buscar.setEnabled(true);
			detener.setEnabled(false);
			barra.setValue(barra.getMaximum());
			for(int i=0; i<hilos.length;i++){
				if(hilos[i].isAlive())
					hilos[i].stop();
			}
		}
	}
	
	public void buscar(){
		if(actual.equals(clave.getText())){
			JOptionPane.showMessageDialog(null, "Busqueda finalizada\nResultados: "+ (Main.getX()-1));
			return;
		}
		
		detener.setEnabled(true);
		buscar.setEnabled(false);
		
		actual = clave.getText();
		Main.clearVector();
		dos.clear();
		procesa(actual);
		
		if(!dos.isEmpty()){
			multiplo=dos.size();
			hilos = new Thread[paginas.length*multiplo];
			inicio = new Main[paginas.length*multiplo];
			barra.setValue(0);
			barra.setMax(paginas.length*multiplo);
			for(int i=0;i<dos.size();i++){
				for(int j=0;j<paginas.length;j++){
					inicio[j+(i*paginas.length)] = new Main(paginas[j], dos.elementAt(i));
					hilos[j+(i*paginas.length)] = new Thread(inicio[j+(i*paginas.length)]);
					hilos[j+(i*paginas.length)].start();
				}
			}
		}
	}

	public void procesa(String s) {
		int bandera = 0, comillas = 0;
		String nuevo = "";
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)==' ' && bandera == 0)
				continue;
			if(Character.isAlphabetic(s.charAt(i))){
				nuevo += s.charAt(i);
				continue;
			}else{
				if(s.charAt(i)=='"' && comillas<1){
					if(nuevo.length()!=0){
						if(nuevo.length()<3){
							JOptionPane.showMessageDialog(null, "Palabra muy corta\n>>Minimo 3 letras<<");
							return;
						}
						dos.add(nuevo);
						nuevo="";
					}
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
					if(nuevo.length()<3){
						JOptionPane.showMessageDialog(null, "Palabra muy corta\n>>Minimo 3 letras<<");
						return;
					}
					dos.add(nuevo);
					nuevo="";
				}
			}
		}
		if(nuevo.length()!=0){
			if(nuevo.length()<3){
				JOptionPane.showMessageDialog(null, "Palabra muy corta\n>>Minimo 3 letras<<");
				return;
			}
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
		return paginas.length;
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
	
	public static int getMultiplo(){
		return multiplo;
	}
}