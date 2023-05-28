package com.laan.sportsda.dto.request;

import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SportAddRequest {

    @NotNull(message = MessagesUtil.AnnotationSupported.MANDATORY_SPORT_NAME)
    @Size(min = 2, max = 150, message = MessagesUtil.AnnotationSupported.INVALID_SPORT_NAME_SIZE)
    private String name;

}
