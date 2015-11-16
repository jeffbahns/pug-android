package com.squad.pug;
import android.util.Log;

import com.squareup.okhttp.internal.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jeffbahns on 11/14/15.
 */
public class HTTPRequest {

    public static String getCourts(String targetURL, String urlParameters) throws IOException {
        BufferedReader rd;
        OutputStreamWriter wr;
        try
        {
            URL url = new URL("https://reddit.com");
            URLConnection conn = url.openConnection();
            InputStream input = conn.getInputStream();

            Log.v("test", input);
            return "";
        }
        catch (Exception e) {
            System.out.println(e.toString());
            Log.v("test", "didnt work ");
            return "";
        }
    }
}
