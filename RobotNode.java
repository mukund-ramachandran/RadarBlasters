/**
 * The RobotNode class creates a node representative of a Robot object
 * in order to be used in RobotTree.
 * @authors Akshara Ganapathi, Mukund Ramachandran, Sanjeet Verma
 * Collaborators: None
 * Teacher Name: Ms. Bailey
 * Period: 03/05
 * Due Date: 05-19-22
 */

public class RobotNode
{
    private Robot value;
    private RobotNode left;
    private RobotNode right;

    /**
     * Constructs a node with the given initValue and no children
     * @param initValue the value to store in node
     */
    public RobotNode(Robot initValue)
    {
        value = initValue;
        left = null;
        right = null;
    }

    /**
     * Returns the Robot object
     * @return Robot object
     */
    public Robot getValue()
    {
        return value;
    }

    /**
     * Returns the left child
     * @return left child node
     */
    public RobotNode getLeft()
    {
        return left;
    }

    /**
     * Returns the right child
     * @return right child node
     */
    public RobotNode getRight()
    {
        return right;
    }

    /**
     * Change the value in the node
     * @param theNewValue new value
     */
    public void setValue(Robot theNewValue)
    {
        value = theNewValue;
    }

    /**
     * Points the node to a different left node
     * @param theNewLeft new left node
     */
    public void setLeft(RobotNode theNewLeft)
    {
        left = theNewLeft;
    }

    /**
     * Points the node to a different right node
     * @param theNewRight new right node
     */
    public void setRight(RobotNode theNewRight)
    {
        right = theNewRight;
    }
}