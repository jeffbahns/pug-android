package com.squad.pug;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bryanpenaloza on 11/11/15.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://6mansquad.esy.es/";


    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storedUserDataInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();

    }
    public void storedGameDataInBackground(Game game, GetGameCallback gameCallback) {
        progressDialog.show();
        new StoreGameDataAsyncTask(game, gameCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callBack).execute();
    }
    public void fetchGameDataInBackground(Game game, GetGameCallback callBack) {
//        progressDialog.show();
        new fetchGameDataAsyncTask(game, callBack).execute();
    }
    public void getIDInBackground(Game game, GetGameCallback callback) {
        //progressDialog.show();
        new getIDAsyncTask(game, callback).execute();
    }
    public void joinedGameInBackground(Game game, GetGameCallback gameCallback) {
        //progressDialog.show();
        new JoinGameAsyncTask(game, gameCallback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("tel_number", user.tel_number));
            dataToSend.add(new BasicNameValuePair("sex", user.sex));
            dataToSend.add(new BasicNameValuePair("age", user.age + ""));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);

        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if(jObject.length() == 0){
                    user = null;
                }else{
                    String name = jObject.getString("name");
                    String tel_number = jObject.getString("tel_number");
                    String sex = jObject.getString("sex");
                    int age = jObject.getInt("age");

                    returnedUser = new User(user.username, name, tel_number, sex, age, user.password);

                }

            } catch (Exception e) {
                e.printStackTrace();

            }


            return returnedUser;
        }


        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);

            progressDialog.dismiss();
            userCallback.done(returnedUser);

        }
    }
    public class StoreGameDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Game game;
        GetGameCallback gameCallback;

        public StoreGameDataAsyncTask(Game game, GetGameCallback gameCallback) {
            this.game = game;
            this.gameCallback = gameCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", game.id + ""));

            dataToSend.add(new BasicNameValuePair("user", game.user));
            dataToSend.add(new BasicNameValuePair("time", game.time));
            dataToSend.add(new BasicNameValuePair("date", game.date));
            dataToSend.add(new BasicNameValuePair("num_players", game.num_players + ""));
            dataToSend.add(new BasicNameValuePair("players_attending", game.players_attending));

            dataToSend.add(new BasicNameValuePair("location", game.location));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Create_Game.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            gameCallback.done(null);
            super.onPostExecute(aVoid);

        }
    }
    public class fetchGameDataAsyncTask extends AsyncTask<Void, Void, Game> {
        Game game;
        GetGameCallback gameCallback;

        public fetchGameDataAsyncTask(Game game, GetGameCallback gameCallback) {
            this.game = game;
            this.gameCallback = gameCallback;
        }

        @Override
        protected Game doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", Integer.toString(game.id)));
            dataToSend.add(new BasicNameValuePair("location", game.location));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchGameData.php");

            Game returnedGame = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if(jObject.length() == 0){
                    game = null;
                }else{
                    String user = jObject.getString("user");
                    String time = jObject.getString("time");
                    String date = jObject.getString("date");
                    int num_players = jObject.getInt("num_players");
                    String players_attending = jObject.getString("players_attending");


                    returnedGame = new Game(game.id, user, time, date, num_players, players_attending, game.location);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return returnedGame;
        }


        @Override
        protected void onPostExecute(Game returnedGame) {
            super.onPostExecute(returnedGame);
            progressDialog.dismiss();
            gameCallback.done(returnedGame);
        }
    }
    public class getIDAsyncTask extends AsyncTask<Void, Void, Game> {
        Game game;
        GetGameCallback gameCallback;

        public getIDAsyncTask(Game game, GetGameCallback gameCallback) {
            this.game = game;
            this.gameCallback = gameCallback;
        }

        @Override
        protected Game doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("location", game.location));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "GetMaxId.php");

            Game returnedGame = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0) {
                    game = null;
                } else {
                    int id = jObject.getInt("id");


                    returnedGame = new Game(id);
                }
                // }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return returnedGame;
        }


        @Override
        protected void onPostExecute(Game returnedGame) {
            super.onPostExecute(returnedGame);
            progressDialog.dismiss();
            gameCallback.done(returnedGame);
        }
    }
    public class JoinGameAsyncTask extends AsyncTask<Void, Void, Void> {
        Game game;
        GetGameCallback gameCallback;

        public JoinGameAsyncTask(Game game, GetGameCallback gameCallback) {
            this.game = game;
            this.gameCallback = gameCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", game.id + ""));
            dataToSend.add(new BasicNameValuePair("user", game.user));
            dataToSend.add(new BasicNameValuePair("location", game.location));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "JoinGame.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            gameCallback.done(null);
            super.onPostExecute(aVoid);

        }
    }
}