package org.cubic.processors;

import org.cubic.models.OtoDJourney;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@Component
public class WebserviceCallProcess implements ItemProcessor<OtoDJourney, OtoDJourney> {


    @Autowired
    private RestTemplate template;

    @Override
    public OtoDJourney process(OtoDJourney otoDJourney) throws Exception {
        Object obj = template.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Object.class);
        System.out.println("Checking the Webservice call invocation: "+template == null);
        return otoDJourney;
    }
}
