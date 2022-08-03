
/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import Control.GameControl;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import model.*;
import ui.UI;
import ui.graphical.GUI;
import ui.graphical.HelloGame;
import ui.text.TextUI;

import java.io.IOException;

import static java.lang.Math.round;


public class GameMain {

    public static boolean flag = false;

    public static void main(String[] args)  throws IOException  {

        boolean useGui = false;
        UI ui;
        GameControl gameControl;
        Thread gameControlThread;

        int playerTurn = (int) round(Math.random());

        Player player1 = new Player('X');
        Player player2 = new Player('O');
        Player [] players = {player1,player2};
        GameBoard board = new GameBoard();


        for (String arg : args) {
            if (arg.equals("-X")) {
                playerTurn = 0;
            }
            if (arg.equals("-O")) {
                playerTurn = 1;
            }
            if (arg.equals("-jam")) {
                flag = true;
            }
            if (arg.equals("-gui")) {
                useGui = true;
            }
        }

        ui = useGui ? new GUI(board,players) : new TextUI(board,players);
        gameControl = new GameControl(ui,playerTurn,flag);
        gameControlThread = new Thread(gameControl);
        gameControlThread.start();

        if (useGui) {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            int height = HelloGame.HEIGHT;
            int width = HelloGame.WIDTH;
            config.setWindowedMode(width, height);
//            config.setResizable(true);
            config.setResizable(false);
            config.setWindowIcon("Images/Screenshot 2022-03-26 140453.png");
            new Lwjgl3Application(new HelloGame(gameControlThread,ui), config);
        }
    }
}
