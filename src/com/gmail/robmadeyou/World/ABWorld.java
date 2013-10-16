package com.gmail.robmadeyou.world;

import com.gmail.robmadeyou.Abereth;
import com.gmail.robmadeyou.block.ABBlock;
import com.gmail.robmadeyou.block.BlockAir;
import com.gmail.robmadeyou.block.BlockStone;
import com.gmail.robmadeyou.entity.ABEntity;
import com.gmail.robmadeyou.peripherals.ABMouse;
import com.gmail.robmadeyou.ABScreen;

import org.newdawn.slick.SlickException;


/*
 * World in this case is meant as the level currently visible to the player.
 * I haven't yet tried swapping levels and worlds so no idea if that would work.
 * 
 * All the collision detection is also done in here, it's very simple just boolean
 * statements checking if the current location is a solid block or not.
 */
public class ABWorld {

    /*
     * Determines the amount of blocks placed horizontally (if this figure was set to be lower
     * than ScreenWidth / BlockSize then there would be errors if the player walks out of the
     * area, so when the figure is changed in the Screen class if the figure is smaller than ScreenWidth / BlockSize
     * it is set to be just enough to cover the whole screen in blocks
     */
    public static int WorldArrayWidth = 0;
    /*
     * Determines the amount of blocks placed vertically on the array list
     */
    public static int WorldArrayHeight = 0;

    /*
     * Array list that holds the current world currently visible on screen
     */
    //public static Block[][] blockList;
    public static ABBlockMap blockList;

    public static int blockEffectX = 0;
    public static int blockEffectY = 0;

    public static String typeEffect = BlockAir.class.toString();

    public static final int BLOCK_SIZE() {
        if (ABScreen.WorldTileSize != 32) {
            return ABScreen.WorldTileSize;
        }
        return 32;
    }



    public static int getWorldWidthInPixels() {
        return WorldArrayWidth * ABWorld.BLOCK_SIZE();
    }

    public static int getWorldHeightInPixels() {
        return WorldArrayHeight * ABWorld.BLOCK_SIZE();
    }

    /*
     * GravityModifier is possible to change, but gravity isn't
     * this is in case you wish to change the modifier on the fly for cool
     * effects :D
     */
    public static double gravityModifier = 0.04;

    public static double gravity(int delta) {
        return (delta * gravityModifier);
    }


    public static void setWorldDimensions(int x, int y) {
        blockList = new ABBlockMap(x, y);
        WorldArrayWidth = x;
        WorldArrayHeight = y;
    }

    /*
     * Returns the type of block at the specified location, these aren't screen coordinates, but instead
     * block coordinates, so you have to make sure you divide by block size and round it before calling this
     * function
     */
    public static ABBlock getBlockTypeAtLocation(int x, int y) throws NullPointerException {
        if (blockList.withinBounds(x, y)) {
            return blockList.getBlock(x, y).getType();
        }
        return null;
    }

    /*
     * Checks if the block at location is solid, again it's in block coordinates so you must divide again
     */
    public static boolean isSolidAtLocation(int x, int y) {
        return getBlockTypeAtLocation(x, y) != null
                && getBlockTypeAtLocation(x, y).isSolid();

    }

    /*
     * Clears the world list and checks if the game type, if it's SIDE_SCROLLER it sets the bottom 2 layers
     * stone to make sure the player doesn't fall through the world.
     */
    public static void setArrayListClear() throws SlickException {
        if (ABScreen.TypeOfGame == ABScreen.GameType.SIDE_SCROLLER) {
            for (int x = 0; x < WorldArrayWidth; x++) {
                for (int y = 0; y < WorldArrayHeight; y++) {
                    if (y == WorldArrayHeight - 2 || y == WorldArrayHeight - 1) {
                        blockList.setBlock(new BlockStone(x, y));
                    } else {
                        blockList.setBlock(new BlockAir(x, y));
                    }
                }
            }
        } else {
            blockList = ABMapLoader.generateMap(ABMapLoader.MAP_STRING);
        }
    }

    /*
     * Checks collision for the legs of the entity, this will
     * decide if player can go down, or when falling is possible to jump on the block and stop
     * instead of falling constantly
     */
    public static boolean isSolidUnder(ABEntity e) {
    	float eX = e.getX();
        float eY = e.getY();
        float eW = e.getWidth();
        float eH = e.getHeight();

        int bDimensions = ABWorld.BLOCK_SIZE();
        /*
         * Starting math to decide where the for loop should start from
		 * and end from, taking into consideration the array lengths so the
		 * engine no longer crashes when player is out of bounds
		 */
        int startX = (int) Math.round(e.getX() / BLOCK_SIZE()) - 2;
        if (startX < 0) {
            startX = 0;
        }
        int durationX = startX + 4;
        while (durationX + startX >= WorldArrayWidth) {
            durationX--;
        }

        int startY = (int) Math.round(e.getY() / BLOCK_SIZE()) - 2;
        if (startY < 0) {
            startY = 0;
        }
        int durationY = startY + 4;
        while (durationY + startY >= WorldArrayHeight) {
            durationY--;
        }
        for (int sX = startX; sX < startX + durationX + 1; sX++) {
            for (int y = startY; y < startY + durationY; y++) {
                int x;
                if (sX >= 1) {
                    x = sX - 1;
                } else {
                    x = sX;
                }
                int bX = blockList.getBlock(x, y).getX() * ABWorld.BLOCK_SIZE();
                int bY = blockList.getBlock(x, y).getY() * ABWorld.BLOCK_SIZE();
                /*
                 * For loop because in case the blocks are smaller than the players width, so the player doesn't fall
				 * through the blocks
				 */
                for (int x2 = 0; x2 < eW / 4; x2++) {
                    boolean one = eX + (x2 * 4) >= bX && eX + (x2 * 4) <= bX + bDimensions && eY + eH + 10 >= bY && eY + eH <= bY + 7;

                    if (one) {
                        if (blockList.getBlock(x, y).isSolid()) {
                            e.setY(bY - 1 - eH);
                            e.doEffectFromBlock(blockList.getBlock(x, y).getType());
                            blockEffectX = x;
                            blockEffectY = y;
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    public static boolean isSolidAbove(ABEntity e) {
        float eX = e.getX();
        float eY = e.getY();
        float eW = e.getWidth();
        int bDimensions = ABWorld.BLOCK_SIZE();

        int startX = (int) Math.round(e.getX() / BLOCK_SIZE()) - 2;
        if (startX < 0) {
            startX = 0;
        }
        int durationX = startX + 4;
        while (durationX + startX >= WorldArrayWidth) {
            durationX--;
        }

        int startY = (int) Math.round(e.getY() / BLOCK_SIZE()) - 2;
        if (startY < 0) {
            startY = 0;
        }
        int durationY = startY + 4;
        while (durationY + startY >= WorldArrayHeight) {
            durationY--;
        }
        for (int sX = startX; sX < startX + durationX + 1; sX++) {
            for (int y = startY; y < startY + durationY; y++) {
                int x;
                if (sX >= 1) {
                    x = sX - 1;
                } else {
                    x = sX;
                }
                int bX = blockList.getBlock(x, y).getX() * ABWorld.BLOCK_SIZE();
                int bY = blockList.getBlock(x, y).getY() * ABWorld.BLOCK_SIZE();
                for (int x2 = 0; x2 <= eW / 4; x2++) {
                    boolean one = eX + (x2 * 4) >= bX && eX + (x2 * 4) <= bX + bDimensions && eY + 5 >= bY + bDimensions - 5 && eY - 5 <= bY + bDimensions + 5;
                    if (one) {
                        if (blockList.getBlock(x, y).isSolid()) {
                            e.setY(y * BLOCK_SIZE() + BLOCK_SIZE() + 5);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSolidLeft(ABEntity e) {
        float eX = (int) e.getX();
        float eY = (int) e.getY();
        int bDimensions = ABWorld.BLOCK_SIZE();

        int startX = (int) Math.round(e.getX() / BLOCK_SIZE()) - 2;
        if (startX < 0) {
            startX = 0;
        }
        int durationX = startX + 4;
        while (durationX + startX >= WorldArrayWidth) {
            durationX--;
        }

        int startY = (int) Math.round(e.getY() / BLOCK_SIZE()) - 2;
        if (startY < 0) {
            startY = 0;
        }
        int durationY = startY + 4;
        while (durationY + startY >= WorldArrayHeight) {
            durationY--;
        }
        for (int sX = startX; sX < startX + durationX + 1; sX++) {
            for (int y = startY; y < startY + durationY; y++) {
                int x;
                if (sX >= 1) {
                    x = sX - 1;
                } else {
                    x = sX;
                }
                int bX = blockList.getBlock(x, y).getX() * ABWorld.BLOCK_SIZE();
                int bY = blockList.getBlock(x, y).getY() * ABWorld.BLOCK_SIZE();

                for (int y2 = 0; y2 <= e.getHeight() / 4; y2++) {
                    boolean one = eX <= bX + bDimensions + 3 && eX >= bX + bDimensions - 3 && eY - 1 + (4 * y2) >= bY && eY - 1 + (4 * y2) <= bY + bDimensions;
                    if (one) {
                        if (blockList.getBlock(x, y).isSolid()) {
                            e.setX(x * BLOCK_SIZE() + BLOCK_SIZE() + 2);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSolidRight(ABEntity e) {
        float eX = e.getX();
        float eY = e.getY();
        float eW = e.getWidth();
        int bDimensions = ABWorld.BLOCK_SIZE();

        int startX = (int) Math.round(e.getX() / BLOCK_SIZE()) - 2;
        if (startX < 0) {
            startX = 0;
        }
        int durationX = startX + 4;
        while (durationX + startX >= WorldArrayWidth) {
            durationX--;
        }

        int startY = (int) Math.round(e.getY() / BLOCK_SIZE()) - 2;
        if (startY < 0) {
            startY = 0;
        }
        int durationY = startY + 4;
        while (durationY + startY >= WorldArrayHeight) {
            durationY--;
        }
        for (int sX = startX; sX < startX + durationX + 1; sX++) {
            for (int y = startY; y < startY + durationY; y++) {
                int x;
                if (sX >= 1) {
                    x = sX - 1;
                } else {
                    x = sX;
                }
                
                int bX = blockList.getBlock(x, y).getX() * ABWorld.BLOCK_SIZE();
                int bY = blockList.getBlock(x, y).getY() * ABWorld.BLOCK_SIZE();

                for (int y2 = 0; y2 <= e.getHeight() / 4; y2++) {
                    boolean one = eX + eW <= bX + 3 && eX + eW >= bX - 3 && eY - 1 + (4 * y2) >= bY && eY - 1 + (4 * y2) <= bY + bDimensions;
                    if (one) {
                        if (blockList.getBlock(x, y).isSolid()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void onUpdate(ABCamera cam) {
    	int camXDivided = (int) Math.round(-cam.getX() / BLOCK_SIZE());
        int camYDivided = (int) Math.round(-cam.getY() / BLOCK_SIZE()-1);
        
        int camWidthDivided = (int) Math.round(cam.getWidth() / BLOCK_SIZE() + 4);
        int camHeightDivided = (int) Math.round(cam.getHeight() / BLOCK_SIZE() + 4);

        int mX = (int) Math.round(((ABMouse.getTranslatedX() - BLOCK_SIZE() / 2) / BLOCK_SIZE()));
        int mY = (int) Math.round(((ABMouse.getTranslatedY() - BLOCK_SIZE() / 2) / BLOCK_SIZE()));
        
        //Nice one
        if (Abereth.isDevMode) {
            try {
                if (!ABMouse.isOverGui) {
                    if (ABMouse.leftMouseButtonDown) {
                        blockList.setBlock(new BlockStone(mX, mY));
                    }
                    if (ABMouse.rightMouseButtonDown) {
                        blockList.setBlock(new BlockAir(mX, mY));
                    }
                }
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }

        for (int sX = camXDivided - 2; sX < camXDivided + camWidthDivided; sX++) {
            for (int sY = camYDivided - 2; sY < camYDivided + camHeightDivided; sY++) {
                int x;
                int y;
                if (sX >= 1) {
                    x = sX - 1;
                } else {
                    x = sX;
                }
                if (sY >= 1) {
                    y = sY - 1;
                } else {
                    y = sY;
                }
                if (x < WorldArrayWidth && y < WorldArrayHeight && x >= 0 && y >= 0) {
                	try{
                		blockList.getBlock(x, y).onUpdate();
                	}catch(NullPointerException ex){
                		ex.printStackTrace();
                	}
                }
            }
        }
    }
}