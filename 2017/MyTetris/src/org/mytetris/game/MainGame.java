package org.mytetris.game;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.mytetris.control.Controller;
import org.mytetris.control.LanController;
import org.mytetris.control.TcpClient;
import org.mytetris.control.TcpServer;
import org.mytetris.entities.ShapeFactory;
import org.mytetris.listener.GameListener;
import org.mytetris.util.ConstantVariable;
import org.mytetris.entities.Ground;
import org.mytetris.view.GamePanel;
import org.mytetris.view.LanGameMenuPanel;
import org.mytetris.view.MainMenuPanel;
import org.mytetris.view.ScorePanel;

public class MainGame implements GameListener {
	
	private GamePanel gamePanel;		// 游戏界面面板
	private ScorePanel scorePanel;		// 得分面板
	private ShapeFactory shapeFactory;	// 图形工厂
	private Ground ground;				// 图形堆叠部分
	private Controller controller;		// 控制器
	

	private static MainGame game;
	
	
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try{ 
					game = new MainGame();
					game.mainMenu();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 游戏主菜单
	 */
	public void mainMenu() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(frame.getToolkit().getScreenSize().width/2 - ConstantVariable.PANEL_WIDTH/2,
								frame.getToolkit().getScreenSize().height/2 - ConstantVariable.PANEL_HEIGHT/2,
								ConstantVariable.PANEL_WIDTH,
								ConstantVariable.PANEL_HEIGHT);
		
		MainMenuPanel mainMenuPanel = new MainMenuPanel();
		mainMenuPanel.getSinglePlayerBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				game.singlePlayerGame();
			}
		});
		
		mainMenuPanel.getLanGameBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				game.lanGameMenu();
			}
		});
		
		mainMenuPanel.getRankListBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				game.rankList();
			}
		});
		
		mainMenuPanel.getExitBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exitGame();
			}
		});
		

		frame.add(mainMenuPanel);
		frame.setVisible(true);
		
	}
	

	/**
	 * 单人游戏
	 */
	public void singlePlayerGame(){
		shapeFactory = new ShapeFactory();
		ground = new Ground();
		gamePanel = new GamePanel();
		scorePanel = new ScorePanel();
		controller = new Controller(ground,shapeFactory,gamePanel,scorePanel);
		
		controller.addGameListener(this);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(ConstantVariable.PANEL_WIDTH,ConstantVariable.PANEL_HEIGHT);
		frame.setLocation(frame.getToolkit().getScreenSize().width / 2 - frame.getWidth() / 2, 
						  frame.getToolkit().getScreenSize().height/ 2 - frame.getHeight() / 2);	// 居中
		
		frame.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				controller.pauseGame();
			}
			@Override 
			public void focusGained(FocusEvent arg0) {
			}
		});
		
		frame.setLayout(null);
		
		gamePanel.setLocation(0, 0);
		scorePanel.setLocation(gamePanel.getSize().width, 0);
		
		// 设置游戏面板的游戏控制监听
		gamePanel.addKeyListener(controller);
		
		scorePanel.getNewGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.requestFocus();
				if(controller.isPlaying()){
					return;
				}
				try {
					controller.newGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		scorePanel.getStopGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.stopGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		scorePanel.getPauseButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(controller.isPausingGame()){
					gamePanel.requestFocus();
					controller.continueGame();
				}else{
					controller.pauseGame();
				}
			}
		});
		
		scorePanel.getReturnButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				game.mainMenu();
			}
		});
		
		frame.add(gamePanel);
		frame.add(scorePanel);
		frame.addKeyListener(controller);
		frame.setVisible(true);
		
	}
	
	public void lanGameMenu() {
			
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(frame.getToolkit().getScreenSize().width/2 - ConstantVariable.PANEL_WIDTH/2,
								frame.getToolkit().getScreenSize().height/2 - ConstantVariable.PANEL_HEIGHT/2,
								ConstantVariable.PANEL_WIDTH,
								ConstantVariable.PANEL_HEIGHT);
	
		frame.setLayout(null);
		LanGameMenuPanel lanGameMenuPanel = new LanGameMenuPanel();
		lanGameMenuPanel.getNewRoomBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String  roomNum;
				int port = 0;
				roomNum = JOptionPane.showInputDialog(null,"输入房间号：10000-20000");
				if(!ConstantVariable.isNumeric(roomNum)){
					port = Integer.valueOf(roomNum);
					if(port<10000 || port > 20000){
						JOptionPane.showMessageDialog(null,"房间号不符合", "错误！",JOptionPane.ERROR_MESSAGE);
						return;
					}
					JOptionPane.showMessageDialog(null,"输入的地址有错", "错误！",JOptionPane.ERROR_MESSAGE);
					return;
				}
				port = Integer.parseInt(roomNum);
				boolean isCreater = true;
				frame.setVisible(false);
				game.LANGame(port,isCreater);
			}
		});
		lanGameMenuPanel.getJoinRoomBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String  roomNum;
				int port = 0;
				roomNum = JOptionPane.showInputDialog(null,"输入房间号：10000-20000");
				if(!ConstantVariable.isNumeric(roomNum)){
					port = Integer.valueOf(roomNum);
					if(port<10000 || port > 20000){
						JOptionPane.showMessageDialog(null,"房间号不符合", "错误！",JOptionPane.ERROR_MESSAGE);
						return;
					}
					JOptionPane.showMessageDialog(null,"输入的地址有错", "错误！",JOptionPane.ERROR_MESSAGE);
					return;
				}
				port = Integer.parseInt(roomNum);
				boolean isCreater = false;
				
				frame.setVisible(false);
				game.LANGame(port,isCreater);
				
			}
		});
		lanGameMenuPanel.getReturnBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				game.mainMenu();
			}
		});
		
		frame.add(lanGameMenuPanel);
		frame.setVisible(true);
	}
	

	/**
	 * 局域网游戏
	 */
	public void LANGame(int port,boolean isCreater) {	
		
		// 初始化游戏参数
		shapeFactory = new ShapeFactory();
		ground = new Ground();
		gamePanel = new GamePanel();
		scorePanel = new ScorePanel();
		controller = new Controller(ground,shapeFactory,gamePanel,scorePanel);
		controller.addGameListener(this);
		
		if(isCreater){
			System.out.println("创建者+ "+port);
			TcpServer.Init(port);
			System.out.println("创建者连接成功");
			controller.setSocketThread(TcpServer.getSocketThread());
		}else {
			TcpClient.Init(port);
			System.out.println("加入者连接成功");
			controller.setSocketThread(TcpClient.getSocketThread());
		}
		
		// 游戏面板都增加按键监听
		gamePanel.addKeyListener(controller);
		
		// 对方游戏面板
		GamePanel gamePanel2 = new GamePanel();
		ScorePanel scorePanel2 = new ScorePanel();
		Ground ground2 = new Ground();
		
		
		
		LanController.lanController = new LanController(ground2, gamePanel2, scorePanel2);
		
		LanController.lanController.addGameListener(this);
		
		// 四个游戏面板位置
		gamePanel.setLocation(0, 0);
		scorePanel.setLocation(gamePanel.getSize().width, 0);
		gamePanel2.setLocation(gamePanel.getSize().width+scorePanel.getSize().width, 0);
		scorePanel2.setLocation(ConstantVariable.GAME_PANEL_WIDTH+ConstantVariable.SCORE_PANEL_WIDTH+ConstantVariable.GAME_PANEL_WIDTH, 0);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(ConstantVariable.PANEL_WIDTH *2,ConstantVariable.PANEL_HEIGHT);
		frame.setLocation(frame.getToolkit().getScreenSize().width / 2 - frame.getWidth() / 2, 
						  frame.getToolkit().getScreenSize().height/ 2 - frame.getHeight() / 2);	// 居中
		frame.setLayout(null);
		
		// 设置对frame的焦点监听，失去焦点，则游戏暂停
		frame.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				controller.pauseGame();
			}
			@Override 
			public void focusGained(FocusEvent arg0) {
			}
		});
		
		try {
			controller.newGame();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// 分数面板中，按钮的点击监听
		scorePanel.getNewGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.requestFocus();
				if(controller.isPlaying()){
					return;
				}
				try {
					controller.newGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		scorePanel.getStopGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.stopGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		scorePanel.getPauseButton().setVisible(false);
		scorePanel.getReturnButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				game.mainMenu();
			}
		});
		
		
		// 对方分数面板中，按钮的隐藏
		scorePanel2.getNewGameButton().setVisible(false);
		scorePanel2.getStopGameButton().setVisible(false);
		scorePanel2.getPauseButton().setVisible(false);
		scorePanel2.getReturnButton().setVisible(false);
		
		frame.add(gamePanel);
		frame.add(scorePanel);
		frame.add(gamePanel2);
		frame.add(scorePanel2);

		frame.setVisible(true);
		
	}
	

	/**
	 * 排行榜
	 */
	public void rankList(){
		System.out.println("排行榜");
	}
	
	public void exitGame() {
		System.exit(0);
	}
	
	@Override
	public void gameStart() {
		scorePanel.getPauseButton().setEnabled(true);
		scorePanel.getPauseButton().setText("暂停游戏");
		scorePanel.getStopGameButton().setEnabled(true);
		scorePanel.getNewGameButton().setEnabled(false);
		scorePanel.getReturnButton().setEnabled(false);
	}

	@Override
	public void gameStop() {
		scorePanel.getPauseButton().setEnabled(false);
		scorePanel.getPauseButton().setText("暂停/继续");
		scorePanel.getNewGameButton().setEnabled(true);
		scorePanel.getStopGameButton().setEnabled(false);
		scorePanel.getReturnButton().setEnabled(true);
		JOptionPane.showMessageDialog(null, "Game Over!");
		
	}

	@Override
	public void gamePause() {
		scorePanel.getPauseButton().setText("继续游戏");
		JOptionPane.showMessageDialog(null, "游戏暂停");
	}

	@Override
	public void gameContinue() {
		scorePanel.getPauseButton().setText("暂停游戏");
	}
	

	

}
