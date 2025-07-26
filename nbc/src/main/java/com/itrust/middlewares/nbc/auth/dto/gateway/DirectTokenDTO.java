package com.itrust.middlewares.nbc.auth.dto.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

/**
 * OTPVerifyDTO  .
 *
 * @author admin
 *
 */
@Data
public class DirectTokenDTO {
    
    @NotBlank
    private String clientId;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Set<String> scopes;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }

}
