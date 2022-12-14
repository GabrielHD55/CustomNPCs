package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;

public class AniDancing implements AnimationBase {

	@Override
	public void animatePost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, BipedModel model, int animationStart) {
		float dancing = entity.tickCount / 4f;
		float dancing2 = (entity.tickCount + 1) / 4f;
		dancing += (dancing2 - dancing) * Minecraft.getInstance().getDeltaFrameTime();
		float x = (float)Math.sin(dancing);
		float y = (float)Math.abs(Math.cos(dancing));

		model.hat.x = model.head.x = x * 0.75F;
		model.hat.y = model.head.y = y * 1.25F - 0.02F + (entity.isCrouching()?4 : 0);
		model.hat.z = model.head.z = -y * 0.75F;

		model.leftArm.x += x * 0.25F;
		model.leftArm.y += y * 1.25F;

		model.rightArm.x += x * 0.25F;
		model.rightArm.y += y * 1.25F;

		model.body.x = x * 0.25F;
	}

	@Override
	public void animatePre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, BipedModel model, int animationStart) {

	}

}
