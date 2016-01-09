package scheduler.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Rate {

	@Id
	@GeneratedValue
	Integer id;

	@Column
	Double best;

	@Column
	Double average;

	@JsonIgnore
	@ManyToOne(targetEntity = Schedule.class)
	Schedule schedule;

	public Rate() {
		super();
	}

	public Rate(Double best, Double average) {
		super();
		this.best = best;
		this.average = average;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getBest() {
		return best;
	}

	public void setBest(Double best) {
		this.best = best;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Rate clone = new Rate(best, average);
		clone.setId(id);
		clone.setSchedule(schedule);
		return clone;
	}
}
