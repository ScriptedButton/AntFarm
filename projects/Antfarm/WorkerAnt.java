/**
 * WorkerAnt.java  05/10/07
 *
 * @author - Jane Doe
 * @author - Period n
 * @author - Id nnnnnnn
 *
 * @author - I received help from ...
 *
 */

package projects.Antfarm;
// TO DO
// fix weird movement



import info.gridworld.actor.Actor;
import info.gridworld.actor.Critter;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * A <code>WorkerAnt</code> is a critter who's mission is
 * to take food from <code>Cake and Cookie</code> objects and to
 * deliver it to a <code>QueenAnt</code> object.
 * Initially it looks for food.  After it finds food, it looks for
 * a queen.
 * Worker ants share the location of food and the queen
 * with other ants they encounter.
 * Worker ants with food are red.  If they don't have food,
 * they are black.
 */
public class WorkerAnt extends Critter implements Processable
{
	/** Current amount of food being carried */
	private int foodQuantity;


	/** Location of a <code>Food</code> object */
	private Location foodLoc;

	/** Location of a <code>QueenAnt</code> object */
	private Location queenLoc;

    /**
     * Constructs a <code>WorkerAnt</code> critter.
     * It is originally black (no food) and
     * its direction is chosen randomly from the
     * eight normal cardinal directions.
     */
    public WorkerAnt()
    {
    	Random r = new Random();
		setColor(Color.BLACK);
		int direction = (int) (Math.random() * 8) * 45;
		System.out.println(direction);
		setDirection(direction);
		foodQuantity = 0;
		foodLoc = null;
		queenLoc = null;
    }

	/**
	 * Gives current food and queen locations to <code>ant</code>.
	 * @param ant the calling <code>WorkerAnt</code>
	 */
    public void process(WorkerAnt ant)
    {
		ant.shareFoodLocation(foodLoc);
		ant.shareQueenLocation(queenLoc);
    }

	/**
	 * Takes <code>fQty</code> amount of food from the
	 * calling <code>Food</code>.
	 * @param fQty the amount of food to take.
	 */
    public void takeFood(int fQty)
    {
		foodQuantity += fQty;
    }

	/**
	 * Gives food to the calling <code>QueenAnt</code>.
	 * @return the amound of food to give.
	 */
    public int giveFood()
    {
    	int currentFood = foodQuantity;
    	foodQuantity = 0;
		return currentFood;
    }

	/**
	 * Receives the <code>fLoc</code> food location from a
	 * <code>Food</code> object.  Saves this location if
	 * it doesn't already have one.
	 * @param fLoc the location of the food.
	 */
    public void shareFoodLocation(Location fLoc)
    {
    	if (foodLoc == null){
    		foodLoc = fLoc;
		}
    }

	/**
	 * Receives the <code>qLoc</code> queen location from a
	 * <code>QueenAnt</code> object.  Saves this location if
	 * it doesn't already have one.
	 * @param qLoc the location of the queen.
	 */
    public void shareQueenLocation(Location qLoc)
    {
		if (queenLoc == null){
			queenLoc = qLoc;
		}
    }

    /**
     * Processes each of the neighboring Ant Farm actors.
     * Implemented to get food from <code>Cake and Cookie</code> actors,
     * give food to <code>QueenAnt</code> actors, and to share locations
     * with other <code>WorkerAnt</code> actors.<br />
     * Precondition: All objects in <code>actors</code>
     * are contained in the same grid as this critter.
     * @param actors the actors to be processed
     */
    @Override
    public void processActors(ArrayList<Actor> actors)
    {
		for (Actor actor: actors){
			Processable processable = (Processable) actor;
			processable.process(this);
		}
    }

    /**
     * Gets the possible locations for the next move.
     * Implemented to return the empty neighboring locations
     * that are roughly in the direction of the current goal
     * (food or queen).  Calles getDesiredDirection to get the
     * direction to the goal.  Then it considers locations which
     * are in that direction or +- Location.HALF_RIGHT degrees.<br />
     * Postcondition: The locations must be valid in the grid
     * of this critter.
     * @return a list of possible locations for the next move
     */
    @Override
    public ArrayList<Location> getMoveLocations()
    {
    	Location l = getLocation();
		ArrayList<Location> adjacentLocations = new ArrayList<>();
		int generalDirection = getDesiredDirecton() + Location.HALF_LEFT;


		for (int i = 0; i < 3; i++){
			Location newLocation = getLocation().getAdjacentLocation(generalDirection);
			if (getGrid().isValid(newLocation) && getGrid().get(newLocation) == null){
				adjacentLocations.add(newLocation);
			}
			generalDirection += Location.HALF_RIGHT;
		}
		return adjacentLocations;
    }

    /**
     * Moves this critter to the given location, sets its direction,
     * and sets its color (red = has food, black = does not have food).
     * Implemented to call moveTo.<br />
     * Precondition: <code>loc</code> is valid in the grid of this critter
     * @param loc the location to move to (must be valid)
     */
    @Override
    public void makeMove(Location loc)
    {
		ArrayList<Location> locations = getMoveLocations();

		Location l = selectMoveLocation(locations);

		if (!l.equals(getLocation())){
			this.moveTo(l);
		}
		else{
			Random r = new Random();

			int i = r.nextInt(2);

			switch (i) {
				case 0:
					this.setDirection(getDirection() + Location.HALF_LEFT);
				case 1:
					this.setDirection(getDirection() + Location.HALF_RIGHT);
				default:
					break;
			}
		}

		if (this.foodQuantity > 0){
			this.setColor(Color.RED);
		}
		else{
			this.setColor(Color.BLACK);
		}
    }

	/**
	 * Returns the direction that the ant wants to go.
	 * @return the direction to the queen (if there is food
	 * and a queen's location is known); the direction to the
	 * food (if there is no food and a food's location is known);
	 * the current ant's direction otherwise.
	 */
    private int getDesiredDirecton()
    {
		if (queenLoc != null && foodQuantity != 0){
			return getLocation().getDirectionToward(queenLoc);
		}
		else if (foodLoc != null && foodQuantity == 0){
			return getLocation().getDirectionToward(foodLoc);
		}
		else{
			return getDirection();
		}
	}

    /**
     * Creates a string that describes this actor.
     * @return a string with the <code>Actor</code> information
     * plus the current amount of food and any known
     * <code>Food</code> and <code>QueenAnt</code> locations.
     */
    @Override
	public String toString()
	{
		return super.toString() + ",FQty=" + foodQuantity + ",FoodLoc=" + foodLoc + ",QueenLoc=" + queenLoc;
	}
}
