package org.mytetris.listener;

import org.mytetris.entities.Ground;



/**
 * GroundListener 的适配器，在使用下面函数时，可以继承接口再重写函数
 *
 */
public class GroundAdapter implements GroundListener {


	@Override
	public void fullLineDeleted(Ground ground, int deletedLineCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groundIsFull(Ground ground) {
		// TODO Auto-generated method stub
		
	}

}
