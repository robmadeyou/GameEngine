package com.abereth.gui;

import com.abereth.draw.Drawable;
import com.abereth.game.Draw;

import java.util.ArrayList;

/**
 * Created by jeremiah on 22/11/2014.
 */
public class Gui extends Drawable
{
	protected boolean isSelected;
	public Gui ( )
	{
		super( );
		this.isSelected = false;
	}

	public Gui( double x, double y, double width, double height )
	{
		super(x, y, width, height );
	}

	@Override
	public void Draw ( Draw d )
	{

	}
}
