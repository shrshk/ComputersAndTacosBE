package com.search;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sample.ApiGatewayResponse;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(Handler.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // React APP -> API Gateway -> AWS Lambda -> return response

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("received: " + input);

        try {

            Type mapType = new TypeToken<Map<String, List<String>>>() {
            }.getType();

            Map<String, List<String>> inputBody = gson.fromJson(input.get("body").toString(), mapType);

            List<String> inputWordList = inputBody.get("queryString");
            SearchAutoComplete searchAutoComplete = new SearchAutoComplete();
            Map<String, List<String>> response = new HashMap<>();

            for (String inputWord: inputWordList) {
                if (inputWord.isEmpty())
                    continue;
                List<String> resultWords = searchAutoComplete.search(inputWord);
                response.put(inputWord, resultWords);
            }
            // [{ key: 'a', resultWords: [.....]}, ..... ]

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(response)
                    .build();


        } catch (Exception e) {
            String errorMsg = "Error searching words for given request" + input.toString() + "Error:" + e.getMessage();
            LOG.error(errorMsg);
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(errorMsg).build();
        }
    }
}































