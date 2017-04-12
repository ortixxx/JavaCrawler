package main;

import java.io.*;

public class importExport{
	File archivo = null;
	FileReader fr = null;	
	BufferedReader br = null;
	FileWriter fw = null;
	BufferedWriter bw = null;
	
	public importExport(){
	      
	}
	
	public void importar(String path){
		try{
	        archivo = new File (path);
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);
	        String linea;
	        while((linea=br.readLine())!=null)
	        	System.out.println(linea);
		}catch(Exception e){
	         e.printStackTrace();
	    }finally{
	         try{
	        	 if(null != fr)
	        		 fr.close();            
	         }catch (Exception e2){
	        	 e2.printStackTrace();
	         }
	   }
	}
	
	public void exportar(String path){
		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			
			bw.write("Hola mundo");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
