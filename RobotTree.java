/**
 * The RobotTree class creates a binary tree of RobotNodes to be used to store Robot objects
 * in RadarBlasterSimulation. It has methods to add and remove Robots to the tree and retrieve
 * the parent node of a given node.
 *
 * @author Akshara Ganapathi, Mukund Ramachandran, Sanjeet Verma
 * Collaborators: None
 * Teacher Name: Ms. Bailey
 * Period: 03/05
 * Due Date: 05-19-22
 */

public class RobotTree {
    private RobotNode root;

    /**
     * Constructs a RobotTree
     */
    public RobotTree() {
        root = null;
    }

    /**
     * Returns the root of the tree
     *
     * @return root
     */
    public RobotNode getRoot() {
        return root;
    }

    /**
     * Returns the parent of the node
     *
     * @param node given node
     * @return parent node
     */
    public RobotNode getParent(RobotNode node) {
        return parentHelper(root, node);
    }

    /**
     * Adds node for given Robot to the tree
     *
     * @param r given Robot
     */
    public void add(Robot r) {
        root = recursiveAdd(r, root);
    }

    /**
     * Removes node for the given Robot from the tree
     *
     * @param r given Robot
     */
    public void remove(Robot r) {
        root = recursiveRemove(r, root);
    }

    /**
     * Recursive find parent helper
     *
     * @param curr current node
     * @param node node to find parent of
     * @return parent node
     */
    private RobotNode parentHelper(RobotNode curr, RobotNode node) {
        if (node == null || curr == null)
            return null;

        if (curr.getLeft().getValue().compareTo(node.getValue()) == 0
                || curr.getRight().getValue().compareTo(node.getValue()) == 0)
            return curr;

        if (curr.getValue().compareTo(node.getValue()) < 0)
            return parentHelper(curr.getRight(), node);
        else
            return parentHelper(curr.getLeft(), node);
    }

    /**
     * Recursive add helper
     *
     * @param r    Robot to be added
     * @param node current node being checked
     * @return next node to check
     */
    private RobotNode recursiveAdd(Robot r, RobotNode node) {
        if (node == null)
            node = new RobotNode(r);
        else {
            int diff = r.compareTo(node.getValue());
            if (diff <= 0) {
                node.setLeft(recursiveAdd(r, node.getLeft()));
            } else
                node.setRight(recursiveAdd(r, node.getRight()));
        }
        return node;
    }

    /**
     * Recursive remove helper
     *
     * @param r    Robot to be removed
     * @param node current node being checked
     * @return next node to check
     */
    private RobotNode recursiveRemove(Robot r, RobotNode node) {
        if (node == null)
            return null;
        int diff = r.compareTo(node.getValue());
        if (diff == 0)
            return removeHelper(node);
        else if (diff < 0)
            node.setLeft(recursiveRemove(r, node.getLeft()));
        else
            node.setRight(recursiveRemove(r, node.getRight()));
        return node;
    }

    /**
     * Helper method for removing node
     *
     * @param node node to be removed
     * @return new node now residing in its place
     */
    private RobotNode removeHelper(RobotNode node) {
        if (node.getLeft() == null && node.getRight() == null)
            return null;
        if (node.getLeft() == null)
            return node.getRight();
        if (node.getRight() == null)
            return node.getLeft();
        RobotNode parent = node;
        RobotNode child = node.getRight();
        while (child.getLeft() != null) {
            parent = child;
            child = child.getLeft();
        }
        node.setValue(child.getValue());
        if (parent == node)
            parent.setRight(child.getRight());
        else
            parent.setLeft(child.getRight());
        return node;
    }
}