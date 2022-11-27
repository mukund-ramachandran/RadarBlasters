/**
 * The Robot class creates a PVP robot that a player controls that has a certain name
 * health, speed, and color. The player moves this Robot around the frame and can control
 * when it shoots weapons.
 * @authors Akshara Ganapathi, Mukund Ramachandran, Sanjeet Verma
 * Collaborators: None
 * Teacher Name: Ms. Bailey
 * Period: 03/05
 * Due Date: 05-19-22
 */

import java.awt.*;

public class Robot implements Comparable<Robot>
{
    private static final int RIGHT_BOUND = 950;
    private static final int LEFT_BOUND = 40;
    private static final int UPPER_BOUND = 150;
    private static final int LOWER_BOUND = 720;
    private static final int SUBSTRING = 14;

    private final String name;
    private final int maxHealth;
    private int currentHealth;
    private final int speed;
    private final Color robotColor;
    private boolean freeze;
    private int x;
    private int y;
    private boolean canShoot;

    /**
     * Creates a robot object
     * @param name given name
     * @param maxHealth given health
     * @param speed given speed
     * @param color given Color
     */
    public Robot(String name, int maxHealth, int speed, Color color)
    {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.speed = speed;
        this.robotColor = color;
        this.freeze = true;
        this.x = 0;
        this.y = 0;
        canShoot = true;
    }

    /**
     * Reduces current health of robot
     * @param change amount change
     */
    public void subtractCurrentHealth(int change)
    {
        if((this.currentHealth - change) < 0)
            currentHealth = 0;
        else
            this.currentHealth -= change;
    }

    /**
     * Returns name of Robot
     * @return String name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns if Robot can shoot a weapon
     * @return boolean representation
     */
    public boolean canShoot()
    {
        return canShoot;
    }

    /**
     * Determines if Robot can shoot or not
     * @param canShoot boolean that determines
     */
    public void setCanShoot(boolean canShoot)
    {
        this.canShoot = canShoot;
    }

    /**
     * Returns current health
     * @return current health
     */
    public int getCurrentHealth()
    {
        return currentHealth;
    }

    /**
     * Returns Robot speed
     * @return Robot speed
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * Returns Color
     * @return Color object
     */
    public Color getRobotColor()
    {
        return robotColor;
    }

    /**
     * Returns if Robot can use freeze powerup
     * @return boolean representation
     */
    public boolean canUseFreeze() {
        return this.freeze;
    }

    /**
     * Determines if Robot can use freeze powerup
     * @param freeze boolean that determines
     */
    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    /**
     * Returns x-value of Robot
     * @return x-value
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Returns y-value of Robot
     * @return y-value
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Sets x-value and y-value of Robot
     * @param xNew new x-value
     * @param yNew new y-value
     */
    public void setLocation(int xNew, int yNew)
    {
        this.x = xNew;
        this.y = yNew;
    }

    /**
     * Changes x-value and y-value of Robot
     * @param xAmt x increment
     * @param yAmt y increment
     */
    public void changeLocation(int xAmt, int yAmt)
    {
        this.x += xAmt;
        if (x > RIGHT_BOUND)
            x = RIGHT_BOUND;
        else if (x < LEFT_BOUND)
            x = LEFT_BOUND;

        this.y += yAmt;
        if (y > LOWER_BOUND)
            y = LOWER_BOUND;
        else if (y < UPPER_BOUND)
            y = UPPER_BOUND;
    }

    /**
     * Displays the Robot in String format
     * @return String format
     */
    @Override
    public String toString()
    {
        String part1 = "Name: " + this.name;
        String part2 = ", Health: " + this.maxHealth;
        String part3 = ", Speed: " + this.speed;
        String part4 = ", Color: " + displayColor();
        return part1 + part2 + part3 + part4;
    }

    /**
     * Displays color of robot
     * @return String representation
     */
    public String displayColor()
    {
        return this.robotColor.toString().substring(SUBSTRING);
    }

    /**
     * Compares Robot to other Robots
     * @param other other Robot
     * @return whether its greater or not based on health
     */
    public int compareTo(Robot other)
    {
        return this.maxHealth - other.maxHealth;
    }

}