package org.mytetris.listener;

import org.mytetris.entities.Ground;

public interface GroundListener {
	
	/**
	 * ��ɾ���������¼�
	 * @param ground
	 * @param deletedLineCount ��ɾ�����к�
	 */
	void fullLineDeleted(Ground ground,int deletedLineCount);
	
	/**
	 * �ϰ����Ƿ񵽴ﶥ��
	 * @param ground
	 */
	void groundIsFull(Ground ground);
	
	
}
