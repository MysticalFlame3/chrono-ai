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
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "task-triggers")
                .withDescription("Trigger for " + task.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .build();
    }

    public void deleteJob(Long taskId) {
        try {
            scheduler.deleteJob(new JobKey(taskId.toString(), "tasks"));
            log.info("Job deleted for task ID: {}", taskId);
        } catch (SchedulerException e) {
            log.error("Error deleting job for task ID: {}", taskId, e);
    }
}
}