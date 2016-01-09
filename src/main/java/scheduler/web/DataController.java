package scheduler.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import scheduler.models.Account;
import scheduler.models.Genetic;
import scheduler.models.Rate;
import scheduler.models.Resource;
import scheduler.models.Schedule;
import scheduler.models.SchedulesFactory;
import scheduler.models.Slot;
import scheduler.models.Subject;
import scheduler.repositories.AppRepo;

@RestController
@RequestMapping(value = "/rest")
public class DataController {

	@Autowired
	AppRepo scheduleService;

	@RequestMapping("/user")
	public Principal getUser(Principal user) {
		return user;
	}

	@RequestMapping("/account/{name}")
	public Account getAccount(@PathVariable String name) {
		return scheduleService.findByAccountName(name);
	}

	@RequestMapping("/logout")
	public void logout(Principal user) {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public void createAccount(@RequestBody Account account) {
		scheduleService.createAccount(account);
	}

	@RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
	public Account getAccount(@PathVariable Long accountId) {
		return scheduleService.findAccount(accountId);
	}

	@RequestMapping(value = "/{accountId}/schedules", method = RequestMethod.GET)
	public @ResponseBody List<Schedule> getSchedules(
			@PathVariable Long accountId) {
		return scheduleService.getSchedulesByAccountId(accountId);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{accountId}/schedules", method = RequestMethod.POST)
	public void createSchedule(@PathVariable Long accountId,
			@RequestBody Schedule schedule) {
		Account account = scheduleService.findAccount(accountId);
		schedule.setOwner(account);
		SchedulesFactory.injectDefaultSettings(schedule);
		scheduleService.createSchedule(schedule);
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}", method = RequestMethod.DELETE)
	public void deleteSchedule(@PathVariable Integer id) {
		scheduleService.deleteSchedule(id);
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}", method = RequestMethod.GET)
	public @ResponseBody Schedule getSchedule(@PathVariable Integer id) {
		return scheduleService.findScheduleById(id);
	}

	public List<Slot> getSlotsBy(Resource name, List<Slot> slots) {
		List<Slot> list = new ArrayList<Slot>();
		for (Slot slot : slots) {
			if (name.equals(slot.getStudents())
					|| name.equals(slot.getTeacher())
					|| name.equals(slot.getRoom())) {
				list.add(slot);
			}
		}
		return list;
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/resources", method = RequestMethod.GET)
	public @ResponseBody List<Resource> getResources(@PathVariable Integer id) {
		Schedule schedule = scheduleService.findScheduleById(id);
		return schedule.getResources();
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/subjects", method = RequestMethod.GET)
	public @ResponseBody List<Subject> getSubjects(@PathVariable Integer id) {
		Schedule schedule = scheduleService.findScheduleById(id);
		return schedule.getSubjects();
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/resources/{resid}", method = RequestMethod.GET)
	public @ResponseBody ArrayList<ArrayList<Slot>> grids(
			@PathVariable Integer resid, @PathVariable Integer id) {
		Schedule schedule = scheduleService.findScheduleById(id);
		Resource res = scheduleService.getResource(resid);
		return prepareGrid(res, schedule.getSlots(), schedule.getResources(),
				schedule.getDays(), schedule.getHours());
	}

	ArrayList<ArrayList<Slot>> prepareGrid(Resource name, List<Slot> slots,
			List<Resource> resources, int dAYS, int hOURS) {
		ArrayList<ArrayList<Slot>> grid = new ArrayList<ArrayList<Slot>>();
		for (int i = 0; i < hOURS; i++) {
			ArrayList<Slot> row = new ArrayList<Slot>();
			for (int j = 0; j < dAYS; j++) {
				row.add(null);
			}
			grid.add(row);
		}
		List<Slot> data = getSlotsBy(name, slots);
		for (Slot slot : data) {
			if (slot.getHour() != null) {
				grid.get(slot.getHour()).set(slot.getDay(), slot);
			}
		}
		return grid;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{accountId}/schedules/{id}/slots", method = RequestMethod.POST)
	public @ResponseBody void createSlot(@PathVariable Integer id,
			@RequestBody Slot data) throws CloneNotSupportedException {

		Resource students = null;
		Resource teacher = null;
		Subject subject = null;

		if (data.getStudents() != null) {
			students = scheduleService.getResource(data.getStudents().getId());
		}

		if (data.getTeacher() != null) {
			teacher = scheduleService.getResource(data.getTeacher().getId());
		}

		if (data.getSubject() != null) {
			subject = scheduleService.findSubject(data.getSubject().getId());
		}

		if (data.isFixed() == null) {
			data.setFixed(false);
		}

		Schedule schedule = scheduleService.findScheduleById(id);
		Integer numberOfClasses = schedule.getNumberOfClasses();
		data.setClassNumber(numberOfClasses);
		schedule.setNumberOfClasses(numberOfClasses + 1);
		scheduleService.updateSchedule(schedule);

		data.setStudents(students);
		data.setTeacher(teacher);
		data.setSubject(subject);

		scheduleService.createSlot(id, data);
		for (int i = 1; i < data.getDuration(); i++) {
			if (data.isFixed()) {
				scheduleService.createSlotCopy(data.getId(), i);
			} else {
				scheduleService.createSlotCopy(data.getId());
			}
		}
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/slots/{slotId}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteSlot(@PathVariable Integer slotId) {
		Slot slot = scheduleService.findSlot(slotId);
		Integer classNumber = slot.getClassNumber();
		scheduleService.deleteSlotsWithClassNumber(classNumber);
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/resources/{resourceId}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteResource(@PathVariable Integer resourceId) {
		scheduleService.deleteResource(resourceId);
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/subjects/{subjectId}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteSubject(@PathVariable Integer subjectId) {
		scheduleService.deleteSubject(subjectId);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{accountId}/schedules/{id}/resources", method = RequestMethod.POST)
	public @ResponseBody void createResource(@PathVariable Integer id,
			@RequestBody Resource data) {
		scheduleService.createResource(id, data);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{accountId}/schedules/{id}/subjects", method = RequestMethod.POST)
	public @ResponseBody void createSubject(@PathVariable Integer id,
			@RequestBody Subject data) {
		scheduleService.createSubject(id, data);
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/slots", method = RequestMethod.GET)
	public @ResponseBody Set<Slot> getSlots(@PathVariable Integer id) {
		Schedule schedule = scheduleService.findScheduleById(id);
		Set<Slot> slotsSet = new HashSet<Slot>();
		slotsSet.addAll(schedule.getSlots());

		return slotsSet;
	}

	@RequestMapping(value = "/{accountId}/schedules/{id}/rates", method = RequestMethod.GET)
	public @ResponseBody List<Rate> getRates(@PathVariable Integer id) {
		return scheduleService.findScheduleById(id).getRates();
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{accountId}/schedules/{id}/generate", method = RequestMethod.POST)
	public @ResponseBody void generateSchedule(@PathVariable Long accountId,
			@PathVariable Integer id, @RequestBody Schedule updatedSchedule)
			throws CloneNotSupportedException {
		Schedule schedule = scheduleService.findScheduleById(id);

		schedule.setHours0(updatedSchedule.getHours0());
		schedule.setHoursA(updatedSchedule.getHoursA());
		schedule.setHoursB(updatedSchedule.getHoursB());
		schedule.setHoursC(updatedSchedule.getHoursC());
		schedule.setHoursD(updatedSchedule.getHoursD());
		schedule.setFreeA(updatedSchedule.getFreeA());
		schedule.setFreeB(updatedSchedule.getFreeB());
		schedule.setCrossoverProbability(updatedSchedule
				.getCrossoverProbability());
		schedule.setMutationProbability(updatedSchedule
				.getMutationProbability());
		schedule.setDays(updatedSchedule.getDays());
		schedule.setHours(updatedSchedule.getHours());
		schedule.setIterations(updatedSchedule.getIterations());
		schedule.setPopulationSize(updatedSchedule.getPopulationSize());
		schedule.setAlgorithm(updatedSchedule.getAlgorithm());
		scheduleService.deleteRates(schedule);
		scheduleService.updateSchedule(schedule);

		Genetic ga = new Genetic();
		ga.setSchedule(schedule);
		schedule = ga.optimize();

		Account owner = scheduleService.findAccount(accountId);
		schedule.setOwner(owner);

		scheduleService.updateSchedule(schedule);
	}
}