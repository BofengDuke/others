package org.mytetris.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.TooManyListenersException;

import org.mytetris.listener.ShapeListener;
import org.mytetris.util.ConstantVariable;

/**
 * 图形类，具有下落，左移，右移，旋转的方法
 * 同时还具有图形显示的方法
 * 图形应该通过事件监听器来实现定时下落且重绘的方法
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
	private int shapeType;		// 表示当前图像的类型，如 L， T ，I 等等
	private int currShape[][];	// 表示当前的图形类型几个状态的坐标构成
	private int status;		// 用于存放当前图形的状态
	
	private int top;		// 图形和上边界以及左边界的距离
	private int left;
	
	private int height = 0;		// 图形的高度
	
	protected boolean pause;	// 暂停状态
	protected boolean life;		// 生命，如果游戏结束则消失
	
	protected Color color;	// 方块的初始颜色

	protected ShapeListener shapeListener;	// 图形下落监听器
	
	public Shape(int type){
		super();
		this.shapeType = type;
		this.currShape = ConstantVariable.TYPES[type];
		
		init();
	}
	
	/**
	 * 对图形进行初始化
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

	
	// 通过与上边界与左边界的距离，判断图形是否能够进行移动
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
	 * 获取当前图形的类型
	 * @return
	 */
	public int getShapeType() {
		return shapeType;
	}

	/**
	 * 设置图形类型,并进行初始化
	 * @param shapeType
	 */
	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
		this.currShape = ConstantVariable.TYPES[shapeType];
		init();
	}

	// 设置图形的状态
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	// 图形下移
	public void moveDown(){
		top++;
	}
	
	// 图形右移
	public void moveRight(){
		left++;
	}
	
	// 图形左移
	public void moveLeft(){
		left--;
	}
	
	// 旋转图形
	public void rotate(){
		status = (status+1) % currShape.length; 
	}
	
	// 返回暂停的状态
	public boolean isPause() {
		return pause;
	}
	
	/**
	 * 设置暂停
	 * @param pause
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	/**
	 * 改变暂停的状态
	 */
	public void changePause(){
		this.pause = !this.pause;
	}
	
	
	/**
	 * 结束图形定时下落的线程
	 */
	public synchronized void die() {
		this.life = false;
	}
	
	
	/**
	 * 绘制图形,单个图形是用 4 x 4 的矩阵来表示，
	 * 在矩阵中，只有值为 1 的格子，才会被填充
	 * @param g 让被填充的格子显示
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
	 * 相对坐标（x，y）是否是图形中的点
	 * @param status
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean getFlagByPoint(int status,int x,int y){
		return currShape[status][y*4 + x] == 1;
	}
	
	/**
	 * 指定位置是否是图像的一部分
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
	
	// 添加图形监听事件
	public void addshapeListener(ShapeListener sl){
		if(sl == null ){
			return;
		}
		
		if(this.shapeListener != null){
			throw new RuntimeException(new TooManyListenersException());
		}
		this.shapeListener = sl; 
		
		// 初始化Shape，并添加了添加后，就启动线程
		new Thread(new shapeDriver()).start();
	}
	
	/**
	 * 启用图形监听器，定时让图形下落
	 */
	private class shapeDriver implements Runnable{
		@Override
		public void run() {
			if(shapeListener == null){
				throw new RuntimeException("Shape类中没有注册 ShapeListener");
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
