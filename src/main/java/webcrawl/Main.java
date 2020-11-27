package webcrawl;

import webcrawl.webcrawl.WebCrawl;
import webcrawl.webcrawl.WebCrawlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    static Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {        
        
        EnvVariableValidator envValidator = new EnvVariableValidator();
        
        if(envValidator.validate(System.getenv("BASE_URL"), System.getenv("MAX_RESULTS"))){
            
            new WebCrawl(new WebCrawlManager(), envValidator.getURIBase(), envValidator.getMaxResults());
            
        }else{
            logger.error("Invalid Environment Variables. Finalizing application.");
            System.exit(1);
        }
    }
}
