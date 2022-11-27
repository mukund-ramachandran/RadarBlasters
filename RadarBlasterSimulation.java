/**
 * The RadarBlasterSimulation class simulates the actual PVP Robot blasting game. It pulls
 * attributes from all the other classes and processes the data.
 * @authors Akshara Ganapathi, Mukund Ramachandran, Sanjeet Verma
 * Collaborators: None
 * Teacher Name: Ms. Bailey
 * Period: 03/05
 * Due Date: 05-19-22
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Queue;
import java.util.Timer;
import java.util.*;

public class RadarBlasterSimulation
{
    private static RobotTree robotTree;
    private static RobotNode root;
    private static Robot robot1;
    private static Robot robot2;
    private static JFrame gameFrame;
    private static JPanel selectionPanel;
    private static JPanel weaponPanel;

    private static JButton leftButton;
    private static JButton rightButton;
    private static JButton selectButton;
    private static JButton backButton;

    private static JLabel robotLabel;

    private static JLabel playerOneLabel;
    private static JLabel playerTwoLabel;

    private static TreeSet<Weapon> weaponBank;

    private static Queue<Weapon> weaponQueue1;
    private static Queue<Weapon> weaponQueue2;

    private static Set<Integer> pressedKeys;
    private static Set<Integer> secondPressedKeys;

    private static GraphicsComponent radarGraphics;

    private static final KeyListener firstRobotListener = new KeyListener()
    {
        /**
         * Invoked when a key has been typed.
         * See the class description for {@link KeyEvent} for a definition of
         * a key typed event.
         *
         * @param e the event to be processed
         */
        @Override
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Invoked when a key has been pressed.
         * See the class description for {@link KeyEvent} for a definition of
         * a key pressed event.
         *
         * @param e the event to be processed
         */
        @Override
        public void keyPressed(KeyEvent e)
        {
            pressedKeys.add(e.getKeyCode());
            for (Integer pressedKey : pressedKeys)
            {
                switch(pressedKey)
                {
                    case KeyEvent.VK_A:
                        radarGraphics.moveLeft(radarGraphics.getFirstRobot());
                        break;
                    case KeyEvent.VK_S:
                        radarGraphics.moveDown(radarGraphics.getFirstRobot());
                        break;
                    case KeyEvent.VK_W:
                        radarGraphics.moveUp(radarGraphics.getFirstRobot());
                        break;
                    case KeyEvent.VK_D:
                        radarGraphics.moveRight(radarGraphics.getFirstRobot());
                        break;
                    case KeyEvent.VK_Q:
                        if(robot1.canUseFreeze())
                        {
                            removeRobotListener(secondRobotListener, secondPressedKeys, robot1);
                            playerOneLabel.setText(displayStats(robot1,weaponQueue1, 1));
                        }
                        break;
                    case KeyEvent.VK_E:
                        if(robot1.canShoot())
                        {
                            robot1.setCanShoot(false);
                            TimerTask addShootBack = new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    robot1.setCanShoot(true);
                                }
                            };

                            Timer timer = new Timer();
                            timer.schedule(addShootBack, 5000);

                            radarGraphics.setFirst(true);
                            radarGraphics.setFirstPlayerWeapon(weaponQueue1.peek());
                            gameFrame.repaint();
                            TimerTask explosion = new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    radarGraphics.setFirst(false);
                                    gameFrame.repaint();
                                }
                            };

                            Timer explosionTimer = new Timer();
                            explosionTimer.schedule(explosion, 200);

                            assert weaponQueue1.peek() != null;
                            if(isInRange(robot1, robot2, weaponQueue1.peek()))
                            {
                                subtractHealth(robot2, weaponQueue1, secondRobotListener, secondPressedKeys);

                                if(robot2.getCurrentHealth() <= 0)
                                {
                                    gameFrame.setVisible(false);
                                    gameFrame = new JFrame("Radar Blasters");
                                    gameFrame.setSize(1000, 800);
                                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    gameFrame.setResizable(false);

                                    JPanel firstWinnerPanel = new JPanel( new GridLayout(2, 0));
                                    JLabel playerOneWins = new JLabel("Player 1 Wins! Press the button "
                                            + "to play again", SwingConstants.CENTER);
                                    setWinLabel(firstWinnerPanel, playerOneWins);
                                }

                                playerTwoLabel.setText(displayStats(robot2,weaponQueue2, 2));
                            }

                            weaponQueue1.offer(weaponQueue1.poll());
                            playerOneLabel.setText(displayStats(robot1,weaponQueue1, 1));
                            break;
                        }
                        break;
                }
            }
        }

        /**
         * Invoked when a key has been released.
         * See the class description for {@link KeyEvent} for a definition of
         * a key released event.
         *
         * @param e the event to be processed
         */
        public void keyReleased(KeyEvent e)
        {
            pressedKeys.remove(e.getKeyCode());
        }
    };

    private static void subtractHealth(Robot robot2, Queue<Weapon> weaponQueue1, KeyListener secondRobotListener, Set<Integer> secondPressedKeys) {
        assert weaponQueue1.peek() != null;
        robot2.subtractCurrentHealth(weaponQueue1.peek().getDamage());
        gameFrame.removeKeyListener(secondRobotListener);
        secondPressedKeys.clear();
        TimerTask addKeyListenerBack = new TimerTask()
        {
            @Override
            public void run()
            {
                gameFrame.addKeyListener(secondRobotListener);
            }
        };

        Timer t = new Timer();
        t.schedule(addKeyListenerBack, 200);
    }

    private static void removeRobotListener(KeyListener secondRobotListener, Set<Integer> secondPressedKeys, Robot robot1) {
        gameFrame.removeKeyListener(secondRobotListener);
        secondPressedKeys.clear();
        TimerTask addKeyListenerBack = new TimerTask()
        {
            @Override
            public void run()
            {
                gameFrame.addKeyListener(secondRobotListener);
            }
        };

        Timer timer = new Timer();
        timer.schedule(addKeyListenerBack, 5000);
        robot1.setFreeze(false);
    }

    private static final KeyListener secondRobotListener = new KeyListener()
    {
        /**
         * Invoked when a key has been typed.
         * See the class description for {@link KeyEvent} for a definition of
         * a key typed event.
         *
         * @param e the event to be processed
         */
        @Override
        public void keyTyped(KeyEvent e) {}

        /**
         * Invoked when a key has been pressed.
         * See the class description for {@link KeyEvent} for a definition of
         * a key pressed event.
         *
         * @param e the event to be processed
         */
        @Override
        public void keyPressed(KeyEvent e)
        {
            secondPressedKeys.add(e.getKeyCode());
            for (Integer pressedKey : secondPressedKeys)
            {
                switch(pressedKey)
                {
                    case KeyEvent.VK_LEFT:
                        radarGraphics.moveLeft(radarGraphics.getSecondRobot());
                        break;
                    case KeyEvent.VK_DOWN:
                        radarGraphics.moveDown(radarGraphics.getSecondRobot());
                        break;
                    case KeyEvent.VK_UP:
                        radarGraphics.moveUp(radarGraphics.getSecondRobot());
                        break;
                    case KeyEvent.VK_RIGHT:
                        radarGraphics.moveRight(radarGraphics.getSecondRobot());
                        break;
                    case KeyEvent.VK_ENTER:
                        if(robot2.canShoot())
                        {
                            robot2.setCanShoot(false);
                            TimerTask addShootBack = new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    robot2.setCanShoot(true);
                                }
                            };

                            Timer timer = new Timer();
                            timer.schedule(addShootBack, 5000);

                            radarGraphics.setSecond(true);
                            radarGraphics.setSecondPlayerWeapon(weaponQueue2.peek());
                            gameFrame.repaint();
                            TimerTask explosion = new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    radarGraphics.setSecond(false);
                                    gameFrame.repaint();
                                }
                            };

                            Timer explosionTimer = new Timer();
                            explosionTimer.schedule(explosion, 200);

                            assert weaponQueue2.peek() != null;
                            if(isInRange(robot2, robot1, weaponQueue2.peek()))
                            {
                                assert weaponQueue2.peek() != null;
                                subtractHealth(robot1, weaponQueue2, firstRobotListener, pressedKeys);

                                if(robot1.getCurrentHealth() <= 0)
                                {
                                    gameFrame.setVisible(false);
                                    gameFrame = new JFrame("Radar Blasters");
                                    gameFrame.setSize(1000, 800);
                                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    gameFrame.setResizable(false);

                                    JPanel secondWinnerPanel = new JPanel( new GridLayout(2, 0));
                                    JLabel playerTwoWins = new JLabel("Player 2 Wins! Press the button "
                                            + "to play again", SwingConstants.CENTER);
                                    setWinLabel(secondWinnerPanel, playerTwoWins);
                                    break;
                                }
                                playerOneLabel.setText(displayStats(robot1,weaponQueue1, 1));
                            }

                            weaponQueue2.offer(weaponQueue2.poll());
                            playerTwoLabel.setText(displayStats(robot2,weaponQueue2, 2));
                            break;
                        }
                        break;
                    case KeyEvent.VK_SHIFT:
                        if(radarGraphics.getSecondRobot().canUseFreeze())
                        {
                            removeRobotListener(firstRobotListener, pressedKeys, robot2);
                            playerTwoLabel.setText(displayStats(robot2,weaponQueue2, 2));
                        }
                        break;
                }
            }
        }

        /**
         * Invoked when a key has been released.
         * See the class description for {@link KeyEvent} for a definition of
         * a key released event.
         *
         * @param e the event to be processed
         */
        @Override
        public void keyReleased(KeyEvent e)
        {
            secondPressedKeys.remove(e.getKeyCode());
        }
    };

    private static void setWinLabel(JPanel secondWinnerPanel, JLabel playerTwoWins) {
        playerTwoWins.setFont(new Font("Arial", Font.BOLD, 12));
        secondWinnerPanel.add(playerTwoWins);

        JButton resetButton = new JButton("Start Over");
        resetButton.addActionListener(new ActionListener() {

            /**
             * Invoked when an action occurs.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startGame();
            }
        });

        JButton endButton = new JButton("End Game");
        endButton.addActionListener(new ActionListener() {

            /**
             * Invoked when an action occurs.
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gameFrame.setVisible(false);
                System.exit(0);
            }
        });

        secondWinnerPanel.add(resetButton);
        secondWinnerPanel.add(endButton);
        gameFrame.add(secondWinnerPanel);
        gameFrame.setVisible(true);
    }

    private static final ActionListener leftButtonListener = new ActionListener()
    {
        /**
         * Implements the given task when button is clicked
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (root.getLeft() != null)
            {
                root = root.getLeft();
                robotLabel.setText(root.getValue().toString());
            }
        }
    };

    private static final ActionListener rightButtonListener = new ActionListener()
    {
        /**
         * Implements the given task when button is clicked
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (root.getRight() != null)
            {
                root = root.getRight();
                robotLabel.setText(root.getValue().toString());
            }
        }
    };

    private static final ActionListener backListener = new ActionListener()
    {
        /**
         * Implements the given task when button is clicked
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (root != robotTree.getRoot())
            {
                root = robotTree.getParent(root);
                robotLabel.setText(root.getValue().toString());
            }
        }
    };

    private static final ActionListener selectButtonListener1 = new ActionListener()
    {
        /**
         * Implements the given task when button is clicked
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            selectRobot1();
        }
    };

    private static final ActionListener selectButtonListener2 = new ActionListener()
    {
        /**
         * Implements the given task when button is clicked
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            selectRobot2();
        }
    };

    private static final ActionListener moveOnListener = new ActionListener()
    {
        /**
         * Implements the given task when button is clicked
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            gameFrame.remove(selectionPanel);
            gameFrame.setVisible(true);
            displayWeapons();
        }
    };

    /**
     * Displays the statistics of each player
     * @param r given Robot
     * @param q given queue of Weapons
     * @param playerNum player number
     * @return the statistics in a String format
     */
    private static String displayStats(Robot r, Queue<Weapon> q, int playerNum)
    {
        return "<html>Player " + playerNum + ": <br>" +
                " Health: " + r.getCurrentHealth() + "<br>" +
                " Freeze available: " + r.canUseFreeze() + "<br>" +
                " Current weapon: " + (q.peek() != null ? q.peek().getName() : null) + "</html>";
    }

    /**
     * Fills the RobotTree with given Robots
     */
    private static void fillTree()
    {
        robotTree.add(new Robot("Tesla", 220, 25, Color.RED));
        robotTree.add(new Robot("Mooginator", 180, 35, Color.GREEN));
        robotTree.add(new Robot("ToyotaCamry", 300, 15, Color.MAGENTA));
        robotTree.add(new Robot("AxiBullet", 150, 40, Color.BLACK));
        robotTree.add(new Robot("JeetBlaster", 200, 32, Color.BLUE));
        robotTree.add(new Robot("PoolCannon", 250, 20, Color.ORANGE));
        robotTree.add(new Robot("RadarIncLimo", 400, 12, Color.LIGHT_GRAY));
    }

    /**
     * Fills the weapon bank with given Weapons
     */
    private static void fillWeapons()
    {
        weaponBank = new TreeSet<>();
        weaponBank.add(new Weapon("Nightbane", 100, 50));
        weaponBank.add(new Weapon("Vanquisher", 70, 60));
        weaponBank.add(new Weapon("Whisperwind", 50, 100));
        weaponBank.add(new Weapon("Scar", 40, 120));
        weaponBank.add(new Weapon("Harbinger", 30, 150));
        weaponBank.add(new Weapon("AX-50", 20, 200));
    }

    /**
     * Displays the buttons for the Weapons
     */
    private static void displayWeapons()
    {
        fillWeapons();
        weaponQueue1 = new LinkedList<>();
        weaponQueue2 = new LinkedList<>();
        JLabel turnIndicator = new JLabel("Make sure to take turns alternating. Player 1's turn",JLabel.CENTER);
        weaponPanel = new JPanel(new GridLayout(0,2));
        weaponPanel.add(turnIndicator);
        JButton start = new JButton("Start Game (only clickable when players have picked all weapons)");
        JLabel queue1 = new JLabel(weaponQueue1.toString(),JLabel.CENTER);
        JLabel queue2 = new JLabel(weaponQueue2.toString(),JLabel.CENTER);
        ActionListener startListener = e ->
        {
            if (weaponQueue1.size() + weaponQueue2.size() == weaponBank.size())
            {
                gameFrame.remove(weaponPanel);
                gameFrame.setVisible(true);
                playGame();
            }
        };

        start.addActionListener(startListener);
        weaponPanel.add(start);
        for (Weapon w : weaponBank)
        {
            JButton b1 = new JButton("Player 1 Choose " + w.getName());
            JButton b2 = new JButton("Player 2 Choose " + w.getName());
            weaponPanel.add(b1);
            weaponPanel.add(b2);
            ActionListener l1 = e -> {
                weaponQueue1.offer(w);
                weaponPanel.remove(b1);
                weaponPanel.remove(b2);
                queue1.setText(weaponQueue1.toString());
                turnIndicator.setText("Make sure to take turns alternating. Player 2's turn");
                gameFrame.setVisible(true);
            };

            ActionListener l2 = e -> {
                weaponQueue2.offer(w);
                weaponPanel.remove(b1);
                weaponPanel.remove(b2);
                queue2.setText(weaponQueue2.toString());
                turnIndicator.setText("Make sure to take turns alternating. Player 1's turn");
                gameFrame.setVisible(true);
            };

            b1.addActionListener(l1);
            b2.addActionListener(l2);
        }
        weaponPanel.add(queue1);
        weaponPanel.add(queue2);
        gameFrame.add(weaponPanel);
        gameFrame.setVisible(true);
    }

    /**
     * Provides the steps for what happens after the first select is clicked
     */
    private static void selectRobot1()
    {
        robot1 = root.getValue();
        robotTree.remove(robot1);
        root = robotTree.getRoot();
        selectButton.removeActionListener(selectButtonListener1);
        selectButton.addActionListener(selectButtonListener2);
        selectionPanel.add(new JLabel("Robot 1 Selected: " + robot1.getName(), JLabel.CENTER));
        robotLabel.setText(root.getValue().toString());
        gameFrame.setVisible(true);
    }

    /**
     * Provides the steps for what happens after the second select is clicked
     */
    private static void selectRobot2()
    {
        robot2 = root.getValue();
        robotTree.remove(robot2);
        root = robotTree.getRoot();
        selectionPanel.add(new JLabel("Robot 2 Selected: " + robot2.getName(), JLabel.CENTER));
        robotLabel.setText("");
        JButton moveOnToWeapons = new JButton("Move On");
        moveOnToWeapons.addActionListener(moveOnListener);
        selectionPanel.remove(leftButton);
        selectionPanel.remove(rightButton);
        selectionPanel.remove(selectButton);
        selectionPanel.remove(backButton);
        selectionPanel.add(moveOnToWeapons);
        gameFrame.setVisible(true);
    }

    /**
     * Provides the steps in the first frame for the users to select their 2 robots
     */
    private static void robotSelectionProcess()
    {
        robotTree = new RobotTree();
        fillTree();
        root = robotTree.getRoot();
        robotLabel = new JLabel(root.getValue().toString(), JLabel.CENTER);
        robotLabel.setFont(new Font("Arial", Font.BOLD,13));
        rightButton = new JButton("More health / Slower");
        leftButton = new JButton("Less health / Faster");
        selectButton = new JButton("Select robot");
        backButton = new JButton("Go Back");
        leftButton.addActionListener(leftButtonListener);
        rightButton.addActionListener(rightButtonListener);
        selectButton.addActionListener(selectButtonListener1);
        backButton.addActionListener(backListener);
        String text = "<html>Instructions: <br>" +
                "This is a two player game. Each player picks a robot and 5 weapons. Player 1<br>"+
                "will move the robot with WASD, Q to freeze robot2, and E to use weapon.<br>"+
                "Player 2 will move the robot with arrow keys, SHIFT to freeze robot1, and<br>"+
                "enter to use weapon. First, player 1 will select their robot followed by <br>"+
                "player 2. In the next screen, the players will alternate picking weapons,<br>"+
                "and then the game will begin. The player to get the opponent robot to 0<br>" +
                "health wins the game. Freeze powerup lasts 5 seconds, weapons can only be<br>"+
                "used once every 5 seconds, and when a player is using their weapon, the<br>"+
                "other robot is briefly paused</html>";
        selectionPanel = new JPanel(new GridLayout(0,2));
        selectionPanel.add(new JLabel(text, JLabel.CENTER));
        selectionPanel.add(robotLabel);
        selectionPanel.add(leftButton);
        selectionPanel.add(rightButton);
        selectionPanel.add(selectButton);
        selectionPanel.add(backButton);
        selectionPanel.setBackground(Color.LIGHT_GRAY);
        gameFrame.add(selectionPanel);
        gameFrame.setVisible(true);
    }

    /**
     * Determines if the victim is in range of the attacking robot's weapon
     * @param attacker attacking Robot
     * @param victim victim Robot
     * @param w the weapon being used
     * @return boolean representation
     */
    private static boolean isInRange(Robot attacker, Robot victim, Weapon w)
    {
        double squareX = Math.pow(victim.getX() - attacker.getX(), 2);
        double squareY = Math.pow(victim.getY() - attacker.getY(), 2);
        double distance = Math.sqrt(squareX + squareY);
        return w.getRange() >= distance;
    }

    /**
     * Starts the actual game after the Robots and Weapons are chosen
     */
    private static void playGame()
    {
        radarGraphics = new GraphicsComponent(robot1, robot2, weaponBank);
        gameFrame.setVisible(false);
        gameFrame = new JFrame("Radar Blasters");
        gameFrame.setSize(1000, 800);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        String firstLabel = null;
        if (weaponQueue1.peek() != null) {
            firstLabel = displayStats(robot1,weaponQueue1, 1);
        }

        String secondLabel = null;
        if (weaponQueue2.peek() != null) {
            secondLabel = displayStats(robot2,weaponQueue2, 2);
        }

        playerOneLabel = new JLabel(firstLabel);
        playerOneLabel.setFont(new Font("Arial", Font.BOLD, 12));
        Dimension size = playerOneLabel.getPreferredSize();
        playerOneLabel.setBounds(20, 20, 200, size.height);
        gameFrame.add(playerOneLabel);
        playerTwoLabel = new JLabel(secondLabel);
        playerTwoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        Dimension secondSize = playerTwoLabel.getPreferredSize();
        playerTwoLabel.setBounds(800, 20, 200, secondSize.height);
        gameFrame.add(playerTwoLabel);
        gameFrame.add(radarGraphics);
        pressedKeys = new HashSet<>();
        secondPressedKeys = new HashSet<>();
        gameFrame.addKeyListener(firstRobotListener);
        gameFrame.addKeyListener(secondRobotListener);
        gameFrame.setVisible(true);
    }

    /**
     * Starts the entire game beginning with the intro screen
     */
    private static void startGame()
    {
        gameFrame = new JFrame("Radar Blasters");
        gameFrame.setSize(1000, 800);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        robotSelectionProcess();
    }

    public static void main(String[] args)
    {
        startGame();
    }
}