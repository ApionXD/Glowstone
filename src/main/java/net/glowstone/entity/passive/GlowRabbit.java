package net.glowstone.entity.passive;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.glowstone.entity.GlowAnimal;
import net.glowstone.entity.meta.MetadataIndex;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkNotNull;

public class GlowRabbit extends GlowAnimal implements Rabbit {

    private static final Set<Material> BREEDING_FOODS = Sets.immutableEnumSet(Material.CARROT,
        Material.GOLDEN_CARROT,
        Material.DANDELION);

    private static final BiMap<Type, Integer> rabbitTypeIntegerMap = ImmutableBiMap
        .<Type, Integer>builder()
        .put(Type.BROWN, 0)
        .put(Type.WHITE, 1)
        .put(Type.BLACK, 2)
        .put(Type.BLACK_AND_WHITE, 3)
        .put(Type.GOLD, 4)
        .put(Type.SALT_AND_PEPPER, 5)
        .put(Type.THE_KILLER_BUNNY, 99)
        .build();

    @Getter
    private Type rabbitType;

    /**
     * Creates a rabbit of a random type.
     *
     * @param location the location
     */
    public GlowRabbit(Location location) {
        super(location, EntityType.RABBIT, 3);
        setSize(0.4F, 0.5F);
        setRabbitType(
            Type.values()[ThreadLocalRandom.current().nextInt(rabbitTypeIntegerMap.size())]);
    }

    @Override
    public void setRabbitType(Type type) {
        checkNotNull(type, "Cannot set a null rabbit type!");
        metadata.set(MetadataIndex.RABBIT_TYPE, rabbitTypeIntegerMap.get(type).byteValue());
        rabbitType = type;
    }

    @Override
    public void setMoreCarrotTicks(int ticks) {

    }

    @Override
    public int getMoreCarrotTicks() {
        return 0;
    }

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_RABBIT_HURT;
    }

    @Override
    public Sound getDeathSound() {
        return Sound.ENTITY_RABBIT_DEATH;
    }

    @Override
    public Sound getAmbientSound() {
        return Sound.ENTITY_RABBIT_AMBIENT;
    }

    @Override
    public Set<Material> getBreedingFoods() {
        return BREEDING_FOODS;
    }
}
