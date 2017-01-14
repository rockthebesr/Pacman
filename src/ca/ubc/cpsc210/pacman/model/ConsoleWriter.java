package ca.ubc.cpsc210.pacman.model;


public class ConsoleWriter implements Observer {


	@Override
	public void update(Boolean b) {
		if (b) {
			System.out.println("ouch, one pac-man is dead!");
		}
	}

}
