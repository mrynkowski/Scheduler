import javax.transaction.Transactional;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scheduler.Scheduler;
import scheduler.models.Schedule;
import scheduler.repositories.AppRepo;
import scheduler.web.SchedulesController;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Scheduler.class)
public class AppRepoTest {

    Schedule s;
    @Autowired
    SchedulesController sc;
    @Autowired
    AppRepo repo;

}
