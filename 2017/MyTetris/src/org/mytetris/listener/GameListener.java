package org.mytetris.listener;

public interface GameListener {
	
	/**
	 * ��Ϸ��ʼ
	 */
	void gameStart();
	
	/**
	 * ��Ϸ����
	 */
	void gameStop();
	
	/**
	 * ��Ϸ��ͣ
	 */
	void gamePause();
	
	/**
	 * ��Ϸ����
	 */
	void gameContinue();
}
