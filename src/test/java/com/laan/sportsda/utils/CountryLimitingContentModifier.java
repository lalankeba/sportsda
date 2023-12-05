package com.laan.sportsda.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.response.CountryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.ContentModifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountryLimitingContentModifier implements ContentModifier {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] modifyContent(byte[] bytes, MediaType mediaType) {
        if (bytes == null) {
            return null;
        }
        byte[] resultBytes = null;
        int maxArrayLength = 0;
        try {
            CountryResponse[] countryResponsesArray = objectMapper.readValue(bytes, CountryResponse[].class);
            if (countryResponsesArray != null) {
                if (countryResponsesArray.length > 3) {
                    maxArrayLength = 3;
                } else if (countryResponsesArray.length > 2) {
                    maxArrayLength = 2;
                } else if (countryResponsesArray.length > 1) {
                    maxArrayLength = 1;
                }
                List<CountryResponse> countryResponses = Stream.of(countryResponsesArray).limit(maxArrayLength).toList();
                resultBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(countryResponses);
            }

        } catch (IOException e) {
            log.error("IOException occurred when limiting countries", e);
        }
        return resultBytes;
    }
}
