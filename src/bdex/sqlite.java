package bdex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class sqlite {
	Vector<String> aux = new Vector<String>(0, 1);
    Connection connection = null;
    Statement statement;
    ResultSet rs;
    
	public sqlite(){    
	    try{
	    	Class.forName("org.sqlite.JDBC");
	    	connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
	    	statement = connection.createStatement();
	    	//statement.executeUpdate("create table if not exists estados (id_estado integer, nombre string)");
	    	statement.executeUpdate("create table if not exists periodicos (nombre string, id_estado integer, UNIQUE(nombre))");
	    	rs = statement.executeQuery("select count(*) from periodicos");
	    	if(rs.getInt(1)==0){
	    		//statement.executeUpdate("insert into estados values (1, 'Aguascalientes'),(2, 'Baja California'), (3, 'Baja California Sur'), (4, 'Campeche'), (5, 'Coahuila'),(6, 'Colima'), (7, 'Chiapas'), (8, 'Chihuahua'), (9, 'CDMX'), (10, 'Durango'), (11, 'Guanajuato'), (12, 'Guerrero'), (13, 'Hidalgo'), (14, 'Jalisco'), (15, 'Edo de M�xico'), (16, 'Michoac�n'), (17, 'Morelos'), (18, 'Nayarit'), (19, 'Nuevo Le�n'), (20, 'Oaxaca'), (21, 'Puebla'), (22, 'Quer�taro'), (23, 'Quintana Roo'), (24, 'San Luis Potos�'), (25, 'Sinaloa'), (26, 'Sonora'), (27, 'Tabasco'), (28, 'Tamaulipas'), (29, 'Tlaxcala'), (30, 'Veracruz'), (31, 'Yucat�n'), (32, 'Zacatecas');");
	    		statement.executeUpdate("insert into periodicos values ('http://eleconomista.com.mx/estados/', 9), ('http://www.jornada.unam.mx/ultimas/estados/', 9), ('http://www.proceso.com.mx/', 14), ('http://www.proceso.com.mx/category/nacional', 9), ('http://www.milenio.com/estados/', 9), ('http://www.cronica.com.mx/', 9), ('http://www.eluniversal.com.mx/estados', 9), ('http://www.elsoldetoluca.com.mx/', 16), ('http://8columnas.com.mx/', 16), ('http://www.elsoldehidalgo.com.mx/', 13), ('http://www.elsoldetulancingo.com.mx/', 13), ('http://www.elsoldecuautla.com.mx/', 17), ('http://www.elsoldecuernavaca.com.mx/', 17), ('https://www.diariodequeretaro.com.mx/', 22),"
	    			+ "('http://www.elsoldesanjuandelrio.com.mx/', 22), ('http://www.lapoliciaca.com/noticias/aguascalientes/', 1), ('http://www.lapoliciaca.com/noticias/baja-california-norte/', 2), ('http://www.lapoliciaca.com/noticias/baja-california-sur/', 3), ('http://www.lapoliciaca.com/noticias/campeche/', 4), ('http://www.lapoliciaca.com/noticias/coahuila/', 5), ('http://www.lapoliciaca.com/noticias/colima/', 6), ('http://www.lapoliciaca.com/noticias/chiapas/', 7), ('http://www.lapoliciaca.com/noticias/chihuahua/', 8), ('http://www.lapoliciaca.com/noticias/distrito-federal/', 9), ('http://www.lapoliciaca.com/noticias/durango/', 10),"
	    			+ "('http://www.lapoliciaca.com/noticias/guanajuato/', 11), ('http://www.lapoliciaca.com/noticias/guerrero/', 12), ('http://www.lapoliciaca.com/noticias/hidalgo/', 13), ('http://www.lapoliciaca.com/noticias/jalisco/', 14), ('http://www.lapoliciaca.com/noticias/estado-de-mexico/', 15), ('http://www.lapoliciaca.com/noticias/michoacan/', 16), ('http://www.lapoliciaca.com/noticias/morelos/', 17), ('http://www.lapoliciaca.com/noticias/nayarit/', 18), ('http://www.lapoliciaca.com/noticias/nuevo-leon/', 19), ('http://www.lapoliciaca.com/noticias/oaxaca/', 20), ('http://www.lapoliciaca.com/noticias/puebla/', 21), ('http://www.lapoliciaca.com/noticias/queretaro/', 22),"
	    			+ "('http://www.lapoliciaca.com/noticias/quintana-roo/', 23), ('http://www.lapoliciaca.com/noticias/san-luis-potosi/', 24), ('http://www.lapoliciaca.com/noticias/sinaloa/', 25), ('http://www.lapoliciaca.com/noticias/sonora/', 26), ('http://www.lapoliciaca.com/noticias/tabasco/', 27), ('http://www.lapoliciaca.com/noticias/tamaulipas/', 28), ('http://www.lapoliciaca.com/noticias/tlaxcala/', 29), ('http://www.lapoliciaca.com/noticias/veracruz/', 30), ('http://www.lapoliciaca.com/noticias/yucatan/', 31),  ('http://www.lapoliciaca.com/noticias/zacatecas/', 32), ('http://www.sintesis.mx/plaza/Tlaxcala', 29), ('https://www.elsoldetlaxcala.com.mx/', 29),"
	    			+ "('http://www.masnoticias.net/', 19), ('http://expreso.com.mx/', 26),  ('http://www.elsiglodetorreon.com.mx/?pub=1', 5), ('http://www.elsiglodetorreon.com.mx/nacional/', 5), ('http://www.siglo.mx/coahuila/', 5), ('http://www.elsiglodedurango.com.mx/', 10), ('http://www.noticiasdelsoldelalaguna.com.mx/', 5),  ('http://eldigital.com.mx/', 8), ('http://www.publimetro.com.mx/mx/', 9), ('http://www.publimetro.com.mx/mx/ciudad/', 9), ('http://elmananadevalles.com.mx/principal/', 24), ('http://planoinformativo.com/index_plano.php', 24), ('http://www.elsoldesanluis.com.mx/', 24), ('http://elheraldoslp.com.mx/', 24), ('http://www.hoytamaulipas.net/', 28),"
	    			+ "('http://www.enlineadirecta.info/', 28), ('http://lineadirectaportal.com/', 25), ('http://astromante.com/sitio/', 28), ('https://www.elsoldetampico.com.mx/', 28), ('http://www.primerahora.com.mx/', 28), ('http://www.cuartopoderdetamaulipas.com.mx/', 28), ('http://laregiontam.com.mx/noticias/', 28), ('http://www.lacronica.com/', 2), ('http://www.el-mexicano.com.mx/inicio.htm', 2), ('http://www.elvigia.net/', 2), ('https://www.lavozdelafrontera.com.mx/', 2), ('http://www.siglo21.com.mx/', 2), ('http://www.tribunadeloscabos.com.mx/', 3), ('http://www.elsudcaliforniano.com.mx/', 3), ('https://www.elsoldezacatecas.com.mx/', 32),"
	    			+ "('http://lasnoticiasya.com/municipios-zac/', 32), ('http://www.periodicomirador.com/secciones/zacatecas/', 32), ('http://www.elsoldelcentro.com.mx/', 1), ('http://www.aguasdigital.com/', 1), ('http://www.hidrocalidodigital.com/', 1), ('https://www.elheraldodechihuahua.com.mx/', 8), ('http://www.elsoldeparral.com.mx/', 8), ('http://laopcion.com.mx/', 8), ('http://eldiariodechihuahua.mx/', 8), ('http://www.periodicoelmexicano.com.mx/', 8), ('http://www.elsoldesinaloa.com.mx/', 25), ('http://www.elsoldemazatlan.com.mx/', 25), ('http://www.elautonomo.mx/', 26), ('http://www.kioscomayor.com/', 26), ('http://tribuna.com.mx/', 26),"
	    			+ "('http://www.elimparcial.com/', 26), ('http://www.eldiariodesonora.com.mx/?r=1&Ancho=1440', 26), ('http://www.tribunadesanluis.com.mx/', 26), ('http://www.ehui.com/', 26), ('http://www.entornoinformativo.com.mx/', 26), ('http://lasnoticiasdesonora.com/', 26), ('http://periodicovictoria.mx/', 10), ('http://www.elsoldedurango.com.mx/', 10), ('http://www.diariodechiapas.com/landing/', 7), ('http://www.diariodelsur.com.mx/', 7), ('http://www.elheraldodechiapas.com.mx/', 7), ('http://sipse.com/novedades/', 23), ('http://laverdadnoticias.com/noticias-de-hoy-desde-quintanaroo/', 23), ('http://www.la-verdad.com.mx/tabasco.html', 27),"
	    			+ "('http://www.rumbonuevo.com.mx/', 27), ('http://www.diariopresente.com.mx/', 27), ('http://www.elheraldodetabasco.com.mx/', 27), ('http://www.tabascohoy.com/noticias', 27), ('http://www.elmundodecordoba.com/', 30), ('http://www.laopinion.net/', 30), ('http://www.elsoldeorizaba.com.mx/', 30), ('http://www.elmartinense.com.mx/', 30), ('http://azdiario.mx/', 30), ('https://www.diariodexalapa.com.mx/', 30), ('http://www.liberal.com.mx/', 30), ('http://www.elsoldecordoba.com.mx/', 30), ('http://www.elchiltepin.com/', 30), ('http://www.cronicadelpoder.com/', 30), ('http://www.elsoldesalamanca.com.mx/', 11), ('http://www.elsoldeirapuato.com.mx/', 11),"
	    			+ "('http://elheraldodetuxpan.com.mx/', 30), ('http://yucatan.com.mx/', 31), ('http://www.am.com.mx/guanajuato', 11), ('http://www.elsoldeleon.com.mx/', 11), ('http://www.periodicoexpress.com.mx', 18), ('http://www.diario21.com.mx/', 12), ('http://www.elsoldeacapulco.com.mx/', 12), ('http://suracapulco.mx/', 12), ('http://www.lajornadaguerrero.com.mx/2017/02/15/index.php', 12), ('http://blogdiariodeguerrero.blogspot.mx/', 12), ('http://www.diariodecolima.com/', 6), ('http://www.eloccidental.com.mx/', 14), ('http://www.informador.com.mx/', 14), ('http://www.notisistema.com/', 14), ('http://www.aztecanoticias.com.mx/index.html', 9),"
	    			+ "('http://noticieros.televisa.com/', 9), ('http://www.proyecto40.com/', 9), ('http://oncenoticias.tv/index.php', 9),  ('http://www.notiarandas.com/', 9), ('http://www.radioformula.com.mx/', 9), ('http://www.lajornadadeoriente.com.mx/?ciudad_puebla=1', 21), ('http://www.lajornadadeoriente.com.mx/?ciudad_tlaxcala=1', 29),  ('https://www.elsoldepuebla.com.mx/', 21), ('http://www.vanguardia.com.mx/', 5), ('http://www.elmanana.com.mx/', 28), ('http://www.rednoroeste.com/', 8), ('http://tiempo.com.mx/', 8),  ('http://www.lapolaka.com/', 8), ('http://www.elfronterizo.com.mx/', 8), ('http://www.noroeste.com.mx/', 25), ('http://riodoce.mx/', 25),"
	    			+ "('http://www.uniradionoticias.com/inicio', 26),  ('http://contactohoy.com.mx/', 10), ('http://expresochiapas.com/noticias/', 7), ('http://noticaribe.com.mx/category/quintana-roo/', 23), ('http://diariodelatarde.com.mx/', 27), ('http://imagendelgolfo.mx/', 30), ('http://tiempodigital.mx/category/secciones/oaxaca/', 20), ('http://www.periodicoelsur.com/', 14), ('http://tribunacampeche.com/', 4),  ('http://www.elsur.mx/', 4), ('http://www.cronicacampeche.com/', 4), ('http://campechehoy.mx/index2.php', 4), ('http://elorbe.com/', 7), ('http://www.elpueblo.com/', 8), ('http://www.vertebracion.com/news2016/index.php/news', 10),"
	    			+ "('http://contextodedurango.com.mx/noticias/', 10), ('http://www.elfarodelacostachica.com/', 12), ('http://www.diariodezihuatanejo.mx/', 12), ('http://agenciainfomania.com/', 16), ('http://www.elsoldezamora.com.mx/', 16), ('http://intoleranciadiario.com/', 21), ('http://imagenpoblana.com/', 21), ('http://rotativo.com.mx/', 22), ('http://www.elreporterodelacomunidad.com/', 26), ('http://www.elcorreodetabasco.com.mx/', 27), ('http://elmercurio.com.mx/', 28),  ('http://www.elbravo.mx/', 28), ('http://pulsored.com.mx/web/', 29), ('http://www.elsoldetijuana.com.mx/', 2), ('http://www.yucatannoticias.com/', 31), ('http://www.carmenenlinea.com/', 4),"
	    			+ "('http://www.comunicacampeche.com/Php/seccionlocal.php', 4), ('http://diario.mx/', 8), ('http://www.durangoaldia.com/', 10), ('http://pueblo-guerrero.sytes.net/', 12), ('http://cambiodemichoacan.com.mx/', 16), ('http://www.diariodemorelos.com/noticias/', 17), ('http://elporvenir.mx/', 19), ('http://sintesis.mx/', 21), ('http://diarioimagenqroo.mx/noticias/', 23), ('http://www.debate.com.mx/', 25), ('http://www.termometroenlinea.com.mx/', 26), ('http://www.e-tlaxcala.mx/', 29), ('http://www.e-consulta.com/', 21),  ('http://www.yucatanalamano.com/', 31), ('http://heraldo.mx/', 1), ('http://www.frontera.info/', 2), ('http://www.afntijuana.info/', 2),"
	    			+ "('http://peninsulardigital.com/', 3), ('http://www.extradelalaguna.com.mx/', 5), ('http://www.diarioavanzada.com.mx/', 6), ('http://www.unomasuno.com.mx/', 9), ('http://www.diariodemexico.com/', 9), ('http://www.diarioelreloj.com.mx/', 13), ('http://www.elindependientedehidalgo.com.mx/', 13), ('http://vallartaopina.net/', 14), ('http://elvalle.com.mx/', 15), ('http://www.elsoldemorelia.com.mx/', 16), ('http://www.eldiariovision.com.mx/', 16), ('http://www.diariomarca.com.mx/', 20), ('http://bbmnoticias.com/', 20), ('http://www.capitalqueretaro.com.mx/', 22), ('http://amqueretaro.com/', 22), ('http://diariozm.com/principal/', 24),"
	    			+ "('http://www.elsoldelbajio.com.mx/', 11), ('https://www.la-prensa.com.mx/', 14), ('http://liderweb.mx/', 28), ('http://www.ctierrablanca.mx/', 30), ('http://www.puntomedio.mx/', 31), ('http://www.noticiasmvs.com/#!/home', 9), ('http://www.elfinanciero.com.mx/', 15), ('http://www.eluniversaledomex.mx/home.html', 15), ('http://www.horacero.com.mx/', 28), ('http://www.latarde.com.mx/', 28), ('http://rvial.mx/noticias', 9), ('http://diariodelistmo.com/', 30), ('http://zocalo.com.mx/', 5), ('http://www.info7.mx/', 19), ('http://muyatiempo.com/es/', 1), ('http://www.lja.mx/', 1), ('http://agpnoticias.com/news/', 2),  ('http://www.uniradioinforma.com/inicio', 2),"
	    			+ "('http://www.balun-canan.com/', 2), ('http://esdiario.com.mx/', 7), ('http://www.alcontacto.com.mx/', 8), ('http://elmonitorparral.com/', 8),  ('http://elcomentario.ucol.mx/', 6), ('http://www.informate.com.mx/', 9), ('http://www.dia7.com.mx/', 11), ('http://www.criteriohidalgo.com/', 13),  ('http://www.semanario.com.mx/ps/', 14), ('http://trespm.com.mx/', 15), ('http://elsurtamazunchale.com/principal/', 24), ('http://www.eluniversaldf.mx/home.html', 15), ('http://www.diariodecomitan.com.mx/', 7), ('http://www.palestraaguascalientes.com/', 1),  ('http://www.omnia.com.mx/', 8), ('http://ovaciones.com/', 9), ('http://www.nuevosiglo.com/', 14),"
	    			+ "('http://www.launion.com.mx/', 17), ('http://www.adiariooax.com/', 20),  ('http://www.eluniversalqueretaro.mx/', 22), ('http://www.elmosquito.com.mx/', 22), ('http://antenasanluis.mx/', 24), ('http://elportalslp.com.mx/', 24), ('http://www.observadortlaxcalteca.com/portal/', 29), ('http://yucatanahora.com/', 31), ('http://yucatanalminuto.com/', 31), ('http://progresohoy.com/', 31), ('http://www.elpopular.mx/', 21), ('http://alinstantenoticias.com/portal/', 25), ('http://imagendeveracruz.com.mx/', 30), ('http://notiver.com.mx/', 30), ('http://www.aldiadallas.com/news/mexico/', 9), ('http://elmundodetehuacan.com/', 21), ('http://www.quequi.com.mx/', 23),"
	    			+ "('http://ntrzacatecas.com/noticias/', 32), ('http://mexicorojo.mx/blog', 15), ('http://eluniversalveracruz.com.mx/', 30),('http://www.noticiasvespertinas.com.mx/', 19), ('http://www.noticiasvespertinasdelsoldeleon.com.mx/', 0), ('https://www.elsoldemexico.com.mx/', 15),('https://www.esto.com.mx/', 9),('https://www.elsoldehermosillo.com.mx/', 26),('http://www.noticiasdequeretaro.com.mx/', 22),('http://vivavoz.com.mx/', 25),('http://codigotlaxcala.com/', 29),('http://www.veracruzenlanoticia.com/', 30),('http://marcha.com.mx/', 30),('http://www.pueblaonline.com.mx/2015/portal/#&panel1-1', 21),('http://oaxacaentrelineas.com/', 20),"
	    			+ "('http://www.poblanerias.com/seccion/puebla-en-100/', 21),('http://zonafranca.mx/', 11),('http://enfoqueoaxaca.com/', 20),('http://zona3.mx/', 14),('http://elmundodeorizaba.com/', 30),('http://www.excelsior.com.mx/', 9),('http://www.elgrafico.mx/', 9),('http://www.maspormas.com/', 9),('http://elobservador.mx/', 8),('http://www.newshidalgo.mx/', 13),('http://www.nvinoticias.com/', 7),('http://expresocampeche.com/', 4),('http://www.multimedios.com/telediario', 19),('http://cvnzacatecas.com.mx/', 32),('http://wradio.com.mx/', 13),('http://www.24-horas.mx/', 9),('http://www.nayaritenlinea.mx/', 18),('http://entrelineas.com.mx/', 8),"
	    			+ "('http://www.libertaddepalabra.com/', 22),('http://www.sdpnoticias.com/', 20),('http://diarioportal.com/', 15),('http://www.sdpnoticias.com/local/baja-california-sur', 3), ('http://www.sdpnoticias.com/local/chiapas', 7),('http://www.sdpnoticias.com/local/ciudad-de-mexico', 9), ('http://www.sdpnoticias.com/local/edomex', 15),('http://www.sdpnoticias.com/local/guadalajara', 14),('http://www.sdpnoticias.com/local/guerrero', 12),('http://www.sdpnoticias.com/local/jalisco', 14),('http://www.sdpnoticias.com/local/monterrey', 19),('http://www.sdpnoticias.com/local/nuevo-leon', 19),('http://www.sdpnoticias.com/local/morelos', 17),"
	    			+ "('http://www.sdpnoticias.com/local/oaxaca', 20),('http://www.sdpnoticias.com/local/puebla', 21),('http://www.sdpnoticias.com/local/quintana-roo', 23),('http://www.sdpnoticias.com/local/sonora', 26),('http://www.sdpnoticias.com/local/tamaulipas', 28),('http://www.sdpnoticias.com/local/veracruz', 30),('http://www.quadratin.com.mx/', 16),('http://mensajerodelasierra.com/', 22),('http://www.elnoticieroenlinea.com/', 6),('http://www.eldiariodecoahuila.com.mx/', 5),('http://periodicoeltiempo.mx/', 5),('http://www.periodicoabc.mx/', 19),('http://dqr.com.mx/', 23),('http://eldictamen.mx/', 30),('http://www.tiempodeveracruz.com/', 30),"
	    			+ "('http://laverdadnoticias.com/', 31), ('http://www.nnc.mx/nayarit.html', 18),('http://www.diarioamanecer.com.mx/', 15),('http://www.edomexaldia.com.mx/', 15),('http://elsurdiario.com.mx/', 20),('http://diariodelyaqui.mx/', 26),('http://www.elregionaldesonora.com.mx/', 26),('http://eldiariodesinaloa.com/', 25),('http://laprensa.mx/', 28),('http://www.elheroico.mx/', 27),('http://www.ahoratabasco.com/', 27),('http://laextra.mx/', 16),('http://www.el-independiente.com.mx/', 16),('http://www.noticiasperfil.com/index/', 30),('http://www.cuartopoder.mx/', 7),('http://pagina24.com.mx/', 1),('http://www.tribunadelabahia.com.mx/', 14),"
	    			+ "('http://www.elfaromx.com/', 14),('http://pagina24zacatecas.com.mx/', 32),('http://www.diariopuntual.com/noticias', 21),('http://www.diariodf.mx/', 9),('http://durango.notigram.com/', 10),('http://www.lajornadazacatecas.com.mx/', 32),('http://www.ljz.mx', 32),('http://www.diarioeyipantla.com/', 30),('http://desdelared.com.mx/', 1),('http://informadorbcs.com/', 3),('http://monitoreconomico.org/noticias/', 2),('http://noticabos.org/', 3),('http://www.campeche.com.mx/', 4),('http://lavozdecoatzacoalcos.com.mx/', 27),('http://diariolavozdelsureste.com/chiapas/', 7),('http://lavozdetapachula.com/', 7),('http://lavozdecomitan.com/', 7),"
	    			+ "('http://expresszacatecas.com/', 32),('http://www.sie7edechiapas.com/', 7),('http://puentelibre.mx/', 8),('http://elheraldodesaltillo.mx/', 5),('http://www.colimanoticias.com/', 6),('http://angelguardian.mx/', 6),('http://colimapm.com/', 6),('http://impacto.mx/', 9),('http://impacto.mx/seccion-alarma', 9),('http://heraldoleon.mx/', 19),('http://periodicocorreo.com.mx/', 11),('http://www.red-noticias.com/', 12),('http://www.novedadesacapulco.mx/', 12),('http://www.diariovialibre.com.mx/', 13),('http://www.diariodelashuastecas.com/principal/', 13),('http://www.cronicahidalgo.com/', 13),('http://abcdezihuatanejo.com/', 12),"
	    			+ "('http://redesdelsur.com.mx/2016/', 12),('http://noticiasdemichoacan.com/', 16),('http://elregional.com.mx/', 17),('http://www.eltiempodenayarit.mx/', 18),('http://www.elhorizonte.mx/', 19),('http://reporteindigo.com/', 19);");
	    	}
	    }catch(Exception e){
	    	System.err.println(e.getMessage());
	    }
    }
	
	public Vector<String> getQuery(String query){
		aux.clear();
		try{
			rs = statement.executeQuery(query);
			while(rs.next()){
				aux.add(rs.getString(1));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return this.aux;
	}
	
	public void setQuery(String query) throws SQLException{
		statement.executeUpdate(query);	
	}
	
	public void dbClose(){
		try{
    		if(connection != null)
    			connection.close();
    	}catch(SQLException e){
    		System.err.println(e);
    	}
	}
	
	public int getTotal() throws SQLException{
		rs = statement.executeQuery("select count(*) from periodicos");
		return rs.getInt(1);
	}
}