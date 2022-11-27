/**
 * The GraphicsComponent class creates the visuals for the Robots on the frame as
 * they move around and creates the animations for each Weapon.
 *
 * @author Akshara Ganapathi, Mukund Ramachandran, Sanjeet Verma
 * Collaborators: None
 * Teacher Name: Ms. Bailey
 * Period: 03/05
 * Due Date: 05-19-22
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphicsComponent extends JComponent {
    private static final int STARTX_1 = 50;
    private static final int STARTY = 350;
    private static final int STARTX_2 = 890;
    private static final int BIG_OFFSET = 30;
    private static final int SMALL_OFFSET = 10;
    private static final int BIG_SIDE = 60;
    private static final int SMALL_SIDE = 20;
    private static final double MAX_DMG = 100.0;
    private static final double MULT = 0.4;
    private static final float COLOR_NUM = 0.9f;

    private final Robot robot1;
    private final Robot robot2;
    private final Rectangle rect1x;
    private final Rectangle rect1y;
    private final Rectangle rect2x;
    private final Rectangle rect2y;
    private final TreeMap<Weapon, Color> bulletColors;
    private Weapon firstPlayerWeapon;
    private Weapon secondPlayerWeapon;
    private boolean first;
    private boolean second;

    /**
     * Constructs a GraphicsComponent object
     * @param robot1 the first PVP robot
     * @param robot2 the second PVP robot
     * @param weaponBank TreeSet of all available weapons
     */
    public GraphicsComponent(Robot robot1, Robot robot2, TreeSet<Weapon> weaponBank) {
        this.robot1 = robot1;
        this.robot2 = robot2;
        robot1.setLocation(STARTX_1, STARTY);
        robot2.setLocation(STARTX_2, STARTY);
        rect1x = new Rectangle();
        rect1y = new Rectangle();
        rect2x = new Rectangle();
        rect2y = new Rectangle();
        setRectangles();
        bulletColors = createWeaponColors(weaponBank);
    }

    /**
     * Uses Graphics2D to paint the robots and weapon on the frame
     * @param gr the Graphics object
     */
    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        setRectangles();
        g.setColor(robot1.getRobotColor());
        g.fill(rect1x);
        g.fill(rect1y);
        g.setColor(robot2.getRobotColor());
        g.fill(rect2x);
        g.fill(rect2y);
        if (first) {
            g.setColor(getFirstWeaponColor());
            Ellipse2D.Double firstPlayerExplosion = new Ellipse2D.Double(robot1.getX() - firstPlayerWeapon.getRange(), robot1.getY() - firstPlayerWeapon.getRange(),
                    firstPlayerWeapon.getRange() * 2, firstPlayerWeapon.getRange() * 2);
            g.fill(firstPlayerExplosion);
        }
        if (second) {
            g.setColor(getSecondWeaponColor());
            Ellipse2D.Double secondPlayerExplosion = new Ellipse2D.Double(robot2.getX() - secondPlayerWeapon.getRange(), robot2.getY() - secondPlayerWeapon.getRange(),
                    secondPlayerWeapon.getRange() * 2, secondPlayerWeapon.getRange() * 2);
            g.fill(secondPlayerExplosion);
        }
    }

    /**
     * Sets the bounds of the rectangles that create the visual for the Robot
     */
    public void setRectangles() {
        rect1x.setBounds(robot1.getX() - BIG_OFFSET, robot1.getY() - SMALL_OFFSET, BIG_SIDE, SMALL_SIDE);
        rect1y.setBounds(robot1.getX() - SMALL_OFFSET, robot1.getY() - BIG_OFFSET, SMALL_SIDE, BIG_SIDE);
        rect2x.setBounds(robot2.getX() - BIG_OFFSET, robot2.getY() - SMALL_OFFSET, BIG_SIDE, SMALL_SIDE);
        rect2y.setBounds(robot2.getX() - SMALL_OFFSET, robot2.getY() - BIG_OFFSET, SMALL_SIDE, BIG_SIDE);
    }

    /**
     * Moves the Robot to the left by the given speed
     * @param r given Robot
     */
    public void moveLeft(Robot r) {
        r.changeLocation((-1) * r.getSpeed(), 0);
        repaint();
    }

    /**
     * Moves the Robot to the right by the given speed
     * @param r given Robot
     */
    public void moveRight(Robot r) {
        r.changeLocation(r.getSpeed(), 0);
        repaint();
    }

    /**
     * Moves the Robot up by the given speed
     * @param r given Robot
     */
    public void moveUp(Robot r) {
        r.changeLocation(0, (-1) * r.getSpeed());
        repaint();
    }

    /**
     * Moves the Robot down by the given speed
     * @param r given Robot
     */
    public void moveDown(Robot r) {
        r.changeLocation(0, r.getSpeed());
        repaint();
    }

    /**
     * Creates a TreeMap mapping Weapon objects to their respective Color for the animation
     * @param bank TreeSet of Weapons
     * @return TreeMap mapping
     */
    public TreeMap<Weapon, Color> createWeaponColors(TreeSet<Weapon> bank) {
        TreeMap<Weapon, Color> mapping = new TreeMap<>();
        for (Weapon w : bank) {
            int dmg = w.getDamage();
            double proportion = (((double) dmg) / MAX_DMG) * MULT;
            Color c = Color.getHSBColor((float) proportion, COLOR_NUM, COLOR_NUM);
            mapping.put(w, c);
        }
        return mapping;
    }

    /**
     * Returns color of current Weapon player 1 is using
     * @return Color
     */
    public Color getFirstWeaponColor() {
        return bulletColors.get(firstPlayerWeapon);
    }

    /**
     * Returns color of current Weapon player 2 is using
     * @return Color
     */
    public Color getSecondWeaponColor() {
        return bulletColors.get(secondPlayerWeapon);
    }

    /**
     * Sets current weapon that first player is using
     * @param w given Weapon
     */
    public void setFirstPlayerWeapon(Weapon w) {
        firstPlayerWeapon = w;
    }

    /**
     * Sets current weapon that second player is using
     * @param w given Weapon
     */
    public void setSecondPlayerWeapon(Weapon w) {
        secondPlayerWeapon = w;
    }

    /**
     * Sets the boolean for first explosion
     * @param first boolean
     */
    public void setFirst(boolean first) {
        this.first = first;
    }

    /**
     * Sets the boolean for the second explosion
     * @param second boolean
     */
    public void setSecond(boolean second) {
        this.second = second;
    }

    /**
     * Returns the first PVP robot
     * @return first PVP robot
     */
    public Robot getFirstRobot() {
        return robot1;
    }

    /**
     * Returns the second PVP robot
     * @return second PVP robot
     */
    public Robot getSecondRobot() {
        return robot2;
    }
}