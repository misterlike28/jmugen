package org.lee.mugen.core.renderer.game;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.air.AirData;
import org.lee.mugen.parser.air.AirData.TypeBlit;
import org.lee.mugen.renderer.AngleDrawProperties;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.renderer.Trans;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.base.AbstractAnimManager.SpriteDrawProperties;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.entity.ExplodSprite;
import org.lee.mugen.sprite.entity.PointF;
import org.lee.mugen.sprite.entity.Postype;

public class ExplodRender implements Renderable {
	protected ExplodSprite sprite;

	public ExplodRender(ExplodSprite sprite) {
		this.sprite = sprite;
	}
	
	public void render() {
		float xScale = sprite.getExplod().getScale().getX();
		float yScale = sprite.getExplod().getScale().getY();
		
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		int _mvX = stage.getCamera().getX();
		int _mvY = stage.getCamera().getY();
		float x = _mvX + stage.getCamera().getWidth()/2f;
		int y = stage.getStageinfo().getZoffset() + _mvY;

		final ImageContainer ic = sprite.getCurrentImage();
		if (ic == null)
			return;


		AirData air = sprite.getSprAnimMng().getCurrentImageSprite().getAirData();
		ImageSpriteSFF imgSprite = sprite.getCurrentImageSpriteSFF();
		float xOffset = (imgSprite.getXAxis()) * xScale;
		float yOffset = (imgSprite.getYAxis()) * yScale;
		if (sprite.getExplod().getPostype() == Postype.left || sprite.getExplod().getPostype() == Postype.right) {
			x = 0;
			y = 0;
		} else {
			xOffset = 0;//imgSprite.getXAxis();
			yOffset = 0;//imgSprite.getYAxis();
		}
		boolean isFlipH = air.isMirrorH;
		if (sprite.getExplod().getPostype() == Postype.p1 || sprite.getExplod().getPostype() == Postype.p2
				|| sprite.getExplod().getPostype() == Postype.back || sprite.getExplod().getPostype() == Postype.front) {
			isFlipH = sprite.isFlip() ^ isFlipH;
		}
		boolean isFlipV = air.isMirrorV ^ sprite.getExplod().getVfacing() == -1;

		isFlipH = isFlipH ^ sprite.getExplod().getFacing() == -1;
		
		PointF pos = sprite.getPosToDraw();

		// rotate if
		SpriteDrawProperties dp = sprite.getSprAnimMng().getSpriteDrawProperties();

		float xPos = pos.getX() + x - xOffset + air.getXOffSet() * xScale;
		if (air.isMirrorH)
			xPos = xPos
			- (
					isFlipH? 
							ic.getWidth() + xOffset + air.getXOffSet() * xScale
							: xOffset + air.getXOffSet() * xScale);
		float yPos = pos.getY() + y - yOffset + air.getYOffSet() * yScale;
		if (air.isMirrorV)
			yPos = yPos
			- (
					air.isMirrorV? 
							ic.getHeight() + yOffset + air.getYOffSet() * yScale
							: yOffset + air.getYOffSet() * yScale);
		DrawProperties drawProperties = new DrawProperties(xPos, yPos, isFlipH, isFlipV, ic);
//		DrawProperties drawProperties = new DrawProperties(73, 133, isFlipH, isFlipV, ic);
		
		
		if (dp.isActive()) {
			AngleDrawProperties angle = new AngleDrawProperties();
			angle.setAngleset(-dp.getAngleset());
			angle.setXAnchor(pos.getX() + x + (imgSprite.getXAxis()) * dp.getXScale());
			angle.setXAnchor(pos.getY() + y + (imgSprite.getYAxis()) * dp.getYScale());
			angle.setXScale(dp.getXScale());
			angle.setYScale(dp.getYScale());
			
			
			drawProperties.setAngleDrawProperties(angle);

		}
		// TODO
		if (TypeBlit.ASD == air.type) {
			if (air.getAddcolor() < air.getDestcolor()) {
			
			} else if (air.getAddcolor() > air.getDestcolor()) {
				
			}
			drawProperties.setTrans(Trans.ADD1);
		} else if (TypeBlit.A == air.type) {
			drawProperties.setTrans(Trans.ADD1);
		} else {
			
		}
		

		drawProperties.setXScaleFactor(xScale);
		drawProperties.setYScaleFactor(yScale);
		
		// TODO TypeBlit.ASD, TypeBlit.A



//		GraphicsWrapper.getInstance().scale(sprite.getExplod().getScale().x, sprite.getExplod().getScale().y);

		if (!sprite.getPalfx().isNoPalFx()) {
			sprite.getPalfx().setDrawProperties(drawProperties);
			//drawProperties.setTrans(Trans.ADD1);
		}
//		drawProperties.setTrans(Trans.ADD1);
		GraphicsWrapper.getInstance().draw(drawProperties);
//		GraphicsWrapper.getInstance().scale(1f/sprite.getExplod().getScale().x, 1f/sprite.getExplod().getScale().y);

	}
	public int getPriority() {
		return sprite.getPriority() - 1;
	}

	public boolean isProcess() {
		return sprite.isProcess();
	}


	public boolean remove() {
		return sprite.remove() || sprite.isForceRemove();
	}
	public void setPriority(int p) {
		// TODO Auto-generated method stub
		
	}


}