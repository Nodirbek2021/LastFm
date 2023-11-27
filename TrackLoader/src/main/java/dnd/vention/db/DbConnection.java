package dnd.vention.db;


import javax.management.Query;
import java.sql.*;


public class DbConnection {
    public static String data_source_url = "jdbc:postgresql://localhost:5432/TrackLoader";
    public static String data_source_username = "postgres";
    public static String data_source_password = "root123";


    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(
                data_source_url,
                data_source_username,
                data_source_password
        );

        return connection;
    }

    public static void createDbTables() throws SQLException, ClassNotFoundException {
        Statement statement1 = getConnection().createStatement();
//        String dropDB="drop database TrackLoader;";
//        String createDB="create database TrackLoader;";
//        statement1.execute(dropDB);
//        statement1.execute(createDB);
        String sqlPageCreation = "CREATE  TABLE Page(" +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "page INTEGER," +
                "per_page INTEGER," +
                "total INTEGER," +
                "totalPages INTEGER," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlRankCreation = "CREATE  TABLE Rank(" +
                "rank INTEGER,"+
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlArtistObjectCreation = "CREATE TABLE ArtistObject(" +
                "mbid character varying," +
                "listeners INTEGER," +
                "playcount INTEGER," +
                "url character varying," +
                "name character varying," +
                "streamable character varying," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlTrackObjectCreation = "CREATE TABLE TrackObject(" +
                "name character varying," +
                "duration INTEGER," +
                "playcount INTEGER," +
                "listeners INTEGER," +
                "mbid character varying," +
                "url character varying," +
                "rank_id integer," +
                "artist_object_id integer," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlArtistsCreation = "CREATE  TABLE Artists(" +
                "artistObjectList_id integer," +
                "page_id integer," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlTopTracksCreation = "CREATE TABLE TopTracks(" +
                "track_object_id integer," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";

        String sqlTracksCreation = "CREATE  TABLE Tracks(" +
                "track_object_id integer," +
                "page_id integer," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";

        String sqlResponseArtistEntity = "CREATE TABLE ResponseArtist(" +
                "artists_id integer," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlResponseTopTracksEntity = "CREATE TABLE ResponseTopTracks(" +
                "toptracks_id integer," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        String sqlResponseTracksEntity = "CREATE TABLE ResponseTracks(" +
                "tracks_id integer ," +
                "id integer NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "PRIMARY KEY (id)"+
                ");";
        Statement statement = getConnection().createStatement();
        boolean executeSqlPageCreation = statement.execute(sqlPageCreation);
        boolean executeSqlRankCreation = statement.execute(sqlRankCreation);
        boolean executeSqlArtistsCreation = statement.execute(sqlArtistsCreation);
        boolean executeSqlArtistObjectCreation = statement.execute(sqlArtistObjectCreation);
        boolean executeSqlResponseArtistEntity = statement.execute(sqlResponseArtistEntity);
        boolean executeSqlResponseTopTracksEntity = statement.execute(sqlResponseTopTracksEntity);
        boolean executeSqlResponseTracksEntity = statement.execute(sqlResponseTracksEntity);
        boolean executeSqlTacksCreation = statement.execute(sqlTracksCreation);
        boolean executeSqlTopTacksCreation = statement.execute(sqlTopTracksCreation);
        boolean executeSqlTrackObjectCreation = statement.execute(sqlTrackObjectCreation);

    }


}
