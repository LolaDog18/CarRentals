package com.woozy.carrentals.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SysPropertyUtils {
    public static void setBaseUri(String serverAddress, int port) {
        String baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(serverAddress)
                .port(port)
                .toUriString();
        System.setProperty("baseUri", baseUri);
    }
}
