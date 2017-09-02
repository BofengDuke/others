package org.mytetris.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.mytetris.util.ConstantVariable;

public class LanGameMenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static JButton newRoomBtn;		// 创建新的房间
	private static JButton joinRoomBtn;		// 加入房间
	private static JButton returnBtn;		// 返回按钮
	
	public LanGameMenuPanel() {
		super();
		this.setSize(ConstantVariable.PANEL_WIDTH,ConstantVariable.PANEL_HEIGHT);
		this.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(this.getSize().width/2 - 80,this.getSize().height/2 - 100,160,150);
		this.add(separator);
		
		int mt = 10;	// margin top 
		int bh = 25;	// button height
		
		newRoomBtn = new JButton();
		newRoomBtn.setBounds(0,0,160,25);
		newRoomBtn.setText("创建房间");
		newRoomBtn.setFont(new Font("宋体",Font.PLAIN,12));
		separator.add(newRoomBtn);
		
		joinRoomBtn = new JButton();
		joinRoomBtn.setBounds(0,35,160,25);
		joinRoomBtn.setText("加入房间");
		joinRoomBtn.setFont(new Font("宋体",Font.PLAIN,12));
		separator.add(joinRoomBtn);
		
		returnBtn = new JButton();
		returnBtn.setBounds(0,70,160,25);
		returnBtn.setText("返回菜单");
		returnBtn.setFont(new Font("宋体",Font.PLAIN,12));
		separator.add(returnBtn);
		

	}

	public static JButton getNewRoomBtn() {
		return newRoomBtn;
	}

	public static JButton getJoinRoomBtn() {
		return joinRoomBtn;
	}

	public static JButton getReturnBtn() {
		return returnBtn;
	}
	
	
	
	
}
