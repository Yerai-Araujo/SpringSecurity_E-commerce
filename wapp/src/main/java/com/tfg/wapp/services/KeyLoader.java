package com.tfg.wapp.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class KeyLoader {

    private final ResourceLoader resourceLoader;

    public KeyLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String loadKey() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:certs/private.pem");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
