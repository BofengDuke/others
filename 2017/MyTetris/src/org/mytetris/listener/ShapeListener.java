package org.mytetris.listener;

import org.mytetris.entities.Shape;

/**
 * 图形下落的监听器，主要功能有：一监听让图形持续下落，二，判断下落是否已经完成
 *
 */
public interface ShapeListener {
	// 图形下落完成前的事件监听
	public abstract void shapeMoveDown(Shape shape);
	
	// 判断图形是否允许下落的监听事件,
	// 需要保证，多线程中，每次只有一个图形可以进行判断
	public abstract boolean shapeIsMoveDownable(Shape shape);
}
