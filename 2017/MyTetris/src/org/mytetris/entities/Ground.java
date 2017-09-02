package org.mytetris.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import org.mytetris.Enum.ShapeAction;
import org.mytetris.listener.GroundListener;
import org.mytetris.util.ConstantVariable;

/**
 * 图形叠块类， 用于标志下落完成后的的图形堆叠的叠块
 */

public class Ground {
	private int[][] ground = new int[ConstantVariable.CELL_WIDTH_NUMS][ConstantVariable.CELL_HEIGHT_NUMS];
	
	protected Set<GroundListener> groundListeners = new HashSet<GroundListener>();
	
	private int score;		// 分数
	protected Color color;	// 障碍物的颜色
	private boolean fullToTop;	// 是否已经满到顶部，满到顶则游戏结束
	
	/**
	 * 初始化障碍物，全部清零
	 */
	public void init() {
		fullToTop = false;
		score = 0;
		color = ConstantVariable.GROUND_COLOR;
		clear();
	}
	
	/**
	 * 清空容器
	 */
	public void  clear() {
		for(int x = 0;x < ConstantVariable.CELL_WIDTH_NUMS;x++){
			for(int y=0; y < ConstantVariable.CELL_HEIGHT_NUMS;y++){
				ground[x][y] = 0;
			}
		}
	}
	
	/**
	 * 绘制叠块
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
	 * 将新下落的图形融合成自己的图形
	 * @param shape
	 */
	public void accept(Shape shape){
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				if(shape.isMember(x,y,false)){
					if(shape.getTop() + y < 0){
						// 如果超出上边界，则放满了，
						fullToTop = true;
						for(GroundListener l:groundListeners){
							l.groundIsFull(this);
						}
					}else{
						// 如果没有，则变成障碍物
						ground[shape.getLeft()+x][shape.getTop()+y] = 1;
					}
					
				}
			}
		}
		deleteFullLine();
	}
	
	/**
	 * 对满行的行进行删除
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
	 * 删除指定行
	 * @param lineNum
	 */
	private void deleteLines(int lineNum){
		
		for(int y=lineNum;y>0;y--){
			for(int x=0;x<ConstantVariable.CELL_WIDTH_NUMS;x++){
				ground[x][y] = ground[x][y-1];
			}
		}
		// 避免新生成的图形也因为下移而显示，，所以要将第一行清除
		for(int x=0;x<ConstantVariable.CELL_WIDTH_NUMS;x++){
			ground[x][0] = 0;
		}
		
		score += ConstantVariable.BASE_SCORE;
		for(GroundListener l:groundListeners){
			l.fullLineDeleted(this, lineNum);
		}
	}
	
	/**
	 * 判断图形是否能够左移，右移，下移
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
		
		
		
		// 依次取出图形的点，判断是否超出边界
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				// 如果这个位置超出边界又是图形的一部分
				if ((left + x < 0 || left + x >= ConstantVariable.CELL_WIDTH_NUMS || top + y >= ConstantVariable.CELL_HEIGHT_NUMS)
						&& shape.isMember(x, y, action == ShapeAction.ROTATE))
					return false;
				else if (top + y < 0)
					continue;
				else {
					// 或者位置不是空白（是障碍物或不可消除的障碍物）又是图形的一部分
					if (shape.isMember(x, y, action == ShapeAction.ROTATE))
						if (ground[left + x][top + y] == 1)
							return false;
				}
			}
		}
		return true;
		
	}
	
	/**
	 * 判断堆叠是否已经到顶部,及判断顶行是否有填充 1 的数据
	 * @return
	 */
	public boolean isFullToTop(){
		return fullToTop;
	}


	/**
	 * 获取当前得分
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 添加监听器，课添加多个
	 * @param l
	 */
	public void addGroundListener(GroundListener l) {
		if(l != null){
			this.groundListeners.add(l);
		}
	}
	
	/**
	 * 移除指定监听器
	 * @param l
	 */
	public void removeGroundListener(GroundListener l){
		if(l != null){
			this.groundListeners.remove(l);
		}
	}
	
}




