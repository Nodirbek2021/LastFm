package dnd.vention.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dnd.vention.db.DbConnection;
import dnd.vention.payload.responses.ResponseTopTracksEntity;
import dnd.vention.service.GetTopArtistsService;
import dnd.vention.service.GetTopTracksService;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;


@WebServlet("/api/v1/getTopTracks")
public class GetTopTracksController extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        String page = req.getParameter("page");
        String artistsFromDb = GetTopTracksService.respondJson(page);
        writer.write(artistsFromDb);
    }


}


