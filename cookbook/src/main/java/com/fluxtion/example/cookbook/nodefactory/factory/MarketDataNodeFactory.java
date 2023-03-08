package com.fluxtion.example.cookbook.nodefactory.factory;

import com.fluxtion.compiler.builder.factory.NodeFactory;
import com.fluxtion.compiler.builder.factory.NodeRegistry;
import com.fluxtion.example.cookbook.nodefactory.MarketDataSupplier;
import com.fluxtion.example.cookbook.nodefactory.config.MarketDataSupplierConfig;
import com.fluxtion.example.cookbook.nodefactory.node.MarketDataCrossNode;
import com.fluxtion.example.cookbook.nodefactory.node.MarketDataNode;
import com.fluxtion.runtime.time.Clock;

import java.util.Collections;
import java.util.Map;

public class MarketDataNodeFactory implements NodeFactory<MarketDataSupplier> {

    @Override
    public MarketDataSupplier createNode(Map<String, Object> config, NodeRegistry registry) {
        MarketDataSupplierConfig mktSupplierConfig = (MarketDataSupplierConfig) config.get(MarketDataSupplierConfig.class.getCanonicalName());

        String symbol = mktSupplierConfig.symbol();
        if(symbol.equals("GBPDKK")){
            return new MarketDataCrossNode(symbol, new MarketDataNode("EURGBP"),new MarketDataNode("EURDKK"));
        }
        return new MarketDataNode(symbol);
    }
}
