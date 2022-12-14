package noppes.npcs.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomItems;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelMesher.class)
public class ItemModelMesherMixin {

    @Inject(at = @At("HEAD"), method = "getItemModel", cancellable = true)
    public void getModel(ItemStack item, CallbackInfoReturnable<IBakedModel> cir){
        if(item.getItem() == CustomItems.scripted_item){
            ItemScriptedWrapper si = (ItemScriptedWrapper) item.getCapability(ItemStackWrapper.ITEMSCRIPTEDDATA_CAPABILITY, null).orElse(null);
            if(si == null)
                return;
            Item i = null;
            if(si.texture != null){
                i = ForgeRegistries.ITEMS.getValue(si.texture);
            }
            if(i == null){
                i = CustomItems.scripted_item;
            }
            IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(i);
            if(model != null){
                cir.setReturnValue(model);
                cir.cancel();
            }
        }
    }
}
