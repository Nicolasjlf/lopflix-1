package com.example.nicolaslopezf.entregablefinal.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolaslopezf.entregablefinal.dao.PeliculaDAO;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.example.nicolaslopezf.entregablefinal.model.MovieDB.MovieDBTrailerContainer;
import com.example.nicolaslopezf.entregablefinal.model.Pelicula;
import com.example.nicolaslopezf.entregablefinal.model.Trackt.WrapperPeliculaTrckt;
import com.example.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.example.nicolaslopezf.entregablefinal.utils.TMDBHelper;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Nicolas Lopez F on 10/22/2016.
 */

public class PeliculaController {

    public ArrayList<Pelicula> obtenerFavoritasDeBD(Context context){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        return peliculaDAO.getAllPeliculasFavoritasFromDatabase();
    }


    public void obtenerPeliculaPorID(Context context, String imdbID, final ResultListener<Pelicula> resultListenerView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        peliculaDAO.damePeliculaPorID(context, new ResultListener<Pelicula>() {
            @Override
            public void finish(Pelicula resultado) {
               resultListenerView.finish(resultado);
            }
        }, imdbID);

    }

    public ArrayList<Pelicula> damePeliculasPorGenero(String genero, Context context){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        ArrayList<Pelicula> todasLasPeliculas = peliculaDAO.dameListaPeliculas(context);
        //ArrayList<Pelicula> peliculasPorGenero = busquedaPorGenero(todasLasPeliculas,genero);
        ArrayList<Pelicula> peliculasPorGenero = encontrarMismoGenero(todasLasPeliculas,genero);


        return peliculasPorGenero;
    }
    public void addPeliculaFavoritaToDatabase(Context context, Pelicula pelicula){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        if(!peliculaDAO.getAllPeliculasFavoritasFromDatabase().contains(pelicula)){
            peliculaDAO.addPeliculaFavoritaToDatabase(pelicula);
        }
    }

    public Boolean esFavorita(Pelicula pelicula, Context context){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        Boolean respuesta = false;
        if (peliculaDAO.getAllPeliculasFavoritasFromDatabase().contains(pelicula)){
            respuesta = true;
        }
        return respuesta;
    }

    public void changeFavoriteStatus(Context context, Pelicula pelicula, String imdbID,String tmdbID){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        if(!esFavorita(pelicula,context)){
            peliculaDAO.addIDsToTable(imdbID,tmdbID);
            peliculaDAO.addPeliculaFavoritaToDatabase(pelicula);
        }else{
            peliculaDAO.removePeliculaFavoritaFromDatabase(pelicula);
        }
    }




    //EL FOREACH QUE BUSCA LAS PELICULAS DEL GENERO QUE LE INDICAMOS COMO PARAMETRO. DEVUELVE UN NUEVO ARRAYLIST
    private ArrayList<Pelicula> busquedaPorGenero(ArrayList<Pelicula> peliculas, String genero){

        ArrayList<Pelicula> peliculasPorGenero = new ArrayList<>();



        for(Pelicula unaPelicula:peliculas){

            if(unaPelicula.getGenre().contains(genero)){

                peliculasPorGenero.add(unaPelicula);
            }
        }

        return peliculasPorGenero;
    }

    private ArrayList<Pelicula> encontrarMismoGenero(ArrayList<Pelicula> peliculas, String genero){
        ArrayList<Pelicula> peliculasPorGenero = new ArrayList<>();
        ArrayList<String> generos = new ArrayList<String>();
        generos.add("Comedy");
        generos.add("Animation");
        generos.add("Action");
        generos.add("Thriller");
        generos.add("War");
        generos.add("Horror");
        generos.add("Sci-Fi");
        generos.add("Family");

        for (String g: generos) {
            if (genero.contains(g)){
                for (Pelicula pelicula: peliculas) {
                    if (pelicula.getGenre().contains(g) && !peliculasPorGenero.contains(pelicula)){
                        peliculasPorGenero.add(pelicula);
                    }
                }
            }
        }
        return peliculasPorGenero;
    }

    public ArrayList<Pelicula> dameTodasLasPeliculas(Context context){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        ArrayList<Pelicula> arrayADevolver;
        arrayADevolver = peliculaDAO.dameListaPeliculas(context);
        return arrayADevolver;
    }


    //OBTENER PELICULAS DE TMDB
    public void obtenerListaDePeliculasTMDB(String TMDBurl, Context context, final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
//        String url = TMDBHelper.getPopularMovies(TMDBHelper.language_ENGLISH, 1);
        peliculaDAO.flasehadaCreadoraDeAsync(new ContainerMovieDB(), TMDBurl, new ResultListener<Object>() {
                    @Override
                    public void finish(Object resultado) {
                        listenerFromView.finish(resultado);
                    }
                }
        ,context);
    }

    public void  obtenerPeliculaTMDB(String movieID,Context context, final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        String url = TMDBHelper.getMovieDetailURL(movieID,TMDBHelper.language_ENGLISH);
        peliculaDAO.flasehadaCreadoraDeAsync(new MovieDB(), url, new ResultListener<Object>() {

            @Override
            public void finish(Object resultado) {
                listenerFromView.finish(resultado);
            }
        },context);
    }


    public void obtenerPeliculasPorGenero(String genero, Context context ,final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        String generoID = null;
        switch (genero){
            case "action" : generoID= TMDBHelper.MOVIE_GENRE_ACTION;
                break;
            case "comedy" : generoID= "35";
                break;
            case "horror" : generoID= "27";
                break;
            case "adventure" : generoID= "12";
                break;
            case "animation" : generoID= "16";
                break;
            case "crime" : generoID= "80";
                break;
            case "documentary" : generoID= "99";
                break;
            case "drama" : generoID= "18";
                break;
            case "family" : generoID= "10751";
                break;
            case "history" : generoID= "36";
                break;
            case "music" : generoID= "10402";
                break;
            case "mistery" : generoID= "9648";
                break;
            case "romance" : generoID= "10749";
                break;
            case "sci-fi" : generoID= "878";
                break;
        }
        String url = TMDBHelper.getMoviesByGenre(generoID,1,TMDBHelper.language_ENGLISH);
        peliculaDAO.flasehadaCreadoraDeAsync(new ContainerMovieDB(), url, new ResultListener<Object>() {
            @Override
            public void finish(Object resultado) {
                listenerFromView.finish(resultado);
            }
        },context);
    }

    public String obtenerTMDBidDeBaseDeDatos(Context context,String imdbID){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        return peliculaDAO.getTMDBidFromDataBase(imdbID);
    }

    public void obtenerTrailerDePeliculaTMDB(Context context,String tmdbID, final ResultListener listenerFromView){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        peliculaDAO.flasehadaCreadoraDeAsync(new MovieDBTrailerContainer(), TMDBHelper.getTrailerURL(tmdbID, TMDBHelper.language_ENGLISH),
                new ResultListener<Object>() {

                    @Override
                    public void finish(Object resultado) {
                        listenerFromView.finish(resultado);
                    }
                },context);
    }
}
