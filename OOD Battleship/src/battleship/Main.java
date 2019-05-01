package battleship;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.*;



public abstract class Main extends Application{
	//Main me = this;
	boolean myTurn = true;
	int shipsPlaced = 0;
	ReceivedCommandFactory fact = new ReceivedCommandFactory();
	Image boardPlayer = new Image("https://i.imgur.com/WCVmiVJ.png");
	Image boardFoe = new Image("https://i.imgur.com/WCVmiVJ.png");
	Space[][] boardPlayerState = new Space[10][10];
	Space[][] boardFoeState = new Space[10][10];
	Label turnDisp = new Label();
	ArrayList<Ship> ships = new ArrayList<Ship>();
	
	public static void main(String[] args) {
		launch();
	}
	
	public abstract void start(Stage primStage) throws Exception;
	
	//for starting the program
	public Pane totalInit() {
		//make the pane that serves as the foundation for the whole thing
		Pane boardPane = new Pane();
		boardPane.setMinSize(700, 400);
		boardPane.setMaxSize(700, 400);
		turnDisp = new Label("Welcome to battleship!");
		ImageView boardPlayDisp = new ImageView(boardPlayer);
		ImageView boardFoeDisp = new ImageView(boardFoe);
		boardPlayDisp.setLayoutY(32);
		boardFoeDisp.setLayoutX(330);
		boardFoeDisp.setLayoutY(32);
		boardPane.getChildren().add(turnDisp);
		boardPane.getChildren().add(boardPlayDisp);
		boardPane.getChildren().add(boardFoeDisp);
		//actually make Spaces (which extend imageviews) for each space
		//then initialize space arrays and draw based on them
		reinitBoards();
		redrawBoards(); 
		//THEN add listeners to every single one of them
		//this is the observer pattern here:
		for(int x=0;x<10;x++) {
			for(int y=0;y<10;y++) {
				boardFoeState[x][y].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event) {
						Space source = (Space)event.getSource();
						//only make a command if it is your turn when you click the space [1]
						if(myTurn && !source.chosen) {
							makeCommands(source);
							source.chosen = true;
						}
					}
				});
				
			}	
		}
		for(int x=0;x<10;x++) {
			for(int y=0;y<10;y++) {
				boardPlayerState[x][y].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event) {
						Space source = (Space)event.getSource();
						//only make a command if it is your turn when you click the space [1]
						if(myTurn && !source.chosen) {
							// abastgrtack RIP Pedro, he had a heart attack writing this line
							shipPlacement(source);
						}
					}
				});
				
			}	
		}
		//add all those pictures to the pane
		for(int x=0;x<10;x++) {
			for(int y=0;y<10;y++) {
				boardPane.getChildren().add(boardPlayerState[x][y]);
				boardPane.getChildren().add(boardFoeState[x][y]);
			}
		}
		//all of that construction is returned here
		return boardPane;
	}
	
	//method to update ImageViews based on state of Space arrays
	public void redrawBoards() {
		for(int x=0;x<10;x++) {
			for(int y=0;y<10;y++) {
				boardPlayerState[x][y].setImage(boardPlayerState[x][y].spacePic);
				boardPlayerState[x][y].setLayoutX(x*32);
				boardPlayerState[x][y].setLayoutY(32+(y*32));
				boardPlayerState[x][y].setIdentifier(this.toString());
				boardFoeState[x][y].setImage(boardFoeState[x][y].spacePic);
				boardFoeState[x][y].setLayoutX(330+(x*32));
				boardFoeState[x][y].setLayoutY(32+(y*32));
				boardFoeState[x][y].setIdentifier(this.toString());
			}
		}
	}
	//this is so we can select a new space to place ships
	abstract void shipPlacement (Space selection);	
	
	//initialize the board state Space arrays to be blank on both sides
	public void reinitBoards() {
		for(int x=0;x<10;x++) {
			for(int y=0;y<10;y++) {
				boardPlayerState[x][y] = new Space(false, x, y); //space constructor w/ boolean determines target/playfield status
				boardFoeState[x][y] = new Space(true,x, y);
			}
		}
	}
	
	//abstract method called by target space listeners
	abstract void makeCommands(Space target);
	
	//method to set the text of the label at the top as well as the boolean that allows/prevents turns to be taken (see totalInit [1])
	public void setTurn(boolean myTurn) {
		this.myTurn = myTurn;
		if(myTurn) {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
					turnDisp.setText("Your turn!");
			    }
			});
		}
		else {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
					turnDisp.setText("Waiting for your turn...");
			    }
			});
		}
	}
	//this method sets the label to a string
	public void setLabel(String stuff) {
		turnDisp.setText(stuff);
	}


}
