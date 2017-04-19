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
	String sql = "", linea;
	Date fecha;
	ResultSet rs;
	int total;
	static importExport logErrores = new importExport();
	
	public importExport(){
	      
	}
	
	public void importar(String path){		
		try{
	        archivo = new File (path);
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);	        
	        while((linea=br.readLine())!=null)
	        	sql += linea;
	        
	        interfaz.getSql().setQuery(sql);
		}catch(Exception e){			
			JOptionPane.showMessageDialog(null,"Erroral importar: "+e.toString().substring(0, 150));
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
		try {
			linea="";
			sql="";
			archivo = new File ("log/errorLog.log");
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);
	        while((linea=br.readLine())!=null){
	        	if(linea.length()>0)
	        		sql += linea+"\n";
	        }	        	
	        
			fw = new FileWriter("log/errorLog.log");
			bw = new BufferedWriter(fw);
			
			try{
				bw.write(sql);
				bw.newLine();
				fecha = new Date();
				bw.write("["+fecha.getDate()+"/"+(fecha.getMonth()+1)+"/"+(1900+fecha.getYear())+" "+fecha.getHours()+":"+fecha.getMinutes()+"]");
				bw.newLine();
				bw.write(s);
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"Error al actualizar archivo log");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	public void closeStream(){
		try {
			if(bw != null)
				bw.close();

			if(fw != null)
				fw.close();
			
			if(fr != null)
        		 fr.close();
			
			if(br != null)
        		 br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static importExport logErrorres(){
		return logErrores;
	}
}
