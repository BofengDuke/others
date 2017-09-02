package org.mytetris.listener;

import org.mytetris.entities.Shape;

/**
 * ͼ������ļ���������Ҫ�����У�һ������ͼ�γ������䣬�����ж������Ƿ��Ѿ����
 *
 */
public interface ShapeListener {
	// ͼ���������ǰ���¼�����
	public abstract void shapeMoveDown(Shape shape);
	
	// �ж�ͼ���Ƿ���������ļ����¼�,
	// ��Ҫ��֤�����߳��У�ÿ��ֻ��һ��ͼ�ο��Խ����ж�
	public abstract boolean shapeIsMoveDownable(Shape shape);
}
