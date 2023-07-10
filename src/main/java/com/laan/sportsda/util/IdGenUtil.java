package com.laan.sportsda.util;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.UUID;

@Component
public class IdGenUtil {

    public String get20LengthId() {
        StringBuilder builder = new StringBuilder();

        String sTime = "" + Calendar.getInstance().getTimeInMillis();
        builder.append(sTime.substring(sTime.length() - 8));

        String uuid = get32LengthId();
        builder.append(uuid.substring(uuid.length() - 12));

        return builder.toString();
    }

    public String get32LengthId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
