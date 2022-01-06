package pets.service.app.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import pets.service.app.service.RefTypesService;
import pets.service.app.util.EndpointUtil;

@Slf4j
public class SetResetCache implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        EndpointUtil.endpointMap();
        RefTypesService.resetRefTypesCaches();
        RefTypesService.setRefTypesCaches();
    }
}
