package com.vince.echotask.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTaskStatusRequest {
    private String id;
    private TaskStatus status;

    @Override
    public String toString() {
        return "UpdateTaskStatusRequest{id='" + id + "', status='" + status + "'}";
    }
}
