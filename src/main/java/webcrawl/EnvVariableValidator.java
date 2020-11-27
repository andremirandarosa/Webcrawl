package webcrawl;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvVariableValidator {
    
    static Logger logger = LoggerFactory.getLogger(EnvVariableValidator.class);
    
    private URI uriBase = null;
    private Integer maxResults = -1;
    
    public URI getURIBase(){
        return this.uriBase;
    }
    
    public int getMaxResults(){
        return this.maxResults;
    }
    
    public boolean validate(String baseURL, String maxResults){
        
        logger.info("Validating Environment Variables: [BASE_URL: " + baseURL + " - MAX_RESULTS: " + maxResults + "]");
        
        if(validateBaseURL(baseURL)){
            
            this.validateMaxResults(maxResults);
            
            return true;
        }
        
        return false;
    }
        
    private boolean validateBaseURL(String baseURL){
        
        if((baseURL != null) && (baseURL.length() > 0) && 
           (baseURL.toLowerCase().contains("http://") || baseURL.toLowerCase().contains("https://")) &&
           baseURL.contains(".")){
        
            try{
                this.uriBase = URI.create(baseURL);
                
                logger.info("BASE_URL validated.");
                
                return true;
                
            }catch(Exception e){                
                logger.error("BASE_URL invalid.");
            }
            
        }else logger.error("BASE_URL invalid.");

        return false;
    }
    
    private void validateMaxResults(String maxResults){
        
        if((maxResults != null) && (maxResults.length() > 0)){
         
            try{
                this.maxResults = Integer.parseInt(maxResults);
                
                if((this.maxResults < -1) || (this.maxResults == 0))
                    this.maxResults = -1;
                
            }catch(NumberFormatException nfe){
                logger.info("MAX_RESULTS invalid. Setting default value: -1");
            }
            
        }else logger.info("MAX_RESULTS invalid. Setting default value: -1");
    }
}
