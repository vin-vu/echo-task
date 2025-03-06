package com.vince.echotask.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParsedIntent {
    private String id;
    private Intent intent;
    private String description;

    @Override
    public String toString() {
        return "ParsedIntent{id='" + id + "', intent='" + intent + "', description='" + description + "'}";
    }
}
