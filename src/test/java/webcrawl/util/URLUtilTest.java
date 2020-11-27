package webcrawl.util;

import webcrawl.util.URLUtil;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class URLUtilTest {
    
    String text = "<html>" +
                  "   <body>" +
                  "	  <a class=\"ulink\" href=\"https://www.gnu.org/software/coreutils/\" target=\"_top\"><span class=\"package\">coreutils-8.31</span></a></li><li class=\"listitem\">\n" +
                  "	  <a class=\"ulink\" href=\"https://www.kernel.org/pub/linux/utils/util-linux/\" target=\"_top\"><span class=\"package\">util-linux-2.33.1</span></a></li><li class=\"listitem\">\n" +
                  "	  <a class=\"ulink\" href=\"http://www.openldap.org\" target=\"_top\"><span class=\"package\">OpenLDAP</span> 2.4.48</a></li><li class=\"listitem\">\n" +
                  "	  <a class=\"ulink\" href=\"http://www.linux-pam.org/library/\" target=\"_top\"><span class=\"package\">Linux-PAM</span> 1.3.0</a></li><li class=\"listitem\">\n" +
                  "	  <a class=\"ulink\" href=\"http://www.PCRE.org/\" target=\"_top\"><span class=\"package\">PCRE</span> 10.34</a></li></ul></div><p>\n" +
                  "   </body>" + 
                  "</html>";
    
    @Test
    public void testGetURLListFromText(){
        
        List<String> urls = URLUtil.getURLListFromText(text);
        assertNotNull(urls);
        assertThat(urls.size() ==5, is(true));
    }
    
    @Test
    public void testGetValidURLListFromText(){
        
        List<String> urls = URLUtil.getValidURLListFromText(text, "https://www.gnu.org/");
        assertNotNull(urls);
        assertThat(urls.size() == 1, is(true));
    }
    
    @Test
    public void testNormalizeURL(){
        
        String urlNormalized = URLUtil.normalizeURL("http://hiring.axreng.com", "http://hiring.axreng.com");
        assertNotNull(urlNormalized);
        assertThat(urlNormalized.endsWith("/"), is(true));
        
        urlNormalized = URLUtil.normalizeURL("software/coreutils", "https://www.gnu.org/");
        assertNotNull(urlNormalized);
        assertThat(urlNormalized.endsWith("/"), is(true));
        assertThat(urlNormalized.equals("https://www.gnu.org/software/coreutils/"), is(true));
        
        urlNormalized = URLUtil.normalizeURL("software/coreutils/", "https://www.gnu.org/");
        assertNotNull(urlNormalized);
        assertThat(urlNormalized.endsWith("/"), is(true));
        assertThat(urlNormalized.equals("https://www.gnu.org/software/coreutils/"), is(true));
        
        
        urlNormalized = URLUtil.normalizeURL("./software/coreutils", "https://www.gnu.org/");
        assertNotNull(urlNormalized);
        assertThat(urlNormalized.endsWith("/"), is(true));
        System.err.println("URL : " + urlNormalized);
        assertThat(urlNormalized.equals("https://www.gnu.org/software/coreutils/"), is(true));
        
        urlNormalized = URLUtil.normalizeURL("../software/coreutils", "https://www.gnu.org/");
        assertNotNull(urlNormalized);
        assertThat(urlNormalized.endsWith("/"), is(true));
        assertThat(urlNormalized.equals("https://www.gnu.org/software/coreutils/"), is(true));
    }
}

