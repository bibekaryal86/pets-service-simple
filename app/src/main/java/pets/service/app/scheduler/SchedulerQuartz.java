package pets.service.app.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class SchedulerQuartz {

    public void start() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // schedule to set/reset cache at midnight
            JobDetail jobDetail = JobBuilder.newJob(SetResetCache.class)
                    .withIdentity("SET_RESET_CACHE_JOB")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("SET_RESET_CACHE_TRIGGER")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInHours(12)
                            .repeatForever())
                    .forJob(jobDetail)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException ex) {
            log.error("Start Scheduler Error", ex);
        }
    }
}
