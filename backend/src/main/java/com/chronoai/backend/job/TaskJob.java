package com.chronoai.backend.job;

import com.chronoai.backend.model.ExecutionLog;
import com.chronoai.backend.model.Task;
import com.chronoai.backend.repository.ExecutionLogRepository;
import com.chronoai.backend.repository.TaskRepository;
import com.chronoai.backend.service.NotificationService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(TaskJob.class);

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ExecutionLogRepository executionLogRepository;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Long taskId = Long.parseLong(context.getJobDetail().getKey().getName());

        ExecutionLog executionLog = new ExecutionLog();
        Task task = taskRepository.findById(taskId).orElse(null);
        
        if (task == null) {
            log.error("Task with ID {} not found for job execution.", taskId);
            return;
        }
        
        executionLog.setTask(task);
        executionLog.setExecutionTime(LocalDateTime.now());

        try {
            String taskName = dataMap.getString("taskName");
            Task.NotificationType notificationType = Task.NotificationType.valueOf(dataMap.getString("notificationType"));
            String notificationTarget = dataMap.getString("notificationTarget");

            log.info("Executing job for task: '{}'", taskName);
            String message = "Task '" + taskName + "' has been executed.";
            notificationService.sendNotification(notificationType, notificationTarget, message);
            
            executionLog.setStatus(ExecutionLog.LogStatus.SUCCESS);
            executionLog.setDetails("Notification sent successfully.");

        } catch (Exception e) {
            log.error("Job execution failed for task: {}", task.getName(), e);
            executionLog.setStatus(ExecutionLog.LogStatus.FAILURE);
            executionLog.setDetails("Error: " + e.getMessage());
        } finally {
            executionLogRepository.save(executionLog);
        }
    }
}