package org.example;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Main {

    public static void sendPost(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response.statusCode() = " + response.statusCode());
    }

    public static void sendDelete(String uri, int id) throws IOException, InterruptedException {
        uri = uri + "/" + id;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .DELETE()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response.statusCode() = " + response.statusCode());
    }
    public static void sendPut(String uri, int id) throws IOException, InterruptedException {
        uri = uri + "/" + id;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response.statusCode() = " + response.statusCode());
        System.out.println("response.body() = " + response.body());
    }


    public static void getAllUsers(String uri) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public static void getUserById(String uri, int id) throws IOException, InterruptedException{
        uri = uri + "/" + id;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public static void getUserByUsername(String uri, String username) throws IOException, InterruptedException{
        uri = uri + "?username=" + username;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public static void getTheLatestCommentsByUserId(String uri, int id) throws IOException, InterruptedException{
        String postUri = uri + "/" + id + "/posts";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(postUri))
                .GET()
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray objects = new JSONArray(response.body().toString());
        JSONObject object = objects.getJSONObject(objects.length()-1);

        String commentsUri = "https://jsonplaceholder.typicode.com/posts";
        commentsUri = commentsUri + "/" + object.getInt("id") + "/comments";

        request = HttpRequest.newBuilder()
                .uri(URI.create(commentsUri))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String fileName = "user-" + id + "-post-" + object.getInt("id") + "-comments.json";
        File file = new File(fileName);
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(response.body().toString());
        writer.close();
    }

    public static void getAllUncomletedByUserId(String uri, int id) throws IOException, InterruptedException{
        uri = uri + "/" + id + "/todos";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray array = new JSONArray(response.body().toString());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            if(!jsonObject.getBoolean("completed")){
                System.out.println(jsonObject.toString());
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        sendPost("https://jsonplaceholder.typicode.com/users");
        getUserById("https://jsonplaceholder.typicode.com/users", 3);
        getUserByUsername("https://jsonplaceholder.typicode.com/users", "Delphine");
        getTheLatestCommentsByUserId("https://jsonplaceholder.typicode.com/users", 1);
        getAllUncomletedByUserId("https://jsonplaceholder.typicode.com/users", 1);

    }

}
