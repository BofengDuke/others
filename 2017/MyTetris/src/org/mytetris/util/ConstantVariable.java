package org.mytetris.util;

import java.awt.Color;
import java.awt.color.ColorSpace;

/**
 * ����̶���ֵ
 */
public class ConstantVariable {
	// ����ÿ����Ԫ��Ŀ�ߣ��Լ���Ԫ������
	public static final int CELL_WIDTH = 20;
	public static final int CELL_HEIGHT = 20;
	public static final int CELL_WIDTH_NUMS = 15;
	public static final int CELL_HEIGHT_NUMS = 20;
	
	// ���������Ϸ����Ĵ�С
	public static final int GAME_PANEL_WIDTH = CELL_WIDTH * CELL_WIDTH_NUMS;
	public static final int GAME_PANEL_HEIGHT = CELL_HEIGHT * CELL_HEIGHT_NUMS;
	
	// �����ұ߼Ʒֵ����Ĵ�С 
	public static final int SCORE_PANEL_WIDTH = 170;
	public static final int SCORE_PANEL_HEIGHT = GAME_PANEL_HEIGHT;
	
	// ����������Ϸ���Ĵ�С
	public static final int PANEL_WIDTH = GAME_PANEL_WIDTH + SCORE_PANEL_WIDTH;
	public static final int PANEL_HEIGHT = GAME_PANEL_HEIGHT + 50;
	

	// ����ÿ�еĵ÷֣��Լ��÷�λ��
	public static final int BASE_SCORE = 10;
	public static final int SCOREX = GAME_PANEL_WIDTH + SCORE_PANEL_WIDTH / 2 ;
	public static final int SCOREY = GAME_PANEL_HEIGHT / 2;
	
	// �����ٶȣ���ʾ��������һ��
	public static final int SPEED = 1;
	
	// ͼ�κ��ϰ������ɫ
	public static final Color SHAPE_COLOR = new Color(0x990066);
	public static final Color GROUND_COLOR = Color.GRAY;
	
	
	// TYPES ��һ����ά���飬��¼ͼ�ε������Լ�ÿ���������̬������
	// ��һά��ʾ�ж�����ͼ��
	// ������ά��ͬ��ʾ��ͼ��ĳ����̬����Ӧ������
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
