package webcrawl.webcrawl;

import webcrawl.webcrawl.type.ProcessStatusType;
import webcrawl.webcrawl.dto.ProcessControlDTO;
import webcrawl.util.Util;
import webcrawl.webcrawl.dto.WebCrawlProcessStatusDTO;
import webcrawl.webcrawl.dto.WebCrawlRequestDTO;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebCrawlManagerTest {
    
    String keyword = "security";
    String url = "http://hiring.axreng.com";
    URI urlBase = URI.create(url);
    int maxResults = 10;
    String id = "12345678";
    
    @InjectMocks    
    WebCrawlManager webCrawlManager;
    
    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreateNewCrawlProcess() {

        WebCrawlRequestDTO requestDTO = new WebCrawlRequestDTO();
        requestDTO.setKeyword("security");
        String id = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);
        assertNotNull(id);
        assertThat(Util.isIDValid(id), is(true));
        
        requestDTO.setKeyword("SeCuRiTy");
        String newId = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);
        assertNotNull(newId);
        assertThat(Util.isIDValid(newId), is(true));
        assertThat(newId.equals(id), is(true));
        
        requestDTO.setKeyword("security2");
        String newId2 = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);
        assertNotNull(newId2);
        assertThat(Util.isIDValid(newId2), is(true));
        assertThat(newId2.equals(id), is(false));
    }

    @Test
    public void testGetProcessStatus() {
        
        WebCrawlRequestDTO requestDTO = new WebCrawlRequestDTO();
        requestDTO.setKeyword(keyword);
        String id = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);        
        assertNotNull(id);
        assertThat(Util.isIDValid(id), is(true));
        
        WebCrawlProcessStatusDTO statusDTO = webCrawlManager.getProcessStatus(id);
        assertNotNull(statusDTO);
        assertThat(statusDTO.getId(), is(id));
        assertThat(statusDTO.getStatus(), is(ProcessStatusType.active));
    }
    
    @Test
    public void testAddURLProcessingStatus(){
        
        WebCrawlRequestDTO requestDTO = new WebCrawlRequestDTO();
        requestDTO.setKeyword(keyword);
        String id = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);        
        assertNotNull(id);
        assertThat(Util.isIDValid(id), is(true));
        
        ProcessControlDTO processControl = new ProcessControlDTO(keyword, url, maxResults);
        WebCrawlManager.addURLProcessingStatus(processControl, id, url + "/index.html");
        
        WebCrawlProcessStatusDTO statusDTO = webCrawlManager.getProcessStatus(id);
        assertNotNull(statusDTO);
        assertThat(statusDTO.getId(), is(id));
        assertThat(statusDTO.getStatus(), is(ProcessStatusType.done));
        assertThat(statusDTO.getUrls().size() > 0, is(true));
        assertThat(statusDTO.getUrls().toArray()[0].equals(url + "/index.html"), is(true));
    }
    
    @Test
    public void testFinalizeProcessing(){
        
        WebCrawlRequestDTO requestDTO = new WebCrawlRequestDTO();
        requestDTO.setKeyword(keyword);
        String id = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);        
        assertNotNull(id);
        assertThat(Util.isIDValid(id), is(true));
                
        WebCrawlProcessStatusDTO statusDTO = webCrawlManager.getProcessStatus(id);
        assertNotNull(statusDTO);
        assertThat(statusDTO.getId(), is(id));
        assertThat(statusDTO.getStatus(), is(ProcessStatusType.active));
        
        boolean ok = WebCrawlManager.finalizeProcessing(id);
        assertThat(ok, is(true));
        statusDTO = webCrawlManager.getProcessStatus(id);
        assertNotNull(statusDTO);
        assertThat(statusDTO.getId(), is(id));
        assertThat(statusDTO.getStatus(), is(ProcessStatusType.done));
    }
    
    @Test
    public void testGetURLsSize(){

        WebCrawlRequestDTO requestDTO = new WebCrawlRequestDTO();
        requestDTO.setKeyword(keyword);
        String id = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);        
        assertNotNull(id);
        assertThat(Util.isIDValid(id), is(true));
        
        ProcessControlDTO processControl = new ProcessControlDTO(keyword, url, maxResults);
        WebCrawlManager.addURLProcessingStatus(processControl, id, url + "/index.html");
        
        int size = WebCrawlManager.getURLsSize(id);
        assertThat(size == 1, is(true));
    }
    
    @Test
    public void testVerfifyIdExists(){
        
        WebCrawlRequestDTO requestDTO = new WebCrawlRequestDTO();
        requestDTO.setKeyword(keyword);
        String id = webCrawlManager.createNewCrawlProcess(requestDTO, urlBase, maxResults);        
        assertNotNull(id);
        assertThat(Util.isIDValid(id), is(true));
        
        assertThat(WebCrawlManager.verfifyIdExists(id), is(true));
    }
}
