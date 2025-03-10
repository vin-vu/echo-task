package com.vince.echotask.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateStatusRequest {
    private String id;
    private boolean completed;
}
