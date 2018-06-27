package webservices;

import model.Rating;
import model.RatingService;
import model.ServiceProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;

@Path("/ratings")
public class RatingResource {
    private RatingService service;

    public RatingResource() {
        this.service = ServiceProvider.getRatingService();
    }

    @GET
    @Path("/{recipeId}/{userId}")
    @Produces("application/json")
    public Response getRatingByRecipeId(@PathParam("recipeId") int recipeId, @PathParam("userId") int userId) {
        Rating rating = this.service.getRatingByRecipeIdAndUserId(userId, recipeId);
        return Response.ok(rating).build();
    }

    @GET
    @Path("/{recipeId}")
    @Produces("application/json")
    public Response getRatingByRecipeId(@PathParam("recipeId") int recipeId) {
        ArrayList<Rating> ratings = this.service.getRatingByRecipeId(recipeId);
        return Response.ok(ratings).build();
    }

    @POST
    @Produces("application/json")
    public Response createRating(@FormParam("userId") int userId, @FormParam("recipeId") int recipeId, @FormParam("value") int value, @FormParam("description") String description) {
        Rating rating = new Rating(recipeId, userId, value, new Date(), description);
        System.out.println(rating);
        if (this.service.createRating(rating) != null) {
            System.out.println("Rating succesfully saved!");

            Rating newRating = service.getRatingByRecipeIdAndUserId(userId, recipeId);

            return Response.ok(newRating).build();
        } else {
            System.out.println("Rating not saved!");
            return Response.status(500).build();
        }
    }

    @PUT
    @Produces("application/json")
    public Response updateRating(@FormParam("userId") int userId, @FormParam("recipeId") int recipeId, @FormParam("value") int value, @FormParam("description") String description) {
        Rating rating = new Rating(recipeId, userId, value, new Date(), description);
        if (this.service.updateRating(rating) == rating) {
            System.out.println("Rating succesfully updated!");
            return Response.ok(rating).build();
        } else {
            System.out.println("Rating not updated!");
            return Response.status(500).build();
        }
    }

    // DELETE for delete beoordeling /rating/{ratingId}
    @DELETE
    @Path("/{recipeId}/{userId}")
    @Produces("application/json")
    public Response deleteRating(@PathParam("userId") int userId, @PathParam("recipeId") int recipeId) {
        if (this.service.deleteRating(userId, recipeId) == 1) {
            System.out.println("Rating succesfully deleted!");
            return Response.ok().build();
        } else {
            System.out.println("Rating not deleted!");
            return Response.status(500).build();
        }
    }
}
