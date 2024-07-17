package org.example.cat.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.cat.model.Cat;
import org.example.cat.model.Food;
import org.example.cat.service.CatService;

@Path("/u/cats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UATCatResource {

    @Inject
    CatService catService;

    @GET
    public Response getCats() {
        return Response.ok(catService.getCats("uat")).build();
    }

    @POST
    @Path("/{catName}/food")
    public Response addFood(@PathParam("catName") String catName, Food food) {
        Cat cat = catService.getCat("uat", catName);
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
        catService.addFood("uat", catName, food.getType(), food.getAmount());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{catName}/food")
    public Response removeFood(@PathParam("catName") String catName, Food food) {
        Cat cat = catService.getCat("uat", catName);
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
        catService.removeFood("uat", catName, food.getType(), food.getAmount());
        return Response.ok().build();
    }
}