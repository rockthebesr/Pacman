package ca.ubc.cpsc210.pacman.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board extends Observable {


	private static int BOARD_WIDTH = 13;
	private static int BOARD_HEIGHT = 8;

	private static String BOARD_DEFN = "WWWWWWWWWWWWW" + 
			"WDRDTDDDDMDDW" +
			"WDWDWWWWWDWDW" +
			"WDWDWDRDWDWDW" +
			"WDWDDDWDDDWDW" +
			"WDWWDWWWDWWDW" +
			"WDD1DDDD2DDDW" +
			"WWWWWWWWWWWWW";


	List<List<GridLocation>> board;

	List<Pacman> pacmans = new LinkedList<Pacman>();
	Pacman pacman1;
	Pacman pacman2;
	boolean pacman1Caught = false;
	boolean pacman2Caught = false;
	List<RandomMonster> randomMonsters;
	List<TrackerMonster> trackerMonsters;
	ConsoleWriter cw = new ConsoleWriter();

	// Requires: nothing
	// Modifies: this
	// Effects:  delegates board initialization to initBoard
	public Board() {
		initBoard();
		addObserver(cw);
	}

	// Requires: nothing
	// Modifies: this
	// Effects:  initializes a default board with food, monsters and pacman1 placed according to BOARD_DEFN
	private void initBoard() {
		pacman1 = new Pacman(this);
		pacman2 = new Pacman(this);
		pacmans.add(pacman1);
		pacmans.add(pacman2);
		randomMonsters = new LinkedList<RandomMonster>();
		trackerMonsters = new LinkedList<TrackerMonster>();

		// Crazy board initialization code.
		board = new ArrayList<List<GridLocation>>(BOARD_HEIGHT);
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			board.add(new ArrayList<GridLocation>(BOARD_WIDTH));
		}

		for (int i = 0; i < BOARD_DEFN.length(); i++) {
			char type = BOARD_DEFN.charAt(i);

			int x = i % BOARD_WIDTH;
			int y = i / BOARD_WIDTH;

			if (type == 'M') {
				TrackerMonster s = new TrackerMonster1(this,x,y);
				trackerMonsters.add(s);
				board.get(y).add(new GridLocation('D', s));
			} else if (type == 'T') {
				TrackerMonster s = new TrackerMonster2(this,x,y);
				trackerMonsters.add(s);
				board.get(y).add(new GridLocation('D', s));
			} else if (type == 'R') {
				RandomMonster s = new RandomMonster(this,x,y);
				randomMonsters.add(s);
				board.get(y).add(new GridLocation('D', s));
			} else if (type == '1') {
				pacman1.setLocation(x,y);
				board.get(y).add(new GridLocation('E', pacman1));
			} else if (type == '2') {
				pacman2.setLocation(x, y);
				board.get(y).add(new GridLocation('E', pacman2));
			} else {
				board.get(y).add(new GridLocation(type));
			}
		}
	}

	// Getter methods:
	// Requires: nothing
	// Modifies: nothing
	// Effects: returns the contents of the relevant field
	public int getBoardWidth() { return BOARD_WIDTH; }
	public int getBoardHeight() { return BOARD_HEIGHT; }
	public GridLocation getLocation(int x, int y) { return board.get(y).get(x);	}
	public List<List<GridLocation>> getGridRows() {	return board; }
	public List<TrackerMonster> getTrackerMonsters() { return trackerMonsters; }

	// Requires: nothing
	// Modifies: this
	// Effects: update pacman1, pacman2, tracker monster, and random monsters.
	public void tickBoard() {
		if (!pacman1Caught) {
			pacmans.get(0).makeMove();}
		if (!pacman2Caught) {
			pacmans.get(1).makeMove();}

		for (TrackerMonster m : trackerMonsters) {
			m.makeMove();
		}

		for (RandomMonster m : randomMonsters) {
			m.makeMove();
		}
	}

	// Requires: nothing
	// Modifies: nothing
	// Effects: if all the food pellets are gone or both pacman has been caught, 
	// 			the game is over.
	public boolean isGameOver() {
		// Check to see if all the food pellets are gone
		boolean existsFood = false;
		for (List<GridLocation> row : getGridRows()) {
			for (GridLocation g : row) {
				if (g.isPassable()) {
					if (g.hasFood()) {	
						existsFood = true;
					}
				}
			}
		}

		for (TrackerMonster m : trackerMonsters) {
			if (m.getX() == pacman1.getX() &&
					m.getY() == pacman1.getY()) 
				pacman1Caught = true;
		}
		for (RandomMonster m : randomMonsters) {
			if (m.getX() == pacman1.getX() &&
					m.getY() == pacman1.getY()) 
				pacman1Caught = true;
		}

		for (TrackerMonster m : trackerMonsters) {
			if (m.getX() == pacman2.getX() &&
					m.getY() == pacman2.getY()) 
				pacman2Caught = true;
		}
		for (RandomMonster m : randomMonsters) {
			if (m.getX() == pacman2.getX() &&
					m.getY() == pacman2.getY()) 
				pacman2Caught = true;
		}
		
			notifyObservers(pacman1Caught || pacman2Caught);


		return (pacman1Caught && pacman2Caught) || !existsFood;
	}

	// Requires: nothing
	// Modifies: nothing
	// Effects: returns true if the given position (new_x, new_y) is a valid board position
	public boolean canMoveTo(int new_x, int new_y) {
		return new_x >= 0 && new_y >= 0 && new_x < BOARD_WIDTH && new_y < BOARD_HEIGHT 
				&& getLocation(new_x, new_y).isPassable();
	}

	// Requires: sprite is a valid Pacman on this board, and position (new_x, new_y) is a valid board position
	// Modifies: this, sprite
	// Effects:  moves the given sprite to the proper location on the board, and changes the (x,y) position of the sprite to reflect the move
	public void moveTo(Pacman sprite, int new_x, int new_y) {
		getLocation(sprite.getX(), sprite.getY()).removeSprite(sprite);
		getLocation(new_x, new_y).addSprite(sprite);
		sprite.setLocation(new_x, new_y);
	}

	// Requires: sprite is a valid TrackerMonster on this board, and position (new_x, new_y) is a valid board position
	// Modifies: this, sprite
	// Effects:  moves the given sprite to the proper location on the board, and changes the (x,y) position of the sprite to reflect the move
	public void moveTo(TrackerMonster sprite, int new_x, int new_y) {
		getLocation(sprite.getX(), sprite.getY()).removeSprite(sprite);
		getLocation(new_x, new_y).addSprite(sprite);
		sprite.setLocation(new_x, new_y);
	}

	// Requires: sprite is a valid RandomMonster on this board, and position (new_x, new_y) is a valid board position
	// Modifies: this, sprite
	// Effects:  moves the given sprite to the proper location on the board, and changes the (x,y) position of the sprite to reflect the move
	public void moveTo(RandomMonster sprite, int new_x, int new_y) {
		getLocation(sprite.getX(), sprite.getY()).removeSprite(sprite);
		getLocation(new_x, new_y).addSprite(sprite);
		sprite.setLocation(new_x, new_y);
	}

	// Requires: nothing
	// Modifies: nothing
	// Effects: returns pacman1
	public Pacman getPacman1() {
		return pacman1;
	}

	public Pacman getPacman2() {
		return pacman2;
	}



}
