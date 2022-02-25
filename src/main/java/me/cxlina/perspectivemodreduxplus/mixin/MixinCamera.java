package me.cxlina.perspectivemodreduxplus.mixin;

import me.cxlina.perspectivemodreduxplus.Main;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public class MixinCamera {
    @Shadow
    private float pitch;

    @Shadow
    private float yaw;

    @Inject(method = {"update"}, at = {@At(value = "INVOKE", target = "net/minecraft/client/render/Camera.moveBy(DDD)V", ordinal = 0)})
    private void updateA(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Main.instance.enabled) {
            this.pitch = Main.instance.cameraPitch;
            this.yaw = Main.instance.cameraYaw;
        }
    }

    @ModifyArgs(method = {"update"}, at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.setRotation(FF)V", ordinal = 0))
    private void updateB(Args args) {
        if (Main.instance.enabled) {
            args.set(0, Main.instance.cameraYaw);
            args.set(1, Main.instance.cameraPitch);
        }
    }
}
