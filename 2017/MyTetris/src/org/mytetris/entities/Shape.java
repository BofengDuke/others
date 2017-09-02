package org.mytetris.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.TooManyListenersException;

import org.mytetris.listener.ShapeListener;
import org.mytetris.util.ConstantVariable;

/**
 * ͼ���࣬�������䣬���ƣ����ƣ���ת�ķ���
 * ͬʱ������ͼ����ʾ�ķ���
 * ͼ��Ӧ��ͨ���¼���������ʵ�ֶ�ʱ�������ػ�ķ���
 *
 */
/**
 * @author 80500
 *
 */
/**
 * @author 80500
 *
 */
public class Shape {	
	private int shapeType;		// ��ʾ��ǰͼ������ͣ��� L�� T ��I �ȵ�
	private int currShape[][];	// ��ʾ��ǰ��ͼ�����ͼ���״̬�����깹��
	private int status;		// ���ڴ�ŵ�ǰͼ�ε�״̬
	
	private int top;		// ͼ�κ��ϱ߽��Լ���߽�ľ���
	private int left;
	
	private int height = 0;		// ͼ�εĸ߶�
	
	protected boolean pause;	// ��ͣ״̬
	protected boolean life;		// �����������Ϸ��������ʧ
	
	protected Color color;	// ����ĳ�ʼ��ɫ

	protected ShapeListener shapeListener;	// ͼ�����������
	
	public Shape(int type){
		super();
		this.shapeType = type;
		this.currShape = ConstantVariable.TYPES[type];
		
		init();
	}
	
	/**
	 * ��ͼ�ν��г�ʼ��
	 */
	public void init() {
		for(int x=3;x>=0;x--){
			for(int y=0;y<4;y++){
				if(isMember(x, y, false)){
					height = x + 1;
					x = -1;
					break;
				}
			}
		}
		
		pause = false;
		life = true;
		status = 0;
		top = 0 - height;
		left = ConstantVariable.CELL_WIDTH_NUMS / 2 - 1;
		color = ConstantVariable.SHAPE_COLOR;
	}

	
	// ͨ�����ϱ߽�����߽�ľ��룬�ж�ͼ���Ƿ��ܹ������ƶ�
	public int getTop(){
		return top;
	}
	public int getLeft(){
		return left;
	}
	
	
	public int getHeight() {
		return height;
	}


	/**
	 * ��ȡ��ǰͼ�ε�����
	 * @return
	 */
	public int getShapeType() {
		return shapeType;
	}

	/**
	 * ����ͼ������,�����г�ʼ��
	 * @param shapeType
	 */
	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
		this.currShape = ConstantVariable.TYPES[shapeType];
		init();
	}

	// ����ͼ�ε�״̬
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	// ͼ������
	public void moveDown(){
		top++;
	}
	
	// ͼ������
	public void moveRight(){
		left++;
	}
	
	// ͼ������
	public void moveLeft(){
		left--;
	}
	
	// ��תͼ��
	public void rotate(){
		status = (status+1) % currShape.length; 
	}
	
	// ������ͣ��״̬
	public boolean isPause() {
		return pause;
	}
	
	/**
	 * ������ͣ
	 * @param pause
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	/**
	 * �ı���ͣ��״̬
	 */
	public void changePause(){
		this.pause = !this.pause;
	}
	
	
	/**
	 * ����ͼ�ζ�ʱ������߳�
	 */
	public synchronized void die() {
		this.life = false;
	}
	
	
	/**
	 * ����ͼ��,����ͼ������ 4 x 4 �ľ�������ʾ��
	 * �ھ����У�ֻ��ֵΪ 1 �ĸ��ӣ��Żᱻ���
	 * @param g �ñ����ĸ�����ʾ
	 */
	public void drawShape(Graphics g){
		if(!life){
			return;
		}
		g.setColor(color);
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				if(getFlagByPoint(status,x,y)){
					g.fill3DRect((left+x)*ConstantVariable.CELL_WIDTH,(top+y )*ConstantVariable.CELL_HEIGHT , ConstantVariable.CELL_WIDTH,ConstantVariable.CELL_HEIGHT, true);
				}
			}
		}
	}
	
	/**
	 * ������꣨x��y���Ƿ���ͼ���еĵ�
	 * @param status
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean getFlagByPoint(int status,int x,int y){
		return currShape[status][y*4 + x] == 1;
	}
	
	/**
	 * ָ��λ���Ƿ���ͼ���һ����
	 * @param x
	 * @param y
	 * @param isRotate
	 * @return
	 */
	public boolean isMember(int x,int y, boolean isRotate){
		int tmpStatus = status;;
		if(isRotate)
			tmpStatus = (status+1) % currShape.length;
		return currShape[tmpStatus][y*4 + x] == 1;
	}
	
	// ���ͼ�μ����¼�
	public void addshapeListener(ShapeListener sl){
		if(sl == null ){
			return;
		}
		
		if(this.shapeListener != null){
			throw new RuntimeException(new TooManyListenersException());
		}
		this.shapeListener = sl; 
		
		// ��ʼ��Shape�����������Ӻ󣬾������߳�
		new Thread(new shapeDriver()).start();
	}
	
	/**
	 * ����ͼ�μ���������ʱ��ͼ������
	 */
	private class shapeDriver implements Runnable{
		@Override
		public void run() {
			if(shapeListener == null){
				throw new RuntimeException("Shape����û��ע�� ShapeListener");
			}
			
			while(life && shapeListener.shapeIsMoveDownable(Shape.this)){
				if(!pause){
					moveDown();
					shapeListener.shapeMoveDown(Shape.this);
				}
				try{
					Thread.sleep(ConstantVariable.SPEED*1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			life = false;
		}
		
	}
	
}
