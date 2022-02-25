package me.cxlina.perspectivemodreduxplus.mixin;

import me.cxlina.perspectivemodreduxplus.Main;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public class MixinMouse {

    @Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "net/minecraft/client/tutorial/TutorialManager.onUpdateMouse(DD)V"),  locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void updateMouseA(CallbackInfo ci, double d, double e, double x, double y, double f, double g, double h, int invert) {
        if (Main.instance.enabled) {
            Main.instance.cameraYaw = (float) (Main.instance.cameraYaw + x / 8.0D);
            Main.instance.cameraPitch = (float) (Main.instance.cameraPitch + y * invert / 8.0D);
            if (Math.abs(Main.instance.cameraPitch) > 90.0F)
                Main.instance.cameraPitch = (Main.instance.cameraPitch > 0.0F) ? 90.0F : -90.0F;
        }
    }

    @Inject(method = {"updateMouse"}, at = {@At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.changeLookDirection(DD)V")}, cancellable = true)
    private void updateMouseB(CallbackInfo info) {
        if (Main.instance.enabled)
            info.cancel();
    }
}