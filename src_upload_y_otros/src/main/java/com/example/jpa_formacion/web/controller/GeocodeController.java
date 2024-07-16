package com.example.jpa_formacion.web.controller;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
public class GeocodeController {

    @RequestMapping(path = "/geocode", method = RequestMethod.GET )
    public String getGeocode(@RequestParam String address) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        /*
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
                .get()
                .addHeader("X-RapidAPI-Key", "1cb2f66773msh050f31d99d83a25p1fc362jsn693ab07edf3f")
                .addHeader("X-RapidAPI-Host", "google-maps-geocoding.p.rapidapi.com")
                .build();
*/
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?address=164%20Townsend%20St.%2C%20San%20Francisco%2C%20CA&language=en")
                .get()
                .addHeader("X-RapidAPI-Key", "1cb2f66773msh050f31d99d83a25p1fc362jsn693ab07edf3f")
                .addHeader("X-RapidAPI-Host", "google-maps-geocoding.p.rapidapi.com")
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        return responseBody.string();
    }

}