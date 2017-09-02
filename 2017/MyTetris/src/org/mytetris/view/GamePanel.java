package org.mytetris.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JPanel;
import org.mytetris.entities.Shape;
import org.mytetris.entities.Ground;
import org.mytetris.util.ConstantVariable;

/**
 * GamePanel�࣬���ڻ���������Ϸ���棬�Լ�ͼ�ε��ػ�
 */
public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Ground ground;
	private Shape shape;
	
	public GamePanel(){
		this.setSize(ConstantVariable.GAME_PANEL_WIDTH,ConstantVariable.GAME_PANEL_HEIGHT);
		this.setLayout(new FlowLayout());
		this.setFocusable(true);
	}
	
	
	/** 
	 * ������Ϸ����
	 * @param ground
	 * @param shape
	 */
	public synchronized void display(Ground ground,Shape shape){
		this.ground = ground;
		this.shape = shape;
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		g.setColor(new Color(0xcfcfcf));
		g.fillRect(0, 0, ConstantVariable.GAME_PANEL_WIDTH, ConstantVariable.GAME_PANEL_HEIGHT);
		if(ground != null && shape != null){
			shape.drawShape(g);
			ground.drawGround(g);
		}
		

	}
	
	
}
