package com.vince.echotask.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParsedIntent {
    private String intent;
    private String taskDescription;
}
