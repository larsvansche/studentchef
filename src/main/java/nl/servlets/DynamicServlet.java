package nl.servlets;

import nl.webservices.RatingResource;
import nl.webservices.RecipeResource;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class DynamicServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Starting application... ");
        RecipeResource recipeResource = new RecipeResource();
        RatingResource ratingResource = new RatingResource();
    }
}
