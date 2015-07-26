package scheduler.repositories;

import java.util.List;

import scheduler.models.Account;
import scheduler.models.Resource;
import scheduler.models.Schedule;
import scheduler.models.Slot;

public interface AppRepo {
	Account findAccount(Long id);
	Account findByAccountName(String name);
	Schedule updateSchedule(Schedule schedule);
	Schedule getSchedule(Integer id);
	void saveSchedule(Schedule schedule);
	List<Schedule> getSchedulesByAccountName(String name);
	List<Schedule> getSchedulesByAccountId(Long id);
	Object getResult(String query);
	void createSchedule(Schedule schedule);
	void createSchedule(Long id, Schedule schedule);
	void createSlot(Integer id, Slot slot);
	void createSlotCopy(Integer id);
	void createResource(Integer id, Resource resource);
	Resource getResource(Integer id);
	List<Resource> getResourcesByType(Integer id, String type);
	void deleteSchedule(Integer id);
	void deleteSlot(Integer id);
	void deleteResource(Integer id);
}
