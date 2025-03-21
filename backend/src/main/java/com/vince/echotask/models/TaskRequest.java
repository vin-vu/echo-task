package com.vince.echotask.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskRequest {
    private String description;

    @Override
    public String toString() {
        return "TaskRequest{description='" + description + "'}";
    }
}
