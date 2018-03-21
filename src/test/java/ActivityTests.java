import com.company.App;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

public class ActivityTests {


    private HttpClient client;
    private ApplicationContext ctx;

    @Before
    public void before() {
        if (ctx == null) ctx = new AnnotationConfigApplicationContext(App.class);
        if (client == null) client = HttpClientBuilder.create().build();
    }

    @Test
    public void saveActivity() throws Exception {
        HttpPost post = new HttpPost("http://localhost:4567/activity");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("uuid", UUID.randomUUID().toString()));
        params.add(new BasicNameValuePair("activity", "1"));

        post.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse resp = client.execute(post);
        assertEquals(200, resp.getStatusLine().getStatusCode());

    }

}
