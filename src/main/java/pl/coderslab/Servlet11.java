package pl.coderslab;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Servlet11", urlPatterns = "/servlet11")
public class Servlet11 extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Hello first servlet");
        response.getWriter().append("Content11");

        String[] tablicaNazwisk = {"Kowalski", "Nowak", "Pietrzykowski"};
        List<String> listaNazwisk = new ArrayList<>();

        for (int i = 0; i < tablicaNazwisk.length; i++) {
            listaNazwisk.add(tablicaNazwisk[i]);
        }
        response.getWriter().println();
        response.getWriter().println(listaNazwisk);

    }
}