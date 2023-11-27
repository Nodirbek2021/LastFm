package dnd.vention.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dnd.vention.db.DbConnection;
import dnd.vention.payload.baseObjects.ArtistObject;
import dnd.vention.payload.collectionObjects.Artists;
import dnd.vention.payload.responses.ResponseArtistEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetTopArtistsService {
//
//    private static int page = 1;
//    private static int perPage = 100;
//
//    private static int totalpages = 10;
//    private static int total = 0;


    public static String urlStringJson(String page) throws IOException, SQLException, ClassNotFoundException {
        int pageInt = Integer.parseInt(page);

//        URL url = new URL("http://ws.audioscrobbler.com/2.0///?method=chart.gettopartists&api_key=25264164c456b374e86800acc6656239&format=json");
        URL url = new URL("http://ws.audioscrobbler.com/2.0//?method=chart.gettopartists&api_key=25264164c456b374e86800acc6656239&format=json&page=" + pageInt);

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


    public static List<ArtistObject> wrapJsonToObjectMapper(String stringBuilder) throws JsonProcessingException {
        //Getting Gson from audioscrobbler Api
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ResponseArtistEntity responseArtistEntity = objectMapper.readValue(stringBuilder.toString(), ResponseArtistEntity.class);
        Artists artists = responseArtistEntity.getArtists();
        List<ArtistObject> artistObjectList = artists.getArtistObjectList();
        return artistObjectList;
    }


    public static void saveArtistsToDB(List<ArtistObject> artistObjectList) throws SQLException, ClassNotFoundException {
        //
        Connection conDeleteAll= DbConnection.getConnection();
        String sqlDeleteAll="delete from ArtistObject where 1=1;";
        Statement statement1 = conDeleteAll.createStatement();
        statement1.execute(sqlDeleteAll);
        //
        Connection dbConnection = DbConnection.getConnection();
        Statement statement = dbConnection.createStatement();
        String saveArtistObjectQuery;
        //
        for (int i = 0; i < artistObjectList.size(); i++) {
            ArtistObject artistObject = artistObjectList.get(i);
            Integer listeners = artistObject.getListeners();
            String mbid = artistObject.getMbid();
            Integer playcount = artistObject.getPlaycount();
            String artistName = artistObject.getName();
            String artistUrl = artistObject.getUrl();
            String artistStreamable = artistObject.getStreamable();
            saveArtistObjectQuery = "INSERT INTO ArtistObject (" +
                    "url,name,playcount,streamable,listeners,mbid" +
                    ") " +
                    "VALUES ("+"?,?,?,?,?,?"+
                    ");";
//            statement.execute(saveArtistObjectQuery);

            PreparedStatement preparedStatement = dbConnection.prepareStatement(saveArtistObjectQuery);
            preparedStatement.setString(1,artistUrl);
            artistName=artistName.replace("'","\\");
            preparedStatement.setString(2,artistName);
            preparedStatement.setInt(3,playcount);
            preparedStatement.setString(4,artistStreamable);
            preparedStatement.setInt(5,listeners);
            preparedStatement.setString(6,mbid);

            preparedStatement.execute();
            //mbid,liste,pla,url,name,streama,id

        }
        dbConnection.close();
    }

    public static String getArtistsFromDBAsJSon(String page) throws SQLException, ClassNotFoundException, IOException {

//        while (page <3) {
//            if (page < 3) {

                String urlStringJson = urlStringJson(page);
//                System.out.println("String js: "+page+": "+ urlStringJson);
                List<ArtistObject> artistObjects = wrapJsonToObjectMapper(urlStringJson);
//                System.out.println("list js: "+ artistObjects.toString());
                saveArtistsToDB(artistObjects);
//            }
//            page = page + 1;
//        }


        Connection dbConnection = DbConnection.getConnection();
        Statement statement = dbConnection.createStatement();
        String sqlGetAllArtists = "select * from ArtistObject;";
        ResultSet resultSet = statement.executeQuery(sqlGetAllArtists);
        List<ArtistObject> artistObjectsFromDB = new ArrayList<>();
        while (resultSet.next()) {
            ArtistObject artistObject = new ArtistObject();
            artistObject.setId(resultSet.getInt("id"));
            artistObject.setListeners(resultSet.getInt("listeners"));
            artistObject.setPlaycount(resultSet.getInt("playcount"));
            artistObject.setName(resultSet.getString("name"));
            artistObject.setMbid(resultSet.getString("mbid"));
            artistObject.setStreamable(resultSet.getString("streamable"));
            artistObject.setUrl(resultSet.getString("url"));
            artistObjectsFromDB.add(artistObject);
        }
//        System.out.println(artistObjectList.toString());
//        System.out.println(artistObjectsFromDB.toString());
        Gson gson = new Gson();
        String response = gson.toJson(artistObjectsFromDB);
        return response;


    }


    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
//        DbConnection.createDbTables();
//        System.out.println(getArtistsFromDBAsJSon());
//        String artistsFromDBAsJSon = getArtistsFromDBAsJSon();
//        System.out.println(artistsFromDBAsJSon);




    }


}
