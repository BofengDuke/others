package org.mytetris.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import org.mytetris.Enum.ShapeAction;
import org.mytetris.listener.GroundListener;
import org.mytetris.util.ConstantVariable;

/**
 * ͼ�ε����࣬ ���ڱ�־������ɺ�ĵ�ͼ�ζѵ��ĵ���
 */

public class Ground {
	private int[][] ground = new int[ConstantVariable.CELL_WIDTH_NUMS][ConstantVariable.CELL_HEIGHT_NUMS];
	
	protected Set<GroundListener> groundListeners = new HashSet<GroundListener>();
	
	private int score;		// ����
	protected Color color;	// �ϰ������ɫ
	private boolean fullToTop;	// �Ƿ��Ѿ���������������������Ϸ����
	
	/**
	 * ��ʼ���ϰ��ȫ������
	 */
	public void init() {
		fullToTop = false;
		score = 0;
		color = ConstantVariable.GROUND_COLOR;
		clear();
	}
	
	/**
	 * �������
	 */
	public void  clear() {
		for(int x = 0;x < ConstantVariable.CELL_WIDTH_NUMS;x++){
			for(int y=0; y < ConstantVariable.CELL_HEIGHT_NUMS;y++){
				ground[x][y] = 0;
			}
		}
	}
	
	/**
	 * ���Ƶ���
	 * @param g
	 */
	public void drawGround(Graphics g){
		for(int x=0;x<ConstantVariable.CELL_WIDTH_NUMS;x++){
			for(int y=0;y<ConstantVariable.CELL_HEIGHT_NUMS;y++){
				if(ground[x][y] == 1){
					g.setColor(color);
					g.fill3DRect(x*ConstantVariable.CELL_WIDTH, y*ConstantVariable.CELL_HEIGHT, ConstantVariable.CELL_WIDTH, ConstantVariable.CELL_HEIGHT, true);
				}
			}
		}
	};
	
	
	/**
	 * ���������ͼ���ںϳ��Լ���ͼ��
	 * @param shape
	 */
	public void accept(Shape shape){
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				if(shape.isMember(x,y,false)){
					if(shape.getTop() + y < 0){
						// ��������ϱ߽磬������ˣ�
						fullToTop = true;
						for(GroundListener l:groundListeners){
							l.groundIsFull(this);
						}
					}else{
						// ���û�У������ϰ���
						ground[shape.getLeft()+x][shape.getTop()+y] = 1;
					}
					
				}
			}
		}
		deleteFullLine();
	}
	
	/**
	 * �����е��н���ɾ��
	 */
	private void deleteFullLine(){
		for(int y=ConstantVariable.CELL_HEIGHT_NUMS-1; y>=0; y--){
			boolean isFullFlage = true;
			for(int x=0; x<ConstantVariable.CELL_WIDTH_NUMS;x++){
				if(ground[x][y] == 0){
					isFullFlage = false;
				}
			}
			if(isFullFlage){
				deleteLines(y);
			}
		}
	}
	
	
	/**
	 * ɾ��ָ����
	 * @param lineNum
	 */
	private void deleteLines(int lineNum){
		
		for(int y=lineNum;y>0;y--){
			for(int x=0;x<ConstantVariable.CELL_WIDTH_NUMS;x++){
				ground[x][y] = ground[x][y-1];
			}
		}
		// ���������ɵ�ͼ��Ҳ��Ϊ���ƶ���ʾ��������Ҫ����һ�����
		for(int x=0;x<ConstantVariable.CELL_WIDTH_NUMS;x++){
			ground[x][0] = 0;
		}
		
		score += ConstantVariable.BASE_SCORE;
		for(GroundListener l:groundListeners){
			l.fullLineDeleted(this, lineNum);
		}
	}
	
	/**
	 * �ж�ͼ���Ƿ��ܹ����ƣ����ƣ�����
	 * @param shape
	 * @param action
	 * @return
	 */
	public boolean isMoveable(Shape shape,ShapeAction action){
		int left  = shape.getLeft();
		int top = shape.getTop();
		
		switch(action){
		case LEFT:
			left--;
			break;
		case RIGHT:
			left++;
			break;
		case DOWN:
			top++;
			break;
		default:
			break;
		}
		
		
		
		// ����ȡ��ͼ�εĵ㣬�ж��Ƿ񳬳��߽�
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				// ������λ�ó����߽�����ͼ�ε�һ����
				if ((left + x < 0 || left + x >= ConstantVariable.CELL_WIDTH_NUMS || top + y >= ConstantVariable.CELL_HEIGHT_NUMS)
						&& shape.isMember(x, y, action == ShapeAction.ROTATE))
					return false;
				else if (top + y < 0)
					continue;
				else {
					// ����λ�ò��ǿհף����ϰ���򲻿��������ϰ������ͼ�ε�һ����
					if (shape.isMember(x, y, action == ShapeAction.ROTATE))
						if (ground[left + x][top + y] == 1)
							return false;
				}
			}
		}
		return true;
		
	}
	
	/**
	 * �ж϶ѵ��Ƿ��Ѿ�������,���ж϶����Ƿ������ 1 ������
	 * @return
	 */
	public boolean isFullToTop(){
		return fullToTop;
	}


	/**
	 * ��ȡ��ǰ�÷�
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * ��Ӽ�����������Ӷ��
	 * @param l
	 */
	public void addGroundListener(GroundListener l) {
		if(l != null){
			this.groundListeners.add(l);
		}
	}
	
	/**
	 * �Ƴ�ָ��������
	 * @param l
	 */
	public void removeGroundListener(GroundListener l){
		if(l != null){
			this.groundListeners.remove(l);
		}
	}
	
}




