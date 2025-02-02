package net.glowstone.entity.monster;

import com.flowpowered.network.Message;
import lombok.Getter;
import lombok.Setter;
import net.glowstone.EventFactory;
import net.glowstone.entity.meta.MetadataIndex;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlowCreeper extends GlowMonster implements Creeper {

    @Getter
    @Setter
    private int explosionRadius;
    @Getter
    @Setter
    private int maxFuseTicks;
    @Getter
    @Setter
    private int fuseTicks;
    @Getter
    @Setter
    private boolean ignited;

    public GlowCreeper(Location loc) {
        super(loc, EntityType.CREEPER, 20);
        setBoundingBox(0.6, 1.7);
    }

    @Override
    public List<Message> createSpawnMessage() {
        // todo Implement the fuse & Ignition later.
        return super.createSpawnMessage();
    }

    @Override
    public boolean isPowered() {
        return metadata.getBoolean(MetadataIndex.CREEPER_POWERED);
    }

    @Override
    public void setPowered(boolean value) {
        metadata.set(MetadataIndex.CREEPER_POWERED, value);
    }

    @Override
    public void explode() {
        // TODO: explode immediately.
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void ignite() {
        // TODO: start ticking down the fuse.
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_CREEPER_DEATH;
    }

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_CREEPER_HURT;
    }

    @Override
    public void damage(double amount, Entity source, @NotNull DamageCause cause) {
        super.damage(amount, source, cause);
        if (DamageCause.LIGHTNING.equals(cause) && !isPowered()) {
            CreeperPowerEvent event = EventFactory.getInstance()
                .callEvent(new CreeperPowerEvent(
                    this,
                    (LightningStrike) source,
                    CreeperPowerEvent.PowerCause.LIGHTNING));

            if (!event.isCancelled()) {
                setPowered(true);
            }
        }
    }
}
