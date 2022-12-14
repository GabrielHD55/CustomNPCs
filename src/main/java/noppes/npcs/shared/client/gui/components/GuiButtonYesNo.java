package noppes.npcs.shared.client.gui.components;

import net.minecraft.client.gui.widget.button.Button;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiButtonYesNo extends GuiButtonNop {

	public GuiButtonYesNo(IGuiInterface gui, int id, int x, int y, boolean bo, Button.IPressable clicked) {
		this(gui, id, x, y, 50, 20, bo, clicked);
	}

	public GuiButtonYesNo(IGuiInterface gui, int id, int x, int y, int width, int height, boolean bo, Button.IPressable clicked) {
		super(gui, id, x, y, width, height, clicked, bo?1:0, "gui.no", "gui.yes");
	}

	public GuiButtonYesNo(IGuiInterface gui, int id, int x, int y, boolean bo) {
		this(gui, id, x, y, 50, 20, bo, clicked);
	}

	public GuiButtonYesNo(IGuiInterface gui, int id, int x, int y, int width, int height, boolean bo) {
		this(gui, id, x, y, width, height, bo, clicked);
	}
	
	public boolean getBoolean(){
		return getValue() == 1;
	}
}
