package com.gmail.robmadeyou.World;

import com.gmail.robmadeyou.Screen;
import com.gmail.robmadeyou.Block.Block;
import com.gmail.robmadeyou.Block.BlockAir;
import com.gmail.robmadeyou.Block.BlockStone;
import com.gmail.robmadeyou.Entity.Entity;
import com.gmail.robmadeyou.Input.Mouse;

public class World {
	public static final int BLOCK_SIZE(){
		if(Screen.WorldTileSize != 32){
			return Screen.WorldTileSize;
		}
		return 32;
	};
	public static void getBlockTypeAtLocation(int x, int y){
		
	}
	public static void setArrayListClear(){
		for(int x = 0; x < blockList.length; x++){
			for(int y = 0; y < blockList.length; y++){
				blockList[x][y] = new BlockAir(x, y);
			}
		}
	}
	private static Block[][] blockList = new Block[75][75];
	
	/*
	 * Checks collision for the legs of the entity, this will
	 * decide if player can go down, or when falling is possible to jump on the block and stop
	 * instead of falling constantly
	 */
	public static boolean isSolidUnder(Entity e){
		for(int x = 0; x < blockList.length; x++){
			for(int y = 0; y < blockList.length; y++){
				int eX = (int)e.getX();
				int eY = (int)e.getY();
				int eW = e.getWidth();
				int eH = e.getHeight();
				
				int bX = blockList[x][y].getX() * World.BLOCK_SIZE();
				int bY = blockList[x][y].getY() * World.BLOCK_SIZE();
				int bDimensions = World.BLOCK_SIZE();
				//Bottom left
				boolean one = eX >= bX && eX <= bX + bDimensions && eY + eH >= bY  - 5 && eY + eH <= bY + 5;
				//Bottom right
				boolean two = eX + eW >= bX && eX + eW <= bX + bDimensions && eY + eH >= bY - 5 && eY + eH <= bY + 5;
				if(one || two){
					if(blockList[x][y].isSolid()){
						e.setY((y * 32) - eH);
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean isSolidAbove(Entity e){
		for(int x = 0; x < blockList.length; x++){
			for(int y = 0; y < blockList.length; y++){
				int eX = (int)e.getX();
				int eY = (int)e.getY();
				int eW = e.getWidth();
				int eH = e.getHeight();
				
				int bX = blockList[x][y].getX() * World.BLOCK_SIZE();
				int bY = blockList[x][y].getY() * World.BLOCK_SIZE();
				int bDimensions = World.BLOCK_SIZE();
				for(int x2 = 0; x2 <= eW / 4; x2++){
					boolean one = eX + (x2 * 4) >= bX && eX + (x2 * 4) <= bX + bDimensions && eY >= bY + bDimensions - 5 && eY <= bY + bDimensions + 5;
					if(one){
						if(blockList[x][y].isSolid()){
							e.setY(y * 32 + 32 + 5);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean isSolidLeft(Entity e){
		for(int x = 0; x < blockList.length; x++){
			for(int y = 0; y < blockList.length; y++){
				int eX = (int)e.getX();
				int eY = (int)e.getY();
				int eW = e.getWidth();
				int eH = e.getHeight();
				
				int bX = blockList[x][y].getX() * World.BLOCK_SIZE();
				int bY = blockList[x][y].getY() * World.BLOCK_SIZE();
				int bDimensions = World.BLOCK_SIZE();
				for(int y2 = 0; y2 <= e.getHeight() / 4; y2++){
					boolean one = eX <= bX + bDimensions + 3 && eX >= bX + bDimensions - 3 && eY - 1+ (4 * y2) >= bY && eY- 1 + (4 * y2) <= bY + bDimensions;
					if(one){
						if(blockList[x][y].isSolid()){
							e.setX(x * 32 + 32 + 2);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static boolean isSolidRight(Entity e){
		for(int x = 0; x < blockList.length; x++){
			for(int y = 0; y < blockList.length; y++){
				int eX = (int)e.getX();
				int eY = (int)e.getY();
				int eW = e.getWidth();
				int eH = e.getHeight();
				
				int bX = blockList[x][y].getX() * World.BLOCK_SIZE();
				int bY = blockList[x][y].getY() * World.BLOCK_SIZE();
				int bDimensions = World.BLOCK_SIZE();
				for(int y2 = 0; y2 <= e.getHeight() / 4; y2++){
					boolean one = eX + eW<= bX + 3 && eX + eW>= bX - 3 && eY - 1+ (4 * y2) >= bY && eY- 1 + (4 * y2) <= bY + bDimensions;
					if(one){
						if(blockList[x][y].isSolid()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static void draw(){
		for(int x = 0; x < blockList.length; x++){
			for(int y = 0; y < blockList.length; y++){
				int mX = Mouse.getX() - (int) Screen.translate_x;
				int mY = Mouse.getY();
				if(mX >= x * 32 + Screen.translate_x && mX <= mX * 32 + 32 + Screen.translate_x && mY >= y * 32 && mY <= y * 32 + 32){
					if(Mouse.leftMouseButtonDown){
						blockList[x][y] = new BlockStone(x,y);
					}
				}
				blockList[x][y].onUpdate();
			}
		}
	}
}