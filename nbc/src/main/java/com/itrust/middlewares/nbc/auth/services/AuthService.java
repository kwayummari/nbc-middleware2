package com.itrust.middlewares.nbc.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.auth.dto.requests.AuthChangePasswordRequestDTO;
import com.itrust.middlewares.nbc.auth.dto.requests.AuthLoginRequestDTO;
import com.itrust.middlewares.nbc.auth.dto.responses.AuthResponse;
import com.itrust.middlewares.nbc.auth.entities.AuthDataEntity;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AuthService extends BaseService {


    @Value("${login_end_point}")
    private String baseEndPoint;

    @Value("${nbc.username}")
    private String nbcUsername;

    @Value("${nbc.auth}")
    private String nbcAuth;

    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;

    public AuthService( SecurityUtils securityUtils,RestTemplate restTemplate) {

        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
    }

    public RestResponse authenticate() {

        try{
            logSlack("NBC Token creation started at "  + DateTimeUtil.dateTime());

            AuthLoginRequestDTO authLoginRequestDTO = new AuthLoginRequestDTO();
            authLoginRequestDTO.setAuth(nbcAuth);
            authLoginRequestDTO.setUsername(nbcUsername);
            String timestamp = DateTimeUtil.dateTime();


            authLoginRequestDTO.setTimestamp(timestamp);
            String encryptedPayload = securityUtils.encrypt(authLoginRequestDTO.toString());
            logger.info("Request to NBC Middleware: {}", authLoginRequestDTO);
            String results = restTemplate.postForObject(baseEndPoint +"/auth", authLoginRequestDTO, String.class);

            if(results != null && !results.isEmpty()) {

                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();

                AuthResponse authResponse = mapper.readValue(results, AuthResponse.class);

                if(authResponse.getHeader().getStatusCode() == 600) {
                    String token = securityUtils.encrypt(authResponse.getHeader().getToken());
                    String refreshToken = securityUtils.encrypt(authResponse.getHeader().getRefreshToken());

                    logSlack("NBC Token created successfully by method invocation "  + DateTimeUtil.dateTime());
                }else{
                    logSlack("NBC Token creation failed by method invocation at " + DateTimeUtil.dateTime());
                }

                Map<String, Object> messages = new HashMap<>();
                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(),messages,authResponse);

            }else {
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,null);
            }

        } catch (Exception e) {
            logSlack("NBC Token created successfully by method invocation "  + DateTimeUtil.dateTime());
            logSlack("NBC Token creation failed with error " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    public RestResponse autoAuth() {

        try{
            logSlack("NBC Token creation started at "  + DateTimeUtil.dateTime());
            AuthLoginRequestDTO authLoginRequestDTO = new AuthLoginRequestDTO();
            authLoginRequestDTO.setAuth(nbcAuth);
            authLoginRequestDTO.setUsername(nbcUsername);
            String timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

            authLoginRequestDTO.setTimestamp(timestamp);
            String encryptedPayload = securityUtils.encrypt(authLoginRequestDTO.toString());

            String results = restTemplate.postForObject(baseEndPoint +"/auth", authLoginRequestDTO, String.class);


            if(results != null && !results.isEmpty()) {

                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();

                AuthResponse authResponse = mapper.readValue(results, AuthResponse.class);

                if(authResponse.getHeader().getStatusCode() == 600) {
                    String token = authResponse.getHeader().getToken();
                    String refreshToken = securityUtils.encrypt(authResponse.getHeader().getRefreshToken());

                    logSlack("NBC Token created successfully by task scheduler at " + DateTimeUtil.dateTime());
                }else{
                    logSlack("NBC Token creation failed by task scheduler at " + DateTimeUtil.dateTime());
                }

                Map<String, Object> messages = new HashMap<>();
                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(),messages,authResponse);

            }else {
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,null);
            }

        } catch (Exception e) {
            logSlack("NBC Token creation failed by task scheduler at " + DateTimeUtil.dateTime());
            logSlack("NBC Token creation failed with error " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    public RestResponse changePassword(AuthChangePasswordRequestDTO resetPasswordRequestDTO) {

        try{
            String encryptedPayload = securityUtils.encrypt(resetPasswordRequestDTO.toString());
            String results = restTemplate.postForObject(baseEndPoint +"/auth", encryptedPayload, String.class);
            if(results != null && !results.isEmpty()) {

                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                AuthResponse authResponse = mapper.readValue(response, AuthResponse.class);

                String message = "Request processed successfully";
                Map<String, Object> messages = new HashMap<>();
                return successResponse(message,"600",messages,authResponse);

            }else {
                Map<String, Object> messages = new HashMap<>();
                                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,null);
            }
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }


}
