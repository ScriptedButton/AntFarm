/**
 * Food.java  05/10/07
 *
 * @author - Jane Doe
 * @author - Period n
 * @author - Id nnnnnnn
 *
 * @author - I received help from ...
 *
 */

package projects.Antfarm;


import info.gridworld.actor.Actor;

/**
 * <code>Food</code> is the top level abstract class for different
 * types of food.  A food object waits for a
 * <code>WorkerAnt</code> to get food from it.  It does not act.
 */
public abstract class Food extends Actor implements Processable
{
	/** The size of a bite */
	private final int BITE_SIZE;
	private int foodEaten;


	/** The total amount of food eaten */


    /**
     * Constructs a <code>Food</code> actor.  The image is
     * displayed for this actor without tinting.
     */
	public Food(int bite)
	{
		BITE_SIZE = bite;
		foodEaten = 0;
		setColor(null);
	}

	/**
	 * Gives food and current location to <code>ant</code>.
	 * @param ant the calling <code>WorkerAnt</code>
	 */
	public void process(WorkerAnt ant)
	{
		ant.takeFood(BITE_SIZE);
		ant.shareFoodLocation(getLocation());
	}

    /**
     * "Do nothing" act method.  This is provided for the Ant Farm
     * <code>Cake, Cookie, and QueenAnt</code> classes which do not act.
     */
    @Override
    public void act()
    {
    }

    /**
     * Creates a string that describes this actor.
     * @return a string with the <code>Actor</code> information
     * plus the amount of food eaten from this <code>Cake</code>
     */
    @Override
	public String toString()
	{
		return super.toString() + ", BSize=" + BITE_SIZE + ", FEaten=" + foodEaten;
	}
}
