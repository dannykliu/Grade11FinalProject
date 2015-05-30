/*
 * Danny Liu
 * Final Project
 * ICS3U-1
 * Mr. Lim
 * May 23rd
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener {

    //Creates image variables 
    BufferedImage imgBackground;
    BufferedImage imgMainCharacter;
    BufferedImage imgPokeball;
    BufferedImage imgExtraBalls;
    BufferedImage imgEnemy;
    BufferedImage imgFinishScreen;
    BufferedImage imgMenuBackground;
    BufferedImage imgExplosion;
    BufferedImage imgCrosshair;
    BufferedImage imgSpacebar;
    BufferedImage imgWasd;
    //Set the beginning coordinates of my animating end screen
    int imgFinishScreenX = 600;
    int imgFinishScreenY = 0;
    //Begins coordinate of x-value of finish screen at 0
    int imgBackgroundX = 0;
    //Starts main character coordinates in the middle of the screen 
    int imgMainCharacterX = 400;
    int imgMainCharacterY = 300;
    //Variables for key actions are set as false first
    boolean MousePressed = false;
    boolean upPressed = false;
    boolean downPressed = false;
    boolean rightPressed = false;
    boolean leftPressed = false;
    //Variables for the pokeballs shot as well as the powerup extra balls
    boolean imgPokeballActive = false;
    boolean imgExtraBallsActive = false;
    //Tracks coordinates of pokeball that's shot
    double imgPokeballX = 0;
    double imgPokeballY = 0;
    //Begin the coordinates of the extra balls at a random coordinate 
    int extraBallsX = (int) (650 * Math.random() + 100);
    int extraBallsY = (int) (450 * Math.random() + 100);
    //Tracks how many pokeballs you have left to throw.
    //Begins with 3
    int PokeballCounter = 3;
    //Very useful counter that tracks the rate at which enemies spawn
    long counter = 0;
    //Total array of 100 enemies
    boolean[] EnemyVisibility = new boolean[100];
    boolean[] enemyIsCaught = new boolean[100];
    int[] EnemyX = new int[100];
    int[] EnemyY = new int[100];
    //Track if the enemy has been hit
    boolean[] enemyHasHit = new boolean[100];
    //Keep track of which enemy we are spawning 
    boolean[] ExplosionVisibility = new boolean[100];
    int EnemyNumber = 0;
    //Begin with 10 health points
    int health = 10;
    double distanceOfEnemy;
    //We are either on the menu screen, playing, or the gameOver screen
    boolean menu = true;
    boolean playing = false;
    boolean gameOver = false;
    int score = 0;
    //Tracks direction vectors of Pokeball
    double xVelocity = 0;
    double yVelocity = 0;
    //Variable of uncertainty is normally distributed with mean = 0
    int uncertainty = 0;
    int imgCrosshairX;
    int imgCrosshairY;

    GamePanel() throws IOException {
        //Load all images
        URL imgURL = getClass().getResource("TreeBackground.png");
        imgBackground = ImageIO.read(imgURL);
        imgURL = getClass().getResource("PkmnTrainer.png");
        imgMainCharacter = ImageIO.read(imgURL);
        imgURL = getClass().getResource("pokeball.png");
        imgPokeball = ImageIO.read(imgURL);
        imgURL = getClass().getResource("pokeball.png");
        imgExtraBalls = ImageIO.read(imgURL);
        imgURL = getClass().getResource("Charizard.png");
        imgEnemy = ImageIO.read(imgURL);
        imgURL = getClass().getResource("Wave.png");
        imgFinishScreen = ImageIO.read(imgURL);
        imgURL = getClass().getResource("PokemonBackground.png");
        imgMenuBackground = ImageIO.read(imgURL);
        imgURL = getClass().getResource("explosion.png");
        imgExplosion = ImageIO.read(imgURL);
        imgURL = getClass().getResource("Crosshair.png");
        imgCrosshair = ImageIO.read(imgURL);
        imgURL = getClass().getResource("spacebar.png");
        imgSpacebar = ImageIO.read(imgURL);
        imgURL = getClass().getResource("wasd.png");
        imgWasd = ImageIO.read(imgURL);


        //Enable listeners
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        //We are in start menu
        if (menu) {
            //Fixing ideal locations of keys
            g.drawImage(imgMenuBackground, 0, 0, this);
            g.drawImage(imgSpacebar, 535, 360, this);
            g.drawImage(imgWasd, 535, 260, this);

            //Fixing ideal locations for instructions and introduction
            g.drawString("Press space to start", 600, 385);
            Font myFont = new Font("Comic Sans MS", Font.BOLD, 24);
            g.setFont(myFont);
            g.drawString("Movement keys", 535, 250);
            g.setColor(Color.CYAN);
            g.drawString("Click to shoot", 535, 220);
            g.setColor(Color.PINK);
            myFont = new Font("Arial", Font.CENTER_BASELINE, 36);
            g.setFont(myFont);
            g.drawString("Instructions:", 535, 190);
            myFont = new Font("Comic Sans MS", Font.BOLD, 18);
            g.setFont(myFont);
            g.setColor(Color.RED);
            g.drawString("Introduction:", 40, 376);
            g.setColor(Color.WHITE);
            g.drawString("You're one of the last Pokemon trainers in the world,", 40, 400);
            g.drawString("and Charizard has mega-evolved into an evil Pokemon.", 40, 424);
            g.drawString("Take down as many with your pokeballs as possible.", 40, 448);
            g.drawString("Good luck, you'll need it!", 40, 472);

        }
        //When health becomes 0, switch to game over mode
        if (gameOver) {
            g.drawImage(imgFinishScreen, imgFinishScreenX, 0, this);
            //Paint score and gameover signs when the background is done animating
            if (imgFinishScreenX < 5) {
                g.setColor(Color.RED);
                Font myFont = new Font("Comic Sans MS", Font.BOLD, 72);
                g.setFont(myFont);
                g.drawString("GAME OVER", 100, 100);
                g.setColor(Color.YELLOW);
                g.drawString("Score: " + score/2, 300, 400);

                //Instructions to restart game
                g.setColor(Color.GREEN);
                myFont = new Font("Comic Sans MS", Font.BOLD, 30);
                g.setFont(myFont);
                g.drawString("Press F to Restart!", 410, 500);
            }


            //When health is not 0 and you are not in the start screen, this code will be run
        } else if (playing) {
            //Drawing of background, main character, pokeballs, extra poke balls, and enemies
            g.drawImage(imgBackground, imgBackgroundX, 0, this);
            g.drawImage(imgBackground, imgBackgroundX + imgBackground.getWidth(), 0, this);

            g.drawImage(imgMainCharacter, imgMainCharacterX - imgMainCharacter.getWidth() / 2, imgMainCharacterY
                    - imgMainCharacter.getHeight() / 2, this);
            if (imgExtraBallsActive) {
                g.drawImage(imgExtraBalls, extraBallsX,
                        extraBallsY, this);
                //draw the extra ball anywhere randomly 
            }
            if (imgPokeballActive) {
                g.drawImage(imgPokeball, (int) imgPokeballX - imgPokeball.getWidth() / 2,
                        (int) imgPokeballY - imgPokeball.getHeight() / 2, this);
            }

            //Colour is contingent upon amount of life left
            if (health > 3) {
                g.setColor(Color.GREEN);
            } else if (health <= 3) {
                g.setColor(Color.RED);
            }
            //Draw Health Bar above main character
            g.fillRect(imgMainCharacterX - imgMainCharacter.getWidth() / 3,
                    imgMainCharacterY - imgMainCharacter.getHeight() / 2 - 8, 8 * health, 8);

            //Print out how many pokeballs the player has left
            g.setColor(Color.BLUE);
            Font myFont = new Font("Comic Sans MS", Font.BOLD, 30);
            g.setFont(myFont);
            g.drawString("Pokeballs Left: " + PokeballCounter, 20, 40);
            //Print out score
            g.setColor(Color.PINK);
            g.drawString("Score: " + score, 20, 80);

            //Print out different levels based on score in the top right corner
            if (score >= 100 && score < 400) {
                g.setColor(Color.MAGENTA);
                g.setFont(myFont);
                g.drawString("Level 2", 600, 50);
            } else if (score >= 400 && score < 800) {
                g.setColor(Color.MAGENTA);
                g.setFont(myFont);
                g.drawString("Level 3", 600, 50);
            } else if (score >= 800) {
                g.setColor(Color.MAGENTA);
                g.setFont(myFont);
                g.drawString("Final Level", 600, 50);
            }

            for (int i = 0; i < 100; i++) {
                //Draw up to 100 enemies at its own center
                if (EnemyVisibility[i]) {
                    g.drawImage(imgEnemy, EnemyX[i] - imgEnemy.getWidth() / 2,
                            EnemyY[i] - imgEnemy.getHeight() / 2, this);
                }
                //Draw explosion upon collision
                if (ExplosionVisibility[i]) {
                    //Set so that when you restart, no explosion will be painted
                    if (!(health == 10)) {
                        g.drawImage(imgExplosion, (imgMainCharacterX + EnemyX[i]) / 2,
                                (imgMainCharacterY + EnemyY[i]) / 2, this);
                    }
                    //After 1/4 of a second, stop drawing the explosion
                    if (counter % 15 == 0) {
                        ExplosionVisibility[i] = false;
                    }
                }

                //Draw crosshair at cursor coordinates
                g.drawImage(imgCrosshair, imgCrosshairX - imgCrosshair.getWidth() / 2,
                        imgCrosshairY - imgCrosshair.getHeight() / 2, this);
            }
        }
    }
//-------------------------------------------------------------------------------------------------
    //END OF PAINT COMPONENT

    public void run() throws InterruptedException {
        //Following code runs 60 times per second
        while (true) {
            if (menu) {
                //Nothing is ran in the menu
                //If statement is here because it makes it easier for me to think logically
            } else if (!gameOver) {
                //if health is not 0, run the code that plays the game
                playing = true;
            }
            //When game has finished, keep animating finish screen
            if (gameOver) {
                if (imgFinishScreenX > 0) {
                    imgFinishScreenX -= 10;
                    repaint();
                    Thread.sleep(17);
                }


            } else if (playing) {
                //Generate new uncertainty 60 times a second
                //Uncertainty variable used to randomnize location of enemy y-coordinate
                uncertainty = (int) (200 * Math.random() - 100);
                //Scroll background leftwards
                imgBackgroundX -= 1 + score / 1000.0;
                if (imgBackgroundX <= -imgBackground.getWidth()) {
                    imgBackgroundX = 0;
                }

                //Moves coordinates of main character
                //As score increases, so does your rate of movement
                if (upPressed) {
                    imgMainCharacterY -= 5 + score / 250.0;
                }

                if (downPressed) {
                    imgMainCharacterY += 5 + score / 250.0;
                }

                if (rightPressed) {
                    imgMainCharacterX += 5 + score / 250.0;
                }

                if (leftPressed) {
                    imgMainCharacterX -= 5 + score / 250.0;
                }

                //Animate pokeball
                if (imgPokeballActive) {
                    imgPokeballX += xVelocity;
                    imgPokeballY += yVelocity;
                }

                //Deactivate pokeball when it leaves the screen
                if (imgPokeballX > 800 || imgPokeballY > 600 || imgPokeballX < 0 || imgPokeballY < 0) {
                    imgPokeballActive = false;
                }
                //Generate extra pokeballs every 8.3 seconds
                if (counter % 500 == 0) {
                    imgExtraBallsActive = true;
                }

                //Collision of main character and extra pokeballs
                if (imgExtraBallsActive && Math.sqrt(Math.pow((imgMainCharacterX - extraBallsX), 2) + Math.pow((imgMainCharacterY - extraBallsY), 2))
                        < 50) {
                    imgExtraBallsActive = false;
                    //Increments counter by 3
                    PokeballCounter += 3;
                    //Restricts maximum amount of Pokeballs to 10
                    //Prevents hoarding at the start 
                    if (PokeballCounter > 10) {
                        PokeballCounter = 10;
                    }
                    //Increments score by 20 
                    score += 20;

                    //Reset the random values for the X and Y coordinates 
                    extraBallsX = (int) (650 * Math.random() + 100);
                    extraBallsY = (int) (450 * Math.random() + 100);

                }
                //----------------------------------------------------------------------------------------------------
                //Speed of enemy spawning in Level 1
                if (score < 100) {
                    if (counter % 120 == 0) {
                        EnemyVisibility[EnemyNumber] = true;
                        EnemyY[EnemyNumber] = imgMainCharacterY;
                        EnemyX[EnemyNumber] = 850;
                        enemyHasHit[EnemyNumber] = false;
                        EnemyNumber++;
                        if (EnemyNumber >= 100) {
                            EnemyNumber = 0;
                        }
                    }
                } //Speed of enemy spawning on level 2
                else if (score >= 100 && score < 400) {
                    if (counter % 80 == 0) {
                        EnemyVisibility[EnemyNumber] = true;
                        //Adds a bit of uncertainty as to where the enemy is spawning
                        EnemyY[EnemyNumber] = imgMainCharacterY + uncertainty;
                        //Following if statements fix cases where enemies spawn half-off the screen
                        if (EnemyY[EnemyNumber] > 525) {
                            EnemyY[EnemyNumber] = 525;
                        } else if (EnemyY[EnemyNumber] < 75) {
                            EnemyY[EnemyNumber] = 75;
                        }
                        //Begin spawning enemy at this x-coordinate
                        EnemyX[EnemyNumber] = 850;
                        enemyHasHit[EnemyNumber] = false;
                        EnemyNumber++;
                        if (EnemyNumber >= 100) {
                            EnemyNumber = 0;
                        }
                    }
                } else if (score >= 400 && score < 800) {
                    //Following code is duplicated, with the only change being a quicker spawn rate
                    if (counter % 45 == 0) {
                        EnemyVisibility[EnemyNumber] = true;
                        EnemyY[EnemyNumber] = imgMainCharacterY + uncertainty;
                        if (EnemyY[EnemyNumber] > 525) {
                            EnemyY[EnemyNumber] = 525;
                        } else if (EnemyY[EnemyNumber] < 75) {
                            EnemyY[EnemyNumber] = 75;
                        }
                        EnemyX[EnemyNumber] = 850;
                        enemyHasHit[EnemyNumber] = false;
                        EnemyNumber++;
                        if (EnemyNumber >= 100) {
                            EnemyNumber = 0;
                        }
                    }
                } else {
                    //Following code is duplicated, with the only change being a quicker spawn rate
                    if (counter % 25 == 0) {
                        EnemyVisibility[EnemyNumber] = true;
                        EnemyY[EnemyNumber] = imgMainCharacterY + uncertainty;
                        if (EnemyY[EnemyNumber] > 525) {
                            EnemyY[EnemyNumber] = 525;
                        } else if (EnemyY[EnemyNumber] < 75) {
                            EnemyY[EnemyNumber] = 75;
                        }
                        EnemyX[EnemyNumber] = 850;
                        enemyHasHit[EnemyNumber] = false;
                        EnemyNumber++;
                        if (EnemyNumber >= 100) {
                            EnemyNumber = 0;
                        }
                    }
                }
//------------------------------------------------------------------------------------------------
                for (int i = 0; i < 100; i++) {
                    if (EnemyVisibility[i]) {
                        //Speed at which enemy moves increases as score increases
                        EnemyX[i] -= score / 115.0 + 2;
                        //When enemy past the 750 pixel mark and score >400, they begin to heat seek
                        if (EnemyX[i] < 750 && EnemyX[i] > imgMainCharacterX && score >= 400) {
                            EnemyY[i] += (imgMainCharacterY - EnemyY[i]) / 75.0;
                        }
                        //Collision code between main character and enemy
                        if (Math.sqrt(Math.pow((imgMainCharacterX - EnemyX[i]), 2) + Math.pow((imgMainCharacterY - EnemyY[i]), 2))
                                < 70) {
                            if (!enemyHasHit[i]) {
                                System.out.println("COLLISION");
                                //You lose a health and the variables are correctly flipped
                                health -= 1;
                                enemyHasHit[i] = true;
                                EnemyVisibility[i] = false;
                                //Draw explosion when they collide
                                ExplosionVisibility[i] = true;

                            }
                        }
                        //Collision code between Pokeball and enemy
                        if (Math.sqrt(Math.pow((imgPokeballX - EnemyX[i]), 2) + Math.pow((imgPokeballY - EnemyY[i]), 2))
                                < 60 && imgPokeballActive) {
                            System.out.println("Caught!");
                            EnemyVisibility[i] = false;
                            imgPokeballActive = false;
                            //Main way to increase score is to catch enemies
                            score += 40;

                        }

                    }

                    //When you have no health left, game ends and the screen switches
                    if (health == 0) {
                        gameOver = true;
                        playing = false;
                    }

                    //Make sure main character stays within a specified area
                    if (imgMainCharacterX > 780) {
                        imgMainCharacterX = 780;
                    } else if (imgMainCharacterX < 20) {
                        imgMainCharacterX = 20;
                    }

                    if (imgMainCharacterY > 535) {
                        imgMainCharacterY = 535;
                    } else if (imgMainCharacterY < 45) {
                        imgMainCharacterY = 45;
                    }

                    //Deactivate enemies when they're off the screen
                    if (EnemyX[i] < -20) {
                        EnemyVisibility[i] = false;
                    }


                }

            }
            
            // will redraw everything in the game panel
            repaint();
            //asks program to pause 17 miliseconds. This will run at 60 frames per second 
            Thread.sleep(17);
            counter++;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);// do this when you want 2 sections the same thing
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //Set coordinates for crosshair to be exactly equal to that of the mouse cursor
        imgCrosshairX = e.getX();
        imgCrosshairY = e.getY();
    }

    //-----------------------------------------------------the rest is for the mouse click
    @Override
    public void mouseClicked(MouseEvent e) {
        //Find distance between cursor and main character
        double distanceToCursor =
                Math.sqrt(Math.pow((imgMainCharacterX - e.getX()), 2) + Math.pow((imgMainCharacterY - e.getY()), 2));
        //Set speed at which pokeball is thrown. Speed is also incremented as score increases
        //This serves to give you an easier time navigating through the enemy filled field
        double ratio = (7 + score / 180.0) / distanceToCursor;
        //Pokeball moves according to the direction vectors of its x and y axises 
        xVelocity = (e.getX() - imgMainCharacterX) * ratio;
        yVelocity = (e.getY() - imgMainCharacterY) * ratio;

        //When you click, the pokeball begins shooting and begins from the main character's coordinates
        if (playing && !imgPokeballActive && PokeballCounter > 0) {
            imgPokeballActive = true;
            imgPokeballX = imgMainCharacterX;
            imgPokeballY = imgMainCharacterY;
            //Debug purpose println
            System.out.println(--PokeballCounter);
            //Set so that you cannot have negative pokeballs
            if (PokeballCounter < 0) {
                PokeballCounter = 0;
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        MousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //----------------------------------------------------------rest is keys
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            //When you press W, A, S, or D, the appropriate variable is set to true
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            //Press spacebar to set the menu to false and begin the game
            case KeyEvent.VK_SPACE:
                if (menu = true) {
                    menu = false;
                }
                break;
                
            //Restart the game at gameOver screen
            case KeyEvent.VK_F:
                if (gameOver) {
                    gameOver = false;
                    playing = true;
                    //Reset all variables
                    imgFinishScreenX = 600;
                    score = 0;
                    PokeballCounter = 3;
                    health = 10;
                    imgMainCharacterX = 400;
                    imgMainCharacterY = 300;
                    imgPokeballActive = false;
                    //Take out previously drawn enemies
                    for (int i = 0; i < 100; i++) {
                        EnemyVisibility[i] = false;
                    }
                }

                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            //Reset the appropriate variables for W, A, S, and D to be false when the key is let go
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
        }
    }
} // #coyotes