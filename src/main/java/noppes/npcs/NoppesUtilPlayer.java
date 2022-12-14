package noppes.npcs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NoppesUtilPlayer {

	public static boolean compareItems(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT){
        if (NoppesUtilServer.IsItemStackNull(item) || NoppesUtilServer.IsItemStackNull(item2)){
            return false;
        }

		return compareItemDetails(item, item2, ignoreDamage, ignoreNBT);
	}
	
	private static boolean compareItemDetails(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT){
        if (item.getItem() != item2.getItem() ){
            return false;
        }
		if (!ignoreDamage && item.getDamageValue() != -1 && item.getDamageValue() != item2.getDamageValue()){
            return false;
        }
        if(!ignoreNBT && item.getTag() != null && (item2.getTag() == null || !item.getTag().equals(item2.getTag()))){
            return false;
        }
        return ignoreNBT || item2.getTag() == null || item.getTag() != null;
    }
	
	public static boolean compareItems(PlayerEntity player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT){

		int size = 0;
		for(int i = 0; i < player.inventory.getContainerSize(); i++) {
			ItemStack is = player.inventory.getItem(i);
			if(!NoppesUtilServer.IsItemStackNull(is) && compareItems(item, is, ignoreDamage, ignoreNBT)) 
				size += is.getCount();
		}
		return size >= item.getCount();
	}
	
	public static void consumeItem(PlayerEntity player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
		if(NoppesUtilServer.IsItemStackNull(item))
			return;
		int size = item.getCount();
		for(int i = 0; i < player.inventory.getContainerSize(); i++) {
			ItemStack is = player.inventory.getItem(i);
			if(NoppesUtilServer.IsItemStackNull(is) || !compareItems(item, is, ignoreDamage, ignoreNBT))
				continue;
			if(size >= is.getCount()){
				size -= is.getCount();
    			player.inventory.setItem(i, ItemStack.EMPTY);
			}
			else{
				is.split(size);
				break;
			}
		}
	}
	
	public static List<ItemStack> countStacks(IInventory inv, boolean ignoreDamage, boolean ignoreNBT){
		List<ItemStack> list = new ArrayList<>();
		for(int i = 0; i < inv.getContainerSize(); i++){
			ItemStack item = inv.getItem(i);
			if(NoppesUtilServer.IsItemStackNull(item))
				continue;
			boolean found = false;
			for(ItemStack is : list){
				if(compareItems(item, is, ignoreDamage, ignoreNBT)){
					is.setCount(is.getCount() + item.getCount());
					found = true;
					break;
				}
			}
			if(!found)
				list.add(item.copy());			
		}
		
		return list;
	}

}
