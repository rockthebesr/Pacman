package ca.ubc.cpsc210.pacman.model;

import java.awt.Color;

public class TrackerMonster1 extends TrackerMonster {

	public TrackerMonster1(Board b) {
		super(b);
	}
	
	public TrackerMonster1(Board b, int x, int y) {
		super(b, x, y);
	}

	@Override
	public void makeMove() {
		char horiz_direction = getHorizontalDirectionToPacman();
		char vert_direction = getVerticalDirectionToPacman();
		
		int horiz_distance = Math.abs(getX() - board.getPacman1().getX());
		int vert_distance  = Math.abs(getY() - board.getPacman1().getY());
		
		direction = horiz_direction;
		boolean canMoveHoriz = canMakeMove();
		
		direction = vert_direction;
		boolean canMoveVert = canMakeMove();
		
		if (horiz_distance > vert_distance && canMoveHoriz) {
			direction = horiz_direction;
		} else if (vert_distance > horiz_distance && canMoveVert) {
			direction = vert_direction;
		} else if (canMoveHoriz) {
			direction = horiz_direction;
		} else if (canMoveVert) {
			direction = vert_direction;
		} else {
			// Can't move in the direction of pacman, so choose a random direction to keep moving.
			do {
				double random = Math.random();
				if (random < 0.25) direction = 'L';
				else if (random < 0.5) direction = 'U';
				else if (random < 0.75) direction = 'R';
				else direction = 'D';
			} while (!canMakeMove());
		}
		
		moveInCurrentDirection();
	}

	@Override
	protected char getVerticalDirectionToPacman() {
		Pacman pacman = board.getPacman1();
		int dy = y_location - pacman.getY();

		if (dy < 0) {
			return 'D';
		} else {
			return 'U';
		}
	}

	@Override
	protected char getHorizontalDirectionToPacman() {
		Pacman pacman = board.getPacman1();
		int dx = x_location - pacman.getX();

		if (dx < 0) {
			return 'R';
		} else {
			return 'L';
		}
	}

	@Override
	public Color getColor() {
		return Color.gray;
	}

}
