package org.example.cat.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.cat.model.FoodTransferOrder;
import org.example.cat.service.CatService;
import org.example.cat.service.FoodTransferService;


@Path("/food-transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodTransferResource {

    @Inject
    FoodTransferService foodTransferService;

    @Inject
    CatService catService;

    @POST
    public Response createFoodTransferOrder(FoodTransferOrder order) {
        catService.removeFood(order.getSender(), order.getType(), order.getAmount());
        foodTransferService.addOrder(order);
        order.setStatus("in_progress");
        return Response.ok(order).build();
    }

    @PUT
    @Path("/{orderId}/complete")
    public Response completeFoodTransferOrder(@PathParam("orderId") int orderId) {
        FoodTransferOrder order = foodTransferService.getOrder(orderId);
        if (order != null && "in_progress".equals(order.getStatus())) {
            catService.addFood(order.getRecipient(), order.getType(), order.getAmount());
            order.setStatus("completed");
            return Response.ok(order).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}