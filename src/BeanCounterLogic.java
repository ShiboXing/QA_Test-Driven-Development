
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Code by @author Wonsun Ahn
 * 
 * <p>BeanCounterLogic: The bean counter, also known as a quincunx or the Galton
 * box, is a device for statistics experiments named after English scientist Sir
 * Francis Galton. It consists of an upright board with evenly spaced nails (or
 * pegs) in a triangular form. Each bean takes a random path and falls into a
 * slot.
 *
 * <p>Beans are dropped from the opening of the board. Every time a bean hits a
 * nail, it has a 50% chance of falling to the left or to the right. The piles
 * of beans are accumulated in the slots at the bottom of the board.
 * 
 * <p>This class implements the core logic of the machine. The MainPanel uses the
 * state inside BeanCounterLogic to display on the screen.
 * 
 * <p>Note that BeanCounterLogic uses a logical coordinate system to store the
 * positions of in-flight beans.For example, for a 4-slot machine: (0, 0) (1, 0)
 *                      (0, 0)
 *               (1, 0)        (1, 1)
 *        (2, 0)        (2, 1)        (2, 2)
 * [Slot0]       [Slot1]       [Slot2]      [Slot3]
 */

public class BeanCounterLogic {
	// TODO: Add member methods and variables as needed

	// No bean in that particular Y coordinate
	public static final int NO_BEAN_IN_YPOS = -1;
	
	private int _numOfSlots;
	private Bean[] _beans;
	private int _counter; //a pointer point to beans array that indicates the last bean released to the machine.
	
	private int[] _slots; //record the number of beans in each slot
	private int _beanCount;
	
	
	/**
	 * Constructor - creates the bean counter logic object that implements the core
	 * logic. Our bean counter should start with a single bean at the top.
	 * 
	 * @param slotCount the number of slots in the machine
	 */
	BeanCounterLogic(int slotCount) {
		// TODO: Implement
		_numOfSlots=slotCount;
		_counter=0;
		_slots=new int[_numOfSlots];
		_beanCount=0;
	}

	/**
	 * Returns the number of beans remaining that are waiting to get inserted.
	 * 
	 * @return number of beans remaining
	 */
	public int getRemainingBeanCount() {
		// TODO: Implement
		return Math.max(0, _beans.length-_counter);
	}

	/**
	 * Returns the x-coordinate for the in-flight bean at the provided y-coordinate.
	 * 
	 * @param yPos the y-coordinate in which to look for the in-flight bean
	 * @return the x-coordinate of the in-flight bean
	 */
	public int getInFlightBeanXPos(int yPos) {
		// TODO: Implement
		//assertTrue(yPos<_numOfSlots);

		if (_counter-yPos<0 || _counter-yPos>=_beans.length)
			return NO_BEAN_IN_YPOS;
		return _beans[_counter-yPos].getX();
	}

	/**
	 * Returns the number of beans in the ith slot.
	 * 
	 * @param i index of slot
	 * @return number of beans in slot
	 */
	public int getSlotBeanCount(int i) {
		// TODO: Implement
		return _slots[i];
	}

	/**
	 * Calculates the average slot bean count.
	 * 
	 * @return average of all slot bean counts
	 */
	public double getAverageSlotBeanCount() {
		// TODO: Implement
		double sum=0.0;
		for(int i=0;i<_slots.length;i++) sum+=i*_slots[i];
		return sum/_beanCount;
		
	}

	/**
	 * Removes the lower half of all beans currently in slots, keeping only the
	 * upper half.
	 */
	public void upperHalf() {
		// TODO: Implement
		for(int i=0;i<_slots.length/2;i++) 
			_slots[i]=0;
		
	}

	/**
	 * Removes the upper half of all beans currently in slots, keeping only the
	 * lower half.
	 */
	public void lowerHalf() { 
		// TODO: Implement
		for(int i=_slots.length/2;i<_slots.length;i++) 
			_slots[i]=0;
	}

	/**
	 * A hard reset. Initializes the machine with the passed beans. The machine
	 * starts with one bean at the top.
	 */
	public void reset(Bean[] beans) {
		// TODO: Implement
		_beans=beans;
		_counter=0;
		_beanCount=0;
	}

	/**
	 * Repeats the experiment by scooping up all beans in the slots and all beans
	 * in-flight and adding them into the pool of remaining beans. As in the
	 * beginning, the machine starts with one bean at the top.
	 */
	public void repeat() {
		// TODO: Implement
		for(int i=0;i<_slots.length;i++) _slots[i]=0;
		for(int i=0;i<_beans.length;i++)
		{ 
			_beans[i].reset();		
		}
		_counter=0;
		_beanCount=0;
	}

	/**
	 * Advances the machine one step. All the in-flight beans fall down one step to
	 * the next peg. A new bean is inserted into the top of the machine if there are
	 * beans remaining.
	 * 
	 * @return whether there has been any status change. If there is no change, that
	 *         means the machine is finished.
	 */
	public boolean advanceStep() {
		if(_counter-_numOfSlots<_beans.length){
			
			_counter++;
			_beanCount++;

			for(int i=Math.max(_counter-_numOfSlots,0);i<Math.min(_counter,_beans.length);i++){

				
				_beans[i].move();

				if(_beans[i].getY()==_numOfSlots-1) // the bean has entered a slot
					_slots[_beans[i].getX()]++;	
			}
			return true;
		}

		return false;
		
	}

	public static void showUsage() {
		System.out.println("Usage: java BeanCounterLogic <number of beans> <luck | skill>");
		System.out.println("Example: java BeanCounterLogic 400 luck");
	}
	
	public static boolean runGame(String[] args) {
		boolean luck;
		int beanCount = 0;
		int slotCount = 0;

		if (args.length == 1 && args[0].equals("test")) {
			// TODO: Verify the model checking passes for beanCount values 0-3 and slotCount
			// values 1-5 using the JPF Verify API.
			
			
			// Create the internal logic
			BeanCounterLogic logic = new BeanCounterLogic(slotCount);
			// Create the beans (in luck mode)
			Bean[] beans = new Bean[beanCount];
			for (int i = 0; i < beanCount; i++) {
				beans[i] = new Bean(true, new Random());
			}
			// Initialize the logic with the beans
			logic.reset(beans);

			while (true) {
				if (!logic.advanceStep()) {
					break;
				}

				// Checks invariant property: all positions of in-flight beans have to be
				// legal positions in the logical coordinate system.
				for (int yPos = 0; yPos < slotCount; yPos++) {
					int xPos = logic.getInFlightBeanXPos(yPos);
					assert xPos == BeanCounterLogic.NO_BEAN_IN_YPOS || (xPos >= 0 && xPos <= yPos);
				}

				// TODO: Check invariant property: the sum of remaining, in-flight, and in-slot
				// beans always have to be equal to beanCount
				
			}
			// TODO: Check invariant property: when the machine finishes,
			// 1. There should be no remaining beans.
			// 2. There should be no beans in-flight.
			// 3. The number of in-slot beans should be equal to beanCount.
			
			return true;
		}

		if (args.length != 2) {
			showUsage();
			return true;
		}

		try {
			beanCount = Integer.parseInt(args[0]);
		} catch (NumberFormatException ne) {
			showUsage();
			return true;
		}
		if (beanCount < 0) {
			showUsage();
			return true;
		}

		if (args[1].equals("luck")) {
			luck = true;
		} else if (args[1].equals("skill")) {
			luck = false;
		} else {
			showUsage();
			return true;
		}
		
		slotCount = 10;

		// Create the internal logic
		BeanCounterLogic logic = new BeanCounterLogic(slotCount);
		// Create the beans (in luck mode)
		Bean[] beans = new Bean[beanCount];
		for (int i = 0; i < beanCount; i++) {
			beans[i] = new Bean(luck, new Random());
		}
		// Initialize the logic with the beans
		logic.reset(beans);
					
		// Perform the experiment
		while (true) {
			if (!logic.advanceStep()) {
				break;
			}
		}
		// display experimental results
		System.out.println("Slot bean counts:");
		for (int i = 0; i < slotCount; i++) {
			System.out.print(logic.getSlotBeanCount(i) + " ");
		}
		System.out.println("");
		return true;
	}
	

	/**
	 * Auxiliary main method. Runs the machine in text mode with no bells and
	 * whistles. It simply shows the slot bean count at the end. Also, when the
	 * string "test" is passed to args[0], the program enters test mode. In test
	 * mode, the Java Pathfinder model checking tool checks the logic of the machine
	 * for a small number of beans and slots.
	 * 
	 * @param args args[0] is an integer bean count, args[1] is a string which is
	 *             either luck or skill.
	 */
	public static void main(String[] args) {
		runGame(args);
	}

}
