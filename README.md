# Blokus Duo by JamAI

This rendition of Blokus Duo was developed by Jason Wan, Abhiram Ajith, and Mario Okeke for our Software Engineering Project module during stage 2 of our college degree

## Tools used
- Intellij IDEA 2021.2.2
- LibGDx v1.1
- Tiled Map Editor V1.8.2
- Skin Composer ver.50
- Universal Tween Engine           
- Typing Label V1.3         
- Github
- Trello    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
## Usage

Once you have downloaded the jar file and associated dependencies you can follow the instructions below to run the game. The instructions of how to play the game can be found [here](https://service.mattel.com/instruction_sheets/FWG43-Eng.pdf).

The game can be run in both command prompt and graphical interface mode and both methods will be outlined below.

### Command prompt
To run the game without a graphical interface the following command can be used
```bash
java -jar jamAI.jar
```
To specify which player goes first you can add a `-X` or `-O` after `jamAI.jar`,
If no argument is specified a random player will go first.

When this command is issued you will be prompted to enter names and the game will start as shown below:

![image](https://user-images.githubusercontent.com/71881526/182949541-57005dee-312e-415e-bc2c-089db93f3e77.png)

As you can see in the image the program shows the current player's pieces that they currently have. These correspond to the pieces in the following image: 

![image](https://user-images.githubusercontent.com/71881526/182950215-712d9b5d-cd96-4e7b-90ca-efcee2cf8e7e.png)

When you want to play a piece on the board a coordinate must be given as to where the piece should be placed. Co-ordinates are specified by X-axis followed by Y-axis. The red dots shown for each piece in the image above is the point at which the piece will be placed on the given coordinate. You are also given the option to flip or rotate a piece before placing it. For example, to place the `W` piece at location `9 4`, flip it once and rotate twice the following input is required:

![image](https://user-images.githubusercontent.com/71881526/182951764-ea0f8dd7-53d5-4db6-b658-13802a5fa66c.png)

Players alternate taking turns until no more moves can be made and a winner is then decided based on the scoring system of the game.


## Graphical Interface

To use the graphical interface just include `-gui` after `jamAI.jar` and the game will start in graphical interface mode.

You will first be met with a splash screen which we added as we thought it would be a nice addition to add a screen that displays our team name briefly when the game starts. For the background, we used two pictures we had drawn ourselves (we each drew a different part of the pictures) and the splash screen randomly picks which to show between the two on each launch of the game.

![image](https://user-images.githubusercontent.com/71881526/182953184-fe952742-f2e9-4ead-b824-575760b538aa.png)

Running demo of splash screen:

![Splash1](https://user-images.githubusercontent.com/71881526/182956095-162684e5-a2de-4a7c-9192-2c33ddf2da1f.gif)

You will then be met with the main menu which allows you to either play the game or exit.

![Main (1)](https://user-images.githubusercontent.com/71881526/182958530-600c63d2-ce9a-45c1-ae06-cf6322f49ff8.gif)

This leads to the name selection screen where you pick the names for player 1 and player 2. You can also change the skin applied to the game from a choice of 3 using the drop down menu on the top right. Musix volume and sound effect volume can be adjusted using the settings cog on the bottom left.

### OG skin:
![image](https://user-images.githubusercontent.com/71881526/182959540-20eec77e-c508-4e1c-a4ce-3cbeef7c4060.png)
![image](https://user-images.githubusercontent.com/71881526/182960663-020b20b9-ce5f-451e-b787-998aefcf697e.png)


### Shine Skin:
![image](https://user-images.githubusercontent.com/71881526/182959633-389dfc70-c4f9-400a-a7e4-9f93bd86fdbf.png)
![image](https://user-images.githubusercontent.com/71881526/182960738-584c8c61-4f9e-43b7-94c7-216470a8187a.png)


### Serenity Skin:
![image](https://user-images.githubusercontent.com/71881526/182959699-53199af9-f5d2-41f3-b8f7-c0c9c0fda709.png)
![image](https://user-images.githubusercontent.com/71881526/182960838-fcff78cd-417c-43c6-ba4a-954fa223fb95.png)


Settings menu:

![image](https://user-images.githubusercontent.com/71881526/182959785-aaefae83-8de5-499e-9767-d6c41624d9b3.png)

Once the game is started the player who will go first is decided and instructions on how to place pieces are displayed.

![image](https://user-images.githubusercontent.com/71881526/182960219-29623f74-782d-4c78-af17-4e16957d04b9.png)

Instructions on how to play the game can also be accessed by clicking the question mark icon at the bottom of the screen.

![image](https://user-images.githubusercontent.com/71881526/182960589-a405ccb6-9e7f-40d2-8622-3a4310613cbc.png)

When it is a player's turn they can click and drag their desired piece to a valid location on the board. They can also rotate and flip the piece by pressing the `r` key and `f` key respectively while holding any piece.

![piece](https://user-images.githubusercontent.com/71881526/182961674-ecf2ae01-62c5-4f48-8a39-10b3df31b372.gif)


When neither player can make a move the points are calculated and the winner is displayed on the screen in addition to the scores.

![image](https://user-images.githubusercontent.com/71881526/182961207-5c35a273-66c0-498a-a34f-ea90a25cfa0e.png)

## Credits:  
Guitar Gentle by PeriTune | https://peritune.com/  
Music promoted by https://www.chosic.com/free-music/all/  
Creative Commons CC BY 4.0
https://creativecommons.org/licenses/by/4.0/  

emotional leeway composed by kyusu
https://dova-s.jp/EN/bgm/play16436.html  

The way by LiQWYD | https://soundcloud.com/liqwyd/
Music promoted by https://www.chosic.com/free-music/all/
Licensed under Creative Commons: Attribution 3.0 Unported (CC BY 3.0)
https://creativecommons.org/licenses/by/3.0/


