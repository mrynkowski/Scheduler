package scheduler.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Genetic {

	public Schedule schedule;
	public List<Schedule> population;
	public int populationSize;
	public int iterations;
	public double rate;
	int days;
	int hours;
	
	public double hours0;
	public double hoursA;
	public double hoursB;
	public double hoursC;
	public double hoursD;
	
	public double freeA;
	public double freeB;
	
	public double crossoverProbability;
	public double mutationProbability;
	
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

	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public double getHours0() {
		return hours0;
	}

	public void setHours0(double hours0) {
		this.hours0 = hours0;
	}

	public double getHoursA() {
		return hoursA;
	}

	public void setHoursA(double hoursA) {
		this.hoursA = hoursA;
	}

	public double getHoursB() {
		return hoursB;
	}

	public void setHoursB(double hoursB) {
		this.hoursB = hoursB;
	}

	public double getHoursC() {
		return hoursC;
	}

	public void setHoursC(double hoursC) {
		this.hoursC = hoursC;
	}

	public double getHoursD() {
		return hoursD;
	}

	public void setHoursD(double hoursD) {
		this.hoursD = hoursD;
	}

	public double getFreeA() {
		return freeA;
	}

	public void setFreeA(double freeA) {
		this.freeA = freeA;
	}

	public double getFreeB() {
		return freeB;
	}

	public void setFreeB(double freeB) {
		this.freeB = freeB;
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

	public void init(Schedule schedule) {
		for (Slot slot : schedule.slots) {
			if (!slot.isFixed) {
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
			if (!slot.isFixed) {
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
			if (!slot.isFixed) {
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
		if (!removed.isFixed) {
			setDayAndHour(removed, mutatedSchedule);
		}
		
		return mutatedSchedule;
	}
	
	public void crossover(Schedule daddy, Schedule mommy) throws CloneNotSupportedException {
		
		//System.out.println("mommy: " + mommyIndex + " daddy: " + daddyIndex);

		/*
		System.out.println("mommy");
		for (Slot slot : mommy.slots) {
			System.out.println(slot);
		}
		System.out.println("daddy");
		for (Slot slot : daddy.slots) {
			System.out.println(slot);
		}
		*/
		Set<Integer> conflictsSet = conflicts(daddy, mommy);
		/*
		for (Integer conflict : conflictsSet) {
			System.out.println("Conflict:" + conflict);
		}
		*/
		
		sortSlotsByClassNumber(daddy.slots);
		sortSlotsByClassNumber(mommy.slots);
		
		Schedule boy = (Schedule) daddy.clone();
		Schedule girl = (Schedule) mommy.clone();
		
		for(int i = 0; i < boy.slots.size(); i++) {
			int classNumber = boy.slots.get(i).getClassNumber();
			if(!conflictsSet.contains(classNumber)) {
				boy.slots.set(i, mommy.slots.get(i));	
			}
		}
		
		for(int i = 0; i < girl.slots.size(); i++) {
			int classNumber = girl.slots.get(i).getClassNumber();
			if(!conflictsSet.contains(classNumber)) {
				girl.slots.set(i, daddy.slots.get(i));
			}
		}
	
		/*
		System.out.println("girl");
		for (Slot slot : girl.slots) {
			System.out.println(slot);
		}
		System.out.println("boy");
		for (Slot slot : boy.slots) {
			System.out.println(slot);
		}
		System.out.println("Stop");
		*/
		
		population.add(0, boy);
		population.add(0, girl);
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
			for (Slot mommySlot : mommy.slots) {
				if (mommySlot.day == daddySlot.day && mommySlot.hour == daddySlot.hour) {
					if (daddySlot.students.equals(mommySlot.students) || daddySlot.teacher.equals(mommySlot.teacher) || daddySlot.room.equals(mommySlot.room)) {
						conflictsSet.add(daddySlot.classNumber);
						conflictsSet.add(mommySlot.classNumber);
					}
				}
			}
		}
		
		return conflictsSet;
	}
	
	public Schedule optimize() throws CloneNotSupportedException {
		rates = new ArrayList<Double>();
		init(this.schedule);
		for (int i = 1; i < populationSize; i++) {
			Schedule clone = (Schedule) schedule.clone();
			init(clone);
			population.add(clone);
		}

		for (int i = 0; i < iterations; i++) {
			/*
			for (Schedule schedule : population) {
				double mutationRandom = Math.random();
				if (mutationRandom < mutationProbability) {
					schedule = mutation(schedule);
				}
			}
			*/
			double crossoverRandom = Math.random();
			if (crossoverRandom < crossoverProbability) {
				int mommyIndex = (int) (Math.random() * population.size());
				int daddyIndex = (int) (Math.random() * population.size());
				Schedule mommy = population.get(mommyIndex);
				Schedule daddy = population.get(daddyIndex);
				crossover(daddy, mommy);
				population.remove(population.size() - 1);
				population.remove(population.size() - 1);
			}
			
			//Schedule clone = (Schedule) population.get(0).clone();
			//population.add(clone);
					
			for (int j = 0; j < population.size(); j++) {
				double mutationRandom = Math.random();
				if (mutationRandom < mutationProbability) {					
					Schedule clone = (Schedule) population.get(j).clone();
					population.remove(j);
					mutation(clone);
					population.add(0, clone);
									
				}				
			}
			
			population.remove(population.size() - 1);
			Schedule clone = (Schedule) population.get(0).clone();
			population.add(clone);
			
			/*
			double mutationRandom = Math.random();
			if (mutationRandom < mutationProbability) {
				//Schedule mutant = population.get((int) (Math.random() * population.size()));
				//mutation(mutant);
				
				Schedule clone = (Schedule) population.get((int) (Math.random() * population.size())).clone();
				mutation(clone);
				population.add(0, clone);
				population.remove(population.size() - 1);
				
			}
			*/
			
			for (Schedule s : population) {
				s.rate = rateSchedule(s);
			}
			
			sortByRate(population);
			rates.add(population.get(0).rate);

		}
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
		rate = 0;
		for (Resource res : schedule.resources) {
			
			if (res.getType().equals("students") || res.getType().equals("teacher")) {

				for (int i = 0; i < schedule.days; i++) {
					List<Slot> day = new ArrayList<Slot>();
					
					for (Slot slot : schedule.slots) {
						if (slot.day == i && (res.equals(slot.students) || res.equals(slot.teacher))) {
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

		if (lessons == hours0) {
			return 0;
		} else if(lessons > 0 && lessons <= this.hoursA){
			return 1;
		} else if (lessons > this.hoursA && lessons <= this.hoursB) {
			return (this.hoursB - lessons) / (this.hoursB - this.hoursA);
		} else if (this.hoursB < lessons && lessons <= this.hoursC) {
			return 0;
		} else if (this.hoursC < lessons && lessons <= this.hoursD) {
			return (lessons - this.hoursC) / (this.hoursD - this.hoursC);
		} else {
			return 1;
		}
	}

	public double rateFreeSlots(int lessons) {
		return this.freeA*lessons + this.freeB;
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
