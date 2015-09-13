package scheduler.repositories;

import java.util.List;

import scheduler.models.Account;
import scheduler.models.Resource;
import scheduler.models.Schedule;
import scheduler.models.Slot;
import scheduler.models.Subject;

public interface AppRepo {
	void createAccount(Account account);
	Account findAccount(Long id);
	Account findByAccountName(String name);
	Schedule updateSchedule(Schedule schedule);
	Schedule getSchedule(Integer id);
	void saveSchedule(Schedule schedule);
	List<Schedule> getSchedulesByAccountName(String name);
	List<Schedule> getSchedulesByAccountId(Long id);
	Object getResult(String query);
	Slot findSlot(Integer id);
	Subject findSubject(Integer id);
	void createSchedule(Schedule schedule);
	void createSchedule(Long id, Schedule schedule);
	void createSlot(Integer id, Slot slot);
	void createSlotCopy(Integer id) throws CloneNotSupportedException;
	void createResource(Integer id, Resource resource);
	Resource getResource(Integer id);
	List<Resource> getResourcesByType(Integer id, String type);
	void deleteSchedule(Integer id);
	void deleteSlot(Integer id);
	void deleteResource(Integer id);
	void createSubject(Integer id, Subject data);
	void deleteSubject(Integer subjectId);
	void deleteSlotsWithClassNumber(Integer classNumber);
	void createSlotCopy(Integer id, int i) throws CloneNotSupportedException;
	Resource getResourceByName(Integer id, String name);
	Subject findSubjectByName(Integer id, String name);
}
