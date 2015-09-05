package scheduler.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Schedule implements Cloneable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;

    @ManyToOne(targetEntity=Account.class)    
	@JoinColumn(name="account_id")
    private Account owner;
	
	@JsonIgnore
	@OneToMany(orphanRemoval=true, targetEntity=Resource.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Resource> resources;

	@JsonIgnore
	@OneToMany(orphanRemoval=true, targetEntity=Subject.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Subject> subjects;

	@JsonIgnore
	@OneToMany(orphanRemoval=true, targetEntity=Slot.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Slot> slots;

	@Column
	Integer days;

	@Column
	Integer hours;

	@Column
	String name;

	@Column
	Double rate;

	@Column
	Integer populationSize;
	
	@Column
	Integer iterations;
	
	@Column
	Integer numberOfClasses;
	
	@Column
	Double hoursA;
	
	@Column
	Double hoursB;
	
	@Column
	Double hoursC;
	
	@Column
	Double hoursD;
	
	@Column
	Double freeA;
	
	@Column
	Double freeB;
	
	@Column
	Double hours0;
	
	@JsonIgnore
	@ElementCollection
	@CollectionTable(name="rates", joinColumns=@JoinColumn(name="schedule_id"))
	List<Double> rates;
	
	public Schedule() {

	}

	public Schedule(String name) {
		this.name = name;
		this.subjects = new ArrayList<Subject>();
		this.slots = new ArrayList<Slot>();
		this.resources = new ArrayList<Resource>();
	}

	public Schedule(Integer id, List<Resource> resources, List<Subject> subjects, List<Slot> slots) {
		super();
		this.id = id;
		this.subjects = subjects;
		this.resources = resources;
		this.slots = slots;
	}

	public String getName() {
		return name;
	}

	public Double getHours0() {
		return hours0;
	}

	public void setHours0(Double hours0) {
		this.hours0 = hours0;
	}

	public Double getHoursA() {
		return hoursA;
	}

	public void setHoursA(Double hoursA) {
		this.hoursA = hoursA;
	}

	public Double getHoursB() {
		return hoursB;
	}

	public void setHoursB(Double hoursB) {
		this.hoursB = hoursB;
	}

	public Double getHoursC() {
		return hoursC;
	}

	public void setHoursC(Double hoursC) {
		this.hoursC = hoursC;
	}

	public Double getHoursD() {
		return hoursD;
	}

	public void setHoursD(Double hoursD) {
		this.hoursD = hoursD;
	}

	public Double getFreeA() {
		return freeA;
	}

	public void setFreeA(Double freeA) {
		this.freeA = freeA;
	}

	public Double getFreeB() {
		return freeB;
	}

	public void setFreeB(Double freeB) {
		this.freeB = freeB;
	}

	@Override
	public Object clone() throws CloneNotSupportedException{		
		Schedule clone = new Schedule(name);
		clone.id = id;
		clone.days = days;
		clone.hours = hours;
		clone.subjects.addAll(subjects);
		clone.resources.addAll(resources);
		
		List<Resource> resCopy = new ArrayList<Resource>();
		for(Resource res : resources){
			resCopy.add((Resource) res.clone());
		}
		
		List<Slot> slotsCopy = new ArrayList<Slot>();
		for(Slot slot : slots){
			slotsCopy.add((Slot) slot.clone());
		}
		clone.slots = slotsCopy;
		return clone;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public void setPopulationSize(Integer populationSize) {
		this.populationSize = populationSize;
	}

	public void setIterations(Integer iterations) {
		this.iterations = iterations;
	}

	public List<Double> getRates() {
		return rates;
	}

	public void setRates(List<Double> rates) {
		this.rates = rates;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getPopulationSize() {
		return populationSize;
	}

	public Integer getIterations() {
		return iterations;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }
	
	public Integer getNumberOfClasses() {
		return numberOfClasses;
	}

	public void setNumberOfClasses(Integer numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}

	@Override
	public String toString() {
		return "Schedule [rate=" + rate + ", populationSize=" + populationSize
				+ ", iterations=" + iterations + ", rates=" + rates + "]";
	}	
}
