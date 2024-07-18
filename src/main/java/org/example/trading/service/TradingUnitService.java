package org.example.trading.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.trading.model.Ccy;
import org.example.trading.model.TradingUnit;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TradingUnitService {
    private Map<String, TradingUnit> tradingUnitsUAT = new HashMap<>();
    private Map<String, TradingUnit> tradingUnitsPROD = new HashMap<>();

    public TradingUnitService() {
        tradingUnitsUAT.put("London", new TradingUnit("London"));
        tradingUnitsUAT.put("Frankfurt", new TradingUnit("Frankfurt"));

        tradingUnitsPROD.put("London", new TradingUnit("London"));
        tradingUnitsPROD.put("Frankfurt", new TradingUnit("Frankfurt"));
    }

    public Map<String, TradingUnit> getTradingUnits(String environment) {
        return environment.equals("uat") ? tradingUnitsUAT : tradingUnitsPROD;
    }

    public TradingUnit getTradingUnit(String environment, String location) {
        return environment.equals("uat") ? tradingUnitsUAT.get(location) : tradingUnitsPROD.get(location);
    }

    public void addCash(String environment, String location, Ccy type, int amount) {
        TradingUnit tradingUnit = getTradingUnit(environment, location);
        if (tradingUnit != null) {
            tradingUnit.addCash(type, amount);
        }
    }

    public void removeCash(String environment, String name, Ccy type, int amount) {
        TradingUnit tradingUnit = getTradingUnit(environment, name);
        if (tradingUnit != null && tradingUnit.getStock().get(type) >= amount) {
            tradingUnit.removeCash(type, amount);
        }
    }
}