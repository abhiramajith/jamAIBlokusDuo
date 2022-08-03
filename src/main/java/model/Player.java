package model;
/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import java.util.ArrayList;
import java.util.*;


public class Player {

    private String playerName;
    private int playerScore;
    private char playerSymbol;
    private boolean hasMadeFirstMove;
    private ArrayList<String> playerPieces = new ArrayList<>(Arrays.asList("I1", "I2", "I3", "I4", "I5", "V3", "L4", "Z4", "O4", "L5", "T5", "V5", "N", "Z5", "T4", "P", "W", "U", "F", "X", "Y"));
    private ArrayList<String> removedPieces;


    public Player(char playerSymbol) {
        this.playerSymbol = playerSymbol;
        this.hasMadeFirstMove = false;
        this.removedPieces = new ArrayList<>();
        this.playerScore = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {this.playerName = playerName;}

    public int getPlayerScore() {
        return playerScore;
    }

    public char getPlayerSymbol() {
        return playerSymbol;
    }

    public ArrayList<String> getPlayerPieces() {
        return playerPieces;
    }

    public void removePiece(String piece){
        removedPieces.add(piece);
        playerPieces.remove(piece);
    }

    public void addPiece(String piece){
        removedPieces.remove(piece);
        playerPieces.add(piece);
    }

    public void madeFirstMove() {this.hasMadeFirstMove = true;}

    public boolean getHasMadeFirstMove() {return this.hasMadeFirstMove;}

    public String getLastPiece(){return removedPieces.get(removedPieces.size()-1);}

    public void updateScore(){
        if(playerPieces.size() == 0){
            if(getLastPiece().equalsIgnoreCase("I1"))
                playerScore += 20;
            else
                playerScore += 15;
        }
        else{
            for(int j=0;j<getPlayerPieces().size();j++){
                playerScore -= GamePiece.getSize(getPlayerPieces().get(j));
            }
        }
    }
}
