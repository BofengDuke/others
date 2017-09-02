package org.mytetris.control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.mytetris.Enum.ShapeAction;
import org.mytetris.entities.Ground;
import org.mytetris.entities.Shape;
import org.mytetris.entities.ShapeFactory;
import org.mytetris.listener.GameListener;
import org.mytetris.listener.GroundListener;
import org.mytetris.listener.ShapeListener;
import org.mytetris.view.GamePanel;
import org.mytetris.view.ScorePanel;

/**
 * 控制器实现的功能：
 * 一： 实现图形下落的监听事件方法
 * 二：实现键盘监听事件
 *
 */

/**
 *  局域网过程：
 *  1,在MainGame 中添加一个 创建房间，加入房间的页面
 *  2,创建房间，则创建一个ServerSocket, 并将IP和PORT 写入文件，以房间名为文件名
 *  3，创建完房间后，进自动进入房间，等待另一玩家进入，进入后才可以开始游戏
 *  
 *  4,选择房间加入，则从对应文件中，读取serverSocker的IP和PORT，
 *  5,进入房间后，及游戏界面中，与对方服务端玩家建立socket连接
 *  
 *  6,由服务端玩家点击开始游戏，游戏开始
 *  7，双方互相发生指令
 *  
 *  8，游戏自动结束，或者玩家按下结束游戏按键，，则游戏结束
 *
 */

/**
 *  进入房间就开始服务端监听
 *  后进房间的一方，就发送 ready 命令，同时先进的一方也回ready命令，同时获取对方的IP和端口
 *  ready后，通过获取到的IP和端口，增加客户端socket
 * 
 *
 */

public class Controller extends KeyAdapter implements ShapeListener,GroundListener{
	private Shape shape;
	private Ground ground;
	private ShapeFactory shapeFactory;
	private GamePanel gamePanel;
	private ScorePanel scorePanel;
	
	private SocketThread socketThread = null;
	
	private boolean playing;

	protected Set<GameListener> gameListeners = new HashSet<GameListener>();
	
	public Controller(){};
	
	public Controller(Ground ground,ShapeFactory shapeFactory,GamePanel gamePanel,ScorePanel scorePanel){
		super();
		this.gamePanel = gamePanel;
		this.shapeFactory = shapeFactory;
		this.ground = ground;
		this.scorePanel = scorePanel;
	}

	
	/* 处理键盘按键响应
	 * UP： 变形
	 * DOWN: 加速下落
	 * LEFT: 向左移动
	 * RIGHT： 向右移动
	 * ENTER： 暂停/继续
	 */
	@Override
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			if(ground.isMoveable(shape, ShapeAction.ROTATE)){
				shape.rotate();
				if(socketThread != null){
					socketThread.sendMessage("rotate");
				}
			}
			break;
		case KeyEvent.VK_DOWN:
			if(shapeIsMoveDownable(shape)){
				shape.moveDown();
				System.out.println("11+ "+shape.getTop());
				if(socketThread != null){
					socketThread.sendMessage("down");
				}
			}
			break;
		case KeyEvent.VK_LEFT:
			if(ground.isMoveable(shape,ShapeAction.LEFT)){
				shape.moveLeft();
				if(socketThread != null){
					socketThread.sendMessage("left");
				}
				
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(ground.isMoveable(shape,ShapeAction.RIGHT)){
				shape.moveRight();
				if(socketThread != null){
					socketThread.sendMessage("right");
				}

			}
			break;
		case KeyEvent.VK_ENTER:
			if(isPausingGame()){
				this.continueGame();
			}else{
				this.pauseGame();
			}
			break;
		}
		
		// 每次响应完，都要重新重绘页面
		gamePanel.display(ground, shape);
	}
	
	
	/**
	 * 当前游戏暂停
	 */
	public void pauseGame(){
		if(shape == null){
			return;
		}
		if(playing){
			shape.setPause(true);
		}
		for(GameListener l : gameListeners){
			l.gamePause();
		}
	}
	
	/**
	 * 继续游戏
	 */
	public void continueGame() {
		shape.setPause(false);
		for(GameListener l:gameListeners){
			l.gameContinue();
		}
	}
	
	/**
	 * 开始一个新游戏
	 * @throws IOException 
	 */
	public void newGame() throws IOException{
		playing = true;
		ground.init();
		ground.addGroundListener(this);
		
		if(socketThread != null){
			socketThread.sendMessage("startGame");
		}
		
		if(playing){
			shape = shapeFactory.getShape(this);
			if(socketThread != null){
				socketThread.sendMessage(String.valueOf(shape.getShapeType()));
			}
		}	
		for(GameListener l:gameListeners){
			l.gameStart();
		}
	}
	
	/**
	 * 停止当前游戏
	 * @throws IOException 
	 */
	public void stopGame() throws IOException{
		if(shape == null){
			return;
		}

		shape.setPause(true);
		shape.die();
		playing = false;
		for(GameListener l:gameListeners){
			l.gameStop();
		}
	}
	
	/**
	 * 游戏是否处于暂停状态
	 * @return
	 */
	public boolean isPausingGame(){
		return shape.isPause();
	}
	
	/**
	 * 判断是否正在游戏中
	 * @return
	 */
	public boolean isPlaying() {
		if(playing && !ground.isFullToTop()){
			return true;
		}
		return false;
	}
	
	
	@Override
	public void shapeMoveDown(Shape shape) {
		gamePanel.display(ground, shape);
	}

	@Override
	public synchronized  boolean shapeIsMoveDownable(Shape shape) {
		if(this.shape != shape){
			return false;
		}
		if(ground.isMoveable(shape, ShapeAction.DOWN)){
			return true;
		}
		
		shape.die();
		ground.accept(this.shape);
		if(!ground.isFullToTop()){
			this.shape = shapeFactory.getShape(this);
		}
		return false;
	}


	@Override
	public void fullLineDeleted(Ground ground, int deletedLineCount) {
		// TODO Auto-generated method stub
		scorePanel.updateScore(ground);
	}

	@Override
	public void groundIsFull(Ground ground) {
		if(playing){
			playing = false;
			for(GameListener l:gameListeners){
				l.gameStop();
			}
		}
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


	public void setSocketThread(SocketThread socketThread) {
		this.socketThread = socketThread;
	}
	

	
	
}
