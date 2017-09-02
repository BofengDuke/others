package org.mytetris.entities;

import java.util.Random;

import org.mytetris.listener.ShapeListener;
import org.mytetris.util.ConstantVariable;

/**
 * ͼ�ι�����������������ͼ��,��ͼ�ε�״̬��Ϊ��ʼ��һ��״̬
 */
public class ShapeFactory {
	public Shape getShape(ShapeListener shapeListener){
		
		int shapeType = (int)(new Random().nextInt(ConstantVariable.TYPES.length));
		Shape shape = new Shape(shapeType);
		shape.addshapeListener(shapeListener);
		return shape;
	}
}
