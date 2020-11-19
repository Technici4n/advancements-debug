package technici4n.advdebug.mixin;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
    @Shadow @Final private Set<Advancement> visibleAdvancements;
    @Shadow @Final private Set<Advancement> visibilityUpdates;
    @Shadow @Final private Set<Advancement> progressUpdates;
    @Shadow @Final private Map<Advancement, AdvancementProgress> advancementToProgress;
    @Shadow protected abstract boolean canSee(Advancement advancement);

    private void updateDisplayDfs(Advancement advancement, Set<Advancement> visited) {
        if(visited.add(advancement)) {
            boolean bl = this.canSee(advancement);
            boolean bl2 = this.visibleAdvancements.contains(advancement);
            if (bl && !bl2) {
                this.visibleAdvancements.add(advancement);
                this.visibilityUpdates.add(advancement);
                if (this.advancementToProgress.containsKey(advancement)) {
                    this.progressUpdates.add(advancement);
                }
            } else if (!bl && bl2) {
                this.visibleAdvancements.remove(advancement);
                this.visibilityUpdates.add(advancement);
            }

            for(Advancement child : advancement.getChildren()) {
                updateDisplayDfs(child, visited);
            }

            Advancement parent = advancement.getParent();
            if (bl != bl2 && parent != null) {
                updateDisplayDfs(parent, visited);
            }
        }
    }

    @Overwrite
    protected void updateDisplay(Advancement advancement) {
        updateDisplayDfs(advancement, new ReferenceOpenHashSet<>());
    }
}
