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
 * ������ʵ�ֵĹ��ܣ�
 * һ�� ʵ��ͼ������ļ����¼�����
 * ����ʵ�ּ��̼����¼�
 *
 */

/**
 *  ���������̣�
 *  1,��MainGame �����һ�� �������䣬���뷿���ҳ��
 *  2,�������䣬�򴴽�һ��ServerSocket, ����IP��PORT д���ļ����Է�����Ϊ�ļ���
 *  3�������귿��󣬽��Զ����뷿�䣬�ȴ���һ��ҽ��룬�����ſ��Կ�ʼ��Ϸ
 *  
 *  4,ѡ�񷿼���룬��Ӷ�Ӧ�ļ��У���ȡserverSocker��IP��PORT��
 *  5,���뷿��󣬼���Ϸ�����У���Է��������ҽ���socket����
 *  
 *  6,�ɷ������ҵ����ʼ��Ϸ����Ϸ��ʼ
 *  7��˫�����෢��ָ��
 *  
 *  8����Ϸ�Զ�������������Ұ��½�����Ϸ������������Ϸ����
 *
 */

/**
 *  ���뷿��Ϳ�ʼ����˼���
 *  ��������һ�����ͷ��� ready ���ͬʱ�Ƚ���һ��Ҳ��ready���ͬʱ��ȡ�Է���IP�Ͷ˿�
 *  ready��ͨ����ȡ����IP�Ͷ˿ڣ����ӿͻ���socket
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

	
	/* ������̰�����Ӧ
	 * UP�� ����
	 * DOWN: ��������
	 * LEFT: �����ƶ�
	 * RIGHT�� �����ƶ�
	 * ENTER�� ��ͣ/����
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
		
		// ÿ����Ӧ�꣬��Ҫ�����ػ�ҳ��
		gamePanel.display(ground, shape);
	}
	
	
	/**
	 * ��ǰ��Ϸ��ͣ
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
	 * ������Ϸ
	 */
	public void continueGame() {
		shape.setPause(false);
		for(GameListener l:gameListeners){
			l.gameContinue();
		}
	}
	
	/**
	 * ��ʼһ������Ϸ
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
	 * ֹͣ��ǰ��Ϸ
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
	 * ��Ϸ�Ƿ�����ͣ״̬
	 * @return
	 */
	public boolean isPausingGame(){
		return shape.isPause();
	}
	
	/**
	 * �ж��Ƿ�������Ϸ��
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
	 * �����Ϸ��������������Ӷ��
	 * @param l
	 */
	public void addGameListener(GameListener l) {
		if(l != null){
			this.gameListeners.add(l);
		}
	}
	
	/**
	 * �Ƴ�������
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
