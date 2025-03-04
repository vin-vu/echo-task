package com.vince.echotask.repository;

import com.vince.echotask.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EchoTaskRepository extends JpaRepository<Task, UUID> {

    default void deleteBestMatchingTask(String description) {

    }
}
