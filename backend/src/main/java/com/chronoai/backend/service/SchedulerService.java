package com.chronoai.backend.service;

import com.chronoai.backend.job.TaskJob;
import com.chronoai.backend.model.Task;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;

    @Autowired
    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleJob(Task task) {
        try {
            JobDetail jobDetail = buildJobDetail(task);
            Trigger trigger = buildTrigger(task, jobDetail);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Job scheduled for task: {}", task.getName());
        } catch (SchedulerException e) {
            log.error("Error scheduling job for task: {}", task.getName(), e);
        }
    }

    private JobDetail buildJobDetail(Task task) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("taskName", task.getName());
        jobDataMap.put("notificationType", task.getNotificationType().toString());
        jobDataMap.put("notificationTarget", task.getNotificationTarget());

        return JobBuilder.newJob(TaskJob.class)
                .withIdentity(task.getId().toString(), "tasks")
                .withDescription(task.getDescription())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(Task task, JobDetail jobDetail) {
        String cronExpression = convertToQuartzCron(task.getCronExpression());
        try {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "task-triggers")
                    .withDescription("Trigger for " + task.getName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("CronExpression '" + cronExpression + "' is invalid.", e);
        }
    }

    /**
     * Converts Unix cron format (5 fields) to Quartz cron format (6 fields)
     * Unix: minute hour day month dayOfWeek
     * Quartz: second minute hour day month dayOfWeek
     */
    private String convertToQuartzCron(String cronExpression) {
        if (cronExpression == null || cronExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("CRON expression cannot be null or empty");
        }
        
        String[] parts = cronExpression.trim().split("\\s+");
        
        // If already 6 fields (Quartz format), return as is
        if (parts.length == 6) {
            return cronExpression;
        }
        
        // If 5 fields (Unix format), prepend "0" for seconds
        if (parts.length == 5) {
            return "0 " + cronExpression;
        }
        
        throw new IllegalArgumentException("Invalid CRON expression format. Expected 5 or 6 fields, got " + parts.length);
    }
}