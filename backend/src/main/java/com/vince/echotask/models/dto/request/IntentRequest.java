package com.vince.echotask.models.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class IntentRequest {
    private String transcript;
}
