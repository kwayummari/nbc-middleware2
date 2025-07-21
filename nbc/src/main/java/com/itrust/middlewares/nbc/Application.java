package com.itrust.middlewares.nbc;/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.itrust.middlewares.nbc.auth.entities.AuthDataEntity;
import com.itrust.middlewares.nbc.auth.repositories.AuthRepository;
import com.itrust.middlewares.nbc.auth.services.AuthService;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.ResourceHelper;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// CHECKSTYLE:OFF
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application {

    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        CamelContext cContext = new DefaultCamelContext();

        // Configure SSLContextParameters
        KeyStoreParameters keyStore = new KeyStoreParameters();
        keyStore.setType("PKCS12");
        keyStore.setResource(String.valueOf(ResourceHelper.resolveResource(cContext, "classpath:keystore/middlewares-nbc.p12")));
        keyStore.setPassword("iTrust@123");
        TrustManagersParameters trustManagers = new TrustManagersParameters();
        trustManagers.setKeyStore(keyStore);

        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setTrustManagers(trustManagers);

        // Configure HTTPS REST endpoint
        cContext.getRegistry().bind("sslContextParameters", sslContextParameters);

    }

    @Autowired
    AuthRepository authRepository;

    @Autowired
    AuthService authService;

    @Value("${nbc.username}")
    private String nbcUsername;

    @Value("${nbc.auth}")
    private String nbcAuth;

    @Value("${nbc.channel}")
    private String nbcChannel;

    @Value("${nbc.signature}")
    private String nbcSignature;

    @Value("${nbc.terminal.id}")
    private String nbcTerminalId;

    @Value("${nbc.thirdparty.id}")
    private String nbcThirdpartyId;

    @Value("${nbc.auth.mode}")
    private String nbcAuthMode;

    @Value("${nbc.sapp.version}")
    private String nbcSappVersion;

    @Bean
    public CommandLineRunner startup() {

        return args -> {
            AuthDataEntity authDataEntity = new AuthDataEntity();
            authDataEntity.setUsername(nbcUsername);
            authDataEntity.setPassword(nbcAuth);
            authDataEntity.setChannel(nbcChannel);
            authDataEntity.setSignature(nbcSignature);
            authDataEntity.setTerminalId(nbcTerminalId);
            authDataEntity.setThirdpartyid(nbcThirdpartyId);
            authDataEntity.setAuthMode(nbcAuthMode);
            authDataEntity.setSabpVersion(nbcSappVersion);
//            authRepository.save(authDataEntity);
        };
    }

}
// CHECKSTYLE:ON
