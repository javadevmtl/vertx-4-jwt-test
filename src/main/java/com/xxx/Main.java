package com.xxx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class Main extends AbstractVerticle {

    public void start() {
        System.out.println("Java: " + System.getProperty("java.version"));
        System.out.println("Starting...");

        JWTAuthOptions config = new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setPath("src/main/conf/keystore.p12")
                        .setPassword("secret"))
//                .addPubSecKey(new PubSecKeyOptions()
//                        .setAlgorithm("HS256")
//                        .setBuffer("secret")
//                )
                ;

        JWTAuth provider = JWTAuth.create(vertx, config);

        String jwt = provider.generateToken(new JsonObject().put("myClaim", "foo"), new JWTOptions()
                .setAlgorithm("HS256")
                .setExpiresInMinutes(15));

        System.out.println("Jwt: " + jwt);

        provider.authenticate(new JsonObject().put("token", jwt))
                .onSuccess(user -> System.out.println("User: " + user.principal()))
                .onFailure(err -> {
                    err.printStackTrace();
                });

    }
}
