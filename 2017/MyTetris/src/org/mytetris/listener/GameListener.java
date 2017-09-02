package org.mytetris.listener;

public interface GameListener {
	
	/**
	 * 游戏开始
	 */
	void gameStart();
	
	/**
	 * 游戏结束
	 */
	void gameStop();
	
	/**
	 * 游戏暂停
	 */
	void gamePause();
	
	/**
	 * 游戏继续
	 */
	void gameContinue();
}
