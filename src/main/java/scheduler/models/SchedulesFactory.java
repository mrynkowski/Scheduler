package scheduler.models;

public class SchedulesFactory {

	public static void injectDefaultSettings(Schedule schedule){
		schedule.setNumberOfClasses(0);
		schedule.setDays(5);
		schedule.setHours(10);
		schedule.setIterations(1000);
		schedule.setPopulationSize(100);
		schedule.setHoursA(2.0);
		schedule.setHoursB(4.0);
		schedule.setHoursC(6.0);
		schedule.setHoursD(8.0);
		schedule.setFreeA(0.5);
		schedule.setFreeB(0.0);
		schedule.setHours0(0.0);
		schedule.setCrossoverProbability(1.0);
		schedule.setMutationProbability(0.1);
	}
}