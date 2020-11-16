package technici4n.advdebug.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import technici4n.advdebug.AdvDebug;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Inject(at = @At("HEAD"), method = "updateDisplay")
    private void updateDisplayHeadInject(Advancement advancement, CallbackInfo ci) {
        AdvDebug.recursionDepth++;
        if (AdvDebug.recursionDepth > 50) {
            String advId = advancement.getId().toString();
            String parentId = advancement.getParent() == null ? "(no parent)" : advancement.getParent().getId().toString();
            AdvDebug.LOGGER.info("updateDisplay called with advancement {}, its parent is {}", advId, parentId);
        }
    }

    @Inject(at = @At("RETURN"), method = "updateDisplay")
    private void updateDisplayTailInject(Advancement advancement, CallbackInfo ci) {
        AdvDebug.recursionDepth--;
    }
}
