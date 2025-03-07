package com.vince.echotask.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParsedIntent {
    private String id;
    private Intent intent;
    private String description;

    public ParsedIntent(UUID id, Intent intent, String taskDescription) {
    }

    @Override
    public String toString() {
        return "ParsedIntent{id='" + id + "', intent='" + intent + "', description='" + description + "'}";
    }
}
