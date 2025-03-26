package com.tfg.wapp;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

//import com.tfg.wapp.repositories.IngredientRepository;
//import com.tfg.wapp.repositories.ProductRepository;
//import com.tfg.wapp.entities.*;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@SpringBootApplication
public class WappApplication {
	
	 @Value("${trust.store}")
	 private Resource trustStore;

	 @Value("${trust.store.password}")
	 private String trustStorePassword;
	 
	 @Bean
	 public RestTemplate restTemplateWithTrustStore(RestTemplateBuilder builder) throws
	   IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
	  SSLContext sslContext = new SSLContextBuilder()
	    .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
	    .build();
	  SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);

	  @SuppressWarnings("unused")
	  HttpClient httpClient = HttpClients.custom()
	    .setSSLSocketFactory(socketFactory)
	    .build();

	  return builder
	    .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
	    .build();
	 }

	public static void main(String[] args) {
		SpringApplication.run(WappApplication.class, args);
	}

//	@Bean
//	  public CommandLineRunner dataLoader(IngredientRepository repoIngredient, ProductRepository
//			  repoProduct) {
//	    return new CommandLineRunner() {
//	      @Override
//	      public void run(String... args) throws Exception {
//	        repoIngredient.save(new Ingredient(null , "Chicken and bacon", 3.5));
//	        repoIngredient.save(new Ingredient(null , "Chicken and cheese", 2.5));
//	        repoProduct.save(new Product(null, "Pizza", 20));
//	        repoProduct.save(new Product(null, "Burger", 15));
//	        repoProduct.save(new Product(null, "Taco", 10));
//	      }
//	    };
//	  }
	
}
