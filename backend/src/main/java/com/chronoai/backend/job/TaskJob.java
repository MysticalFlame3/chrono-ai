package com.chronoai.backend.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(TaskJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String taskName = dataMap.getString("taskName");
        String notificationType = dataMap.getString("notificationType");
        String notificationTarget = dataMap.getString("notificationTarget");

        // This is where the task's work happens.
        // For now, we'll just log a message.
        log.info("Executing job for task: '{}' -> Type: {}, Target: {}",
                taskName, notificationType, notificationTarget);
    }
} 