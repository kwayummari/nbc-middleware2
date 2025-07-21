package com.itrust.middlewares.nbc.tips.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.responses.NBCResponseDTO;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.tips.tdo.requests.TIPSConfirmRequestDTO;
import com.itrust.middlewares.nbc.tips.tdo.requests.TIPSLookupRequestDTO;
import com.itrust.middlewares.nbc.tips.tdo.requests.TIPSTransferRequestDTO;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service class for handling TIPS (Tanzania Instant Payment System) operations.
 * This service provides functionality for looking up, confirming, and transferring funds through TIPS.
 * It communicates with external TIPS services through REST APIs and supports both direct and iTrust routing.
 */
@Service("tipsService")
public class TipsService extends BaseService {

    @Value("${tips_end_point}")
    private String baseEndPoint;

    private final ProducerTemplate producerTemplate;
    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;
    private final Environment environment;

    /**
     * Constructs a new TipsService with the required dependencies.
     *
     * 
     * @param securityUtils Utility for encryption and decryption operations
     * @param restTemplate Template for making REST API calls
     * @param producerTemplate Template for producing messages to Camel routes
     * @param environment Spring environment for accessing configuration properties
     */
    public TipsService(SecurityUtils securityUtils, RestTemplate restTemplate, 
                       ProducerTemplate producerTemplate, Environment environment) {
        
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        this.producerTemplate = producerTemplate;
        this.environment = environment;
        logInfo("TipsService initialized with endpoint: " + baseEndPoint);
    }

    /**
     * Performs a TIPS lookup operation to find recipient information.
     *
     * @param lookupRequestDTO The request data containing lookup details
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the result of the lookup operation
     */
    public RestResponse lookup(TIPSLookupRequestDTO lookupRequestDTO, String source) {
        logInfo("Starting TIPS lookup for account: " + lookupRequestDTO.getCustomerAccount() + " from source: " + source);

        try {

            String encryptedPayload = securityUtils.encrypt(lookupRequestDTO.toString());
            logDebug("Sending TIPS lookup request via " + (source.equals("itrust") ? "iTrust route" : "direct REST call"));

            String results;
            if (source.equals("itrust")) {
                logInfo("Routing TIPS lookup request through iTrust");
                results = producerTemplate.requestBody("direct:tips-inquiry", lookupRequestDTO, String.class);
            } else {
                logInfo("Sending TIPS lookup request to: " + baseEndPoint + "/lookup/card-less");
                results = restTemplate.postForObject(baseEndPoint + "/lookup/card-less", lookupRequestDTO, String.class);
            }


            if (results != null && !results.isEmpty()) {
                logInfo("Received response from TIPS lookup service");
                logDebug("TIPS lookup response: " + results);

                String decryptedResponse = securityUtils.decrypt(results);
                logDebug("Response decrypted successfully");

                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO restResponse = mapper.readValue(results, NBCResponseDTO.class);
                Map<String, Object> messages = new HashMap<>();

                logInfo("TIPS lookup response status code: " + restResponse.getStatusCode());

                if (Objects.equals(restResponse.getStatusCode(), "600")) {
                    logInfo("Processing successful TIPS lookup response with message: " + restResponse.getMessage());

                    ObjectNode objectNode = mapper.valueToTree(restResponse);

                    RestResponse response = successResponse(restResponse.getMessage(), ResponseCode.SUCCESS.getCode(), messages, objectNode);
                    logInfo("TIPS lookup completed with status: SUCCESS");
                    return response;
                } else {
                    logWarn("Processing error TIPS lookup response with code: " + restResponse.getStatusCode() + ", message: " + restResponse.getMessage());

                    RestResponse response = errorResponse(restResponse.getMessage(), restResponse.getStatusCode(), messages, null);
                    logInfo("TIPS lookup completed with status: FAILED");
                    return response;
                }
            } else {
                logError("Empty or null response received from TIPS lookup service");

                Map<String, Object> messages = new HashMap<>();
                RestResponse response = errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), 
                                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                                    messages, null);
                logInfo("TIPS lookup completed with status: FAILED");
                return response;
            }
        } catch (Exception e) {
            logError("Error during TIPS lookup: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Confirms a TIPS transaction after lookup.
     *
     * @param confirmationRequestDTO The request data containing confirmation details
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the result of the confirmation operation
     */
    public RestResponse confirm(TIPSConfirmRequestDTO confirmationRequestDTO, String source) {
        logInfo("Starting TIPS confirmation for channel reference: " + confirmationRequestDTO.getChannelRef() + " from source: " + source);

        try{

            String encryptedPayload = securityUtils.encrypt(confirmationRequestDTO.toString());
            logDebug("Sending TIPS confirmation request via " + (source.equals("itrust") ? "iTrust route" : "direct REST call"));

            String results;
            if (source.equals("itrust")) {
                logInfo("Routing TIPS confirmation request through iTrust");
                results = producerTemplate.requestBody("direct:tips-confirm", confirmationRequestDTO, String.class);
            }else {
                logInfo("Sending TIPS confirmation request to: " + baseEndPoint + "/confirm/card-less");
                results = restTemplate.postForObject(baseEndPoint + "/confirm/card-less", confirmationRequestDTO, String.class);
            }

            if(results != null && !results.isEmpty()) {
                logInfo("Received response from TIPS confirmation service");
                logDebug("TIPS confirmation response: " + results);

                String decryptedResponse = securityUtils.decrypt(results);
                logDebug("Response decrypted successfully");

                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO restResponse = mapper.readValue(results, NBCResponseDTO.class);
                Map<String, Object> messages = new HashMap<>();

                logInfo("TIPS confirmation response status code: " + restResponse.getStatusCode());

                if(Objects.equals(restResponse.getStatusCode(), "600")) {
                    logInfo("Processing successful TIPS confirmation response with message: " + restResponse.getMessage());

                    ObjectNode objectNode = mapper.valueToTree(restResponse);

                    RestResponse successResult = successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), messages, objectNode);
                    logInfo("TIPS confirmation completed with status: SUCCESS");
                    return successResult;
                }else{
                    logWarn("Processing error TIPS confirmation response with code: " + restResponse.getStatusCode() + ", message: " + restResponse.getMessage());

                    RestResponse errorResult = errorResponse(restResponse.getMessage(), restResponse.getStatusCode(), messages,null);
                    logInfo("TIPS confirmation completed with status: FAILED");
                    return errorResult;
                }

            }else {
                logError("Empty or null response received from TIPS confirmation service");

                Map<String, Object> messages = new HashMap<>();
                RestResponse response = errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                                    messages,null);
                logInfo("TIPS confirmation completed with status: FAILED");
                return response;
            }

        } catch (Exception e) {
            logError("Error during TIPS confirmation: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    /**
     * Initiates a funds transfer through TIPS.
     *
     * @param transferRequestDTO The request data containing transfer details
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the result of the transfer operation
     */
    public RestResponse transfer(TIPSTransferRequestDTO transferRequestDTO, String source) {
        logInfo("Starting TIPS transfer for channel reference: " + transferRequestDTO.getChannelRef() + " from source: " + source);

        try{
            // Set callback URL from environment properties
            transferRequestDTO.setCallbackUrl(environment.getProperty("tips_callback_url"));
            logDebug("Setting callback URL: " + environment.getProperty("tips_callback_url"));

            String encryptedPayload = securityUtils.encrypt(transferRequestDTO.toString());
            logDebug("Sending TIPS transfer request via " + (source.equals("itrust") ? "iTrust route" : "direct REST call"));

            String results;
            if (source.equals("itrust")) {
                logInfo("Routing TIPS transfer request through iTrust");
                results = producerTemplate.requestBody("direct:tips-transfer", transferRequestDTO, String.class);
            }else {
                logInfo("Sending TIPS transfer request to: " + baseEndPoint + "/transfer/card-less");
                results = restTemplate.postForObject(baseEndPoint + "/transfer/card-less", transferRequestDTO, String.class);
            }


            if(results != null && !results.isEmpty()) {
                logInfo("Received response from TIPS transfer service");
                logDebug("TIPS transfer response: " + results);

                String decryptedResponse = securityUtils.decrypt(results);
                logDebug("Response decrypted successfully");

                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO restResponse = mapper.readValue(results, NBCResponseDTO.class);
                Map<String, Object> messages = new HashMap<>();

                logInfo("TIPS transfer response status code: " + restResponse.getStatusCode());

                if(Objects.equals(restResponse.getStatusCode(), "600")) {
                    logInfo("Processing successful TIPS transfer response with message: " + restResponse.getMessage());

                    ObjectNode objectNode = mapper.valueToTree(restResponse);
                    objectNode.remove("statusCode");

                    RestResponse successResult = successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), messages, objectNode);
                    logInfo("TIPS transfer completed with status: SUCCESS");
                    return successResult;
                }else{
                    logWarn("Processing error TIPS transfer response with code: " + restResponse.getStatusCode() + ", message: " + restResponse.getMessage());

                    RestResponse errorResult = errorResponse(restResponse.getMessage(), restResponse.getStatusCode(), messages,null);
                    logInfo("TIPS transfer completed with status: FAILED");
                    return errorResult;
                }
            }else {
                logError("Empty or null response received from TIPS transfer service");

                Map<String, Object> messages = new HashMap<>();
                RestResponse errorResult = errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                                      ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                                      messages,null);
                logInfo("TIPS transfer completed with status: FAILED");
                return errorResult;
            }
         } catch (Exception e) {
            logError("Error during TIPS transfer: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    /**
     * Retrieves a list of Financial Service Providers (FSPs) from TIPS.
     *
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the list of available FSPs
     */
    public RestResponse fsp(String source) {
        logInfo("Starting TIPS FSP retrieval from source: " + source);

        try{
            String encryptedPayload = securityUtils.encrypt("");
            logDebug("Sending TIPS FSP request via " + (source.equals("itrust") ? "iTrust route" : "direct REST call"));

            String results;
            if (source.equals("itrust")) {
                logInfo("Routing TIPS FSP request through iTrust");
                results = producerTemplate.requestBody("direct:tips-service-providers", null, String.class);
            }else {
                logInfo("Sending TIPS FSP request to: " + baseEndPoint + "/fsp");
                results = restTemplate.getForObject(baseEndPoint + "/fsp", String.class);
            }

            if(results != null && !results.isEmpty()) {
                logInfo("Received response from TIPS FSP service");
                logDebug("TIPS FSP response: " + results);

                String decryptedResponse = securityUtils.decrypt(results);
                logDebug("Response decrypted successfully");

                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO restResponse = mapper.readValue(results, NBCResponseDTO.class);
                Map<String, Object> messages = new HashMap<>();

                logInfo("TIPS FSP response status code: " + restResponse.getStatusCode());

                if(Objects.equals(restResponse.getStatusCode(), "600")) {
                    logInfo("Processing successful TIPS FSP response with message: " + restResponse.getMessage());

                    RestResponse successResult;
                    if (source.equals("itrust")) {
                        logDebug("Using data field for iTrust source");
                        successResult = successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), messages, restResponse.getData());
                    } else {
                        logDebug("Using body field for non-iTrust source");
                        successResult = successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), messages, restResponse.getBody());
                    }

                    logInfo("TIPS FSP retrieval completed with status: SUCCESS");
                    return successResult;
                }else{
                    logWarn("Processing error TIPS FSP response with code: " + restResponse.getStatusCode() + ", message: " + restResponse.getMessage());

                    RestResponse errorResult = errorResponse(restResponse.getMessage(), restResponse.getStatusCode(), messages,null);
                    logInfo("TIPS FSP retrieval completed with status: FAILED");
                    return errorResult;
                }
            }else {
                logError("Empty or null response received from TIPS FSP service");

                Map<String, Object> messages = new HashMap<>();
                RestResponse errorResult = errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                                      ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                                      messages,null);
                logInfo("TIPS FSP retrieval completed with status: FAILED");
                return errorResult;
            }
        } catch (Exception e) {
            logError("Error during TIPS FSP retrieval: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

}
