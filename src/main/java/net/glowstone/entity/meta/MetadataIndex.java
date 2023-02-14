package net.glowstone.entity.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.glowstone.entity.passive.GlowParrot;
import net.glowstone.entity.passive.GlowTameable;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Fish;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Trident;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;

import static net.glowstone.entity.meta.MetadataType.*;

/**
 * Index constants for entity metadata.
 */
@RequiredArgsConstructor
public enum MetadataIndex {

    //Entity
    STATUS(0, BYTE, Entity.class),
    AIR_TIME(1, INT, Entity.class),
    NAME_TAG(2, OPTCHAT, Entity.class),
    SHOW_NAME_TAG(3, BOOLEAN, Entity.class),
    SILENT(4, BOOLEAN, Entity.class),
    NOGRAVITY(5, BOOLEAN, Entity.class),
    ENTITY_POSE(6, POSE, Entity.class),
    TICKS_FROZEN(7, INT, Entity.class),
    AREAEFFECTCLOUD_RADIUS(8, FLOAT, AreaEffectCloud.class),
    AREAEFFECTCLOUD_COLOR(9, INT, AreaEffectCloud.class),
    AREAEFFECTCLOUD_POINT(10, BOOLEAN, AreaEffectCloud.class),
    AREAEFFECTCLOUD_PARTICLE(11, PARTICLE, AreaEffectCloud.class),

    ABSTRACT_ARROW_STATUS(8, BYTE, AbstractArrow.class),
    ABSTRACT_ARROW_PIERCING(9, BYTE, AbstractArrow.class),

    TIPPEDARROW_COLOR(10, INT, Arrow.class),

    TRIDENT_LOYALTY(10, INT, Trident.class),
    TRIDENT_GLINT(11, BOOLEAN, Trident.class),

    BOAT_HIT_TIME(8, INT, Boat.class),
    BOAT_DIRECTION(9, INT, Boat.class),
    BOAT_DAMAGE_TAKEN(10, FLOAT, Boat.class),
    BOAT_TYPE(11, INT, Boat.class),
    BOAT_RIGHT_PADDLE_TURNING(13, BOOLEAN, Boat.class),
    BOAT_LEFT_PADDLE_TURNING(12, BOOLEAN, Boat.class),
    BOAT_SPLASH_TIMER(14, INT, Boat.class),

    ENDERCRYSTAL_BEAM_TARGET(8, OPTPOSITION, EnderCrystal.class),
    ENDERCRYSTAL_SHOW_BOTTOM(9, BOOLEAN, EnderCrystal.class),

    WITHERSKULL_INVULNERABLE(8, BOOLEAN, WitherSkull.class),

    FIREWORK_INFO(8, ITEM, Firework.class),
    FIREWORK_ENTITY(9, OPTINT, Firework.class),
    FIREWORK_SHOT_AT_ANGLE(10, OPTINT, Firework.class),

    ITEM_FRAME_ITEM(8, ITEM, ItemFrame.class),
    ITEM_FRAME_ROTATION(9, INT, ItemFrame.class),
    ITEM_ITEM(8, ITEM, Item.class),

    HAND_USED(8, BYTE, LivingEntity.class),
    HEALTH(9, FLOAT, LivingEntity.class),
    POTION_COLOR(10, INT, LivingEntity.class),
    POTION_AMBIENT(11, BOOLEAN, LivingEntity.class),
    ARROW_COUNT(12, BYTE, LivingEntity.class),
    BEE_STINGS(13, INT, LivingEntity.class),
    SLEEPING_LOC(14, OPTPOSITION, LivingEntity.class),

    PLAYER_EXTRA_HEARTS(15, FLOAT, Player.class),
    PLAYER_SCORE(16, INT, Player.class),
    PLAYER_SKIN_PARTS(17, BYTE, Player.class),
    PLAYER_MAIN_HAND(18, BYTE, Player.class),
    PLAYER_LEFT_SHOULDER(19, NBTTAG, Player.class),
    PLAYER_RIGHT_SHOULDER(20, NBTTAG, Player.class),

    ARMORSTAND_FLAGS(15, BYTE, ArmorStand.class),
    ARMORSTAND_HEAD_POSITION(16, VECTOR, ArmorStand.class),
    ARMORSTAND_BODY_POSITION(17, VECTOR, ArmorStand.class),
    ARMORSTAND_LEFT_ARM_POSITION(18, VECTOR, ArmorStand.class),
    ARMORSTAND_RIGHT_ARM_POSITION(19, VECTOR, ArmorStand.class),
    ARMORSTAND_LEFT_LEG_POSITION(20, VECTOR, ArmorStand.class),
    ARMORSTAND_RIGHT_LEG_POSITION(21, VECTOR, ArmorStand.class),

    NO_AI(15, BYTE, Mob.class),

    // TODO - 1.9 "Insentient extends Living". Need more information


    BAT_FLAGS(16, BYTE, Bat.class),

    AGE_ISBABY(16, BOOLEAN, Ageable.class),

    ABSTRACT_HORSE_FLAGS(17, BYTE, AbstractHorse.class),
    ABSTRACT_HORSE_OWNER(18, OPTUUID, AbstractHorse.class),

    HORSE_STYLE(19, INT, Horse.class),

    CHESTED_HORSE_HAS_CHEST(19, BOOLEAN, ChestedHorse.class),

    LLAMA_STRENGTH(20, INT, Llama.class),
    LLAMA_CARPET(21, INT, Llama.class),
    LLAMA_VARIANT(22, INT, Llama.class),

    PIG_SADDLE(17, BOOLEAN, Pig.class),
    PIG_BOOST(18, INT, Pig.class),

    RABBIT_TYPE(17, INT, Rabbit.class),

    SHEEP_DATA(17, BYTE, Sheep.class),

    TAMEABLEAANIMAL_STATUS(17, BYTE, GlowTameable.class),
    TAMEABLEANIMAL_OWNER(18, OPTUUID, GlowTameable.class),

    OCELOT_TYPE(17, INT, Ocelot.class),

    WOLF_HEALTH(19, FLOAT, Wolf.class),
    WOLF_COLOR(20, INT, Wolf.class),
    WOLF_ANGER_TIME(21, INT, Wolf.class),
    ABSTRACT_VILLAGER_SHAKE(17, INT, AbstractVillager.class),

    //Villager data has been changed significantly
    //VILLAGER_PROFESSION(13, INT, Villager.class),

    GOLEM_PLAYER_BUILT(16, BYTE, IronGolem.class),

    SNOWMAN_NOHAT(16, BYTE, Snowman.class),

    SHULKER_FACING_DIRECTION(16, DIRECTION, Shulker.class),
    SHULKER_ATTACHMENT_POSITION(17, OPTPOSITION, Shulker.class),
    SHULKER_SHIELD_HEIGHT(18, BYTE, Shulker.class),
    SHULKER_COLOR(19, BYTE, Shulker.class),

    BLAZE_ON_FIRE(16, BYTE, Blaze.class),

    CREEPER_STATE(16, INT, Creeper.class),
    CREEPER_POWERED(17, BOOLEAN, Creeper.class),
    CREEPER_IGNITED(18, BOOLEAN, Creeper.class),

    GUARDIAN_SPIKES(16, BOOLEAN, Guardian.class),
    GUARDIAN_TARGET(17, INT, Guardian.class),

    SPIDER_CLIMBING(16, BYTE, Spider.class),

    DRINKING_POTION(17, BOOLEAN, Witch.class),

    WITHER_TARGET_1(16, INT, Wither.class),
    WITHER_TARGET_2(17, INT, Wither.class),
    WITHER_TARGET_3(18, INT, Wither.class),
    WITHER_INVULN_TIME(19, INT, Wither.class),

    ZOMBIE_IS_CHILD(16, BOOLEAN, Zombie.class),
    ZOMBIE_PROFESSION(17, INT, Zombie.class), // Unused as of 1.11
    ZOMBIE_BECOMING_DROWNED(18, BOOLEAN, Zombie.class),

    ZOMBIE_VILLAGER_IS_CONVERTING(16, BOOLEAN, ZombieVillager.class),
    //Villager data is very different now
    ZOMBIE_VILLAGER_PROFESSION(17, INT, ZombieVillager.class),

    ENDERMAN_BLOCK(16, BLOCKID, Enderman.class),
    ENDERMAN_SCREAMING(17, BOOLEAN, Enderman.class),
    ENDERMAN_STARING(18, BOOLEAN, Enderman.class),

    ENDERDRAGON_PHASE(16, INT, EnderDragon.class),

    GHAST_ATTACKING(16, BOOLEAN, Ghast.class),

    SLIME_SIZE(16, INT, Slime.class),

    POLARBEAR_STANDING(17, BOOLEAN, PolarBear.class),

    MINECART_SHAKE_POWER(8, INT, Minecart.class),
    MINECART_SHAKE_DIRECTION(9, INT, Minecart.class),
    MINECART_DAMAGE_TAKEN(10, FLOAT, Minecart.class),
    MINECART_BLOCK(11, INT, Minecart.class),
    MINECART_BLOCK_OFFSET(12, INT, Minecart.class),
    MINECART_BLOCK_SHOWN(13, BOOLEAN, Minecart.class),

    EVOKER_SPELL(17, BYTE, Evoker.class),

    VEX_STATE(16, BYTE, Vex.class),

    PHANTOM_SIZE(16, INT, Phantom.class),

    DOLPHIN_TREASURE_POSITION(16, POSITION, Dolphin.class),
    DOLPHIN_HAS_FISH(17, POSITION, Dolphin.class),
    DOLPHIN_MOISTURE(18, INT, Dolphin.class),

    FISH_FROM_BUCKET(16, BOOLEAN, Fish.class),

    PUFFER_FISH_STATE(17, INT, PufferFish.class),

    TROPICAL_FISH_VARIANT(17, INT, TropicalFish.class),

    TURTLE_HOME_POSITION(17, POSITION, Turtle.class),
    TURTLE_HAS_EGG(18, BOOLEAN, Turtle.class),
    TURTLE_LAYING_EGG(19, BOOLEAN, Turtle.class),
    TURTLE_TRAVEL_POS(20, POSITION, Turtle.class),
    TURTLE_GOING_HOME(21, BOOLEAN, Turtle.class),
    TURTLE_TRAVELLING(22, BOOLEAN, Turtle.class),

    PARROT_VARIANT(19, INT, GlowParrot.class),

    MINECARTCOMMANDBLOCK_COMMAND(14, STRING, CommandMinecart.class),
    MINECARTCOMMANDBLOCK_LAST_OUTPUT(15, CHAT, CommandMinecart.class),

    FURNACE_MINECART_POWERED(14, BOOLEAN, PoweredMinecart.class),
    TNT_PRIMED(8, INT, TNTPrimed.class),

    /**
     * Hooked entity id + 1, or 0 if there is no hooked entity.
     */
    FISHING_HOOK_HOOKED_ENTITY(8, INT, FishHook.class),
    FISHING_HOOK_CATCHABLE(9, BOOLEAN, FishHook.class);

    @Getter
    private final int index;
    @Getter
    private final MetadataType type;
    @Getter
    private final Class<? extends Entity> appliesTo;

    /**
     * Returns the first {@link MetadataIndex} with a given index and {@link MetadataType}.
     *
     * @param index the index to look up
     * @param type  the type to look up
     * @return a {@link MetadataIndex} with that index and type, or null if none match
     */
    public static MetadataIndex getIndex(int index, MetadataType type) {
        MetadataIndex output = null;
        for (MetadataIndex entry : values()) {
            if (entry.getIndex() == index && entry.getType().equals(type)) {
                output = entry;
                break;
            }
        }
        return output;
    }

    public boolean appliesTo(Class<? extends Entity> clazz) {
        return appliesTo.isAssignableFrom(clazz);
    }

    public interface StatusFlags {

        int ON_FIRE = 0x01;
        int SNEAKING = 0x02;
        int SPRINTING = 0x08;
        int ARM_UP = 0x10; // eating, drinking, blocking
        int INVISIBLE = 0x20;
        int GLOWING = 0x40;
        int GLIDING = 0x80;
    }

    public interface ArmorStandFlags {

        int IS_SMALL = 0x01;
        int HAS_GRAVITY = 0x02;
        int HAS_ARMS = 0x04;
        int NO_BASE_PLATE = 0x08;
        int IS_MARKER = 0x10;
    }

    public interface HorseFlags {

        int IS_TAME = 0x02;
        int HAS_SADDLE = 0x04;
        int HAS_CHEST = 0x08;
        int IS_BRED = 0x10;
        int IS_EATING = 0x20;
        int IS_REARING = 0x40;
        int MOUTH_OPEN = 0x80;
    }

    public interface TameableFlags {

        int IS_SITTING = 0x01;
        int WOLF_IS_ANGRY = 0x02;
        int IS_TAME = 0x04;
    }

    public interface BatFlags {

        int IS_HANGING = 0x01;
    }
}
