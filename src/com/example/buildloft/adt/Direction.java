package com.example.buildloft.adt;


public enum Direction {
	LEFT,
	RIGHT;
	
	public static Direction opposite(final Direction pDirection) {
		switch(pDirection) {
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			default:
				return null;
		}
	}
}
