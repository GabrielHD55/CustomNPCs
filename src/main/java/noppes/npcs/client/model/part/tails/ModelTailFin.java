package noppes.npcs.client.model.part.tails;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTailFin extends ModelRenderer {

	public ModelTailFin(BipedModel base) {
		super(base.texWidth, base.texHeight, 0, 0);
		ModelRenderer Shape1 = new ModelRenderer(base.texWidth, base.texHeight, 0, 0);
		Shape1.addBox(-2F, -2F, -2F, 3, 3, 8);
		Shape1.setPos(0.5F, 0F, 1F);
		setRotation(Shape1, -0.669215F, 0F, 0F);
		addChild(Shape1);
		
		ModelRenderer Shape2 = new ModelRenderer(base.texWidth, base.texHeight, 2, 2);
		Shape2.addBox(-1F, -1F, 1F, 3, 2, 6);
		Shape2.setPos(-0.5F, 3F, 4.5F);
		setRotation(Shape2, -0.2602503F, 0F, 0F);
		addChild(Shape2);
		
		ModelRenderer Shape3 = new ModelRenderer(base.texWidth, base.texHeight, 0, 11);
		Shape3.addBox(-1F, -1F, -1F, 3, 1, 6);
		Shape3.setPos(0.5F, 5F, 12F);
		setRotation(Shape3, 0F, 1.07818F, 0F);
		addChild(Shape3);
		
		ModelRenderer Shape4 = new ModelRenderer(base.texWidth, base.texHeight, 0, 11);
		Shape4.mirror = true;
		Shape4.addBox(-2F, 0F, -1F, 3, 1, 6);
		Shape4.setPos(-0.5F, 4F, 12F);
		setRotation(Shape4, 0F, -1.003822F, 0F);
		addChild(Shape4);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}


}
