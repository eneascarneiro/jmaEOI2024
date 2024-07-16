package com.example.jpa_formacion.util;


// Data streaming
import com.example.jpa_formacion.dto.ListarArchivosDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.json.JSONObject;

import java.io.*;

// Networking and HTTP/HTTPS
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

// Charsets
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

// Utilities
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.List;

// Constants

public class Request {
    public String method;
    public String url;
    public String output;
    public HttpURLConnection connection;
    public Map<String, List<String>> headers;

    /**
     * Default constructor that always makes a GET request
     * @param url URL to make a GET request
     */
    public Request(String url) {
        this.url = url;
        this.method = Constants.GET;

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            this.connection = connection;

            connection.setRequestMethod(this.method);

            connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
            connection.setReadTimeout(Constants.STANDARD_TIMEOUT);

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER
                        || status == 308)
                    redirect = true;
            }
            System.out.println("Response Code ... " + status);
            if (redirect) {

                // get redirect url from "location" header field
                String newUrl = connection.getHeaderField("Location");

                // get the cookie if need, for login
                String cookies = connection.getHeaderField("Set-Cookie");

                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setRequestProperty("Cookie", cookies);
                connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
                connection.setReadTimeout(Constants.STANDARD_TIMEOUT);
            }
            this.output = read(connection);
            this.headers = connection.getHeaderFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parameterized constructor for read-only and writable requests that also accepts headers
     * @param url URL to request
     * @param method Request method
     * @param data Data to write to the request
     * @param headers Headers for the request
     */
    public Request(String url, String method, String data, String[][] headers) {
        this.url = url;
        this.method = method.toUpperCase();

        if (data == null) data = "{}";

        HttpURLConnection connection;

        if (this.method.equals(Constants.POST) || this.method.equals(Constants.DELETE) || this.method.equals(Constants.PUT) || this.method.equals(Constants.PATCH)) {
            if (headers == null) {
                Request writable = new Request(url, method, data);
                this.output = writable.output;
            } else {
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    this.connection = connection;

                    connection.setRequestMethod(this.method);

                    connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
                    connection.setReadTimeout(Constants.STANDARD_TIMEOUT);

                    connection.setDoOutput(true);

                    // Add the headers
                    setHeaders(connection, headers);

                    try {
                        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                        int length = bytes.length;
                        connection.setFixedLengthStreamingMode(length);

                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(bytes, 0, length);
                        outputStream.flush();
                        outputStream.close();
                    } finally {
                        this.output = read(connection);
                        connection.getInputStream().close();
                    }
                    this.headers = connection.getHeaderFields();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (headers == null) {
                Request readOnly = new Request(url);
                this.output = readOnly.output;
            } else {
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    this.connection = connection;

                    connection.setRequestMethod(this.method);

                    connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
                    connection.setReadTimeout(Constants.STANDARD_TIMEOUT);

                    // Adds headers
                    setHeaders(connection, headers);

                    this.output = read(connection);
                    this.headers = connection.getHeaderFields();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Parameterized constructor for read-only and writable requests
     * @param url URL to request
     * @param method Request method
     * @param data Data to write to the request
     */
    public Request(String url, String method, String data) {
        this.url = url;
        this.method = method.toUpperCase();

        if (data == null) data = "{}";

        HttpURLConnection connection;

        if (this.method.equals(Constants.POST) || this.method.equals(Constants.DELETE) || this.method.equals(Constants.PUT) || this.method.equals(Constants.PATCH)) {
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                this.connection = connection;

                connection.setRequestMethod(this.method);

                connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
                connection.setReadTimeout(Constants.STANDARD_TIMEOUT);

                connection.setDoOutput(true);

                try {
                    byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                    int length = bytes.length;
                    connection.setFixedLengthStreamingMode(length);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(bytes, 0, length);
                    outputStream.flush();
                    outputStream.close();
                } finally {
                    this.output = read(connection);
                    connection.getInputStream().close();
                }

                this.headers = connection.getHeaderFields();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Request readOnly = new Request(url);
            this.output = readOnly.output;
            this.headers = readOnly.headers;
        }
    }
    /**
     * Overloaded constructor for POST requests only
     * @param url URL to request
     * @param jsonobject
     */
    public Request(String url, JSONObject requestParam) {
        this.url = url;
        this.method = Constants.POST;

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            this.connection = connection;
            connection.setRequestMethod(this.method);

            connection.setConnectTimeout(Constants.STANDARD_TIMEOUT1);
            connection.setReadTimeout(Constants.STANDARD_TIMEOUT1);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            System.out.println("Request(String url, JSONObject requestParam) : " + url + "01");


            try {
                byte[] bytes = requestParam.toString().getBytes("utf-8");
                int length = bytes.length;
                connection.setFixedLengthStreamingMode(length);
                OutputStream os = connection.getOutputStream();
                os.write(bytes, 0, bytes.length);
            } finally {
                if (connection.getResponseCode() == 200){
                    this.output = read(connection);
                    connection.getInputStream().close();
                }
                else
                {
                    this.output = "{\"errormessage\": \""+ connection.getResponseMessage() + "\", \"errorcode\": " + connection.getResponseCode() + "}";
                }
            }

            this.headers = connection.getHeaderFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Overloaded constructor for POST requests only
     * @param url URL to request
     * @param jsonobject
     */
    public Request(String url, JSONObject jsonObject, Integer proc) {
        this.url = url;
        this.method = Constants.POST;

        try {

            byte[] responseBody = null;
            byte[] request = jsonObject.toString().getBytes();

            var client = HttpClient.newHttpClient();

            var httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(request))
                    .build();

            HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(httpRequest, bodyHandler);
            future.thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Overloaded constructor for POST requests only
     * @param url URL to request
     * @param jsonobject
     * @param path on java server
     */
    public Request(String url, JSONObject requestParam, String path) {
        this.url = url;
        this.method = Constants.POST;

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            this.connection = connection;
            connection.setRequestMethod(this.method);

            connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
            connection.setReadTimeout(Constants.STANDARD_TIMEOUT);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            System.out.println("Request(String url, JSONObject requestParam , String action) : " + url + "01");

            // This will get input data from the server
            InputStream inputStream = null;

            // This will read the data from the server;
            OutputStream outputStream = null;
            try {
                byte[] bytes = requestParam.toString().getBytes("utf-8");
                int length_json = bytes.length;
                connection.setFixedLengthStreamingMode(length_json);
                OutputStream os = connection.getOutputStream();
                os.write(bytes, 0, bytes.length);

                // Requesting input data from server
                inputStream = connection.getInputStream();

                //get the path from input parameter
                //String pathdir =  path.substring(0,path.lastIndexOf("/"));

                // Open local file writer
                outputStream = new FileOutputStream(path);

                // Limiting byte written to file per loop
                byte[] bufferout = new byte[2048];

                // Increments file size
                int length;

                // Looping until server finishes
                while ((length = inputStream.read(bufferout)) != -1) {
                    // Writing data
                    outputStream.write(bufferout, 0, length);
                }
            } catch (Exception ex) {
                System.out.println("error al descargar archivo:" + ex);
            } finally {
                this.output = read(connection);
                connection.getInputStream().close();
            }
            // closing used resources
            // The computer will not be able to use the image
            // This is a must

            outputStream.close();
            inputStream.close();

            this.headers = connection.getHeaderFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Overloaded constructor for POST requests only
     * @param url URL to request
     * @param data Data to post
     */
    public Request(String url, String data) {
        this.url = url;
        this.method = Constants.POST;

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            this.connection = connection;

            connection.setRequestMethod(this.method);

            connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
            connection.setReadTimeout(Constants.STANDARD_TIMEOUT);

            connection.setDoOutput(true);

            try {
                byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                int length = bytes.length;
                connection.setFixedLengthStreamingMode(length);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bytes, 0, length);
                outputStream.flush();
                outputStream.close();
            } finally {
                this.output = read(connection);
                connection.getInputStream().close();
            }

            this.headers = connection.getHeaderFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method will read output from HTTP/HTTPS requests
     * @param connection The connection as an instance of HttpURLConnection
     * @return Output as a String from reading the connection output
     */
    public static String read(HttpURLConnection connection) {
        InputStream connectionInputStream = null;
        try {
            connectionInputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader reader = null;
        if (connection.getContentEncoding() != null) {
            try {
                assert connectionInputStream != null;
                reader = new InputStreamReader(new GZIPInputStream(connectionInputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                reader = new InputStreamReader(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Empty char value
        int ch;

        // String Builder to add to the final string
        StringBuilder stringBuilder = new StringBuilder();

        // Appending the data to a String Builder
        while (true) {
            try {
                assert reader != null;
                ch = reader.read();
                if (ch == -1) {
                    return stringBuilder.toString();
                }

                stringBuilder.append((char) ch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the headers with the given connection
     * @param connection The HttpURLConnection connection
     * @param headers And headers in the form of a 2-dimensional array
     */
    public static void setHeaders(HttpURLConnection connection, String[][] headers) {
        final Map<String, String> mapHeaders = new HashMap<>(headers.length);
        for (String[] map : headers) {
            mapHeaders.put(map[0], map[1]);
        }

        mapHeaders.forEach(connection::setRequestProperty);
    }

    /**
     * Direct get method, only uses the URL and any provided headers
     * @param url URL for the GET request
     * @param headers Headers in the form of a 2-dimensional array
     * @return Output of the request as a String
     */
    public static String get(String url, String[][] headers) {
        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(Constants.GET);

            connection.setConnectTimeout(Constants.STANDARD_TIMEOUT);
            connection.setReadTimeout(Constants.STANDARD_TIMEOUT);

            if (headers != null) setHeaders(connection, headers);

            return read(connection);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Method to get the response headers and return them as a regular Map
     * @return Mapped response headers
     */
    public Map<String, String> getResponseHeaders() {
        Map<String, String> mapHeaders = new HashMap<>();
        this.headers.forEach((key, value) -> value.forEach(k -> mapHeaders.put(k, key)));
        return mapHeaders;
    }
}