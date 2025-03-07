package com.vince.echotask.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSummary {

    @Id
    private UUID id;
    private String description;
    private String status;
}
