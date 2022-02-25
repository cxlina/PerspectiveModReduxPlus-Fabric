package me.cxlina.perspectivemodreduxplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;

public class Main implements ModInitializer {

    public static Main instance;
    private static KeyBinding toggleKey;
    private MinecraftClient client;
    public boolean enabled;
    public float cameraPitch;
    public float cameraYaw;
    private boolean toggled = false;
    private Perspective lastPerspective;

    public Main() {
        this.client = MinecraftClient.getInstance();
        this.enabled = false;
        instance = this;
    }

    public void onInitialize() {
        KeyBindingRegistryImpl.addCategory("key.perspectivemodreduxplus.category");
        KeyBindingRegistryImpl.registerKeyBinding(toggleKey = new KeyBinding("key.perspectivemodreduxplus.toggle", InputUtil.Type.KEYSYM, 293, "key.perspectivemodreduxplus.category"));
        ClientTickEvents.START_CLIENT_TICK.register(e -> {
            if (this.client.player != null) {
                if (toggleKey.wasPressed()) {
                    this.enabled = !this.enabled;
                    this.cameraPitch = this.client.player.getPitch();
                    this.cameraYaw = this.client.player.getYaw();
                    if (this.enabled) {
                        if (!toggled) {
                            this.lastPerspective = this.client.options.getPerspective();
                            toggled = true;
                        }

                        if (client.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) { // double f5
                            this.cameraYaw = ((180 + this.client.player.getYaw() + 180) % 360) - 180;
                            this.cameraPitch = -this.client.player.getPitch();
                        }
                        this.client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                    } else {
                        this.client.options.setPerspective(this.lastPerspective);
                        toggled = false;
                    }
                }
                if (this.enabled && this.client.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
                    this.enabled = false;
                }
            }
        });
    }
}
