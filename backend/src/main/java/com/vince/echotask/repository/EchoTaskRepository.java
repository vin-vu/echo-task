package com.vince.echotask.repository;

import com.vince.echotask.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EchoTaskRepository extends JpaRepository<Task, UUID> {


    default void deleteBestMatchingTask(String input) {
        String query = "DELETE FROM tasks WHERE id = ("
                + "SELECT id FROM tasks "
                + "WHERE to_tsvector('english', description) @@ plainto_tsquery(:input) "
                + "ORDER BY ts_rank(to_tsvector('english', description), plainto_tsquery(:input)) DESC "
                + "limit 1) RETURNING *";
    }
}
