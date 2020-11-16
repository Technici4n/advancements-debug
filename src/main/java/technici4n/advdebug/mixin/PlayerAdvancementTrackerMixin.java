package technici4n.advdebug.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import technici4n.advdebug.AdvDebug;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
    @Shadow
    protected abstract void updateDisplay(Advancement advancement);

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/PlayerAdvancementTracker;updateDisplay(Lnet/minecraft/advancement/Advancement;)V"), method = "updateDisplay", require = 1)
    private void updateDisplayRedirect(PlayerAdvancementTracker tracker, Advancement advancement) {
        AdvDebug.recursionDepth++;
        if (AdvDebug.recursionDepth > 50) {
            String advId = advancement.getId().toString();
            String parentId = advancement.getParent() == null ? "(no parent)" : advancement.getParent().getId().toString();
            AdvDebug.LOGGER.info("updateDisplay called with advancement {}, its parent is {}, depth is {}", advId, parentId, AdvDebug.recursionDepth);
        }
        updateDisplay(advancement);
        AdvDebug.recursionDepth--;
    }
}
