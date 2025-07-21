package com.itrust.middlewares.nbc.accounts.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Setter
@Getter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOpenCallbackType<Object> implements Serializable {

}
