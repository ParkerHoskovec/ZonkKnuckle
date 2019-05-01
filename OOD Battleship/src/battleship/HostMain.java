package battleship;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HostMain extends Main {
	static ServerSocket ss;
	static Socket s;
	static DataInputStream din;
	static DataOutputStream dout;
	int port;
	
	public static void main(String[] args) {
		launch();
	}
	
	String msgout = "X|X|error";
	String msgin = "";
	boolean dead = false;
	//TODO: remove temp command
	Command temp;
	
	@Override
	public void start(Stage primStage) throws Exception {
		//myTurn = false;
		//UI Stuff
		Pane contentPane = totalInit();
		Scene boardScene = new Scene(contentPane);
		Label lblPort = new Label("Port");
		lblPort.setLayoutX(160);
		lblPort.setLayoutY(360);
		TextField txtPort = new TextField();
		txtPort.setLayoutY(355);
		Button btnHost = new Button("Host");
		btnHost.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						port = Integer.parseInt(txtPort.getText());
						startHandler();
					}
				});
			}
		});
		
		contentPane.getChildren().add(lblPort);
		contentPane.getChildren().add(txtPort);
		contentPane.getChildren().add(btnHost);
		btnHost.setLayoutY(385);
		primStage.setScene(boardScene);
		primStage.show();
	}

	//test
	public void stop() {
		try {
			dead = true;
			ss.close();
			din.close();
			dout.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	void makeCommands(Space target) {
		//when you click a space, make an attack command and send it
		int ex = target.x;
		int ey = target.y;
		String msg = (ex+"|"+ey+"|attack|init|fromHostmain");
		try {
			dout.writeUTF(msg);
			setTurn(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Hostmain";
	}

	//Placement of 5 ships only on non occupied spaces
	@Override
	void shipPlacement(Space selection) {
		int ex = selection.x;
		int ey = selection.y;
		Space[] spaces;
		Space origin = boardPlayerState[ex][ey];
		ExShip newShip;
		switch (shipsPlaced) {
		case 0:
			if (!selection.hasShip) {
				newShip = new ExShip(origin,new Space[] {origin}); 
				ships.add(newShip);
				redrawBoards();
				shipsPlaced++;
			}
			break;

		case 1:
			if (!selection.hasShip) {
				newShip = new ExShip(origin,new Space[] {origin}); 
				ships.add(newShip);
				redrawBoards();
				shipsPlaced++;
			}
			break;

		case 2:
			if (!selection.hasShip) {
				newShip = new ExShip(origin,new Space[] {origin}); 
				ships.add(newShip);
				redrawBoards();
				shipsPlaced++;
			}
			break;

		case 3:
			if (!selection.hasShip) {
				newShip = new ExShip(origin,new Space[] {origin}); 
				ships.add(newShip);
				redrawBoards();
				setTurn(true);
				shipsPlaced++;
			}
			break;

		default:
			break;

		}

	}
	

	//this is the socket handler thread- it continually listens to the socket in the background and uses the results to make commands
	public void startHandler() {
		new Thread(() -> {
			try {
				ss = new ServerSocket(this.port);
				s = ss.accept();
				din = new DataInputStream(s.getInputStream());
				dout = new DataOutputStream(s.getOutputStream());
				//ideally this loop will allow us to continually read inputs from the client
				while(!dead) {
					msgout = "";
					msgin = din.readUTF();
					if(!msgin.equals("")) {
						//if the message received is not blank, start processing
						String[] splitArr = msgin.split("\\|");	
						//These strings are used for holding the message footer- splat is init or response, splot is the sender
						String splat = splitArr[3];
						String splot = splitArr[4];
						//if the command received is for us, we want to execute it
						if(splot.equals("fromClientmain")){
							//execute the command and store the response if it's from the right sender
							temp = this.fact.makeCommand(this, msgin);
							msgout = temp.execute();
						}
						else {
							splat = "response"; //force it not to run if it's not from the right sender
						}
						if(!splat.equals("response")){
							//if it's already a response, don't respond
							// (responding to a response makes it re-execute)
							try {
								dout.writeUTF(msgout);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return;
				//kill thread when dead
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

}
