package noppes.npcs.controllers.data;

import java.util.HashMap;

import net.minecraft.nbt.CompoundNBT;
import noppes.npcs.NBTTags;
import noppes.npcs.roles.JobItemGiver;

public class PlayerItemGiverData{
	private HashMap<Integer, Long> itemgivers = new HashMap<Integer,Long>();
	private HashMap<Integer, Integer> chained = new HashMap<Integer,Integer>();
	
	public void loadNBTData(CompoundNBT compound) {
		chained = NBTTags.getIntegerIntegerMap(compound.getList("ItemGiverChained", 10));
		itemgivers = NBTTags.getIntegerLongMap(compound.getList("ItemGiversList", 10));
	}

	public void saveNBTData(CompoundNBT compound) {
		compound.put("ItemGiverChained", NBTTags.nbtIntegerIntegerMap(chained));
		compound.put("ItemGiversList", NBTTags.nbtIntegerLongMap(itemgivers));
	}
	public boolean hasInteractedBefore(JobItemGiver jobItemGiver) {
		return itemgivers.containsKey(jobItemGiver.itemGiverId);
	}
	
	public long getTime(JobItemGiver jobItemGiver){
		return itemgivers.get(jobItemGiver.itemGiverId);
	}
	public void setTime(JobItemGiver jobItemGiver, long day) {
		itemgivers.put(jobItemGiver.itemGiverId, day);
	}
	public int getItemIndex(JobItemGiver jobItemGiver) {
		if(chained.containsKey(jobItemGiver.itemGiverId))
			return chained.get(jobItemGiver.itemGiverId);
		return 0;
	}
	public void setItemIndex(JobItemGiver jobItemGiver, int i) {
		chained.put(jobItemGiver.itemGiverId, i);
	}
}
