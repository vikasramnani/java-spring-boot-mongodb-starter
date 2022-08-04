package com.mongodb.starter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

@Configuration
public class SpringConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Value("${javax.net.ssl.trustStore}")
    private String trustStore;

    @Value("${javax.net.ssl.trustStorePassword}")
    private String trustStorePassword;


    @Bean
    public MongoClient mongoClient() throws NoSuchAlgorithmException {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        System.setProperty ("javax.net.ssl.trustStore",trustStore);
        System.setProperty ("javax.net.ssl.trustStorePassword",trustStorePassword);  

        SSLContext sslContext = SSLContext.getDefault();
        MongoClientSettings settings = MongoClientSettings.builder()
        .applyToSslSettings(builder -> {
          builder.enabled(true);
          builder.context(sslContext);
        }).applyConnectionString(new ConnectionString(connectionString)).codecRegistry(codecRegistry)
        .build();


        return MongoClients.create(settings);
    }

}
