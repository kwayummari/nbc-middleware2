package com.itrust.middlewares.nbc.kycValidation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.kycValidation.KycValidationMapper;
import com.itrust.middlewares.nbc.kycValidation.dtos.NidaResponseFullDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire.NIDAQuestionnaireResponseDataDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire.NIDAQuestionnaireWrapperResponse;
import com.itrust.middlewares.nbc.kycValidation.entity.KycValidationEntity;
import com.itrust.middlewares.nbc.kycValidation.repository.KycValidationRepository;
import com.itrust.middlewares.nbc.logging.data.LoggingCategory;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.kycValidation.dtos.biometric.NIDABiometricRequestDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.biometric.NIDABiometricResponseDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire.NIDAQuestionnaireRequestDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire.NIDAQuestionnaireResponseDTO;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class OnboardingService extends BaseService {

    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;
    private final ProducerTemplate producerTemplate;
    private final KycValidationRepository kycValidationRepository;

    @Value("${base_end_point}")
    private String baseEndPoint;

    public OnboardingService(RestTemplate restTemplate, SecurityUtils securityUtils, ProducerTemplate producerTemplate, KycValidationRepository kycValidationRepository) {
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        this.producerTemplate = producerTemplate;
        this.kycValidationRepository = kycValidationRepository;
    }

    public RestResponse biometricKYC(NIDABiometricRequestDTO nidaBiometricRequestDTO, String source){
        try{

            String encryptedPayload = securityUtils.encrypt(nidaBiometricRequestDTO.toString());

            String results;

            if(source.equals("itrust")){
                results = producerTemplate.requestBody("direct:validation-biometric", nidaBiometricRequestDTO, String.class);
            }else {
                results = restTemplate.postForObject(baseEndPoint + "/nida/validation/biometric", nidaBiometricRequestDTO, String.class);
            }

            if(results != null && !results.isEmpty()) {

                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                NIDABiometricResponseDTO nidaBiometricResponseDTO = mapper.readValue(results, NIDABiometricResponseDTO.class);

                if(nidaBiometricResponseDTO.getStatusCode() != 600){
                    Map<String, Object> messages = new HashMap<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("nin", nidaBiometricRequestDTO.getNin());
                    if(nidaBiometricResponseDTO.getData() != null) {
                        data.put("status", nidaBiometricResponseDTO.getData().getStatus());
                    }
                    return errorResponse(nidaBiometricResponseDTO.getMessage(),String.valueOf(nidaBiometricResponseDTO.getStatusCode()),messages,data);
                }

                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), null,nidaBiometricResponseDTO.getData());

            }else {
                Map<String, Object> messages = new HashMap<>();
                Map<String, Object> data = new HashMap<>();
                data.put("nin", nidaBiometricRequestDTO.getNin());
                data.put("status", "122");
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,null);
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    @Transactional
    public RestResponse questionnaireKYC(NIDAQuestionnaireRequestDTO nidaQuestionnaireRequestDTO, String source){
        try{

            // validate request

            if(nidaQuestionnaireRequestDTO.getRqCode() != null && !nidaQuestionnaireRequestDTO.getRqCode().isEmpty()){
                if(nidaQuestionnaireRequestDTO.getQnAnsw() == null || nidaQuestionnaireRequestDTO.getQnAnsw().isEmpty()){
                    Map<String, Object> messages = new HashMap<>();
                    messages.put("qnAnsw", "The field qnAnsw can not be empty");
                    Map<String, Object> data = new HashMap<>();
                    data.put("nin", nidaQuestionnaireRequestDTO.getNin());
                    data.put("status", nidaQuestionnaireRequestDTO.getNin());
                    return errorResponse("The field qnAnsw can not be empty", ResponseCode.VALIDATION_FAILED.getCode(), messages,data);
                }
            }

            if(nidaQuestionnaireRequestDTO.getQnAnsw() != null && !nidaQuestionnaireRequestDTO.getQnAnsw().isEmpty()){
                if(nidaQuestionnaireRequestDTO.getRqCode() == null || nidaQuestionnaireRequestDTO.getRqCode().isEmpty()){
                    Map<String, Object> messages = new HashMap<>();
                    messages.put("rqCode", "The field rqCode can not be empty");
                    Map<String, Object> data = new HashMap<>();
                    data.put("nin", nidaQuestionnaireRequestDTO.getNin());
                    return errorResponse("The field rqCode can not be empty", ResponseCode.VALIDATION_FAILED.getCode(), messages,data);
                }
            }

            // encrypt payload
//            String encryptedPayload = securityUtils.encrypt(nidaQuestionnaireRequestDTO.toString());

            String results;
            // attempt nida validation because the investor has not completed validation
            if(source.equals("itrust")){
                results = producerTemplate.requestBody("direct:validation-questionnaire", nidaQuestionnaireRequestDTO, String.class);
            }else {
                results = restTemplate.postForObject(baseEndPoint + "/nida/validation/questionnaire", nidaQuestionnaireRequestDTO, String.class);
            }

            ObjectMapper mapper = new ObjectMapper();

            if(results != null && !results.isEmpty()) {
                String response = securityUtils.decrypt(results);

                NIDAQuestionnaireResponseDTO nidaQuestionnaireResponseDTO = mapper.readValue(results, NIDAQuestionnaireResponseDTO.class);

                if(source.equals("nbc")) {
                    NIDAQuestionnaireWrapperResponse wrapper = mapper.readValue(results, NIDAQuestionnaireWrapperResponse.class);
                    NIDAQuestionnaireResponseDataDTO dto = wrapper.getParsedData();
                    nidaQuestionnaireResponseDTO.setData(dto);
                }

                if(nidaQuestionnaireResponseDTO.getStatusCode() != 600){
                    Map<String, Object> messages = new HashMap<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("nin", nidaQuestionnaireRequestDTO.getNin());
                    if (nidaQuestionnaireResponseDTO.getData() != null) {
                        data.put("status", nidaQuestionnaireResponseDTO.getData().getStatus());
                    } else {
                        data.put("status", "122");
                    }
                    return errorResponse(nidaQuestionnaireResponseDTO.getMessage(), String.valueOf(nidaQuestionnaireResponseDTO.getStatusCode()),messages,data);
                }

                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), null,nidaQuestionnaireResponseDTO.getData());

            }else {
                Map<String, Object> messages = new HashMap<>();
                Map<String, Object> data = new HashMap<>();
                data.put("nin", nidaQuestionnaireRequestDTO.getNin());
                data.put("status", "122");
                  return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,data);
            }

        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }

    @Transactional
    public void createKycEntry(NidaResponseFullDTO nidaResponseFullDTO){
        try{
            // Populate nidaResponse with data
            KycValidationEntity kycValidationEntity = KycValidationMapper.mapToKycValidationEntity(nidaResponseFullDTO);
            // Save the entity to the database
            kycValidationRepository.save(kycValidationEntity);
            // Log the successful save operation
            logger.info("KYC entry created successfully for NIN: {}", nidaResponseFullDTO.getNin());

            // Flush the repository to ensure the changes are persisted
            kycValidationRepository.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
