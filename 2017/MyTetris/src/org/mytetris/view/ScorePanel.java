package org.mytetris.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;

import org.mytetris.entities.Ground;
import org.mytetris.util.ConstantVariable;

public class ScorePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Ground ground;
	
	private final JButton newGameButton = new JButton();
	private final JButton stopGameButton = new JButton();
	private final JButton returnButton = new JButton();
	private final JButton pauseButton = new JButton();
	
	private JLabel scoreLabel = new JLabel();	// ��Ϸ����
	

	public ScorePanel() {
		super();
		
		this.setSize(ConstantVariable.SCORE_PANEL_WIDTH,ConstantVariable.SCORE_PANEL_HEIGHT+30);
		this.setLayout(null);
		this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		this.setFocusable(false);
	
		final JSeparator separator = new JSeparator();
		separator.setOrientation(JSeparator.VERTICAL);
		separator.setBounds(0,0,ConstantVariable.SCORE_PANEL_WIDTH,ConstantVariable.GAME_PANEL_HEIGHT);
		this.add(separator);
		
		final JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0,0,ConstantVariable.SCORE_PANEL_WIDTH,50);
		this.add(separator_1);
		
		final JLabel label_1 = new JLabel();
		label_1.setBounds(10, 10, 60, 15);
		label_1.setFont(new Font("����", Font.PLAIN, 12));
		label_1.setText("����: ");
		separator_1.add(label_1);
		
		scoreLabel.setBounds(80, 10 , 120, 15);
		scoreLabel.setFont(new Font("����",	 Font.PLAIN, 18));
		scoreLabel.setText("0");
		separator_1.add(scoreLabel);
		
		final JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0,50,ConstantVariable.SCORE_PANEL_WIDTH,35*4);
		this.add(separator_2);
		
		pauseButton.setBounds(10,10,103,25);
		pauseButton.setText("��ͣ/����");
		pauseButton.setFont(new Font("����", Font.PLAIN, 12));
		pauseButton.setEnabled(false);
		separator_2.add(pauseButton);
		
		stopGameButton.setBounds(10,45,103,25);
		stopGameButton.setText("ֹͣ��Ϸ");
		stopGameButton.setFont(new Font("����", Font.PLAIN, 12));
		stopGameButton.setEnabled(false);
		separator_2.add(stopGameButton);
		
		newGameButton.setBounds(10,80,103,25);
		newGameButton.setText("�½���Ϸ");
		newGameButton.setFont(new Font("����", Font.PLAIN, 12));
		newGameButton.setEnabled(true);
		separator_2.add(newGameButton);
		
		returnButton.setBounds(10,115,103,25);
		returnButton.setText("���ز˵�");
		returnButton.setFont(new Font("����", Font.PLAIN, 12));
		returnButton.setEnabled(true);
		separator_2.add(returnButton);
	
	}
	
	
	/**
	 * ���·���
	 * @param ground
	 */
	public void updateScore(Ground ground) {
		this.ground = ground;
		scoreLabel.setText(String.valueOf(ground.getScore()));
	}
	
	/**
	 * @param ground
	 */
	public void display(Ground ground) {
		this.ground = ground;
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		if(ground != null){
			scoreLabel.setText(String.valueOf(ground.getScore()));
		}
	}

	public JButton getNewGameButton() {
		return newGameButton;
	}

	public JButton getStopGameButton() {
		return stopGameButton;
	}

	public JButton getReturnButton() {
		return returnButton;
	}

	public JButton getPauseButton() {
		return pauseButton;
	}



	
	
}
