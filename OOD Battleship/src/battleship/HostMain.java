package battleship;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HostMain extends Main {
	static ServerSocket ss;
	static Socket s;
	static DataInputStream din;
	static DataOutputStream dout;
	
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
		//UI Stuff
		Pane contentPane = totalInit();
		Scene boardScene = new Scene(contentPane);
		primStage.setScene(boardScene);
		primStage.show();
		//TODO: REMOVE this hardcoded ship
		ExShip hardShip = new ExShip(boardPlayerState[2][3],  new Space[]{boardPlayerState[2][3]});
		boardPlayerState[2][3].addShip(hardShip);
		ships.add(hardShip);
		redrawBoards();
		//okay here we go again
		//makes a background thread to read from the socket
		new Thread(() -> {
			try {
				ss = new ServerSocket(1201);
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

	@Override
	void shipPlacement(Space selection) {
		// TODO Auto-generated method stub
		
	}

}
