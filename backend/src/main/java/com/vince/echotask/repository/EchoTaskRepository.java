package com.vince.echotask.repository;

import com.vince.echotask.models.Task;
import com.vince.echotask.models.TaskSummary;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EchoTaskRepository extends JpaRepository<Task, UUID> {

    @Query(value = "SELECT * FROM tasks " +
            "WHERE to_tsvector('english', description) @@ plainto_tsquery(:input) " +
            "ORDER BY ts_rank(to_tsvector('english', description), plainto_tsquery(:input)) DESC " +
            "LIMIT 1", nativeQuery = true)
    Task findBestMatch(@Param("input") String input);

    @Query(value = "SELECT new com.vince.echotask.models.TaskSummary(t.id, t.description, t.completed) FROM Task t")
    List<TaskSummary> getAllTaskSummaries();

    @Transactional
    @Query(value = "UPDATE tasks SET completed = :completedStatus WHERE id = :id RETURNING *", nativeQuery = true)
    Task updateTaskStatus(@Param("completedStatus") boolean completedStatus, @Param("id") UUID id);
}
