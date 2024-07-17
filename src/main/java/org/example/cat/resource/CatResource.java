package org.example.cat.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.cat.model.Cat;
import org.example.cat.model.Food;
import org.example.cat.service.CatService;


@Path("/cats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatResource {

    @Inject
    CatService catService;

    @GET
    public Response getCats() {
        return Response.ok(catService.getCats()).build();
    }

    @POST
    @Path("/{catName}/food")
    public Response addFood(@PathParam("catName") String catName, Food food) {
        catService.addFood(catName, food.getType(), food.getAmount());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{catName}/food")
    public Response removeFood(@PathParam("catName") String catName, Food food) {
        catService.removeFood(catName, food.getType(), food.getAmount());
        return Response.ok().build();
    }
}
