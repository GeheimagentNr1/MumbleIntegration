package de.geheimagentnr1.mumbleintegration.config.gui.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.geheimagentnr1.mumbleintegration.config.gui.GuiOptions;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public class OptionsListWidget extends AbstractOptionList<OptionsListWidgetEntry> {
	
	
	@Nonnull
	private final GuiOptions owner;
	
	public OptionsListWidget(
		@Nonnull GuiOptions _owner,
		@Nonnull Minecraft _minecraft,
		int x,
		int _height,
		int _top,
		int y,
		int entryHeight ) {
		
		super( _minecraft, x, _height, _top, y, entryHeight );
		
		owner = _owner;
	}
	
	@Override
	public int func_230949_c_() {
		
		return 250;
	}
	
	@Override
	public void func_230430_a_( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		func_230433_a_( matrixStack );
		RenderSystem.disableLighting();
		RenderSystem.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		field_230668_b_.getTextureManager().bindTexture( field_230663_f_ );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		int left_start = func_230968_n_();
		int top_start = field_230672_i_ + 4 - (int)func_230966_l_();
		
		func_238478_a_( matrixStack, left_start, top_start, mouseX, mouseY, partialTicks );
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(
			GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
			GlStateManager.SourceFactor.ZERO,
			GlStateManager.DestFactor.ONE
		);
		RenderSystem.disableAlphaTest();
		RenderSystem.shadeModel( 7425 );
		RenderSystem.disableTexture();
		bufferBuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR_TEX );
		bufferBuilder.pos( field_230675_l_, field_230672_i_ + 4, 0.0D )
			.color( 0, 0, 0, 0 )
			.tex( 0.0F, 1.0F )
			.endVertex();
		bufferBuilder.pos( field_230674_k_, field_230672_i_ + 4, 0.0D )
			.color( 0, 0, 0, 0 )
			.tex( 1.0F, 1.0F )
			.endVertex();
		bufferBuilder.pos( field_230674_k_, field_230672_i_, 0.0D )
			.color( 0, 0, 0, 255 )
			.tex( 1.0F, 0.0F )
			.endVertex();
		bufferBuilder.pos( field_230675_l_, field_230672_i_, 0.0D )
			.color( 0, 0, 0, 255 )
			.tex( 0.0F, 0.0F )
			.endVertex();
		bufferBuilder.pos( field_230675_l_, field_230673_j_, 0.0D )
			.color( 0, 0, 0, 255 )
			.tex( 0.0F, 1.0F
			).endVertex();
		bufferBuilder.pos( field_230674_k_, field_230673_j_, 0.0D )
			.color( 0, 0, 0, 255 )
			.tex( 1.0F, 1.0F ).endVertex();
		bufferBuilder.pos( field_230674_k_, field_230673_j_ - 4, 0.0D )
			.color( 0, 0, 0, 0 )
			.tex( 1.0F, 0.0F )
			.endVertex();
		bufferBuilder.pos( field_230675_l_, field_230673_j_ - 4, 0.0D )
			.color( 0, 0, 0, 0 )
			.tex( 0.0F, 0.0F )
			.endVertex();
		tessellator.draw();
		int maxPosition = Math.max( 0, func_230945_b_() - ( field_230673_j_ - field_230672_i_ - 4 ) );
		if( maxPosition > 0 ) {
			int yp = (int)( (float)( ( field_230673_j_ - field_230672_i_ ) * ( field_230673_j_ - field_230672_i_ ) ) / func_230945_b_() );
			yp = MathHelper.clamp( yp, 32, field_230673_j_ - field_230672_i_ - 8 );
			int ye =
				(int)func_230966_l_() * ( field_230673_j_ - field_230672_i_ - yp ) / mpy + field_230672_i_;
			if( ye < field_230672_i_ ) {
				ye = field_230672_i_;
			}
		int maxPosition = Math.max( 0, getMaxPosition() - ( y1 - y0 - 4 ) );
		if( maxPosition > 0 ) {
			int yCurrentPos = MathHelper.clamp( ( y1 - y0 ) * ( y1 - y0 ) / getMaxPosition(), 32, y1 - y0 - 8 );
			int yEndPos = Math.max( (int)getScrollAmount() * ( y1 - y0 - yCurrentPos ) / maxPosition + y0, y0 );
			
			bufferBuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR_TEX );
			bufferBuilder.pos( getScrollbarPosition(), field_230673_j_, 0.0D )
				.color( 0, 0, 0, 255 )
				.tex( 0.0F, 1.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition() + 6, field_230673_j_, 0.0D )
				.color( 0, 0, 0, 255 )
				.tex( 1.0F, 1.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition() + 6, field_230672_i_, 0.0D )
				.color( 0, 0, 0, 255 )
				.tex( 1.0F, 0.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition(), field_230672_i_, 0.0D )
				.color( 0, 0, 0, 255 )
				.tex( 0.0F, 0.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition(), yEndPos + yCurrentPos, 0.0D )
				.color( 128, 128, 128, 255 )
				.tex( 0.0F, 1.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition() + 6, yEndPos + yCurrentPos, 0.0D )
				.color( 128, 128, 128, 255 )
				.tex( 1.0F, 1.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition() + 6, yEndPos, 0.0D )
				.color( 128, 128, 128, 255 )
				.tex( 1.0F, 0.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition(), yEndPos, 0.0D )
				.color( 128, 128, 128, 255 )
				.tex( 0.0F, 0.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition(), yEndPos + yCurrentPos - 1, 0.0D )
				.color( 192, 192, 192, 255 )
				.tex( 0.0F, 1.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition() + 5, yEndPos + yCurrentPos - 1, 0.0D )
				.color( 192, 192, 192, 255 )
				.tex( 1.0F, 1.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition() + 5, yEndPos, 0.0D )
				.color( 192, 192, 192, 255 )
				.tex( 1.0F, 0.0F )
				.endVertex();
			bufferBuilder.pos( getScrollbarPosition(), yEndPos, 0.0D )
				.color( 192, 192, 192, 255 )
				.tex( 0.0F, 0.0F )
				.endVertex();
			tessellator.draw();
		}
		func_230447_a_( matrixStack, mouseX, mouseY );
		RenderSystem.enableTexture();
		RenderSystem.shadeModel( 7424 );
		RenderSystem.enableAlphaTest();
		RenderSystem.disableBlend();
	}
	
	public void save() {
		
		func_231039_at__().stream()
			.filter( entry -> entry instanceof OptionsEntryValue )
			.map( entry -> (OptionsEntryValue<?>)entry )
			.forEach( OptionsEntryValue::save );
	}
	
	public void add( @Nonnull OptionsListWidgetEntry entry ) {
		
		if( entry instanceof OptionsEntryValue ) {
			owner.addListener( ( (OptionsEntryValue<?>)entry ).getListener() );
		}
		func_230513_b_( entry );
	}
	
	public void forEach( @Nonnull Consumer<OptionsListWidgetEntry> consumer ) {
		
		for( OptionsListWidgetEntry value : func_231039_at__() ) {
			consumer.accept( value );
		}
	}
}
