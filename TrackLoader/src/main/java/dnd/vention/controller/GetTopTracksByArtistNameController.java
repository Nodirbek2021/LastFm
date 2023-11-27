package dnd.vention.controller;

import com.google.gson.Gson;
import dnd.vention.service.GetTopTracksByArtistNameService;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/v1/getTopTracksByArtistName")
public class GetTopTracksByArtistNameController extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String artistName = req.getParameter("artistName");
        String page = req.getParameter("page");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        String s = GetTopTracksByArtistNameService.respondJson(artistName,page);
        writer.write(s);
    }
}
