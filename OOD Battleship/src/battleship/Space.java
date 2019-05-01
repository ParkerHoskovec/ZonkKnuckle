package battleship;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Space extends ImageView {
	static Image trans = new Image("https://i.imgur.com/5YogoxQ.png");
	static Image missTemp = new Image("https://i.imgur.com/bVRXYCS.png");
	static Image hitTemp = new Image("https://i.imgur.com/7oww838.png");
	static Image shipTemp = new Image("https://i.imgur.com/Q4kQ6dp.png");
	static Image hitShipTemp = new Image("https://i.imgur.com/FeHX1jq.png");
	String identifier;
	Image spacePic;
	int x;
	int y;
	boolean isTarget;
	boolean hasShip;
	boolean isHit;
	boolean chosen;
	Ship ship;
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public boolean isHit() {
		return isHit;
	}
	public void addShip(Ship ship) {
		if(!hasShip) {
			this.ship = ship;
			this.hasShip = true;
			spacePic = shipTemp;
		}
	}
	public void targMissed() {
		spacePic = missTemp;
	}
	public void targHit() {
		spacePic = hitTemp;
	}
	public boolean hasShip() {
		return this.hasShip();
	}
	public String hit() {
		this.isHit = true;
		if(hasShip) {
			spacePic = hitShipTemp;
			return (x+"|"+y+"|hit|response|from"+identifier);
		}
		else return (x+"|"+y+"|miss|response|from"+identifier);
	}
	public Space(boolean target){
		hasShip = false;
		isHit = false;
		isTarget = target;
		if(target) {
			spacePic = trans;
		}
		else {
			spacePic = trans;
		}
	}
	public Space(Image image) {
		super(image);
	}
	public Space() {
		super();
	}
	public Space(boolean target, int x, int y) {
		hasShip = false;
		isHit = false;
		isTarget = target;
		this.x = x;
		this.y = y;		
		if(target) {
			spacePic = trans;
		}
		else {
			spacePic = trans;
		}
	}
	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
