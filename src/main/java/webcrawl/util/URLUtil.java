package webcrawl.util;

import static webcrawl.util.Util.isStringValid;
import static webcrawl.util.Util.logger;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class URLUtil {
    
    static public String getTextFromURL(String urlAddress){
        
        try{
            return new Scanner(new URL(urlAddress).openStream(), "UTF-8").useDelimiter("\\A").next();
        }catch(IOException e){
            logger.error("I/O Error: " + e.getMessage());
        }
       
        return null;
    }    
    
    static public List<String> getURLListFromText(String text){
        
        if(isStringValid(text)){
            
            LinkedList<String> listWords = Util.stringToTokenList(text, " ");

            if((listWords != null) && (listWords.size() > 0)){

                return listWords.parallelStream().filter(w -> w.toLowerCase().contains("href=\""))
                                                 .map(v -> cleanURL(v))   
                                                 .distinct()
                                                 .collect(Collectors.toList());
            }
        }
        
        return null;
    }
    
    static public List<String> getValidURLListFromText(String text, String urlBase){
        
        if(isStringValid(text) && isStringValid(urlBase)){
            
            urlBase = normalizeUrlLastBar(urlBase);

            List<String> listURLs = getURLListFromText(text);
            
            if((listURLs != null) && (listURLs.size() > 0)){
             
                final String finalUrlBase = urlBase;
                
                return listURLs.parallelStream().filter(url -> isURLValid(url, finalUrlBase))
                                                .map(url -> normalizeURL(url, finalUrlBase))
                                                .distinct()
                                                .collect(Collectors.toList());
            }
        }
        
        return null;
    }
    
    static private boolean isURLValid(String urlAddress, String urlBase){
        
        if(isStringValid(urlAddress) && isStringValid(urlBase)){
            
            urlBase = normalizeUrlLastBar(urlBase);
            
            if(urlBase.equals(urlAddress) || urlBase.equals(urlAddress + "/")) 
                return true;
            
            if(urlAddress.toLowerCase().contains("://")){
                
                boolean ok = urlAddress.toLowerCase().contains(urlBase);
                
                if(ok)
                    return urlAddress.toLowerCase().endsWith(".html") || urlAddress.toLowerCase().endsWith("/");
                
            }else return urlAddress.toLowerCase().endsWith(".html") || urlAddress.toLowerCase().endsWith("/");
        }
        
        return false;
    }
    
    static private String cleanURL(String url){
        
        if(Util.isStringValid(url)){

            url = url.replace("href=\"", "");
            
            if(url.contains("\""))
                url = url.substring(0, url.indexOf("\""));
            
            if(url.startsWith("/"))
                url = url.substring(1);
            
            if(url.startsWith("//"))
                url = url.substring(2);
            
            if(url.startsWith("./"))
                url = url.substring(2);
            
            if(url.startsWith("../"))
                url = url.substring(3);
            
            return url;
        }
        
        return null;
    }
    
    static public String normalizeURL(String urlAddress, String urlBase){
        
        if(isStringValid(urlAddress) && isStringValid(urlBase)){
            
            urlAddress = cleanURL(urlAddress);
            urlBase = normalizeUrlLastBar(urlBase);
            
            if(urlAddress.toLowerCase().contains("http://") || 
               urlAddress.toLowerCase().contains("https://")){
                
                return normalizeUrlLastBar(urlAddress);
                
            }else return normalizeUrlLastBar(urlBase + urlAddress);
        }
        
        return null;
    }
    
     static private String normalizeUrlLastBar(String url){
         
         if(url.toLowerCase().endsWith(".html"))
             return url;
         
         if(url.endsWith("/")) return url;
         else return url + "/";
     }
}
