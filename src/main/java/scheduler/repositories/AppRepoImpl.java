package scheduler.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import scheduler.models.Account;
import scheduler.models.Resource;
import scheduler.models.Schedule;
import scheduler.models.Slot;

@Repository
public class AppRepoImpl implements AppRepo{

	@Autowired
	EntityManager em;

	@Transactional
	public void saveSchedule(Schedule schedule) {
		em.persist(schedule);
	}	
	
    @Override
    public Account findAccount(Long id) {
        return em.find(Account.class, id);
    }

	public Schedule getSchedule(Integer id) {
		Schedule s = em.find(Schedule.class, id);
		s.getResources().size();
		s.getSubjects().size();
		s.getSlots().size();
		s.getRates().size();
		return s;
	}

	@Override
	public List<Schedule> getSchedulesByAccountId(Long id) {
        Query query = em.createQuery("SELECT s from Schedule s where s.owner.id=?1");
        query.setParameter(1, id);
        return query.getResultList();
	}
	
	@Override
	public List<Schedule> getSchedulesByAccountName(String name) {
        Query query = em.createQuery("SELECT s from Schedule s where s.owner.name=?1");
        query.setParameter(1, name);
        return query.getResultList();
	}
	
	public List<Schedule> getSchedules() {
		return em.createQuery("SELECT s FROM Schedule s").getResultList();
	}

	public Object getResult(String query){
		return em.createQuery(query).getResultList();
	}

	@Transactional
	public void createSchedule(Schedule schedule) {
		try {
			em.persist(schedule);
		} catch (Exception e) {
			
		}
	}

	@Override
	public void createSchedule(Long id, Schedule schedule) {
		try {
			Account account = em.find(Account.class, id);
			schedule.setOwner(account);
			em.persist(schedule);
		} catch (Exception e) {
			
		}	
	}
	
	@Override
	public Resource getResource(Integer id) {
		Resource res = em.find(Resource.class, id);
		return res;
	}

	@Transactional
	public Schedule updateSchedule(Schedule schedule) {
		em.merge(schedule);
		return schedule;
	}

	@Override
	public List<Resource> getResourcesByType(Integer id, String type) {
		Schedule s = em.find(Schedule.class, id);
		
		List<Resource> list = s.getResources();
		List<Resource> output = new ArrayList<Resource>();
		for (Resource res : list) {
			if(res.getType().equals("room")){
				output.add(res);
			}
		}
		return output;
	}

	@Transactional
	public void deleteSchedule(Integer id) {
		Schedule schedule = em.find(Schedule.class, id);
		em.remove(schedule);
	}

	@Transactional
	public void createSlot(Integer id, Slot slot) {
		Schedule schedule = em.getReference(Schedule.class, id);
		slot.setSchedule(schedule);
		em.persist(slot);
	}

	@Transactional
	public void createSlotCopy(Integer id) {
		Slot clone = em.find(Slot.class, id);
		em.detach(clone);
		clone.setId(null);
		em.persist(clone);
	}

	@Transactional
	public void createResource(Integer id, Resource resource) {
		Schedule schedule = em.getReference(Schedule.class, id);
		resource.setSchedule(schedule);
		em.persist(resource);
	}

	@Transactional
	public void deleteSlot(Integer id) {
		Slot slot = em.find(Slot.class, id);
		em.remove(slot);
	}

	@Override
	public Account findByAccountName(String name) {
        Query query = em.createQuery("SELECT a FROM Account a WHERE a.name=?1");
        query.setParameter(1, name);
        List<Account> accounts = query.getResultList();
        if(accounts.size() == 0) {
            return null;
        } else {
            return accounts.get(0);
        }
	}

}
