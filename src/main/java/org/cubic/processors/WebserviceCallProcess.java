package org.cubic.processors;

import org.cubic.models.OtoDJourney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Yuvaraj on 15/01/2017.
 */
@Component
public class WebserviceCallProcess implements ItemProcessor<OtoDJourney, OtoDJourney> {

    @Autowired
    private RestTemplate template;


    private static final Logger log = LoggerFactory.getLogger(JourneyItemProcess.class);

    @Override
    public OtoDJourney process(OtoDJourney otoDJourney) throws Exception {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer oauth token"));
        headers.put(HttpHeaders.ACCEPT, Collections.singletonList("application/vnd.api+json"));
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/vnd.api+json"));
        log.info("Make a webservice call");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OtoDJourney> jou  = template.exchange(new URI("https://gturnquist-quoters.cfapps.io/api/random"),
                HttpMethod.GET, httpEntity, OtoDJourney.class);
        log.info("Response Entity is {}", jou.getBody().getDestination());
//        Object obj = template.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Object.class);
        System.out.println("Checking the Webservice call invocation: "+template == null);
        return otoDJourney;
    }
}
