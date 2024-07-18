package org.example.trading.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.trading.model.Ccy;
import org.example.trading.model.TradingUnit;
import org.example.trading.model.Cash;
import org.example.trading.service.TradingUnitService;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Path("/p/tradingLocations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PRODTradingResource {

    private String env = "prod";
    Map<String, String> error = new HashMap<>();

    @Inject
    TradingUnitService tradingUnitService;

    @GET
    public Response getCats() {
        Map<String, TradingUnit> cats = tradingUnitService.getTradingUnits(env);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String catsJson = mapper.writeValueAsString(cats);
            return Response.ok(catsJson).build();
        } catch (JsonProcessingException e) {
            return Response.serverError().entity("Error converting entities to JSON").build();
        }
    }

    @POST
    @Path("/{tradingUnitLocation}/cash")
    public Response addCash(@PathParam("tradingUnitLocation") String tradingUnitLocation, Cash cash) {
        TradingUnit tradingUnit = tradingUnitService.getTradingUnit(env, tradingUnitLocation);
        if (tradingUnit == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cat with name " + tradingUnitLocation + " not found.")
                    .build();
        }
        if (cash.getCcy() == null || !EnumSet.allOf(Ccy.class).contains(cash.getCcy())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unknown currency type: " + cash.getCcy())
                    .build();
        }
        tradingUnitService.addCash(env, tradingUnitLocation, cash.getCcy(), cash.getAmount());
        return Response.ok().entity(tradingUnitService.getTradingUnits(env)).build();
    }

    @DELETE
    @Path("/{tradingUnitLocation}/cash")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCash(@PathParam("tradingUnitLocation") String tradingUnitLocation, Cash cash) {
        TradingUnit tradingUnit = tradingUnitService.getTradingUnit(env, tradingUnitLocation);
        if (tradingUnit == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Location with name " + tradingUnitLocation + " is not found.")
                    .build();
        }

        Ccy currency = cash.getCcy();
        if (currency == null || !EnumSet.allOf(Ccy.class).contains(currency)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unknown currency type: " + cash.getCcy())
                    .build();
        }
        if (tradingUnit.getStock().get(currency) < cash.getAmount()) {
            error.put("Error", "Can't remove " + cash.getAmount() + " " + cash.getCcy() + " from " + tradingUnitLocation);
            error.put("Details", tradingUnitLocation + " has " + tradingUnit.getStock().get(currency) + " " + cash.getCcy() + " in stock");

            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(error).build();
        }

        tradingUnitService.removeCash(env, tradingUnitLocation, currency, cash.getAmount());
        return Response.ok().entity(tradingUnitService.getTradingUnits(env)).build();
    }
}