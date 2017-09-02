package org.mytetris.listener;

import org.mytetris.entities.Ground;

public interface LanListener {
	
	void moveLeft();
	
	void moveRight();
	
	void moveDown();
	
	void rotate();
	
	void fullLineDeleted(Ground ground,int deletedLineCount);
	
	void groundIsFull(Ground ground);
	
	
	
}
