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
import scheduler.models.Subject;

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

	@Override
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

	@Override
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
	@Override
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
	@Override
	public void deleteSchedule(Integer id) {
		Schedule schedule = em.find(Schedule.class, id);
		em.remove(schedule);
	}

	@Transactional
	@Override
	public void createSlot(Integer id, Slot slot) {
		Schedule schedule = em.getReference(Schedule.class, id);
		slot.setSchedule(schedule);
		em.persist(slot);
	}

	@Transactional
	@Override
	public void createSlotCopy(Integer id) throws CloneNotSupportedException {
		Slot slot = em.find(Slot.class, id);		
		Slot clone = (Slot) slot.clone();
		clone.setId(null);
		em.persist(clone);
	}

	@Transactional
	@Override
	public void createResource(Integer id, Resource resource) {
		Schedule schedule = em.getReference(Schedule.class, id);
		resource.setSchedule(schedule);
		em.persist(resource);
	}

	@Transactional
	@Override
	public void deleteSlotsWithClassNumber(Integer classNumber) {
		Query query = em.createQuery("DELETE FROM Slot s WHERE s.classNumber=?1");
		query.setParameter(1, classNumber);
		query.executeUpdate();
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

	@Transactional
	@Override
	public void deleteResource(Integer id) {
		Resource resource = em.find(Resource.class, id);	
		em.remove(resource);
	}

	@Transactional
	@Override
	public void createSubject(Integer id, Subject data) {
		Schedule schedule = em.getReference(Schedule.class, id);
		data.setSchedule(schedule);
		em.persist(data);
	}

	@Transactional
	@Override
	public void deleteSubject(Integer subjectId) {
		Subject subject = em.find(Subject.class, subjectId);	
		em.remove(subject);
	}

	@Override
	public void deleteSlot(Integer id) {
		Slot slot = em.find(Slot.class, id);
		em.remove(slot);
	}

	@Override
	public Slot findSlot(Integer id) {
		Slot slot = em.find(Slot.class, id);
		return slot;
	}

	@Override
	public Subject findSubject(Integer id) {
		Subject subject = em.find(Subject.class, id);
		return subject;
	}
}
