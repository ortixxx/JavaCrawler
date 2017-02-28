package mozilla;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MetaTagsExtractor {
	public static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	public String title, des, img, date;
	public Element metaTag;
	public Document doc;
	
    public MetaTagsExtractor(String url) {
    	String link = url;
    	try{
    		doc = Jsoup.connect(link).userAgent(userAgent).get();
    	}catch(Exception e){
    		return;
    	}
    	    	
    	//Elements metas = doc.select("meta");
    	//System.out.println(metas);
        //System.out.println("Link :\t"+link);
    	
    	try{
        	metaTag = doc.select("meta[property=og:title]").first();
        	this.title = metaTag.attr("content");
        }catch(Exception e){
        	title="No title";
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
        		this.date = "No date";
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
    }
    
	public String getTitle(){
    	return title;
    }
    public String getDes(){
    	return des;
    }
    public String getImage(){
    	return img;
    }
    public String getDate(){
    	return date;
    }
}