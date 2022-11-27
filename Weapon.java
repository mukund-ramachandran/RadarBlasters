/**
 * The Weapon class creates a weapon that Robots use on one another.
 * The Weapon contains attributes such as name, damage, and range.
 * @authors Akshara Ganapathi, Mukund Ramachandran, Sanjeet Verma
 * Collaborators: None
 * Teacher Name: Ms. Bailey
 * Period: 03/05
 * Due Date: 05-19-22
 */

public class Weapon implements Comparable<Weapon>
{
    private final String name;
    private final int damage;
    private final int range;

    /**
     * Creates Weapon object
     * @param name given name
     * @param damage given damage
     * @param range given range
     */
    public Weapon(String name, int damage, int range)
    {
        this.name = name;
        this.damage = damage;
        this.range = range;
    }

    /**
     * Returns Weapon name
     * @return Weapon name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns Weapon damage
     * @return Weapon damage
     */
    public int getDamage()
    {
        return damage;
    }

    /**
     * Returns Weapon range
     * @return Weapon range
     */
    public int getRange()
    {
        return range;
    }

    /**
     * Compares Weapon to other Weapons
     * @param other other Weapon
     * @return whether it is greater based on damage
     */
    @Override
    public int compareTo(Weapon other) {
        return this.damage - other.damage;
    }

    /**
     * Displays String representation of Weapon
     * @return String representation
     */
    @Override
    public String toString()
    {
        return name;
    }
}