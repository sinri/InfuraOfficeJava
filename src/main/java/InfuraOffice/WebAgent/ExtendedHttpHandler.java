package InfuraOffice.WebAgent;

import InfuraOffice.ThyLogger;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExtendedHttpHandler implements HttpHandler {
    private static Map<String, String> formData2Dic(String formData) {
        Map<String, String> result = new HashMap<>();
        if (formData == null || formData.trim().length() == 0) {
            return result;
        }
        final String[] items = formData.split("&");
        Arrays.stream(items).forEach(item -> {
            final String[] keyAndVal = item.split("=");
            if (keyAndVal.length == 2) {
                try {
                    final String key = URLDecoder.decode(keyAndVal[0], "utf8");
                    final String val = URLDecoder.decode(keyAndVal[1], "utf8");
                    result.put(key, val);
                } catch (UnsupportedEncodingException e) {
                    //
                }
            }
        });
        return result;
    }

    HttpExchange httpExchange;
    Map<String, String> queryMap;
    Map<String, String> postMap;
    HashMap<String, Object> outputDataMap;


    public ExtendedHttpHandler() {
        queryMap = null;
        postMap = null;
        outputDataMap = new HashMap<>();
    }

    protected Map<String, String> getQueryMap(HttpExchange httpExchange) {
        //获得查询字符串(get)
        String queryString = httpExchange.getRequestURI().getQuery();
        return formData2Dic(queryString);
    }

    protected Map<String, String> getPostMap(HttpExchange httpExchange) throws IOException {
        //获得表单提交数据(post)
        String postString = IOUtils.toString(httpExchange.getRequestBody(), "utf8");
        return formData2Dic(postString);
    }

    protected String seekQuery(String field) {
        return seekQuery(field, null);
    }

    protected String seekPost(String field) {
        return seekPost(field, null);
    }

    protected String seekQuery(String field, String defaultValue) {
        return queryMap.getOrDefault(field, defaultValue);
    }

    protected String seekPost(String field, String defaultValue) {
        return postMap.getOrDefault(field, defaultValue);
    }

    protected Map<String, String> getQueryMap() {
        return queryMap;
    }

    protected Map<String, String> getPostMap() {
        return postMap;
    }

    protected void setContentTypeHeaderForExtension(String extension) {
        String contentType = "text/html; charset=UTF-8";
        switch (extension) {
            case "js":
                contentType = "application/x-javascript";
                break;
            case "css":
                contentType = "text/css";
                break;
            case "woff":
                contentType = "application/octet-stream";
                break;
            case "json":
                contentType = "application/json";
                break;
        }
        httpExchange.getResponseHeaders().set("Content-Type", contentType);
    }

    protected void redirect(String location, int code) throws IOException {
        httpExchange.getResponseHeaders().set("Location", location);
        output("Redirect to " + location, code);
    }

    protected void output(String output, int code) throws IOException {
        httpExchange.sendResponseHeaders(code, output.getBytes("utf8").length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(output.getBytes("utf8"));
        os.close();
    }

    protected void outputJSON(Map<String, Object> map) throws IOException {
        setContentTypeHeaderForExtension("json");
        String json = (new Gson()).toJson(map);
        output(json, 200);
    }

    protected void sayOK(String data) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "OK");
        map.put("data", data);
        outputJSON(map);
    }

    protected void sayOK(Map<String, Object> data) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "OK");
        map.put("data", data);
        outputJSON(map);
    }

    protected void registerDataProperty(String key, Object value) {
        outputDataMap.put(key, value);
    }

    protected void sayOK() throws IOException {
        ThyLogger.logDebug("outputDataMap: " + outputDataMap);
        sayOK(outputDataMap);
    }

    protected void sayFail(String data) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "FAIL");
        map.put("data", data);
        outputJSON(map);
    }

    protected void showFrontendPage(String filename) throws IOException {
        showFrontendPage(filename, 200);
    }

    protected void showFrontendPage(String filename, int code) throws IOException {
        ThyLogger.logInfo("showFrontendPage: " + filename + " for code " + code);
        InputStream resourceAsStream = ExtendedHttpHandler.class.getClassLoader().getResourceAsStream("frontend" + filename);
        //String path = ExtendedHttpHandler.class.getClassLoader().getResource("frontend/"+filename).getPath();
        ThyLogger.logInfo("fetch page from resources: " + resourceAsStream);// -> /Users/Sinri/Codes/idea/InfuraOfficeJava/target/classes/

        try {
            //BufferedReader br = new BufferedReader(new FileReader(path));
            InputStreamReader x = new InputStreamReader(resourceAsStream);
            BufferedReader br = new BufferedReader(x);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                sb.append(line);
            }
            br.close();
            output(sb.toString(), code);
        } catch (IOException exception) {
            String output = "500 IO Error " + exception.getMessage();
            output(output, 500);
        }
    }

    protected void showFrontendResourceFile(String filename) throws IOException {
        String extension = FilenameUtils.getExtension(filename);
        setContentTypeHeaderForExtension(extension);
        showFrontendPage(filename);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        queryMap = getQueryMap(httpExchange);
        postMap = getPostMap(httpExchange);

        ThyLogger.logInfo("Handling request: " + httpExchange.getRequestURI().getRawPath());
    }

    protected WebSessionAgent.WebSessionEntity validateUserSession() {
        String token = seekPost("token", "");
        WebSessionAgent.WebSessionEntity webSessionEntity = WebSessionAgent.getSharedInstance().validateSession(token);
        ThyLogger.logDebug("validateUserSession for token " + token + " -> " + webSessionEntity);
        // here might need to set some user validation
        if (webSessionEntity == null || webSessionEntity.getCurrentUser() == null) {
            return null;
        }
        return webSessionEntity;
    }

    protected void assertRequestFromValidUserSession() throws Exception {
        WebSessionAgent.WebSessionEntity webSessionEntity = validateUserSession();
        if (webSessionEntity == null) {
            throw new Exception("Not a valid session!");
        }
    }
}
