package com.itrust.middlewares.nbc.auth.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthLoginRequestDTO implements Serializable {

    @NotBlank(message = "Username must not be blank")
    private String username = "NBCSB01";

    @NotBlank(message = "auth must not be blank")
    private String auth = "ItRu2t@202f*!5A";

    private String timestamp;

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", auth='" + auth + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public AuthLoginRequestDTO(String username, String auth) {
        String timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        this.username = username;
        this.auth = auth;
        this.timestamp = timestamp;
    }
}
