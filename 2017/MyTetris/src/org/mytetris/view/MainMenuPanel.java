package org.mytetris.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.mytetris.util.ConstantVariable;

public class MainMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton singlePlayerBtn;	// ������Ϸ��ť
	private JButton lanGameBtn;			// ��������Ϸ��ť
	private JButton rankListBtn;		// ���а�ť
	private JButton exitBtn;			// �˳���ť
	
	
	public MainMenuPanel(){
		super();
		this.setSize(ConstantVariable.PANEL_WIDTH,ConstantVariable.PANEL_HEIGHT);
		this.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(this.getSize().width/2 - 50,this.getSize().height/2 - 100,100,150);
		this.add(separator);
		
		int mt = 10;	// margin top 
		int bh = 25;	// button height
		
		singlePlayerBtn = new JButton();
		singlePlayerBtn.setBounds(0,mt,100,25);
		singlePlayerBtn.setText("������Ϸ");
		singlePlayerBtn.setFont(new Font("����",Font.PLAIN,12));
		separator.add(singlePlayerBtn);
		
		lanGameBtn = new JButton();
		lanGameBtn.setBounds(0,mt+bh+mt,100,25);
		lanGameBtn.setText("��������Ϸ");
		lanGameBtn.setFont(new Font("����", Font.PLAIN, 12));
		separator.add(lanGameBtn);
		
		rankListBtn = new JButton();
		rankListBtn.setBounds(0,2*(mt+bh)+mt,100,25);
		rankListBtn.setText("���а�");
		rankListBtn.setFont(new Font("����", Font.PLAIN, 12));
		separator.add(rankListBtn);
		
		exitBtn = new JButton();
		exitBtn.setBounds(0,3*(mt+bh)+mt,100,25);
		exitBtn.setText("�˳���Ϸ");
		exitBtn.setFont(new Font("����", Font.PLAIN, 12));
		separator.add(exitBtn);
		
		
		
	}
	
	/**
	 * ��ȡ������Ϸ��ť
	 * @return
	 */
	public JButton getSinglePlayerBtn() {
		return singlePlayerBtn;
	}

	public JButton getLanGameBtn() {
		return lanGameBtn;
	}

	public JButton getRankListBtn() {
		return rankListBtn;
	}

	public JButton getExitBtn() {
		return exitBtn;
	}



}
