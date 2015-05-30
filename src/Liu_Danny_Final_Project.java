/*
 * Danny Liu
 * Final Project
 * ICS3U-1
 * Mr. Lim
 * May 23rd
 */

import java.awt.Cursor;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Liu_Danny_Final_Project {

    public static void main(String[] args) throws IOException, InterruptedException 
    {
        //Create new JFrame
        JFrame myFrame = new JFrame();
        //Close panel upon exist
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Make the gamepanel visible
        myFrame.setVisible(true);
        //Sets size of frame
        myFrame.setSize(800, 600);
        //Sets name of game
        myFrame.setTitle("Mystery Pokeball");
        //Makes sure that the player cannot change the dimensions
        myFrame.setResizable(false);
        //Makes my program always on top of other screens
        myFrame.setAlwaysOnTop(true);

        //Set dimensions of game panel
        GamePanel myPanel = new GamePanel();
        myPanel.setSize(800,600);
        myPanel.setVisible(true);
        myFrame.setContentPane(myPanel);
        
        //Get focus into your game panel
        myPanel.setFocusable(true);
        myPanel.requestFocus();
        
        //run the program 
        myPanel.run();
    }
}
