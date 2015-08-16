package scheduler.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Genetic {

	public Schedule schedule;
	public List<Schedule> population;
	public int populationSize;
	public int iterations;
	public double rate;
	int days;
	int hours;
	
	public ArrayList<Double> rates;

	public Genetic() {
		super();
	}

	public Genetic(Schedule schedule) {
		this.schedule = schedule;
		this.iterations = schedule.iterations;
		this.populationSize = schedule.populationSize;
		population = new ArrayList<Schedule>();
		population.add(schedule);
	}

	public List<Schedule> getPopulation() {
		return population;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public void init() {
		for (Slot slot : schedule.slots) {
			slot.day = Integer.MAX_VALUE;
			slot.hour = Integer.MAX_VALUE;

			List<Resource> list = schedule.getResources();
			List<Resource> output = new ArrayList<Resource>();
			for (Resource res : list) {
				if (res.getType().equals("room")) {
					output.add(res);
				}
			}

			slot.setRooms(output);
			slot.room = null;
		}
		for (Slot slot : schedule.slots) {
			setDayAndHour(slot, schedule);
		}
	}

	public void setDayAndHour(Slot slot, Schedule s) {
		boolean write = false;
		while (!write) {
			int day = (int) (Math.random() * s.days);
			int hour = (int) (Math.random() * s.hours - slot.duration) ;
			
			Resource room = slot.rooms.get((int) (Math.random() * slot.rooms
					.size()));
			int i = 0;
			boolean good = true;
			while (i < s.slots.size() && good) {
				for (int h = 0; h < slot.duration; h++) {
					good = good && isFree(day, hour + h, slot, room, s);
					good = good && (hour + slot.duration <= s.hours);
				}
				i++;
			}
			if (good == true) {
				write = true;
				int j = 0;
				for (Slot sl : s.slots) {
					if (sl.students.equals(slot.students)
							&& sl.teacher.equals(slot.teacher)
							&& sl.subject.equals(slot.subject)) {
						sl.day = day;
						sl.hour = hour + j;
						sl.room = room.getName();
						j++;
					}
				}
			}
		}
	}

	public Schedule optimize() throws CloneNotSupportedException {
		init();

		rates = new ArrayList<Double>();

		for (int i = 1; i < populationSize; i++) {
			Schedule clone = (Schedule) schedule.clone();
			population.add(clone);
		}

		for (int i = 0; i < iterations; i++) {
			mutation();
			for (Schedule s : population) {
				sort(s.slots);
				s.rate = rateSchedule(s);
			}
			sortByRate(population);

			rates.add(population.get(0).rate);

			population.remove(population.size() - 1);
			Schedule clone = (Schedule) population.get(0).clone();
			population.add(clone);
		}
		sort(population.get(0).slots);
		return population.get(0);
	}

	public void sortByRate(List<Schedule> p) {
		Collections.sort(p, new Comparator<Schedule>() {
			@Override
			public int compare(Schedule o1, Schedule o2) {
				return o1.rate.compareTo(o2.rate);
			}
		});
	}

	public void mutation() throws CloneNotSupportedException {
		Schedule s = population.get((int) (Math.random() * population.size()));
		Slot removed = (Slot) s.slots
				.get((int) (Math.random() * s.slots.size()));

		for (Slot slot : s.slots) {
			if (slot.students.equals(removed.students)
					&& slot.teacher.equals(removed.teacher)
					&& slot.subject.equals(removed.subject)) {
				slot.day = 99;
				slot.hour = 99;
				slot.room = null;
			}
		}
		setDayAndHour(removed, s);
	}

	public void sort(List<Slot> slots) {
		Collections.sort(slots, new Comparator<Slot>() {
			@Override
			public int compare(Slot o1, Slot o2) {
				int c;
				c = o1.day.compareTo(o2.day);
				if (c == 0) {
					c = o1.hour.compareTo(o2.hour);
					if (c == 0) {
						c = o1.students.compareTo(o2.students);
					}
				}
				return c;
			}
		});
	}

	public boolean isFree(Integer day, Integer hour, Slot match, Resource room,
			Schedule s) {
		for (Slot slot : s.slots) {
			if (slot.day == day && slot.hour == hour) {
				if (slot.students.equals(match.students)
						|| slot.teacher.equals(match.teacher)
						|| slot.room.equals(room.getName())) {
					return false;
				}
			}
		}
		return true;
	}

	public double rateSchedule(Schedule schedule) {
		rate = 0;
		for (Resource res : schedule.resources) {
			
			if (res.getType().equals("students") || res.getType().equals("teacher")) {

				for (int i = 0; i < schedule.days; i++) {
					List<Slot> day = new ArrayList<Slot>();
					
					for (Slot slot : schedule.slots) {
						if (slot.day == i && (slot.students.equals(res.getName()) || slot.teacher.equals(res.getName()))) {
							day.add(slot);
						}
					}

					rate = rate + rateDay(day);
				}
			}
		}
		return rate;
	}

	public double rateDay(List<Slot> day){
		int freeSlots = 0;
		double output = 0;
		if (day.size() > 1) {
			int curr = day.get(0).hour;
			for (int d = 1; d < day.size(); d++) {
				freeSlots = freeSlots + day.get(d).hour - curr - 1;
				curr = day.get(d).hour;
			}
			output = output + rateFreeSlots(freeSlots);
		}
		output = output + rateLessonsNumber(day.size());
		return output;
	}
	
	public double rateLessonsNumber(int lessons) {

		double a, b, c, d;
		a = 2;
		b = 4;
		c = 6;
		d = 8;

		if (lessons == 0) {
			return 0;
		} else if(lessons > 0 && lessons <= a){
			return 1;
		} else if (lessons > a && lessons <= b) {
			return (b - lessons) / (b - a);
		} else if (b < lessons && lessons <= c) {
			return 0;
		} else if (c < lessons && lessons <= d) {
			return (lessons - c) / (d - c);
		} else {
			return 1;
		}
	}

	public double rateFreeSlots(int lessons) {
		double a = 0.5;
		double b = 0;
		return a*lessons + b;
	}

	public double getRate() {
		return rate;
	}

	public int getIterations() {
		return iterations;
	}

	public void setiterations(int iterations) {
		this.iterations = iterations;
	}
}
