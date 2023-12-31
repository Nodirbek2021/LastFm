package dnd.vention.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dnd.vention.db.DbConnection;
import dnd.vention.payload.attributes.Rank;
import dnd.vention.payload.baseObjects.ArtistObject;
import dnd.vention.payload.baseObjects.TrackObject;
import dnd.vention.payload.collectionObjects.TopTracks;
import dnd.vention.payload.collectionObjects.Tracks;
import dnd.vention.payload.responses.ResponseTopTracksEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class GetTopTracksService {
//    private static int page = 1;
//    private static int perPage = 100;
//
//    private static int totalpages = 0;
//    private static int total = 0;


    public static String urlStringJson(String page) throws IOException, SQLException, ClassNotFoundException {
        ///Bunda Rank yo'q
//        URL url = new URL("http://ws.audioscrobbler.com/2.0//?method=chart.gettoptracks&api_key=25264164c456b374e86800acc6656239&format=json");
        int pageInt = Integer.parseInt(page);
        URL url = new URL("http://ws.audioscrobbler.com/2.0//?method=chart.gettoptracks&api_key=25264164c456b374e86800acc6656239&format=json&page=" + pageInt);
        URLConnection connection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String inputline;
        while ((inputline = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputline);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }


    public static List<TrackObject> wrapObjectsToObjectMapper(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ResponseTopTracksEntity responseTopTracksEntity = objectMapper.readValue(jsonString.toString(), ResponseTopTracksEntity.class);
        TopTracks toptracks = responseTopTracksEntity.getToptracks();
        List<TrackObject> trackObjectList = toptracks.getTrackObjectList();
        return trackObjectList;
    }

    public static void saveTracksToDb(List<TrackObject> trackObjectList) throws SQLException, ClassNotFoundException {

        //
        Connection conDeleteAll = DbConnection.getConnection();
        String sqlDeleteAll = "delete from TrackObject where 1=1;";
        Statement statement1 = conDeleteAll.createStatement();
        statement1.execute(sqlDeleteAll);


        //
        Connection dbConnection = DbConnection.getConnection();
        Statement statement = dbConnection.createStatement();


        String saveTrackObjectQuery;
        //
        for (int i = 0; i < trackObjectList.size(); i++) {
//            System.out.println("aaaaaaaaaaaaaaaaaa: " + i);
            TrackObject trackObject = trackObjectList.get(i);

            String name = trackObject.getName();
            Integer duration = trackObject.getDuration();
            Integer listeners = trackObject.getListeners();
            String mbid = trackObject.getMbid();
            Integer playcount = trackObject.getPlaycount();
            ArtistObject artist = trackObject.getArtist();
            String artistName = artist.getName();
//            Rank rank = trackObject.getRank();
            String url = trackObject.getUrl();
            Integer artistObjectId = null;


            String sqlGetArtistObjectId = "SELECT * FROM ArtistObject WHERE name ='" + artist.getName() + "';";

            ResultSet resultGetArtistId = statement.executeQuery(sqlGetArtistObjectId);

            if (!resultGetArtistId.isBeforeFirst()){
                saveArtistObjectMethod(artist);
            }

            ResultSet resultGetArtistId2 = statement.executeQuery(sqlGetArtistObjectId);
            while (resultGetArtistId2.next()) {
                artistObjectId = resultGetArtistId2.getInt("id");
            }


            String sqlSaveObjectDb = ("INSERT INTO TrackObject(" +
                    "name,duration,listeners,mbid,playcount,artist_object_id,url" +
                    ")" +
                    "values(" +
                    "?,?,?,?,?,?,?"
                    +
                    ");");
            if (artistObjectId == null) artistObjectId = -1;
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlSaveObjectDb);
            name = name.replace("'", "\\");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, duration);
            preparedStatement.setInt(3, listeners);
            preparedStatement.setString(4, mbid);
            preparedStatement.setInt(5, playcount);
            preparedStatement.setInt(6, artistObjectId);
            preparedStatement.setString(7, url);
            preparedStatement.execute();
        }
        dbConnection.close();
    }

    private static void saveArtistObjectMethod(ArtistObject artistObject) throws SQLException, ClassNotFoundException {
        Connection dbConnection=DbConnection.getConnection();
        String mbid = artistObject.getMbid();
        String artistName = artistObject.getName();
        String artistUrl = artistObject.getUrl();
        String saveArtistObjectQuery = "INSERT INTO ArtistObject (" +
                "url,name,mbid" +
                ") " +
                "VALUES ("+"?,?,?"+
                ");";
//            statement.execute(saveArtistObjectQuery);

        PreparedStatement preparedStatement = dbConnection.prepareStatement(saveArtistObjectQuery);
        preparedStatement.setString(1,artistUrl);
        artistName=artistName.replace("'","\\");
        preparedStatement.setString(2,artistName);
        preparedStatement.setString(3,mbid);

        preparedStatement.execute();
        dbConnection.close();
    }

    public static String respondJson(String page) throws SQLException, IOException, ClassNotFoundException {

        String urlStringJson = urlStringJson(page);
        List<TrackObject> trackObjectListFromUrl = wrapObjectsToObjectMapper(urlStringJson);
        saveTracksToDb(trackObjectListFromUrl);


        Connection connection = DbConnection.getConnection();
        Connection connection2 = DbConnection.getConnection();

        String sqlGetAllTopTrack = "select * from TrackObject;";
        Statement statement = connection.createStatement();
        Statement statement2 = connection2.createStatement();
        List<TrackObject> trackObjectList = new ArrayList<>();
        ResultSet resultSetTopTracks = statement.executeQuery(sqlGetAllTopTrack);
        while (resultSetTopTracks.next()) {
            TrackObject trackObject = new TrackObject();


            trackObject.setId(resultSetTopTracks.getInt("id"));
            trackObject.setName(resultSetTopTracks.getString("name"));
            trackObject.setListeners(resultSetTopTracks.getInt("listeners"));
            trackObject.setPlaycount(resultSetTopTracks.getInt("playcount"));
            trackObject.setUrl(resultSetTopTracks.getString("url"));
            trackObject.setMbid(resultSetTopTracks.getString("mbid"));
            trackObject.setDuration(resultSetTopTracks.getInt("duration"));
            int artistId = resultSetTopTracks.getInt("artist_object_id");
            String sqlGetArtist = "select * from ArtistObject where id=" + artistId + ";";
            ResultSet resultSetGetArtist = statement2.executeQuery(sqlGetArtist);
            ArtistObject artistObject = new ArtistObject();
            while (resultSetGetArtist.next()) {
                artistObject.setId(resultSetGetArtist.getInt("id"));
                artistObject.setName(resultSetGetArtist.getString("name"));
                artistObject.setUrl(resultSetGetArtist.getString("url"));
                artistObject.setPlaycount(resultSetGetArtist.getInt("playcount"));
                artistObject.setStreamable(resultSetGetArtist.getString("streamable"));
                artistObject.setListeners(resultSetGetArtist.getInt("listeners"));
                artistObject.setMbid(resultSetGetArtist.getString("mbid"));
            }
            int rankId = resultSetTopTracks.getInt("rank_id");
            String sqlGetRank = "SELECT * from Rank where id=" + rankId + ";";
            ResultSet resultSetRank = statement2.executeQuery(sqlGetRank);
            Rank rank = new Rank();
            while (resultSetRank.next()) {
                rank.setId(resultSetRank.getInt("id"));
                rank.setRank(resultSetRank.getInt("rank"));
            }
            if (artistObject != null)
                trackObject.setArtist(artistObject);
            if (rank != null)
                trackObject.setRank(rank);
            trackObjectList.add(trackObject);
        }
        connection.close();
        connection2.close();


        Gson gson = new Gson();
        String response = gson.toJson(trackObjectList);

        return response;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        System.out.println(respondJson("5"));
    }

}