package org.cubic.processors;

import org.cubic.models.OtoDJourney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
public class JourneyItemProcess implements ItemProcessor<OtoDJourney, OtoDJourney> {

    private static final Logger log = LoggerFactory.getLogger(JourneyItemProcess.class);

    @Override
    public OtoDJourney process(OtoDJourney otoDJourney) throws Exception {
        String destination = otoDJourney.getDestination();
        String origin = otoDJourney.getOrigin();
        String priceMIn = otoDJourney.getMinPrice();
        String priceMax = otoDJourney.getMaxPrice();
        log.info("Make a webservice call");
        log.info("Converted dto is:{},{},{},{}", destination, origin, priceMax, priceMIn);
        return otoDJourney;
    }
}
