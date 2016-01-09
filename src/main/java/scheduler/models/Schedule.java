package scheduler.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Schedule implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(name = "account_id")
	Account owner;

	@JsonIgnore
	@OneToMany(orphanRemoval = true, targetEntity = Resource.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Resource> resources;

	@JsonIgnore
	@OneToMany(orphanRemoval = true, targetEntity = Subject.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Subject> subjects;

	@JsonIgnore
	@OneToMany(orphanRemoval = true, targetEntity = Slot.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Slot> slots;

	@JsonIgnore
	@OneToMany(targetEntity = Rate.class, cascade = CascadeType.ALL, mappedBy = "schedule")
	List<Rate> rates;

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

	@Column
	Double crossoverProbability;

	@Column
	Double mutationProbability;

	@Column
	String algorithm;

	public Schedule() {

	}

	public Schedule(String name) {
		this.name = name;
		this.subjects = new ArrayList<Subject>();
		this.slots = new ArrayList<Slot>();
		this.resources = new ArrayList<Resource>();
	}

	public Schedule(Integer id, List<Resource> resources,
			List<Subject> subjects, List<Slot> slots) {
		super();
		this.id = id;
		this.subjects = subjects;
		this.resources = resources;
		this.slots = slots;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
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
	public Object clone() throws CloneNotSupportedException {
		Schedule clone = new Schedule(name);
		clone.id = id;
		clone.days = days;
		clone.hours = hours;
		clone.subjects.addAll(subjects);
		clone.resources.addAll(resources);
		clone.setHours0(hours0);
		clone.setHoursA(hoursA);
		clone.setHoursB(hoursB);
		clone.setHoursC(hoursC);
		clone.setHoursD(hoursD);
		clone.setFreeA(freeA);
		clone.setFreeB(freeB);
		clone.setCrossoverProbability(crossoverProbability);
		clone.setMutationProbability(mutationProbability);
		clone.setDays(days);
		clone.setHours(hours);
		clone.setIterations(iterations);
		clone.setPopulationSize(populationSize);
		clone.setNumberOfClasses(numberOfClasses);
		clone.setRate(rate);
		clone.setAlgorithm(algorithm);

		List<Rate> ratesCopy = new ArrayList<Rate>();
		for (Rate rate : rates) {
			ratesCopy.add((Rate) rate.clone());
		}

		clone.setRates(ratesCopy);

		List<Resource> resCopy = new ArrayList<Resource>();
		for (Resource res : resources) {
			resCopy.add((Resource) res.clone());
		}

		List<Slot> slotsCopy = new ArrayList<Slot>();
		for (Slot slot : slots) {
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

	public List<Rate> getRates() {
		return rates;
	}

	public void setRates(List<Rate> rates) {
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

	public Double getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setCrossoverProbability(Double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public Double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(Double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	@Override
	public String toString() {
		return "Schedule [rate=" + rate + ", populationSize=" + populationSize
				+ ", iterations=" + iterations + ", rates=" + rates + "]";
	}
}
