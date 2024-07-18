package org.example.cat.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.cat.model.Cat;
import org.example.cat.model.Food;
import org.example.cat.model.FoodTransferOrder;
import org.example.cat.service.CatService;
import org.example.cat.service.FoodTransferService;

import java.util.HashMap;
import java.util.Map;

@Path("/u/food-transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UATFoodTransferResource {

    String env = "uat";
    Map<String, String> error = new HashMap<>();

    @Inject
    FoodTransferService foodTransferService;

    @Inject
    CatService catService;

    @GET
    public Response getOrders() {
        return Response.ok().entity(foodTransferService.getAllOrders(env)).build();
    }

    @POST
    public Response createFoodTransferOrder(FoodTransferOrder order) {
        Cat senderCat = catService.getCat(env, order.getSender());
        Cat recipientCat = catService.getCat(env, order.getRecipient());

        if (senderCat == null || recipientCat == null) {
            error.put("Error", "One or both cats not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        if (!order.getType().equals("dry") && !order.getType().equals("wet")) {
            error.put("Error", "Unknown food type: " + order.getType());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        if (senderCat.getFoodStock().get(order.getType()) < order.getAmount()) {
            error.put("Error", "Not enough food in stock for transfer");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        order.setStatus("new");
        foodTransferService.addOrder(env, order);
        catService.removeFood(env, order.getSender(), order.getType(), order.getAmount());
        return Response.ok(order).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getFoodTransferOrder(@PathParam("orderId") String orderId) {
        FoodTransferOrder order = foodTransferService.getOrder(env, orderId);
        if (order == null) {
            error.put("Error", "Order with id " + orderId + " not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }
        return Response.ok(order).build();
    }

    @PUT
    @Path("/{orderId}/complete")
    public Response completeFoodTransferOrder(@PathParam("orderId") String orderId) {
        FoodTransferOrder order = foodTransferService.getOrder(env, orderId);
        if (order != null && "in_progress".equals(order.getStatus())) {
            catService.addFood(env, order.getRecipient(), order.getType(), order.getAmount());
            order.setStatus("completed");
            return Response.ok(order).build();
        }
        error.put("Error", "Order with id " + orderId + " not found or not in progress");
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}