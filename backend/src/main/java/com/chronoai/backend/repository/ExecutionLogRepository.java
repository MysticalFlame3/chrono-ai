package com.chronoai.backend.repository;

import com.chronoai.backend.model.ExecutionLog;
import com.chronoai.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {
    List<ExecutionLog> findByTask(Task task);
}