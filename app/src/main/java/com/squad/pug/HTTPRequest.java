package com.squad.pug;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jeffbahns on 11/14/15.
 */
class HTTPRequest  {

    public String request() {
        return "This is a http request, threaded!";
    }

}
