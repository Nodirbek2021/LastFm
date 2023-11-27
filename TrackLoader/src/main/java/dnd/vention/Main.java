package dnd.vention;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dnd.vention.db.DbConnection;
import dnd.vention.payload.attributes.Rank;
import dnd.vention.payload.baseObjects.TrackObject;
import dnd.vention.payload.collectionObjects.Tracks;
import dnd.vention.payload.responses.ResponseTrackByArtistEntity;
import dnd.vention.service.GetTopArtistsService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String artistName="TheWeeknd";
//        URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist="+artistName+"&api_key=25264164c456b374e86800acc6656239&format=json");
        URL url = new URL("http://localhost:8081/api/v1/getTopArtists");
        URLConnection connection = url.openConnection();


        URL obj = new URL("http://localhost:8081/api/v1/getTopArtists");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        con.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.8,es;q=0.6");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Host", "localhost:8000");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);








        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String inputline;
        while ((inputline = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputline);
        }
        bufferedReader.close();
        System.out.println(inputline);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        ResponseTrackByArtistEntity responseTrackByArtistEntity = objectMapper.readValue(stringBuilder.toString(), ResponseTrackByArtistEntity.class);
//        Tracks tracks = responseTrackByArtistEntity.getTracks();
//        List<TrackObject> trackObjectList = tracks.getTrackObjectList();
//
//        System.out.println(trackObjectList.get(1));
//        System.out.println(tracks);


//        ResponseArtistEntity artist = objectMapper.readValue(stringBuilder.toString(), ResponseArtistEntity.class);
//        System.out.println(artist);
//        Artists artists = artist.getArtists();
//        List<ArtistObject> artist1 = artists.getArtist();
//        for (int i = 0; i < artist1.size(); i++) {
//            System.out.print(i + ":> ");
//            System.out.println(artist1.get(i));
//            ArtistObject artistObject = artist1.get(i);
//            System.out.println("-----");
//        }
//        System.out.println();

//        ResponseTrackEntity responseTrackEntity = objectMapper.readValue(stringBuilder.toString(), ResponseTrackEntity.class);
//        System.out.println(responseTrackEntity);
//        Tracks tracks = responseTrackEntity.getTracks();
//        List<TrackObject> track = tracks.getTrack();
//        int size = track.size();
//        TrackObject trackObject = track.get(size-1);
//        System.out.println(trackObject);


//        ResponseTopTracksEntity responseTopTracksEntity = objectMapper.readValue(stringBuilder.toString(), ResponseTopTracksEntity.class);
//        System.out.println(responseTopTracksEntity);
//        TopTracks toptracks = responseTopTracksEntity.getToptracks();
//        List<TrackObject> track = toptracks.getTrackObjectList();
//        TrackObject trackObject = track.get(3);
//        Rank name = trackObject.getRank();
//        Long rank = name.getRank();
//        System.out.println("Rankkkkkkk:>> "+rank);


        //------------------------------------
//        DbConnection.createDbTables();
//        Connection connection = DbConnection.getConnection();
//        Statement statement = connection.createStatement();
//        String query = "select * from Rank;";
//        ResultSet resultSet = statement.executeQuery(query);
//        List<Rank> products=new ArrayList<>();
//        Gson gson = new Gson();
//        while (resultSet.next()){
//            Rank product = new Rank();
//            product.setRank(resultSet.getLong("rank"));
//            product.setId(resultSet.getInt("id"));
//            products.add(product)  ;
//
//        }
//        String s = gson.toJson(products);
//        System.out.println("S::>>>"+s);
//        System.out.println(products);


//        connection.close();

//Connection Exception. Exception. AppContants. Security, pages
//artistname null kevooti chunki pageablemas;
//ByArtistName save qivorish kerakmikan yoki yangidan save qisishmikan.
//        String artistsFromDBAsJSon = GetTopArtistsService.getArtistsFromDBAsJSon();
//        System.out.println(artistsFromDBAsJSon);
    }

    //
}
