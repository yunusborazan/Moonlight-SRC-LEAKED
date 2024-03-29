package dev.moonlight.ui.clickgui.settings;

import dev.moonlight.Moonlight;
import dev.moonlight.module.mods.client.Font;
import dev.moonlight.module.mods.client.GUI;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.ui.clickgui.api.SettingComponent;
import dev.moonlight.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComponent extends SettingComponent {
    public FloatSetting setting;
    public boolean isDragging;
    public float sliderWidth;

    public SliderComponent(FloatSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && (mouseButton == 1 || mouseButton == 0)) {
            isDragging = true;
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 || mouseButton == 0) {
            isDragging = false;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        updateSliderShit(mouseX);
        if (isInside(mouseX, mouseY))
            Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
        final StringBuilder sb = new StringBuilder(setting.getName() + ": " + setting.getValue());

        int r = (int) Moonlight.INSTANCE.getModuleManager().getModule(GUI.class).r.getValue();
        int g = (int) Moonlight.INSTANCE.getModuleManager().getModule(GUI.class).g.getValue();
        int b = (int) Moonlight.INSTANCE.getModuleManager().getModule(GUI.class).b.getValue();
        int a = (int) Moonlight.INSTANCE.getModuleManager().getModule(GUI.class).a.getValue();
        Gui.drawRect(x, y + (height / 2) - 1, x + width, y + (height / 2), new Color(r, g, b, 50).getRGB());
        Gui.drawRect(x, y + (height / 2) - 1, x + (int) sliderWidth, y + (height / 2), new Color(r, g, b, a).getRGB());
        RenderUtil.drawRoundedRect(x + sliderWidth - 4, y + (height / 2) - 4, 8, 8, 9, isInside(mouseX, mouseY) ? new Color(r, g, b, a) : new Color(r, g, b, a));
        if(Moonlight.INSTANCE.getModuleManager().getModule(Font.class).isEnabled()) {
            cfont.drawStringWithShadow(sb.toString(), x + 2f, y, -1);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(sb.toString(), x + 2f, y, -1);
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) {

    }

    protected void updateSliderShit(int mouseX) {
        float diff = Math.min(width, Math.max(0, mouseX - x));
        float min = setting.getMin();
        float max = setting.getMax();
        sliderWidth = width * (setting.getValue() - min) / (max - min);
        if (isDragging) {
            if (diff == 0) {
                setting.setValue(setting.getMin());
            } else {
                float value = roundToPlace(diff / width * (max - min) + min, 1);
                setting.setValue(value);
            }
        }
    }

    public static float roundToPlace(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
