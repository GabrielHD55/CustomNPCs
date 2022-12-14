package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.shared.client.gui.listeners.IScrollData;
import noppes.npcs.shared.common.PacketBasic;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class PacketGuiScrollData extends PacketBasic {
	private final Map<String, Integer> data;

    public PacketGuiScrollData(Map<String, Integer> data) {
    	this.data = data;
    }

    public static void encode(PacketGuiScrollData msg, PacketBuffer buf) {
        buf.writeInt(msg.data.size());
        for(Map.Entry<String, Integer> e : msg.data.entrySet()){
            buf.writeUtf(e.getKey());
            buf.writeInt(e.getValue());
        }
    }

    public static PacketGuiScrollData decode(PacketBuffer buf) {
        HashMap<String, Integer> data = new HashMap<>();
        int size = buf.readInt();
        for(int i = 0; i < size; i++){
            data.put(buf.readUtf(32767), buf.readInt());
        }
        return new PacketGuiScrollData(data);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
	public void handle() {
        Screen gui = Minecraft.getInstance().screen;
        if(gui == null)
            return;
        if(gui instanceof GuiNPCInterface && ((GuiNPCInterface)gui).hasSubGui()){
            gui = ((GuiNPCInterface)gui).getSubGui();
        }
        if(gui instanceof GuiContainerNPCInterface && ((GuiContainerNPCInterface)gui).hasSubGui()){
            gui = ((GuiContainerNPCInterface)gui).getSubGui();
        }
        if(gui instanceof IScrollData)
            ((IScrollData)gui).setData(new Vector<String>(data.keySet()), data);
	}
}