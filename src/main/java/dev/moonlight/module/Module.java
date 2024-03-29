package dev.moonlight.module;

import dev.moonlight.Moonlight;
import dev.moonlight.event.events.ModuleToggleEvent;
import dev.moonlight.misc.Bind;
import dev.moonlight.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

public abstract class Module extends Bind {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final Moonlight moonlight = Moonlight.INSTANCE;

    private final ArrayList<Setting> settings = new ArrayList<>();
    private final String name;
    private final Category category;
    private final String desc;
    private boolean enabled;
    private boolean visible = true;

    public Module() {
        if (getClass().isAnnotationPresent(Info.class)) {
            final Info info = getClass().getAnnotation(Info.class);
            this.name = info.name();
            this.category = info.category();
            this.desc = info.desc();
            this.setBind(info.bind());
            if (info.enabled())
                enable();
        } else {
            throw new RuntimeException(String.format("Module (%s) is missing @Info annotation", getClass().getName()));
        }
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getDesc() {
        return desc;
    }

    public void enable() {
        enabled = true;
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Enable(this));
        onEnable();
    }

    public void disable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Disable(this));
        onDisable();
    }

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    // 莱利是个愚蠢的笨蛋
    public String getMetaData() {
        return "";
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible() {
        visible = !visible;
    }

    public boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }

    public enum Category {
        Combat,
        Render,
        Movement,
        World,
        Player,
        Client,
        HUD
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();

        Category category();

        String desc();

        int bind() default Keyboard.KEY_NONE;

        boolean enabled() default false;
    }
}
