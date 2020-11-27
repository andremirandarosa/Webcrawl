package webcrawl.webcrawl;

import webcrawl.util.JSONUtil;
import webcrawl.util.Util;
import webcrawl.webcrawl.dto.ProcessResponseDTO;
import webcrawl.webcrawl.dto.WebCrawlProcessStatusDTO;
import webcrawl.webcrawl.dto.WebCrawlRequestDTO;
import com.google.gson.JsonSyntaxException;
import java.net.URI;
import static spark.Spark.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class WebCrawl {

    Logger logger = LoggerFactory.getLogger(WebCrawl.class);
    
    WebCrawlManager webCrawlManager;

    public WebCrawl() {}
    
    public WebCrawl(WebCrawlManager webCrawlManager, URI uriBase, int maxResults){
        
        this.webCrawlManager = webCrawlManager;
        
        logger.info("Initializing WebCrawling WebServices...");
        
        post("/crawl", (req, res) -> postRequest(req, res, uriBase, maxResults));
        
        get("/crawl/:id", (req, res) -> getRequest(req, res));
        
        logger.info("WebCrawling WebServices initialized successfully.");
    }
    
    public String postRequest(Request req, Response res, URI uriBase, int maxResults){
        
        WebCrawlRequestDTO requestDTO = validatePostRequestBody(req.body());

        if(requestDTO != null){

            String processID = webCrawlManager.createNewCrawlProcess(requestDTO, uriBase, maxResults);

            if(Util.isIDValid(processID)){

                res.status(200);  
                
                res.type("application/json"); 
                
                return JSONUtil.objToJSON(new ProcessResponseDTO(processID));

            }else return Util.generateResponseError(res, 500, "Error on initialize WebCrawl Process.");

        }else return Util.generateResponseError(res, 400, "Invalid request body.");
    }
    
    public WebCrawlRequestDTO validatePostRequestBody(String jsonRequestBody){
        
        if((jsonRequestBody != null) || (jsonRequestBody.length() > 0)){
            
            try{
                WebCrawlRequestDTO dto = (WebCrawlRequestDTO) JSONUtil.jsonToObject(jsonRequestBody, WebCrawlRequestDTO.class);
                
                if((dto != null) && (dto.getKeyword() != null) && (dto.getKeyword().length() >= 4) && (dto.getKeyword().length() <= 32)) return dto;
                else logger.error("Invalid Keyword value.");
                
            }catch(JsonSyntaxException e){
                logger.error("Invalid JSON Request Body.");
            }
        }
        
        return null;
    }
    
    public String getRequest(Request req, Response res){
            
        String id = req.params("id");
        
        if(Util.isIDValid(id)){
            
            WebCrawlProcessStatusDTO statusSDO = webCrawlManager.getProcessStatus(id);
            
            if(statusSDO != null){
                
                res.status(200);  
                
                res.type("application/json");
                
                return JSONUtil.objToJSON(statusSDO);
                
            }else return Util.generateResponseError(res, 400, "The request id does not exist.");
            
        }else return Util.generateResponseError(res, 400, "Invalid request id.");
    }
}
