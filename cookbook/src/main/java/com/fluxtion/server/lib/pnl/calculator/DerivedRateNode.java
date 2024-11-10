/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.server.lib.pnl.calculator;

import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.server.lib.pnl.events.MidPrice;
import com.fluxtion.server.lib.pnl.events.MtmInstrument;
import com.fluxtion.server.lib.pnl.refdata.Instrument;
import com.fluxtion.server.lib.pnl.refdata.RefData;
import lombok.Data;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.Map;

@Data
public class DerivedRateNode {

    private Instrument mtmInstrument = RefData.USD;
    private Map<Instrument, Double> directMtmRatesByInstrument = new HashMap<>();
    private Map<Instrument, Double> derivedMtmRatesByInstrument = new HashMap<>();
    private final DefaultDirectedWeightedGraph<Instrument, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private BellmanFordShortestPath<Instrument, DefaultWeightedEdge> shortestPath;

    @OnEventHandler
    public boolean updateMtmInstrument(MtmInstrument mtmInstrumentUpdate) {
        boolean change = mtmInstrument != mtmInstrumentUpdate.instrument();
        if (change) {
            mtmInstrument = mtmInstrumentUpdate.instrument();
            directMtmRatesByInstrument.clear();
            derivedMtmRatesByInstrument.clear();
        }
        return change;
    }

    @OnEventHandler
    public boolean midRate(MidPrice midPrice) {
        Instrument dealtInstrument = midPrice.dealtInstrument();
        Instrument contraInstrument = midPrice.contraInstrument();

        //no self cycles allowed
        if (dealtInstrument == contraInstrument | dealtInstrument == null | contraInstrument == null) {
            return false;
        }
        derivedMtmRatesByInstrument.clear();
        //add to directMtmRatesByInstrument
        if (midPrice.getOppositeInstrument(mtmInstrument) != null) {
            directMtmRatesByInstrument.put(midPrice.getOppositeInstrument(mtmInstrument), midPrice.getRateForInstrument(mtmInstrument));
        }

        double rate = midPrice.rate();
        double logRate = Math.log10(rate);
        double logInverseRate = Math.log10(1 / rate);
        int vertexCount = graph.vertexSet().size();


        graph.addVertex(dealtInstrument);
        graph.addVertex(contraInstrument);

        if(shortestPath == null || graph.vertexSet().size() > vertexCount) {
            shortestPath = new BellmanFordShortestPath<>(graph, 0.000001, graph.vertexSet().size() - 1 );
        }

        if (graph.containsEdge(dealtInstrument, contraInstrument)) {
            graph.setEdgeWeight(dealtInstrument, contraInstrument, logRate);
            graph.setEdgeWeight(contraInstrument, dealtInstrument, logInverseRate);
        } else {
            graph.setEdgeWeight(graph.addEdge(dealtInstrument, contraInstrument), logRate);
            graph.setEdgeWeight(graph.addEdge(contraInstrument, dealtInstrument), logInverseRate);
        }
        return false;
    }

    public InstrumentPosMtm calculateInstrumentPosMtm(InstrumentPosMtm instrumentPosMtm) {
        Map<Instrument, Double> mtmPositionsMap = instrumentPosMtm.resetMtm().getMtmPositionsMap();
        instrumentPosMtm.getPositionMap()
                .forEach((key, value) -> {
                    mtmPositionsMap.put(key, value * getRateForInstrument(key));
                });
        instrumentPosMtm.calcTradePnl();
        return instrumentPosMtm;
    }

    public Double getRateForInstrument(Instrument instrument) {
        if (instrument.equals(mtmInstrument)) {
            return 1.0;
        }
        Double rate = directMtmRatesByInstrument.get(instrument);
        if (rate == null) {
            rate = derivedMtmRatesByInstrument.computeIfAbsent(
                    instrument,
                    positionInstrument -> {
                        if (graph.containsVertex(positionInstrument) & graph.containsVertex(mtmInstrument)) {
                            GraphPath<Instrument, DefaultWeightedEdge> path = shortestPath.getPath(positionInstrument, mtmInstrument);
                            double log10Rate = shortestPath.getPathWeight(positionInstrument, mtmInstrument);
                            return Double.isInfinite(log10Rate) ? Double.NaN : Math.pow(10, log10Rate);
                        }
                        return Double.NaN;
                    });
        }
        return rate;
    }
}
