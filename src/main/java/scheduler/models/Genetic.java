package scheduler.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Genetic {

	Schedule schedule;
	List<Schedule> population;

	public Genetic() {
		super();
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
		population = new ArrayList<Schedule>();
		population.add(schedule);	
	}

	public List<Schedule> getPopulation() {
		return population;
	}

	public void init(Schedule schedule) {
		for (Slot slot : schedule.slots) {
			if (!slot.fixed) {
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
		}
		for (Slot slot : schedule.slots) {
			if (!slot.fixed) {
				setDayAndHour(slot, schedule);	
			}
		}
	}
	
	public void setDayAndHour(Slot slot, Schedule s) {
		
		ArrayList<ArrayList<Boolean>> grid = new ArrayList<ArrayList<Boolean>>();
				
		boolean write = false;
		while (!write) {
			int day = (int) (Math.random() * s.days);
			int hour = (int) (Math.random() * s.hours) ;
			
				Resource room = slot.rooms.get((int) (Math.random() * slot.rooms.size()));
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
						if (slot.students.equals(sl.students)  && slot.teacher.equals(sl.teacher) && slot.subject.equals(sl.subject)
								&& slot.classNumber.equals(sl.classNumber)) {
							sl.day = day;
							sl.hour = hour + j;
							sl.room = room;
							j++;
						}
					}
				}
		}
	}

	public Schedule mutation(Schedule mutatedSchedule) throws CloneNotSupportedException {
		Slot removed = (Slot) mutatedSchedule.slots.get((int) (Math.random() * mutatedSchedule.slots.size()));

		for (Slot slot : mutatedSchedule.slots) {
			if (!slot.fixed) {
				if (slot.students.equals(removed.students)
						&& slot.teacher.equals(removed.teacher)
						&& slot.subject.equals(removed.subject)
						&& slot.classNumber.equals(removed.classNumber)) {
					slot.day = 99;
					slot.hour = 99;
					slot.room = null;
				}
			}

		}
		if (!removed.fixed) {
			setDayAndHour(removed, mutatedSchedule);
		}
		
		return mutatedSchedule;
	}
	
	public void crossover(Schedule daddy, Schedule mommy) throws CloneNotSupportedException {		
		Set<Integer> conflictsSet = conflicts(daddy, mommy);
		
		sortSlotsByClassNumber(daddy.slots);
		sortSlotsByClassNumber(mommy.slots);
		
		Schedule boy = (Schedule) daddy.clone();
		Schedule girl = (Schedule) mommy.clone();
		
		for(int i = 0; i < boy.slots.size(); i++) {
			if (!boy.slots.get(i).fixed) {
				
				if (!conflictsSet.contains(boy.slots.get(i).getClassNumber())) {
					int duration = boy.slots.get(i).getDuration();
					
					if (Math.random() < 0.5) {
						for (int j = 0; j < duration-1; j++) {
							if (i+j == 10) {
								System.out.println("break");
							}
							boy.slots.set(i+j, mommy.slots.get(i+j));	
						}
					}
					i = i + duration-1;
				}
			}
		}
		
		for(int i = 0; i < girl.slots.size(); i++) {
			if (!girl.slots.get(i).fixed) {
				
				if(!conflictsSet.contains(girl.slots.get(i).getClassNumber())) {
					int duration = boy.slots.get(i).getDuration();
					
					if (Math.random() < 0.5) {
						for (int j = 0; j < duration-1; j++) {
							if (i+j == 10) {
								System.out.println("break");
							}
							girl.slots.set(i+j, daddy.slots.get(i+j));						
						}
					}		
					i = i + duration-1;
				}
			}
		}
		
		population.add(boy);
		population.add(girl);
	}
	
	public void sortSlotsByClassNumber(List<Slot> slots) {
		Collections.sort(slots, new Comparator<Slot>() {
			@Override
			public int compare(Slot o1, Slot o2) {
				int c;
				c = o1.classNumber.compareTo(o2.classNumber);
				return c;
			}
		});
	}
	
	public Set<Integer> conflicts(Schedule daddy, Schedule mommy) {
		Set<Integer> conflictsSet = new TreeSet<Integer>();
		
		for (Slot daddySlot : daddy.slots) {
			if (!daddySlot.fixed) {
				for (Slot mommySlot : mommy.slots) {
					if (mommySlot.day == daddySlot.day && mommySlot.hour == daddySlot.hour) {

						if (daddySlot.students.equals(mommySlot.students) || daddySlot.teacher.equals(mommySlot.teacher) || daddySlot.room.equals(mommySlot.room)) {
							conflictsSet.add(daddySlot.classNumber);
							conflictsSet.add(mommySlot.classNumber);
						}
					}
				}				
			}
		}
		
		return conflictsSet;
	}
	
	public Schedule optimize() throws CloneNotSupportedException {
		if (this.schedule.slots.size() > 0) {
		this.schedule.rates = new ArrayList<Double>();
		System.out.println("Init: 0");
		init(this.schedule);
		for (int i = 1; i < this.schedule.populationSize; i++) {
			System.out.println("Init: " + i);
			Schedule clone = (Schedule) schedule.clone();
			init(clone);
			population.add(clone);
		}

		for (Schedule s : population) {
			s.rate = rateSchedule(s);
		}
			
		sortByRate(population);
		
		for (int i = 0; i < this.schedule.iterations; i++) {
			
			System.out.println("i: " + i);
			
			int numberOfCrossovers = (int) (population.size()*0.1);
			
			for (int j = 0; j < numberOfCrossovers; j++) {
				double crossoverRandom = Math.random();
				
				if (crossoverRandom < this.schedule.crossoverProbability) {
					int mommyRandom = (int)(Math.random()*population.size()*0.1);
					int daddyRandom = (int)(Math.random()*population.size()*0.1);
					Schedule mommy = population.get(mommyRandom);
					Schedule daddy = population.get(daddyRandom);
					crossover(daddy, mommy);
				}
			}
					
			for (int j = 0; j < population.size(); j++) {
				double mutationRandom = Math.random();
				if (mutationRandom < this.schedule.mutationProbability) {					
					Schedule clone = (Schedule) population.get(j).clone();
					population.remove(j);
					mutation(clone);
					population.add(clone);
									
				}				
			}
			
			for (Schedule s : population) {
				s.rate = rateSchedule(s);
			}
					
			sortByRate(population);
			
			if (population.size() > this.schedule.populationSize) {
				for (int j = population.size()-1; j >= this.schedule.populationSize; j--) {
					population.remove(j);
				}
			}
			
			this.schedule.rates.add(population.get(0).rate);

		}
		System.out.println(population.size());
		return population.get(0);
		} else {
			return this.schedule;
		}
	}

	public void sortByRate(List<Schedule> p) {
		Collections.sort(p, new Comparator<Schedule>() {
			@Override
			public int compare(Schedule o1, Schedule o2) {
				return o1.rate.compareTo(o2.rate);
			}
		});
	}

	public boolean isFree(Integer day, Integer hour, Slot match, Resource room,
			Schedule s) {
		for (Slot slot : s.slots) {
			if (slot.day == day && slot.hour == hour) {
				if (match.students.equals(slot.students) || match.teacher.equals(slot.teacher) || room.equals(slot.room)) {
					return false;
				}
			}
		}
		return true;
	}

	public double rateSchedule(Schedule schedule) {
		this.schedule.rate = 0.0;
		
		for (Resource res : schedule.resources) {
			
			if (res.getType().equals("students") || res.getType().equals("teacher")) {

				for (int i = 0; i < schedule.days; i++) {
					List<Slot> day = new ArrayList<Slot>();
					
					for (Slot slot : schedule.slots) {
						if (slot.day == i && (res.equals(slot.students) || res.equals(slot.teacher))) {
							day.add(slot);
						}
					}

					this.schedule.rate = this.schedule.rate + rateDay(day);
				}
			}
		}
		return this.schedule.rate;
	}

	public double rateDay(List<Slot> day){
		
		Collections.sort(day, new Comparator<Slot>() {
			@Override
			public int compare(Slot o1, Slot o2) {
				int c = o1.hour.compareTo(o2.hour);
				return c;
			}
		});
		
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
		if (lessons == this.schedule.hours0) {
			return 0;
		} else if(lessons > 0 && lessons <= this.schedule.hoursA){
			return 1;
		} else if (lessons > this.schedule.hoursA && lessons <= this.schedule.hoursB) {
			return (this.schedule.hoursB - lessons) / (this.schedule.hoursB - this.schedule.hoursA);
		} else if (this.schedule.hoursB < lessons && lessons <= this.schedule.hoursC) {
			return 0;
		} else if (this.schedule.hoursC < lessons && lessons <= this.schedule.hoursD) {
			return (lessons - this.schedule.hoursC) / (this.schedule.hoursD - this.schedule.hoursC);
		} else {
			return 1;
		}
	}

	public double rateFreeSlots(int lessons) {
		return this.schedule.freeA*lessons + this.schedule.freeB;
	}
}
