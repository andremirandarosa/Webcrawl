package webcrawl.webcrawl;

import webcrawl.util.JSONUtil;
import webcrawl.webcrawl.dto.ErrorResponseDTO;
import webcrawl.webcrawl.dto.ProcessResponseDTO;
import webcrawl.webcrawl.dto.WebCrawlProcessStatusDTO;
import webcrawl.webcrawl.dto.WebCrawlRequestDTO;
import webcrawl.webcrawl.type.ProcessStatusType;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import spark.Request;
import spark.Response;

@TestInstance(Lifecycle.PER_CLASS)
public class WebCrawlTest {
    
    URI urlBase = URI.create("http://hiring.axreng.com");
    int maxResults = 10;
    String id = "12345678";
    
    @InjectMocks 
    WebCrawl webCrawl;
    
    @Mock
    Request req;
    
    @Mock
    Response res;
    
    @Mock
    WebCrawlManager webCrawlManager;
    
    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testValidatePostRequestBody() {
       
        webCrawl = new WebCrawl(webCrawlManager, null, -1);
        
        WebCrawlRequestDTO dto = webCrawl.validatePostRequestBody("{ \"keyword\": \"security\" }");        
        assertNotNull(dto);
        assertEquals("security", dto.getKeyword());
        
        dto = webCrawl.validatePostRequestBody("{ \"keyword\": \"1234\" }");        
        assertNotNull(dto);
        assertEquals("1234", dto.getKeyword());
        
        dto = webCrawl.validatePostRequestBody("{ \"keyword\": \"12345678901234567890123456789012\" }");        
        assertNotNull(dto);
        assertEquals("12345678901234567890123456789012", dto.getKeyword());
        
        dto = webCrawl.validatePostRequestBody("{ \"keyword\": \"aaa\" }");        
        assertNull(dto);
        
        dto = webCrawl.validatePostRequestBody("{ \"keyword\": \"123456789012345678901234567890123\" }");        
        assertNull(dto);
    }
    
    @Test
    public void testPostRequest(){
        
        Mockito.when(req.body()).thenReturn("{ \"keyword\": \"security\" }");
        Mockito.when(webCrawlManager.createNewCrawlProcess(Mockito.any(WebCrawlRequestDTO.class), Mockito.any(URI.class), Mockito.anyInt())).thenReturn(id);
                
        webCrawl = new WebCrawl(webCrawlManager, urlBase, maxResults);
        String json = webCrawl.postRequest(req, res, urlBase, maxResults);
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        
        ProcessResponseDTO responseDTO = (ProcessResponseDTO) JSONUtil.jsonToObject(json, ProcessResponseDTO.class);
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());        
        assertThat(responseDTO.getId().length(), is(8));
        assertThat(responseDTO.getId(), is(id));
    }
    
    @Test
    public void testPostRequestError400(){
        
        Mockito.when(req.body()).thenReturn("{ \"keyword\": \"sec\" }");
        
        webCrawl = new WebCrawl(webCrawlManager, urlBase, maxResults);
        String json = webCrawl.postRequest(req, res, urlBase, maxResults);
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        
        ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) JSONUtil.jsonToObject(json, ErrorResponseDTO.class);
        assertNotNull(errorResponseDTO);
        assertNotNull(errorResponseDTO.getMessage());        
        assertThat(errorResponseDTO.getStatus(), is(400));
        assertThat(errorResponseDTO.getMessage(), is("Invalid request body."));
    }
    
    @Test
    public void testPostRequestError500(){
        
        Mockito.when(req.body()).thenReturn("{ \"keyword\": \"security\" }");
        
        webCrawl = new WebCrawl(webCrawlManager, urlBase, maxResults);
        String json = webCrawl.postRequest(req, res, null, maxResults);
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) JSONUtil.jsonToObject(json, ErrorResponseDTO.class);
        assertNotNull(errorResponseDTO);        
        assertNotNull(errorResponseDTO.getMessage());        
        assertThat(errorResponseDTO.getStatus(), is(500));
        assertThat(errorResponseDTO.getMessage(), is("Error on initialize WebCrawl Process."));
    }
    
    @Test
    public void testGetRequest(){
        
        Mockito.when(req.params("id")).thenReturn(id);                
        Mockito.when(webCrawlManager.getProcessStatus(id)).thenReturn(new WebCrawlProcessStatusDTO(id, ProcessStatusType.active));
        
        webCrawl = new WebCrawl(webCrawlManager, urlBase, maxResults);        
        String json = webCrawl.getRequest(req, res);
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        
        WebCrawlProcessStatusDTO responseDTO = (WebCrawlProcessStatusDTO) JSONUtil.jsonToObject(json, WebCrawlProcessStatusDTO.class);
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());        
        assertThat(responseDTO.getId().length(), is(8));
        assertThat(responseDTO.getId(), is(id));
        assertThat(responseDTO.getStatus(), is(ProcessStatusType.active));
        assertThat(responseDTO.getUrls().size(), is(0));
    }
    
    @Test
    public void testGetRequestError400(){
        
        Mockito.when(req.params("id")).thenReturn("123");                

        webCrawl = new WebCrawl(webCrawlManager, urlBase, maxResults);        
        String json = webCrawl.getRequest(req, res);
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        
        ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) JSONUtil.jsonToObject(json, ErrorResponseDTO.class);
        assertNotNull(errorResponseDTO);
        assertNotNull(errorResponseDTO.getMessage());        
        assertThat(errorResponseDTO.getStatus(), is(400));
        assertThat(errorResponseDTO.getMessage(), is("Invalid request id."));
    }
    
    @Test
    public void testGetRequestError400_2(){
        
        Mockito.when(req.params("id")).thenReturn(id);                
        Mockito.when(webCrawlManager.getProcessStatus(id)).thenReturn(null);
        
        webCrawl = new WebCrawl(webCrawlManager, urlBase, maxResults);        
        String json = webCrawl.getRequest(req, res);
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        
        ErrorResponseDTO errorResponseDTO = (ErrorResponseDTO) JSONUtil.jsonToObject(json, ErrorResponseDTO.class);
        assertNotNull(errorResponseDTO);
        assertNotNull(errorResponseDTO.getMessage());        
        assertThat(errorResponseDTO.getStatus(), is(400));
        assertThat(errorResponseDTO.getMessage(), is("The request id does not exist."));
    }
}
