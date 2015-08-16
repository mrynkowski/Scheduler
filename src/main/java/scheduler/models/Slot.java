package scheduler.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "slots")
public class Slot implements Cloneable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	@JsonIgnore
	@ManyToOne(targetEntity=Schedule.class)    
	@JoinColumn(name="schedule_id")
	public Schedule schedule; 

	@Column
	public Integer classNumber;
	
	@Column
	public Integer day;

	@Column
	public Integer hour;

	@Column
	public String students;

	@Column
	public String teacher;

	@Column
	public String subject;

	@Column
	public String room;

	@Column
	public Integer duration;

	@Transient
	public List<Resource> rooms;

	public Slot(){

	}

	public Slot(String students, String teacher, List<Resource> rooms, String subject, Integer duration) {
		super();
		this.students = students;
		this.teacher = teacher;
		this.subject = subject;
		this.rooms = rooms;
		this.duration = duration;
	}

	public Slot(Integer day, Integer hour, String students, String teacher, String room, String subject, Integer duration) {
		super();
		this.day = day;
		this.hour = hour;
		this.students = students;
		this.teacher = teacher;
		this.subject = subject;
		this.room = room;
		this.duration = duration;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public String getStudents() {
		return students;
	}

	public void setStudents(String students) {
		this.students = students;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<Resource> getRooms() {
		return rooms;
	}

	public void setRooms(List<Resource> rooms) {
		this.rooms = rooms;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result
				+ ((students == null) ? 0 : students.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slot other = (Slot) obj;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (students == null) {
			if (other.students != null)
				return false;
		} else if (!students.equals(other.students))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		return true;
	}	

	@Override
	public Object clone() throws CloneNotSupportedException {
		Slot clone = new Slot(day, hour, students, teacher, room, subject, duration);
		List<Resource> rooms = new ArrayList<Resource>();
		rooms.addAll(this.rooms);
		clone.setClassNumber(this.getClassNumber());
		clone.setRooms(rooms);
		clone.setSchedule(this.schedule);
		clone.setId(this.id);
		return clone;
	}

	@Override
	public String toString() {
		return "Slot [day=" + day + ", hour=" + hour + ", students=" + students
				+ ", teacher=" + teacher + ", subject=" + subject + ", room="
				+ room + "]";
	}	
}