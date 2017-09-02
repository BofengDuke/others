package org.mytetris.entities;

import java.util.Random;

import org.mytetris.listener.ShapeListener;
import org.mytetris.util.ConstantVariable;

/**
 * 图形工厂用于生产出各种图形,但图形的状态都为初始第一个状态
 */
public class ShapeFactory {
	public Shape getShape(ShapeListener shapeListener){
		
		int shapeType = (int)(new Random().nextInt(ConstantVariable.TYPES.length));
		Shape shape = new Shape(shapeType);
		shape.addshapeListener(shapeListener);
		return shape;
	}
}
