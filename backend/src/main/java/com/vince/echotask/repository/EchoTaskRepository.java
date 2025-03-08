package com.vince.echotask.repository;

import com.vince.echotask.models.Task;
import com.vince.echotask.models.TaskSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface EchoTaskRepository extends JpaRepository<Task, UUID> {

    // Query method to find the best matching task
    @Query(value = "SELECT * FROM tasks " +
            "WHERE to_tsvector('english', description) @@ plainto_tsquery(:input) " +
            "ORDER BY ts_rank(to_tsvector('english', description), plainto_tsquery(:input)) DESC " +
            "LIMIT 1", nativeQuery = true)
    Task findBestMatch(@Param("input") String input);

    @Query(value = "SELECT id, description, status FROM tasks", nativeQuery = true)
    ArrayList<TaskSummary> getAllTaskSummary();

    @Query(value = "UPDATE tasks SET status = 'done' where id = :input")
    Task updateTaskStatus(@Param("input") String input);
}
