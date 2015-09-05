package scheduler.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Subject {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column
	public String name;

	@JsonIgnore
	@ManyToOne(targetEntity=Schedule.class)    
	@JoinColumn(name="schedule_id")
	public Schedule schedule; 
		
	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Subject() {
		super();
	}
	
	public Subject(String id) {
		super();
		this.id = Integer.parseInt(id);
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}