package com.itrust.middlewares.nbc.gateway.mock.kyc.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.exceptions.KycRestResponse;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.kycValidation.dtos.NidaResponseFullDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire.NIDAQuestionnaireResponseDataDTO;
import com.itrust.middlewares.nbc.kycValidation.services.OnboardingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class KYCResponseProcessor implements Processor {

    /**
     * The OnboardingService instance used for processing KYC responses.
     */
    private final OnboardingService onboardingService;

    /**
     * Constructor for KYCResponseProcessor.
     *
     * @param onboardingService The OnboardingService instance to use for processing.
     */
    public KYCResponseProcessor(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    /**
     * Process the KYC response from NIDA and map it to the RestResponse object.
     *
     * @param exchange The Camel exchange containing the KYC response.
     * @throws Exception If an error occurs during processing.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String body = exchange.getIn().getBody(String.class);
        KycRestResponse genericRestResponse = mapper.readValue(body, KycRestResponse.class);
        RestResponse restResponse = new RestResponse();
        mapNidaValues(restResponse,genericRestResponse.getData());
        restResponse.setStatusCode(genericRestResponse.getStatusCode());
        restResponse.setMessage(genericRestResponse.getMessage());
        restResponse.setTimestamp(genericRestResponse.getTimestamp());
        restResponse.setMessages(genericRestResponse.getMessages());


        log.debug("KYCResponseProcessor: {}", restResponse);

        if(genericRestResponse.getData() != null) {
            if(genericRestResponse.getData().getStatus().equals("00")) {
                onboardingService.createKycEntry(genericRestResponse.getData());
            }
        }else{
            log.error("KYCResponseProcessor: {}", "NIDA data is null");
        }

        exchange.getIn().setBody(mapper.writeValueAsString(restResponse));

    }

    /**
     * Map the NIDA values to the RestResponse object.
     *
     * @param restResponse The RestResponse object to populate.
     * @param nidaData    The NIDA data to map.
     */
    private void mapNidaValues(RestResponse restResponse, NidaResponseFullDTO nidaData) {

        if(nidaData != null) {
            NIDAQuestionnaireResponseDataDTO questionnaireResponseDataDTO = new NIDAQuestionnaireResponseDataDTO();
            questionnaireResponseDataDTO.setAnswer(null);
            questionnaireResponseDataDTO.setBirthcertificateno(null);
            questionnaireResponseDataDTO.setBirthcountry(nidaData.getBirthCountry());
            questionnaireResponseDataDTO.setBirthdistrict(nidaData.getBirthDistrict());
            questionnaireResponseDataDTO.setBirthregion(nidaData.getBirthRegion());
            questionnaireResponseDataDTO.setBirthward(nidaData.getBirthWard());
            questionnaireResponseDataDTO.setCopReference(nidaData.getCopReference());
            questionnaireResponseDataDTO.setDob(nidaData.getDateOfBirth());
            questionnaireResponseDataDTO.setEn(nidaData.getEn());
            questionnaireResponseDataDTO.setFirstname(nidaData.getFirstName());
            questionnaireResponseDataDTO.setMaritalstatus(nidaData.getMaritalStatus());
            questionnaireResponseDataDTO.setMiddlename(nidaData.getMiddleName());
            questionnaireResponseDataDTO.setNationality(nidaData.getNationality());
            questionnaireResponseDataDTO.setNin(nidaData.getNin());
            questionnaireResponseDataDTO.setOthernames(nidaData.getOtherNames());
            questionnaireResponseDataDTO.setPhonenumber(nidaData.getPhoneNumber());
            questionnaireResponseDataDTO.setPhoto(nidaData.getPhoto());
            questionnaireResponseDataDTO.setResidentpostcode(nidaData.getResidentPostCode());
            questionnaireResponseDataDTO.setResidentregion(nidaData.getResidentRegion());
            questionnaireResponseDataDTO.setResidentdistrict(nidaData.getResidentDistrict());
            questionnaireResponseDataDTO.setResidenthouseno(nidaData.getResidentHouseNo());
            questionnaireResponseDataDTO.setResidentpostaladdress(nidaData.getResidentPostalAddress());
            questionnaireResponseDataDTO.setResidentstreet(null);
            questionnaireResponseDataDTO.setResidentvillage(nidaData.getResidentVillage());
            questionnaireResponseDataDTO.setResidentward(nidaData.getResidentWard());
            questionnaireResponseDataDTO.setRqcode(nidaData.getRqCode());
            questionnaireResponseDataDTO.setSex(nidaData.getSex());
            questionnaireResponseDataDTO.setSw(nidaData.getSw());
            questionnaireResponseDataDTO.setSignature(nidaData.getSignature());
            questionnaireResponseDataDTO.setStatus(nidaData.getStatus());
            questionnaireResponseDataDTO.setSurname(nidaData.getSurName());
            questionnaireResponseDataDTO.setPrevanscode(nidaData.getPreviousAnswecode());
            restResponse.setData(questionnaireResponseDataDTO);
        }
    }

}
