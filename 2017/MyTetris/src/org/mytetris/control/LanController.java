package org.mytetris.control;

import java.util.HashSet;
import java.util.Set;

import org.mytetris.entities.Ground;
import org.mytetris.entities.Shape;
import org.mytetris.listener.GameListener;
import org.mytetris.listener.GroundListener;
import org.mytetris.listener.ShapeListener;
import org.mytetris.view.GamePanel;
import org.mytetris.view.ScorePanel;

/**
 * 局域网中，根据TCP传输的值，页面对对战方的面板控制
 *
 */

public class LanController  implements ShapeListener,GroundListener {

	public static LanController lanController;
	
	private static Shape shape = null;
	private static Ground ground;
	private static GamePanel gamePanel;
	private static ScorePanel scorePanel;
	
	// 用于监听自己的游戏状态，判断当前游戏是否结束
	protected Set<GameListener> gameListeners = new HashSet<GameListener>();

	
	public LanController(Ground ground,GamePanel gamePanel,ScorePanel scorePanel){
		super();
		LanController.ground = ground;
		LanController.gamePanel = gamePanel;
		LanController.scorePanel = scorePanel;

	}
	
	public void startGame() {
		ground.init();
		ground.addGroundListener(this);
		System.out.println("startGame111");
	}

	public void rotate() {
		shape.rotate();
	}
	
	public void moveDown() {
		shape.moveDown();
		System.out.println(shape.getTop());
	}
	
	public void moveLeft() {
		shape.moveLeft();
		System.out.println(shape.getShapeType());
	}
	
	public void moveRight() {
		shape.moveRight();
	}
	
	public void accpetShape() {
		ground.accept(shape);
	}
	
	public void gameover() {
		System.out.println("game over!");
	}
	
	

	@Override
	public void fullLineDeleted(Ground ground, int deletedLineCount) {
		// TODO Auto-generated method stub
		scorePanel.updateScore(ground);
	}

	@Override
	public void groundIsFull(Ground ground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shapeMoveDown(Shape shape) {
		gamePanel.display(ground, shape);
		
	}

	@Override
	public boolean shapeIsMoveDownable(Shape shape) {

		return false;
	}
	
	/**
	 * 添加游戏监听器，可以添加多个
	 * @param l
	 */
	public void addGameListener(GameListener l) {
		if(l != null){
			this.gameListeners.add(l);
		}
	}
	
	/**
	 * 移除监听器
	 * @param l
	 */
	public void removeGameListener(GameListener l) {
		if(l != null){
			this.gameListeners.remove(l);
		}
	}

	public static Shape getShape() {
		return shape;
	}

	public static void setShape(Shape shape) {
		LanController.shape = shape;
		shape.addshapeListener(lanController);
	}

}
