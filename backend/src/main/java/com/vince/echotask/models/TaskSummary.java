package com.vince.echotask.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSummary {
    @Id
    private UUID id;
    private String description;
    private boolean completed;
}
