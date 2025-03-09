package com.vince.echotask.repository;

import com.vince.echotask.models.Task;
import com.vince.echotask.models.TaskStatus;
import com.vince.echotask.models.TaskSummary;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "SELECT new com.vince.echotask.models.TaskSummary(t.id, t.description, t.status) FROM Task t")
    List<TaskSummary> getAllTaskSummaries();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Task t SET t.status = :newStatus where t.id = :id")
    int updateTaskStatus(@Param("newStatus") TaskStatus newStatus, @Param("id") UUID id);
}
