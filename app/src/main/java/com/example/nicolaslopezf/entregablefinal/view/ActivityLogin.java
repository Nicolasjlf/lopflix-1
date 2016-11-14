package com.example.nicolaslopezf.entregablefinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nicolaslopezf.entregablefinal.R;
import com.example.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.example.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.example.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nicolas Lopez F on 11/13/2016.
 */

public class ActivityLogin  extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "o0AwMxuktney6F0OxNZXhup35";
    private static final String TWITTER_SECRET = "bkItOFtZmpTPmBDWP3zEylH2iYeOUL1eLy6a3AVaV444eXra0w";

    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;
    private TwitterLoginButton loginButton;

    ArrayList<MovieDB> backgroundList;


    protected void onCreate(Bundle savedInstanceState){
        final ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;


        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        PeliculaController peliculaController = new PeliculaController();
        imageView1 = (ImageView) findViewById(R.id.activityLogin_imageView11);
        imageView2 = (ImageView) findViewById(R.id.activityLogin_imageView12);
        imageView3 = (ImageView) findViewById(R.id.activityLogin_imageView13);
        imageView4 = (ImageView) findViewById(R.id.activityLogin_imageView21);
        imageView5 = (ImageView) findViewById(R.id.activityLogin_imageView22);
        imageView6 = (ImageView) findViewById(R.id.activityLogin_imageView23);
        imageView7 = (ImageView) findViewById(R.id.activityLogin_imageView31);
        imageView8 = (ImageView) findViewById(R.id.activityLogin_imageView32);
        imageView9 = (ImageView) findViewById(R.id.activityLogin_imageView33);


        peliculaController.obtenerListaDePeliculasTMDB(TMDBHelper.getPopularMovies(TMDBHelper.language_ENGLISH,1),this, new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieDB peliculasTrending = (ContainerMovieDB) resultado;
                backgroundList = peliculasTrending.getResult();
                long seed = System.nanoTime();
                Collections.shuffle(backgroundList, new Random(seed));
                insertMovieOntoImageView(0,imageView1);
                insertMovieOntoImageView(1,imageView2);
                insertMovieOntoImageView(2,imageView3);
                insertMovieOntoImageView(3,imageView4);
                insertMovieOntoImageView(4,imageView5);
                insertMovieOntoImageView(5,imageView6);
                insertMovieOntoImageView(6,imageView7);
                insertMovieOntoImageView(7,imageView8);
                insertMovieOntoImageView(8,imageView9);

            }

        });
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(intent);

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });




    }
    public void insertMovieOntoImageView(int movieNumber,ImageView imageView){
        Picasso.with(ActivityLogin.this).load(TMDBHelper.getImagePoster(TMDBHelper.IMAGE_SIZE_H632, backgroundList.get(movieNumber).getPoster())).
                error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(imageView);

    }
    public void onClickFondo(View view){


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
