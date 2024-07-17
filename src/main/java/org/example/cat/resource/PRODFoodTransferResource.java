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

@Path("/p/food-transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PRODFoodTransferResource {

    @Inject
    FoodTransferService foodTransferService;

    @Inject
    CatService catService;

    @POST
    public Response createFoodTransferOrder(FoodTransferOrder order) {
        Cat senderCat = catService.getCat("prod", order.getSender());
        Cat recipientCat = catService.getCat("prod", order.getRecipient());

        if (senderCat == null || recipientCat == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("One or both cats not found.")
                    .build();
        }

        if (!order.getType().equals("dry") && !order.getType().equals("wet")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unknown food type: " + order.getType())
                    .build();
        }

        if (senderCat.getFoodStock().get(order.getType()) < order.getAmount()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Not enough food in stock for transfer.")
                    .build();
        }

        order.setStatus("in_progress");
        foodTransferService.addOrder("prod", order);
        catService.removeFood("prod", order.getSender(), order.getType(), order.getAmount());
        return Response.ok(order).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getFoodTransferOrder(@PathParam("orderId") String orderId) {
        FoodTransferOrder order = foodTransferService.getOrder("prod", orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order with id " + orderId + " not found.")
                    .build();
        }
        return Response.ok(order).build();
    }

    @PUT
    @Path("/{orderId}/complete")
    public Response completeFoodTransferOrder(@PathParam("orderId") String orderId) {
        FoodTransferOrder order = foodTransferService.getOrder("prod", orderId);
        if (order != null && "in_progress".equals(order.getStatus())) {
            catService.addFood("prod", order.getRecipient(), order.getType(), order.getAmount());
            order.setStatus("completed");
            return Response.ok(order).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Order with id " + orderId + " not found or not in progress.")
                .build();
    }
}