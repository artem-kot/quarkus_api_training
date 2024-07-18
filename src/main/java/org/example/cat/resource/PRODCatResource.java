package org.example.cat.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.cat.model.Cat;
import org.example.cat.model.Food;
import org.example.cat.service.CatService;

import java.util.HashMap;
import java.util.Map;

@Path("/p/cats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PRODCatResource {

    private String env = "prod";
    Map<String, String> error = new HashMap<>();



    @Inject
    CatService catService;

    @GET
    public Response getCats() {
        Map<String, Cat> cats = catService.getCats(env);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String catsJson = mapper.writeValueAsString(cats);
            return Response.ok(catsJson).build();
        } catch (JsonProcessingException e) {
            return Response.serverError().entity("Error converting cats to JSON").build();
        }
    }

    @POST
    @Path("/{catName}/food")
    public Response addFood(@PathParam("catName") String catName, Food food) {
        Cat cat = catService.getCat(env, catName);
        if (cat == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cat with name " + catName + " not found.")
                    .build();
        }
        if (!food.getType().equals("dry") && !food.getType().equals("wet")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unknown food type: " + food.getType())
                    .build();
        }
        catService.addFood(env, catName, food.getType(), food.getAmount());
        return Response.ok().entity(catService.getCats(env)).build();
    }

    @DELETE
    @Path("/{catName}/food")
    public Response removeFood(@PathParam("catName") String catName, Food food) {
        Cat cat = catService.getCat(env, catName);
        if (cat == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cat with name " + catName + " not found.")
                    .build();
        }
        if (!food.getType().equals("dry") && !food.getType().equals("wet")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unknown food type: " + food.getType())
                    .build();
        }
        if (("wet".equals(food.getType()) && cat.getFoodStock().get("wet") < food.getAmount())
                || "dry".equals(food.getType()) && cat.getFoodStock().get("dry") < food.getAmount()) {
            error.put("Error", "Can't remove " + food.getAmount() + " of " + food.getType() + " food from " + catName);
            error.put("Details", catName + " has " + cat.getFoodStock().get(food.getType()) + " of " + food.getType() + " food");

            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(error).build();
        }
        catService.removeFood(env, catName, food.getType(), food.getAmount());
        return Response.ok().entity(catService.getCats(env)).build();
    }
}