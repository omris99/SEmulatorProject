package http;

import okhttp3.*;

import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.function.Consumer;

public class HttpClientUtil {


    private final static chat.client.util.http.SimpleCookieManager simpleCookieManager = new chat.client.util.http.SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(Request request, Callback callback) {
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static Response runSync(Request request) throws Exception {
        Call call = HTTP_CLIENT.newCall(request);
        return call.execute();
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    public static Request buildUploadFileRequest(File selectedFile) {
        String finalUrl = HttpUrl
                .parse(ServerPaths.LOAD_FILE)
                .newBuilder()
                .build()
                .toString();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "fileContent",
                        selectedFile.getName(),
                        RequestBody.create(selectedFile, MediaType.parse("text/plain"))
                )
                .build();

        return new Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .build();
    }

    public static Request createEmptyBodyPostRequest(String url) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .build();
    }

    public static Request createGetRequest(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }
}
