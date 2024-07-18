package org.example.trading.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.trading.model.Ccy;
import org.example.trading.model.TradingUnit;
import org.example.trading.model.CashTransferOrder;
import org.example.trading.service.TradingUnitService;
import org.example.trading.service.MoneyTransferService;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Path("/u/money-transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UATMoneyTransferResource {

    String env = "uat";
    Map<String, String> error = new HashMap<>();

    @Inject
    MoneyTransferService moneyTransferService;

    @Inject
    TradingUnitService tradingUnitService;

    @GET
    public Response getOrders() {
        return Response.ok().entity(moneyTransferService.getAllOrders(env)).build();
    }

    @POST
    public Response createFoodTransferOrder(CashTransferOrder inputOrder) {
        TradingUnit senderTradingUnit = tradingUnitService.getTradingUnit(env, inputOrder.getSender());
        TradingUnit recipientTradingUnit = tradingUnitService.getTradingUnit(env, inputOrder.getRecipient());

        if (senderTradingUnit == null || recipientTradingUnit == null) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "One or both locations not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        if (inputOrder.getCcy() == null || !EnumSet.allOf(Ccy.class).contains(inputOrder.getCcy())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unknown currency type: " + inputOrder.getCcy())
                    .build();
        }

        if (senderTradingUnit.getStock().get(inputOrder.getCcy()) < inputOrder.getAmount()) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "Not enough cash in stock for transfer");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        CashTransferOrder order = new CashTransferOrder(
                inputOrder.getSender(),
                inputOrder.getRecipient(),
                inputOrder.getCcy(),
                inputOrder.getAmount()
        );
        order.setStatus("new");
        moneyTransferService.addOrder(env, order);
        tradingUnitService.removeCash(env, order.getSender(), order.getCcy(), order.getAmount());
        return Response.ok(order).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getFoodTransferOrder(@PathParam("orderId") String orderId) {
        CashTransferOrder order = moneyTransferService.getOrder(env, orderId);
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
    public Response completeMoneyTransferOrder(@PathParam("orderId") String orderId) {
        CashTransferOrder order = moneyTransferService.getOrder(env, orderId);
        if (order != null && "in_progress".equals(order.getStatus())) {
            tradingUnitService.addCash(env, order.getRecipient(), order.getCcy(), order.getAmount());
            order.setStatus("completed");
            return Response.ok(order).build();
        }
        error.put("Error", "Order with id " + orderId + " not found or not in progress");
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}