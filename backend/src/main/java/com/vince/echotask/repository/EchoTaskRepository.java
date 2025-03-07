package com.vince.echotask.repository;

import com.vince.echotask.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EchoTaskRepository extends JpaRepository<Task, UUID> {

    // Query method to find the best matching task
    @Query(value = "SELECT * FROM tasks " +
            "WHERE to_tsvector('english', description) @@ plainto_tsquery(:input) " +
            "ORDER BY ts_rank(to_tsvector('english', description), plainto_tsquery(:input)) DESC " +
            "LIMIT 1", nativeQuery = true)
    Task findBestMatch(@Param("input") String input);
}
