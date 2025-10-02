package http;

import okhttp3.*;

import java.io.File;

public class HttpClientUtil {
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .build();

    public static void runAsync(Request request, Callback callback) {
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    public static Request buildUploadFileRequest(File selectedFile) {
        String finalUrl = HttpUrl
                .parse(Constants.LOADFILE)
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
}
