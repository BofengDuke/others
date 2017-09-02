package org.mytetris.listener;

import org.mytetris.entities.Ground;

public interface GroundListener {
	
	/**
	 * 被删除的满行事件
	 * @param ground
	 * @param deletedLineCount 被删除的行号
	 */
	void fullLineDeleted(Ground ground,int deletedLineCount);
	
	/**
	 * 障碍物是否到达顶部
	 * @param ground
	 */
	void groundIsFull(Ground ground);
	
	
}
