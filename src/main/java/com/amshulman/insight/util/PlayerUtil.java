package com.amshulman.insight.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.google.common.base.CharMatcher;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class PlayerUtil {

    static String USER_AGENT = "Insight";
    static String SESSION_SERVER = "https://sessionserver.mojang.com/session/minecraft/profile/";

    public static String getCurrentName(UUID uuid) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(SESSION_SERVER + uuid.toString().replace("-", "")).openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String name = response.toString().replaceAll("\"properties\":\\[.*?\\],", "").replaceAll(".+?\"name\":\"([A-Za-z0-9_]+)\".*", "$1");

            if (name.isEmpty() || !CharMatcher.JAVA_LETTER_OR_DIGIT.matchesAllOf(name)) {
                return uuid.toString().replace("-", "");
            }

            return name;
        } catch (IOException e) {
            return uuid.toString().replace("-", "");
        }
    }
}
