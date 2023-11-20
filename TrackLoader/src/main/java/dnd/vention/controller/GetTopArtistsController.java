package dnd.vention.controller;


import com.google.gson.Gson;
import dnd.vention.payload.baseObjects.ArtistObject;
import dnd.vention.service.GetTopArtistsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@WebServlet("/api/v1/getTopArtists")
public class GetTopArtistsController extends HttpServlet {

    private static final String SECRET_KEY = "yourSecretKey"; // Replace with a strong secret key


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
//
        String page = req.getParameter("page");
        String artistsFromDBAsJSon = GetTopArtistsService.getArtistsFromDBAsJSon(page);
        System.out.println(artistsFromDBAsJSon);
        String string = artistsFromDBAsJSon.toString();

        writer.write(string);

    }






}
