package main;

import java.io.*;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;

public class importExport{
	File archivo = null;
	FileReader fr = null;	
	BufferedReader br = null;
	FileWriter fw = null;
	BufferedWriter bw = null;
	String acumulado="", sql = "", linea, auxInt;
	Date fecha;
	ResultSet rs;
	int total;
	static importExport logErrores = new importExport();
	
	public importExport(){
	      acumulado="";
	}
	
	public void importar(String path){		
		try{
			sql="";
	        archivo = new File (path);
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);	        
	        while((linea=br.readLine())!=null)
	        	sql += linea;
	        
	        interfaz.getSql().setQuery(sql);
		}catch(Exception e){
			if(e.toString().length()>120)
				JOptionPane.showMessageDialog(null,"Erroral importar: "+e.toString().substring(0, 120));
			else
				JOptionPane.showMessageDialog(null,"Erroral importar: "+e.toString());
			
			logErrores.error(e.toString());	        
	    }finally{
	         try{
	        	 if(fr != null)
	        		 fr.close();
	        	 
	        	 if(br != null)
	        		 br.close();
	         }catch (Exception e2){
	        	 logErrores.error(e2.toString());
	         }
	   }
	}
	
	public void exportar(String path){
		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			
			try{
				bw.write("insert into periodicos values ");
				rs = interfaz.getSql().getState().executeQuery("select count(*) from periodicos");
				total = rs.getInt(1);
				rs = interfaz.getSql().getState().executeQuery("select * from periodicos");
				while(rs.next()){					
					if(total>1){
						bw.write("('"+rs.getString(1)+"', "+rs.getInt(2)+"), ");
					}else{
						bw.write("('"+rs.getString(1)+"', "+rs.getInt(2)+");");
					}
					total--;
				}
				
			}catch (Exception e){
				logErrores.error(e.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				logErrores.error(ex.toString());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void error(String s){		
		fecha = new Date();
		if(fecha.getMinutes()<10){
			auxInt = "0"+fecha.getMinutes();
		}else{
			auxInt = fecha.getMinutes()+"";
		}
		acumulado += "["+fecha.getDate()+"/"+(fecha.getMonth()+1)+"/"+(1900+fecha.getYear())+" "+fecha.getHours()+":"+auxInt+"]";
		acumulado += "\n"+s+"\n";
	}
		
	public void saveLog(){
		if(acumulado.length()==0)
			return;
		try {
			archivo = new File ("log/errorLog.log");
	        
			fw = new FileWriter(archivo.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			try{
				bw.write(acumulado);
			}catch (Exception e){
				System.out.println("Error al actualizar archivo log");
			}

		}catch (IOException e){
			System.out.println("Error al usar el stream");
		}finally{
			acumulado="";
			try {
				if(bw != null)
					bw.close();

				if(fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
