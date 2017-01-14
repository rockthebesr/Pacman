package ca.ubc.cpsc210.pacman.model;

import java.util.LinkedList;
import java.util.List;

public class Observable {

	public List<Observer> observers = new LinkedList<Observer>();
	public Observable() {
	}
	
	public void addObserver(Observer o){
		observers.add(o);
	}
	
	public void notifyObservers(Boolean b) {
		for (Observer next : observers) {
			next.update(b);
		}
	}
}
