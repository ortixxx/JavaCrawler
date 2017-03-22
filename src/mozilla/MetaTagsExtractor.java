package mozilla;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bdex.sqlite;

public class MetaTagsExtractor {
	public static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	public String title, aux, des, img, date, ico;
	public Element metaTag;
	public Document doc;
	public Elements hrefs;
	public sqlite queryDos = new sqlite();
	public Vector<String> semi = new Vector<String>(0, 1), finale = new Vector<String>(0, 1);
	
	
	
    public MetaTagsExtractor(String url) {
    	String link = url;
    	try{
    		doc = Jsoup.connect(link).userAgent(userAgent).get();
    	}catch(Exception e){
    		return;
    	}
    	 
    	paginasNivelDos();
    	
    	//Elements metas = doc.select("meta");
    	//System.out.println(metas);
        //System.out.println("Link :\t"+link);
    	
    	try{
        	metaTag = doc.select("meta[property=og:title]").first();
        	this.title = metaTag.attr("content");
        }catch(Exception e){
        	try{
        		title="No title";
        		metaTag = doc.select("title").first();
        		aux = metaTag.text();
        	}catch(Exception ex){
        		aux = "No aux";
        	}   	
        }
        
    	try{        
        	metaTag = doc.select("meta[property=og:description]").first();
        	this.des = metaTag.attr("content");
        }catch(Exception e){
        	try{
        		metaTag = doc.select("meta[name=description]").first();
               	this.des = metaTag.attr("content");
        	}catch(Exception ex){
        		this.des = "No description";
        	}        	
        }
    	
        try{
        	metaTag = doc.select("meta[property=article:published_time]").first();
        	this.date = metaTag.attr("content");
        }catch(Exception e){
        	try{
        		metaTag = doc.select("name=revised").first();
               	this.date = metaTag.attr("content");
        	}catch(Exception ex){
        		try{
        			metaTag = doc.select("name=date").first();
        			this.date = metaTag.attr("content");
        		}catch(Exception exe){
        			this.date = "No date";
        		}
        	}  
        }
        
        try{        
        	metaTag = doc.select("meta[property=og:image]").first();
        	this.img = metaTag.attr("content");
        }catch(Exception e){
        	try{
        		metaTag = doc.select("link[rel=image_src]").first();
        		this.img = metaTag.attr("href");
        	}catch(Exception ex){
        		try{
        			Elements imgs = doc.select("img[src*=.png],img[src*=.jpeg],img[src*=.jpg]");
        			int aux;
        			for(Element uno: imgs){
        				this.img = uno.toString().replaceAll("width", "°");
        				aux = Integer.parseInt(img.substring(img.indexOf('°')+3, img.indexOf('°')+6));
        				if(aux>100)
        					this.img = uno.attr("src");
        			}
        		}catch(Exception exe){
        			this.img = null;
        		}
        	}
        }
        
        try{
        	metaTag = doc.select("link[href*=.ico]").first();
        	this.ico = metaTag.attr("abs:href");
        }catch(Exception e){
        	this.ico = null;
        }
    }
    
	public void paginasNivelDos() {
		hrefs = doc.select("a[href]");
		String sql = "select nombre from periodicos";
		semi = queryDos.getQuery(sql);
		
		for(Element link : hrefs){
			if(semi.contains(link.attr("abs:href")) || link.attr("abs:href").length()==0 || link.attr("abs:href").contains("facebook") || link.attr("abs:href").contains("twitter") || link.attr("abs:href").contains("google") || link.attr("abs:href").contains("youtube") || link.attr("abs:href").contains("pinterest") || link.attr("abs:href").contains("instagram") || link.attr("abs:href").contains("whatsapp"))
				continue;
			
			finale.add(link.attr("abs:href"));
		}
		
		if(finale.isEmpty())
			finale.add(semi.firstElement());
	}

	public String getTitle(){
		if(title==null)
    		title="No title";
		
    	return title;
    }
    public String getDes(){
    	if(des==null)
    		des="No description";
    	
    	return des;
    }
    public String getImage(){
    	return img;
    }
    public String getDate(){
    	if(date==null)
    		date="No date";
    		
    	return date;
    }
    public String getAux(){
    	return aux;
    }
    public Vector<String> getHref(){
    	return finale;
    }
    public void closeBd(){
    	queryDos.dbClose();
    }
}