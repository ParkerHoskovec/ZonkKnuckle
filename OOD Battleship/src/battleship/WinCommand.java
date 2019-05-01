package battleship;

public class WinCommand implements Command{
	Main target;

	public WinCommand(Main target) {
		this.target = target;

	}
	@Override
	public String execute() {
		target.setTurn(false);
		target.setLabel("YOU WIN!");
		target.lastSelected.targHit();
		target.redrawBoards();
		return "win";
	}

	@Override
	public void execute(Space[][] spaces) {
		// TODO Auto-generated method stub
		
	}

}
