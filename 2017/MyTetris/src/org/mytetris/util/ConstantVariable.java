package org.mytetris.util;

import java.awt.Color;
import java.awt.color.ColorSpace;

/**
 * 定义固定的值
 */
public class ConstantVariable {
	// 设置每个单元格的宽高，以及单元格数量
	public static final int CELL_WIDTH = 20;
	public static final int CELL_HEIGHT = 20;
	public static final int CELL_WIDTH_NUMS = 15;
	public static final int CELL_HEIGHT_NUMS = 20;
	
	// 设置左边游戏画面的大小
	public static final int GAME_PANEL_WIDTH = CELL_WIDTH * CELL_WIDTH_NUMS;
	public static final int GAME_PANEL_HEIGHT = CELL_HEIGHT * CELL_HEIGHT_NUMS;
	
	// 设置右边计分的面板的大小 
	public static final int SCORE_PANEL_WIDTH = 170;
	public static final int SCORE_PANEL_HEIGHT = GAME_PANEL_HEIGHT;
	
	// 设置整个游戏面板的大小
	public static final int PANEL_WIDTH = GAME_PANEL_WIDTH + SCORE_PANEL_WIDTH;
	public static final int PANEL_HEIGHT = GAME_PANEL_HEIGHT + 50;
	

	// 消除每行的得分，以及得分位置
	public static final int BASE_SCORE = 10;
	public static final int SCOREX = GAME_PANEL_WIDTH + SCORE_PANEL_WIDTH / 2 ;
	public static final int SCOREY = GAME_PANEL_HEIGHT / 2;
	
	// 下落速度，表示几秒下落一次
	public static final int SPEED = 1;
	
	// 图形和障碍物的颜色
	public static final Color SHAPE_COLOR = new Color(0x990066);
	public static final Color GROUND_COLOR = Color.GRAY;
	
	
	// TYPES 是一个三维数组，记录图形的种类以及每个种类的形态和坐标
	// 第一维表示有多少种图形
	// 后面两维共同表示该图形某种形态所对应的坐标
	public static final int[][][] TYPES = new int[][][]{
		{
			{
				1,0,0,0,
				1,1,1,0,
				0,0,0,0,
				0,0,0,0
			},
			{
				1,1,0,0,
				1,0,0,0,
				1,0,0,0,
				0,0,0,0
			},
			{
				1,1,1,0,
				0,0,1,0,
				0,0,0,0,
				0,0,0,0
			},
			{
				0,1,0,0,
				0,1,0,0,
				1,1,0,0,
				0,0,0,0
			}
		},
	};
	
	
	public static boolean isNumeric(String str)
	{
		for(int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	
	
}
