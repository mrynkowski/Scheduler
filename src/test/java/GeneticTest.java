import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import scheduler.models.Genetic;
import scheduler.models.Resource;
import scheduler.models.Schedule;
import scheduler.models.Slot;

public class GeneticTest {

	Schedule s;
	Genetic g;

	@Before
	public void setup(){
		s = new Schedule();
		s.setDays(5);
		s.setHours(10);
		g = new Genetic();
	}

	@Test
	public void testRateGivenLessons() {
		Assert.assertEquals(1, g.rateLessonsNumber(1), 0.1);
	}

	@Test
	public void testRateGivenFree() {
		Assert.assertEquals(0.5, g.rateFreeSlots(1), 0.1);
	}

	@Test
	public void testRateGivenWeek(){
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("A", "students"));
		resources.add(new Resource("Chang", "teacher"));
		resources.add(new Resource("Kasia", "teacher"));
		resources.add(new Resource("100", "room"));
		s.setResources(resources);
		List<Slot> slots = new ArrayList<Slot>();
		/*
		slots.add(new Slot(0, 1, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(0, 2, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(1, 3, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(1, 4, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(1, 5, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(1, 6, "A", "Chang", "100", "Chinese", 2));
		*/
		s.setSlots(slots);
		Assert.assertEquals(1, g.rateSchedule(s), 0.1);
	}
	
	/*
	@Test
	public void testRateGivenDayWith2SlotsNoFrees(){
		List<Slot> slots = new ArrayList<Slot>();
		slots.add(new Slot(2, 0, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(2, 1, "A", "Chang", "100", "Chinese", 2));
		Assert.assertEquals(1, g.rateDay(slots), 0.1);
	}
	
	@Test
	public void testRateGivenDay4SlotsNoFrees(){
		List<Slot> slots = new ArrayList<Slot>();
		slots.add(new Slot(2, 0, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(2, 1, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(2, 2, "A", "Chang", "100", "Chinese", 2));
		slots.add(new Slot(2, 3, "A", "Chang", "100", "Chinese", 2));
		Assert.assertEquals(0, g.rateDay(slots), 0.1);
	}
	*/
}
