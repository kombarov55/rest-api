import com.company.App;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SyncTests {

    private HttpClient client;
    private ApplicationContext ctx;

    @Before
    public void before() {
        if (ctx == null) ctx = new AnnotationConfigApplicationContext(App.class);
        if (client == null) client = HttpClientBuilder.create().build();

    }

    @Test
    public void putToSync_shouldSucceed() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", UUID.randomUUID().toString());
        params.put("data", new JSONObject().put("money", 15000).put("country", "RU"));

        HttpResponse resp = executePut(params);
        assertEquals(200, resp.getStatusLine().getStatusCode());

        HttpGet get = new HttpGet("http://localhost:4567/sync?uuid=" + params.get("uuid"));
        resp = client.execute(get);

        String strResponse = readStream(resp.getEntity().getContent());
        JSONObject obj = new JSONObject(strResponse);
        JSONObject originalObj = (JSONObject) params.get("data");

        assertEquals(originalObj.getInt("money"), obj.getInt("money"));
        assertEquals(originalObj.get("country"), obj.get("country"));
    }

    @Test
    public void putToSyncWithMissingHttpParams_shouldFail() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", 1);

        HttpResponse resp = executePut(params);
        assertNotEquals(200, resp.getStatusLine().getStatusCode());
    }

    @Test
    public void putWithInvalidJson_shouldFail() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", UUID.randomUUID().toString());
        params.put("data", new JSONObject().put("country", "RU"));

        HttpResponse resp = executePut(params);
        assertNotEquals(200, resp.getStatusLine().getStatusCode());

        params.put("data", new JSONObject().put("money", 1000));

        resp = executePut(params);
        assertNotEquals(200, resp.getStatusLine().getStatusCode());
    }

    @Test
    public void putWithInvalidParamType_shouldFail() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", UUID.randomUUID().toString());
        params.put("data", new JSONObject().put("money", "string value!"));

        HttpResponse resp = executePut(params);
        assertNotEquals(200, resp.getStatusLine().getStatusCode());

        params.put("data", new JSONObject()
                .put("country", -1));

        resp = executePut(params);
        assertNotEquals(200, resp.getStatusLine().getStatusCode());
    }

    @Test
    public void putWithTooBigJson_shouldFail() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", UUID.randomUUID().toString());

        String superLongString = "";

        for (int i = 0; i < 11 * 1024; i++) {
            superLongString += "R";
        }

        params.put("data", new JSONObject()
                .put("money", 1)
                .put("country", "UA")
                .put("longStr", superLongString));

        HttpResponse resp = executePut(params);
        assertNotEquals(200, resp.getStatusLine().getStatusCode());
    }

    private HttpResponse executePut(Map<String, Object> params) throws Exception {
        HttpPut put = new HttpPut("http://localhost:4567/sync");

        List<NameValuePair> httpParams = new ArrayList<>();

        params.forEach((key, value) -> httpParams.add(new BasicNameValuePair(key, value.toString())));

        put.setEntity(new UrlEncodedFormEntity(httpParams));
        return client.execute(put);
    }

    private String readStream(InputStream in) throws Exception {
        return new BufferedReader(new InputStreamReader(in)).readLine();
    }

}
