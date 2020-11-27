package webcrawl.util;

import webcrawl.webcrawl.dto.ErrorResponseDTO;
import webcrawl.webcrawl.WebCrawlManager;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

public class Util {

    static Logger logger = LoggerFactory.getLogger(Util.class);
    
    static public String generateID(){         
        
        String id = UUID.randomUUID().toString().substring(0, 8);
        
        if(!WebCrawlManager.verfifyIdExists(id))
            return id;
        
        return null;
    }
    
    static public String generateResponseError(Response res, int status, String message){
        
        if(res != null){
            
            res.status(status);

            res.type("application/json"); 
        }
        
        return JSONUtil.objToJSON(new ErrorResponseDTO(status, message));
    }
    
    static public boolean isStringValid(String string){
        
        return ((string != null) && (string.length() > 0));
    }
    
    static public boolean isIDValid(String id){
        return (isStringValid(id) && (id.length() == 8));
    }
    
    static public void sleep(int milliseconds){
        
        try{
            Thread.sleep(milliseconds);            
        }catch(Exception e){}
    }
    
    static public LinkedList<String> stringToTokenList(String string, String delimitator){
                
        if(isStringValid(string) && isStringValid(delimitator)){

            LinkedList<String> ll = new LinkedList<>();

            if(delimitator.length() == 1){

                StringTokenizer st = new StringTokenizer(string, delimitator);

                while(st.hasMoreTokens())
                    ll.add(st.nextToken());

            }else{

                String[] vet_tokens = string.split(delimitator);

                ll.addAll(Arrays.asList(vet_tokens));
            }

            if(ll.size() > 0) return ll;
            else{

                ll.add(string);

                return ll;
            }
        }
        
        return null;
    }
}
