package de.geheimagentnr1.mumbleintegration.config.gui.config;

import com.mojang.blaze3d.platform.GlStateManager;
import de.geheimagentnr1.mumbleintegration.config.gui.GuiOptions;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public class OptionsListWidget extends AbstractList<OptionsListWidgetEntry> {
	
	
	@Nonnull
	private final GuiOptions owner;
	
	public OptionsListWidget( @Nonnull GuiOptions _owner, @Nonnull Minecraft client, int x, int _height, int _top,
		int y, int entryHeight ) {
		
		super( client, x, _height, _top, y, entryHeight );
		owner = _owner;
	}
	
	@Override
	public int getRowWidth() {
		
		return 250;
	}
	
	@Override
	public void render( int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground();
		int sp = getScrollbarPosition();
		int spss = sp + 6;
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		minecraft.getTextureManager().bindTexture( BACKGROUND_LOCATION );
		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		int left_start = getRowLeft();
		int top_start = y0 + 4 - (int)getScrollAmount();
		
		renderList( left_start, top_start, mouseX, mouseY, partialTicks );
		GlStateManager.disableDepthTest();
		renderHoleBackground( 0, y0, 255, 255 );
		renderHoleBackground( y1, height, 255, 255 );
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO,
			GlStateManager.DestFactor.ONE );
		GlStateManager.disableAlphaTest();
		GlStateManager.shadeModel( 7425 );
		GlStateManager.disableTexture();
		bufferBuilder.begin( 7, DefaultVertexFormats.POSITION_TEX_COLOR );
		bufferBuilder.pos( x0, y0 + 4, 0.0D ).tex( 0.0D, 1.0D ).color( 0, 0, 0, 0 ).endVertex();
		bufferBuilder.pos( x1, y0 + 4, 0.0D ).tex( 1.0D, 1.0D ).color( 0, 0, 0, 0 ).endVertex();
		bufferBuilder.pos( x1, y0, 0.0D ).tex( 1.0D, 0.0D ).color( 0, 0, 0, 255 ).endVertex();
		bufferBuilder.pos( x0, y0, 0.0D ).tex( 0.0D, 0.0D ).color( 0, 0, 0, 255 ).endVertex();
		bufferBuilder.pos( x0, y1, 0.0D ).tex( 0.0D, 1.0D ).color( 0, 0, 0, 255 ).endVertex();
		bufferBuilder.pos( x1, y1, 0.0D ).tex( 1.0D, 1.0D ).color( 0, 0, 0, 255 ).endVertex();
		bufferBuilder.pos( x1, y1 - 4, 0.0D ).tex( 1.0D, 0.0D ).color( 0, 0, 0, 0 ).endVertex();
		bufferBuilder.pos( x0, y1 - 4, 0.0D ).tex( 0.0D, 0.0D ).color( 0, 0, 0, 0 ).endVertex();
		tessellator.draw();
		int mpy = Math.max( 0, getMaxPosition() - ( y1 - y0 - 4 ) );
		if( mpy > 0 ) {
			int yp = (int)( (float)( ( y1 - y0 ) * ( y1 - y0 ) ) / getMaxPosition() );
			yp = MathHelper.clamp( yp, 32, y1 - y0 - 8 );
			int ye = (int)getScrollAmount() * ( y1 - y0 - yp ) / mpy + y0;
			if( ye < y0 ) {
				ye = y0;
			}
			
			bufferBuilder.begin( 7, DefaultVertexFormats.POSITION_TEX_COLOR );
			bufferBuilder.pos( sp, y1, 0.0D ).tex( 0.0D, 1.0D ).color( 0, 0, 0, 255 ).endVertex();
			bufferBuilder.pos( spss, y1, 0.0D ).tex( 1.0D, 1.0D ).color( 0, 0, 0, 255 ).endVertex();
			bufferBuilder.pos( spss, y0, 0.0D ).tex( 1.0D, 0.0D ).color( 0, 0, 0, 255 ).endVertex();
			bufferBuilder.pos( sp, y0, 0.0D ).tex( 0.0D, 0.0D ).color( 0, 0, 0, 255 ).endVertex();
			bufferBuilder.pos( sp, ye + yp, 0.0D ).tex( 0.0D, 1.0D ).color( 128, 128, 128, 255 ).endVertex();
			bufferBuilder.pos( spss, ye + yp, 0.0D ).tex( 1.0D, 1.0D ).color( 128, 128, 128, 255 ).endVertex();
			bufferBuilder.pos( spss, ye, 0.0D ).tex( 1.0D, 0.0D ).color( 128, 128, 128, 255 ).endVertex();
			bufferBuilder.pos( sp, ye, 0.0D ).tex( 0.0D, 0.0D ).color( 128, 128, 128, 255 ).endVertex();
			bufferBuilder.pos( sp, ye + yp - 1, 0.0D ).tex( 0.0D, 1.0D ).color( 192, 192, 192, 255 ).endVertex();
			bufferBuilder.pos( spss - 1, ye + yp - 1, 0.0D ).tex( 1.0D, 1.0D ).color( 192, 192, 192, 255 ).endVertex();
			bufferBuilder.pos( spss - 1, ye, 0.0D ).tex( 1.0D, 0.0D ).color( 192, 192, 192, 255 ).endVertex();
			bufferBuilder.pos( sp, ye, 0.0D ).tex( 0.0D, 0.0D ).color( 192, 192, 192, 255 ).endVertex();
			tessellator.draw();
		}
		renderDecorations( mouseX, mouseY );
		GlStateManager.enableTexture();
		GlStateManager.shadeModel( 7424 );
		GlStateManager.enableAlphaTest();
		GlStateManager.disableBlend();
	}
	
	public void save() {
		
		children().stream().filter( entry -> entry instanceof OptionsEntryValue ).map(
			entry -> (OptionsEntryValue<?>)entry ).forEach( OptionsEntryValue::save );
	}
	
	public void add( @Nonnull OptionsListWidgetEntry entry ) {
		
		if( entry instanceof OptionsEntryValue ) {
			owner.addListener( ( (OptionsEntryValue<?>)entry ).getListener() );
		}
		addEntry( entry );
	}
	
	public void forEach( @Nonnull Consumer<OptionsListWidgetEntry> consumer ) {
		
		for( OptionsListWidgetEntry value : children() ) {
			consumer.accept( value );
		}
	}
}