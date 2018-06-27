package webservices;

import model.ServiceProvider;
import model.Recipe;
import model.RecipeService;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/recipes")
public class RecipeResource {
    private RecipeService service;

    public RecipeResource() {
        this.service = ServiceProvider.getRecipeService();
    }

    @GET
    @Produces("application/json")
    public Response getRecipes() {
        System.out.println("Fetching recipes...");

        List<Recipe> countries = this.service.getAllRecipes();
        return Response.ok(countries).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getRecipeById(@PathParam("id") int id) {
        System.out.println("Fetching recipe with code " + id + "...");

        Recipe recipe = this.service.getRecipeById(id);
        return Response.ok(recipe).build();
    }
}
