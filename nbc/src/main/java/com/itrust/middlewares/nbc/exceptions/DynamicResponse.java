package com.itrust.middlewares.nbc.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class DynamicResponse<T> {

    private HashMap<String,Object> data;

}
