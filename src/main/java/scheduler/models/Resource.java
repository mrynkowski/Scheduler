package scheduler.models;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Resource implements Cloneable{

	@Id @GeneratedValue
	public Integer id;
	
	@Column(unique=true)
	public String name;
	
	@Column
	public String type;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Schedule.class)
	public Schedule schedule;
			
	public Resource() {
		super();
	}

	public Resource(String id) {
		super();
		this.id = Integer.parseInt(id);
	}

	public Resource(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public Resource(Integer id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public Resource(Integer id, String name, String type, Schedule schedule) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.schedule = schedule;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Resource clone = new Resource(name, type);
		clone.setId(id);
		clone.setSchedule(schedule);
		return clone;
	}
}
