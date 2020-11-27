package webcrawl.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {
    
    static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
    
    static public Object jsonToObject(String json, Class objClass){
        
        if((json != null) &&  (json.length() > 0) && (objClass != null)){
            
            try{
                return new Gson().fromJson(json, objClass);
                
            }catch(JsonSyntaxException e){
                logger.error("Error on convert JSON to Object: JSON: " + json + " - Object: " + objClass.getName());
            }
        }
        
        return null;
    }
    
    static public String objToJSON(Object obj){

        if(obj != null){

            try{
                return new Gson().toJson(obj);

            }catch(JsonSyntaxException e){
                logger.error("Error on convert Object to JSON: Object Class: " + obj.getClass());
            }
        }
        
        return null;
    }
}
