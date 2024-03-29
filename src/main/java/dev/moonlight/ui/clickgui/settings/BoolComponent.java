package dev.moonlight.ui.clickgui.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.ui.clickgui.api.SettingComponent;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public final class BoolComponent extends SettingComponent {
    private final BoolSetting boolSetting;

    public BoolComponent(BoolSetting boolSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.boolSetting = boolSetting;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(boolSetting.isParent()){
            if (isInside(mouseX, mouseY) && mouseButton == 1) {
                boolSetting.toggle();
            }
        } else {
            if (isInside(mouseX, mouseY) && mouseButton == 0) {
                boolSetting.toggle();
            }
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(!boolSetting.isParent()) {
            if (isInside(mouseX, mouseY)) {
                Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
            }

            final StringBuilder sb = new StringBuilder(boolSetting.getName() + ":");

            if (boolSetting.getValue()) {
                sb.append(ChatFormatting.GREEN + " True");
            } else {
                sb.append(ChatFormatting.RED + " False");
            }

            if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
                cfont.drawStringWithShadow(sb.toString(), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(sb.toString(), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
            }
        } else {
            if(isInside(mouseX, mouseY)){
                Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
                RenderUtil.outline2d(x + 1, y, x + width -1, y + height - 1, 0x20ffffff);
            }
            Gui.drawRect(x , y, x + width - 1, y + height, 0x90000000);
            if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
                cfont.drawStringWithShadow(boolSetting.getValue() ? "> " + boolSetting.getName() : boolSetting.getName(), x + width / 2 - Moonlight.INSTANCE.getFontRenderer().getStringWidth(boolSetting.getValue() ? "> " + boolSetting.getName() : boolSetting.getName()) / 2, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(boolSetting.getValue() ? "> " + boolSetting.getName() : boolSetting.getName(), x + width / 2 - Moonlight.INSTANCE.getFontRenderer().getStringWidth(boolSetting.getValue() ? "> " + boolSetting.getName() : boolSetting.getName()) / 2, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
            }
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) {
    }

    @Override
    public boolean isVisible() {
        return boolSetting.isVisible();
    }
}
