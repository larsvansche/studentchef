package webservices;

import model.Recipe;
import model.RecipeService;
import model.ServiceProvider;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class RecipeResource {
    private RecipeService service;

    public RecipeResource () {
        this.service = ServiceProvider.getRecipeService();
    }

    @GET
    @Path("/recipes")
    @Produces("application/json")
    public Response getRecipes() {
        System.out.println("Fetching recipes...");

        List<Recipe> countries = this.service.getAllRecipes();
        return Response.ok(countries).build();
    }

    // GET for single recipe: /recipe/{recipeId}
    @GET
    @Path("/recipe/{id}")
    @Produces("application/json")
    public Response getRecipeById(@PathParam("id") int id) {
        System.out.println("Fetching recipe with code " + id + "...");

        Recipe recipe = this.service.getRecipeById(id);
        return Response.ok(recipe).build();
    }

    // GET for all ratings of a recipe: /ratings/{recipeId}

    // GET for single rating of a recipe: /rating/{ratingId}

    // POST for create beoordeling: /ratings

    // PUT for update beoordeling: /rating/{ratingId}

    // DELETE for delete beoordeling /rating/{ratingId}
}
