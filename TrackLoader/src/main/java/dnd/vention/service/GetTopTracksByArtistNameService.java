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
import dnd.vention.payload.responses.ResponseTrackByArtistEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetTopTracksByArtistNameService {


    private static String artistNameForRespond = "";


    public static String urlStringJson(String artistName,String page) throws IOException, SQLException, ClassNotFoundException {
        ///Bunda Rank bor
        int pageInt = Integer.parseInt(page);

        artistNameForRespond = artistName;
        URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=" + artistName + "&api_key=25264164c456b374e86800acc6656239&format=json&page=" + pageInt);

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

        ResponseTrackByArtistEntity responseTrackByArtistEntity = objectMapper.readValue(jsonString.toString(), ResponseTrackByArtistEntity.class);
        Tracks tracks = responseTrackByArtistEntity.getTracks();
        List<TrackObject> trackObjectList = tracks.getTrackObjectList();
        return trackObjectList;
    }


    public static String respondJson(String artistName,String page) throws SQLException, IOException, ClassNotFoundException {

                String urlStringJson = urlStringJson(artistName,page);
                List<TrackObject> trackObjects = wrapObjectsToObjectMapper(urlStringJson);
//                saveTracksToDb(trackObjects);

/*
//
//        Connection connection = DbConnection.getConnection();
//        Connection connection2 = DbConnection.getConnection();
//        Statement statement = connection.createStatement();
//        Statement statement2 = connection2.createStatement();
//
//        int artistObjectId = -5;
//        String sqlGetArtistObjectId = "select * from ArtistObject where name='" + artistNameForRespond + "';";
//        ResultSet resultGetArtistId = statement.executeQuery(sqlGetArtistObjectId);
//        while (resultGetArtistId.next()) {
//            artistObjectId = resultGetArtistId.getInt("id");
//        }
//
//
//        String getAllTrackObjectsSql = "SELECT * from  TrackObject where artist_object_id=" + artistObjectId + ";";
//        ResultSet resultSetTackObject = statement.executeQuery(getAllTrackObjectsSql);
//        List<TrackObject> trackObjectList = new ArrayList<>();
//
//
//        while (resultSetTackObject.next()) {
//            TrackObject trackObject = new TrackObject();
//            //rank get
//
//
//            //respond
//            trackObject.setId(resultSetTackObject.getInt("id"));
//            trackObject.setName(resultSetTackObject.getString("name"));
//            trackObject.setListeners(resultSetTackObject.getInt("listeners"));
//            trackObject.setPlaycount(resultSetTackObject.getInt("playcount"));
//            trackObject.setUrl(resultSetTackObject.getString("url"));
//            trackObject.setMbid(resultSetTackObject.getString("mbid"));
//            trackObject.setDuration(resultSetTackObject.getInt("duration"));
//
//
//            Integer rankId = resultSetTackObject.getInt("rank_id");
//            Rank rank = new Rank();
//
//            if (rankId == 0 || rankId == null) {
//                rankId = -5;
//                rank.setId(-15);
//            } else {
//
//                String sqlGetRank = "SELECT * from Rank where id=" + rankId + ";";
//                ResultSet resultSetRank = statement2.executeQuery(sqlGetRank);
//                while (resultSetRank.next()) {
//                    rank.setId(resultSetRank.getInt("id"));
//                    rank.setRank(resultSetRank.getInt("rank"));
//                }
//            }
//
//            //Artist get
//            Integer artistId = resultSetTackObject.getInt("artist_object_id");
//            ArtistObject artistObject = new ArtistObject();
//            if (artistId == null || artistId == 0) {
//                artistId = -5;
//                artistObject.setId(-15);
//            } else {
//
//
//                String sqlGetArtist = "select * from ArtistObject where id=" + artistId + ";";
//                ResultSet resultSetGetArtist = statement2.executeQuery(sqlGetArtist);
//                while (resultSetGetArtist.next()) {
//                    artistObject.setId(resultSetGetArtist.getInt("id"));
//                    artistObject.setName(resultSetGetArtist.getString("name"));
//                    artistObject.setUrl(resultSetGetArtist.getString("url"));
//                    artistObject.setPlaycount(resultSetGetArtist.getInt("playcount"));
//                    artistObject.setStreamable(resultSetGetArtist.getString("streamable"));
//                    artistObject.setListeners(resultSetGetArtist.getInt("listeners"));
//                    artistObject.setMbid(resultSetGetArtist.getString("mbid"));
//                }
//            }
//
//            trackObject.setArtist(artistObject);
//            trackObject.setRank(rank);
//            trackObjectList.add(trackObject);
//        }
//        connection.close();

 */

        Gson gson = new Gson();
        String response = gson.toJson(trackObjects);

        return response;
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
    }


    public static void saveTracksToDb(List<TrackObject> trackObjectList) throws SQLException, ClassNotFoundException {
        Connection dbConnection = DbConnection.getConnection();
        Statement statement = dbConnection.createStatement();
        Connection conDeleteAll = DbConnection.getConnection();
        String sqlDeleteAll = "delete from TrackObject where 1=1;";
        Statement statement1 = conDeleteAll.createStatement();
        statement1.execute(sqlDeleteAll);
        saveRankToDb(trackObjectList);

        for (int i = 0; i < trackObjectList.size(); i++) {
            TrackObject trackObject = trackObjectList.get(i);
            String name = trackObject.getName();
            Integer duration = trackObject.getDuration();
            Integer listeners = trackObject.getListeners();
            String mbid = trackObject.getMbid();
            Integer playcount = trackObject.getPlaycount();
            ArtistObject artist = trackObject.getArtist();
            String artistName = artist.getName();
            Rank rank = trackObject.getRank();
            String url = trackObject.getUrl();
            name = name.replace("'", "\\");
            Integer artistObjectId = null;
            Integer rankId = null;
            //get ArtistObjectId
            artistObjectId = getArtistObjectIdMethod(artist);
            System.out.println(artistObjectId);
            //getRankId
            rankId = rankId(rank);
            System.out.println(rankId);


            //getTrack
            String sqlGetTrackFromDb = "select * from TrackObject where name='" + name + "';";
            ResultSet resultSet = statement.executeQuery(sqlGetTrackFromDb);

            boolean next = resultSet.next();
            if (next != false) {
                int id = resultSet.getInt("id");
                String sqlDeleteTrackFromDb = "delete from TrackObject where id=" + id + ";";
                statement.execute(sqlDeleteTrackFromDb);
            }
            String sqlSaveObjectDb = ("INSERT INTO TrackObject(" +
                    "name,duration,listeners,mbid,playcount,artist_object_id,url,rank_id" +
                    ")" +
                    "values(" +
                    "?,?,?,?,?,?,?,?"
                    +
                    ");");
            if (artistObjectId == null) artistObjectId = -1;
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlSaveObjectDb);
            name = name.replace("'", "\\");
            preparedStatement.setString(1, name);
            duration = -1;
            preparedStatement.setInt(2, duration);
            preparedStatement.setInt(3, listeners);
            preparedStatement.setString(4, mbid);
            preparedStatement.setInt(5, playcount);
            preparedStatement.setInt(6, artistObjectId);
            preparedStatement.setString(7, url);
            preparedStatement.setInt(8, rankId);
            preparedStatement.execute();
        }
        dbConnection.close();
    }

    public static Integer getArtistObjectIdMethod(ArtistObject artist) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        Statement statement = connection.createStatement();
        Integer artistObjectId = null;


        String sqlGetArtistObjectId = "SELECT * FROM ArtistObject WHERE name ='" + artist.getName() + "';";
        ResultSet resultGetArtistId = statement.executeQuery(sqlGetArtistObjectId);
//        while (resultGetArtistId.next()) {
        if (!resultGetArtistId.next()) {
            Integer artistObjectListeners = artist.getListeners();
            String artistObjectMbid = artist.getMbid();
            Integer artistObjectPlaycount = artist.getPlaycount();
            String artistObjectName = artist.getName();
            String artistUrl = artist.getUrl();
            String artistStreamable = artist.getStreamable();
            String saveArtistObjectQuery = "INSERT INTO ArtistObject (" +
                    "url,name,playcount,streamable,listeners,mbid" +
                    ") " +
                    "VALUES (" + "?,?,?,?,?,?" +
                    ");";
//            statement.execute(saveArtistObjectQuery);

            PreparedStatement preparedStatement = connection.prepareStatement(saveArtistObjectQuery);
            preparedStatement.setString(1, artistUrl);
            artistObjectName = artistObjectName.replace("'", "\\");
            preparedStatement.setString(2, artistObjectName);
            preparedStatement.setInt(3, artistObjectPlaycount);
            preparedStatement.setString(4, artistStreamable);
            preparedStatement.setInt(5, artistObjectListeners);
            preparedStatement.setString(6, artistObjectMbid);
            preparedStatement.execute();
        }
//        }
        ResultSet resultGetArtistId2 = statement.executeQuery(sqlGetArtistObjectId);
        while (resultGetArtistId2.next()) {
            artistObjectId = resultGetArtistId2.getInt("id");
        }
        connection.close();
        return artistObjectId;
    }

    private static Integer rankId(Rank rank) throws SQLException, ClassNotFoundException {

        Connection connection = DbConnection.getConnection();
        Statement statement = connection.createStatement();
        String sqlGetRankId = "select * from Rank where rank=" + rank.getRank() + ";";
        ResultSet resultSet = statement.executeQuery(sqlGetRankId);
        Integer rankId = null;
        while (resultSet.next()) {
            rankId = resultSet.getInt("id");
        }
        connection.close();
        return rankId;
    }

    public static void saveRankToDb(List<TrackObject> trackObjectList) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        for (int i = 0; i < trackObjectList.size(); i++) {
            TrackObject trackObject = trackObjectList.get(i);
            Rank rank = trackObject.getRank();
            Integer rankLong = rank.getRank();
            String sqlSaveRank = "INSERT INTO Rank(" +
                    "rank" +
                    ")" +
                    "values(" +
                    rankLong +
                    ")";
            Statement statement = connection.createStatement();
            statement.execute(sqlSaveRank);
        }
        connection.close();
    }


}
