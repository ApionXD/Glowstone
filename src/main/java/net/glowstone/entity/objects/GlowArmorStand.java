package net.glowstone.entity.objects;

import com.flowpowered.network.Message;
import io.papermc.paper.math.Rotations;
import net.glowstone.EventFactory;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataIndex.ArmorStandFlags;
import net.glowstone.entity.meta.MetadataIndex.StatusFlags;
import net.glowstone.inventory.ClothType;
import net.glowstone.inventory.GlowEntityEquipment;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.SpawnEntityMessage;
import net.glowstone.net.message.play.player.InteractEntityMessage;
import net.glowstone.net.message.play.player.InteractEntityMessage.Action;
import net.glowstone.util.InventoryUtil;
import net.kyori.adventure.util.TriState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Consumer;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GlowArmorStand extends GlowLivingEntity implements ArmorStand {

    private static final EulerAngle[] defaultPose = new EulerAngle[6];

    static {
        defaultPose[0] = EulerAngle.ZERO;
        defaultPose[1] = EulerAngle.ZERO;
        double ten = 0.17453292519943295; // Math.toRadians(10)
        defaultPose[2] = new EulerAngle(-ten, 0, -ten);
        double fifteen = 0.2617993877991494; // Math.toRadians(15)
        defaultPose[3] = new EulerAngle(-fifteen, 0, ten);
        double one = 0.017453292519943295; // Math.toRadians(1)
        defaultPose[4] = new EulerAngle(-one, 0, -one);
        defaultPose[5] = new EulerAngle(one, 0, one);
    }


    private final GlowEntityEquipment equipment;
    private final EulerAngle[] pose = new EulerAngle[6];

    private boolean isMarker;
    private boolean isVisible = true;
    private boolean isSmall;
    private boolean hasBasePlate = true;
    private boolean hasGravity = true;
    private boolean hasArms;

    private boolean needsKill;
    private boolean canTick = true;

    /**
     * Creates an armor stand.
     *
     * @param location the location of the armor stand
     */
    public GlowArmorStand(Location location) {
        super(location, 2);

        equipment = new GlowEntityEquipment(this);

        System.arraycopy(defaultPose, 0, pose, 0, 6);
        this.getEquipmentMonitor().resetChanges();
        setSize(false);
    }

    @Override
    public void reset() {
        super.reset();
        if (needsKill) {
            needsKill = false;
        }
    }

    @Override
    public void pulse() {
        super.pulse();
        if (isDead()) {
            remove();
            needsKill = true;
        } else if (ticksLived % 10 == 0) { //player needs to click fast (2 times) to kill the entity
            setHealth(2);
        }
    }

    @Override
    public void damage(double amount, Entity source, @NotNull DamageCause cause) {
        if (getNoDamageTicks() > 0 || health <= 0 || !canTakeDamage(cause)) {
            return;
        }
        if (source instanceof Projectile && !(source instanceof Arrow)) {
            return;
        }
        EntityDamageEvent event = EventFactory.getInstance().onEntityDamage(source == null
            ? new EntityDamageEvent(this, cause, amount)
            : new EntityDamageByEntityEvent(source, this, cause, amount));
        if (event.isCancelled()) {
            return;
        }
        boolean drop = false;
        if (source instanceof GlowPlayer || source instanceof Arrow && ((Projectile) source)
            .getShooter() instanceof GlowPlayer) {
            GlowPlayer damager = (GlowPlayer) (source instanceof GlowPlayer ? source
                : ((Arrow) source).getShooter());
            if (damager.getGameMode() == GameMode.ADVENTURE) {
                return;
            } else if (damager.getGameMode() == GameMode.CREATIVE) {
                amount = 2; //Instantly kill the entity
            } else {
                amount = 1; //Needs two hits
                drop = true;
            }
        }
        setLastDamage(amount);
        setHealth(health - amount, drop);
    }

    @Override
    public void setHealth(double health) {
        setHealth(health, false);
    }

    private void setHealth(double health, boolean drop) {
        if (health < 0) {
            health = 0;
        }
        if (health > getMaxHealth()) {
            health = getMaxHealth();
        }
        this.health = health;

        metadata.set(MetadataIndex.HEALTH, (float) health);
        for (Objective objective : getServer().getScoreboardManager().getMainScoreboard()
            .getObjectivesByCriteria(Criterias.HEALTH)) {
            objective.getScore(getName()).setScore((int) health);
        }

        if (health == 0) {
            kill(drop);
        }
    }

    private void kill(boolean dropArmorStand) {
        active = false;
        // TODO: set block to oak wood, requires flattening (numerical ID)
        location.getWorld().spawnParticle(
            Particle.BLOCK_DUST,
            location.clone().add(0, 1.317, 0), 1, 0.125f, 0.494f, 0.125f);
        for (ItemStack stack : equipment.getArmorContents()) {
            if (InventoryUtil.isEmpty(stack)) {
                continue;
            }
            getWorld().dropItemNaturally(location, stack);
        }
        if (dropArmorStand) {
            getWorld().dropItemNaturally(location, new ItemStack(Material.ARMOR_STAND));
        }
    }

    @Override
    public boolean entityInteract(GlowPlayer player, InteractEntityMessage msg) {
        if (player.getGameMode() == GameMode.SPECTATOR || isMarker) {
            return false;
        }
        if (msg.getAction() == Action.INTERACT_AT.ordinal()) {
            if (InventoryUtil.isEmpty(player.getItemInHand())) {
                EquipmentSlot slot = getEditSlot(msg.getTargetY());

                PlayerArmorStandManipulateEvent event = new PlayerArmorStandManipulateEvent(player,
                    this, InventoryUtil.itemOrEmpty(null),
                    InventoryUtil.itemOrEmpty(equipment.getItem(slot)), slot);
                EventFactory.getInstance().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                if (isEmpty(slot)) {
                    return false;
                }

                ItemStack stack = equipment.getItem(slot);
                player.setItemInHand(stack);
                equipment.setItem(slot, InventoryUtil.createEmptyStack());
                return true;
            } else {
                EquipmentSlot slot = player.getItemInHand().getType().getEquipmentSlot();
                if ((slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND) && !hasArms) {
                    return false;
                }

                PlayerArmorStandManipulateEvent event = new PlayerArmorStandManipulateEvent(player,
                    this, player.getItemInHand(),
                    InventoryUtil.itemOrEmpty(equipment.getItem(slot)), slot);
                EventFactory.getInstance().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                ItemStack stack = player.getItemInHand();
                ItemStack back;
                if (isEmpty(slot)) {
                    if (stack.getAmount() > 1) {
                        stack.setAmount(stack.getAmount() - 1);
                        back = stack;
                    } else {
                        back = InventoryUtil.createEmptyStack();
                    }
                } else {
                    if (stack.getAmount() > 1) {
                        return false;
                    }
                    back = equipment.getItem(slot);
                }

                if (!InventoryUtil.isEmpty(back)) {
                    player.setItemInHand(back);
                }
                equipment.setItem(slot, stack);
                player.playSound(location, getEquipSound(stack.getType()), SoundCategory.NEUTRAL, 1,
                    1);
                return true;
            }
        }
        return false;
    }

    private Sound getEquipSound(Material mat) {
        if (mat == Material.ELYTRA) {
            return Sound.ITEM_ARMOR_EQUIP_ELYTRA;
        }
        if (ClothType.LEATHER.matches(mat)) {
            return Sound.ITEM_ARMOR_EQUIP_LEATHER;
        }
        if (ClothType.CHAINMAIL.matches(mat)) {
            return Sound.ITEM_ARMOR_EQUIP_CHAIN;
        }
        if (ClothType.IRON.matches(mat)) {
            return Sound.ITEM_ARMOR_EQUIP_IRON;
        }
        if (ClothType.GOLD.matches(mat)) {
            return Sound.ITEM_ARMOR_EQUIP_GOLD;
        }
        if (ClothType.DIAMOND.matches(mat)) {
            return Sound.ITEM_ARMOR_EQUIP_DIAMOND;
        }

        return Sound.ITEM_ARMOR_EQUIP_GENERIC;
    }

    private EquipmentSlot getEditSlot(float height) {
        if (isSmall) {
            height *= 2;
        }

        if (height >= 0.1 && height < 0.1 + (isSmall ? 0.8 : 0.45) && !isEmpty(
            EquipmentSlot.FEET)) {
            return EquipmentSlot.FEET;
        } else if (height >= 0.9 + (isSmall ? 0.3 : 0) && height < 0.9 + (isSmall ? 1 : 0.7)
            && !isEmpty(EquipmentSlot.CHEST)) {
            return EquipmentSlot.CHEST;
        } else if (height >= 0.4 && height < 0.4 + (isSmall ? 1 : 0.8) && !isEmpty(
            EquipmentSlot.LEGS)) {
            return EquipmentSlot.LEGS;
        } else if (height >= 1.6 && !isEmpty(EquipmentSlot.HEAD)) {
            return EquipmentSlot.HEAD;
        }
        return EquipmentSlot.HAND;
    }

    private boolean isEmpty(EquipmentSlot slot) {
        return InventoryUtil.isEmpty(equipment.getItem(slot));
    }

    @Override
    public boolean canTakeDamage(DamageCause cause) {
        switch (cause) {
            case ENTITY_ATTACK:
            case PROJECTILE:
            case FIRE_TICK:
            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
            case VOID:
            case CUSTOM:
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<Message> createSpawnMessage() {

        return Arrays.asList(
            new SpawnEntityMessage(entityId, UUID.randomUUID(), 78, location),
            // TODO: once UUID is documented, actually use the appropriate ID here
            new EntityMetadataMessage(entityId, metadata.getEntryList()),
            new EntityEquipmentMessage(entityId, EntityEquipmentMessage.HELD_ITEM,
                getItemInHand()),
            new EntityEquipmentMessage(entityId, EntityEquipmentMessage.OFF_HAND,
                equipment.getItemInOffHand()),
            new EntityEquipmentMessage(entityId, EntityEquipmentMessage.BOOTS_SLOT, getBoots()),
            new EntityEquipmentMessage(entityId, EntityEquipmentMessage.LEGGINGS_SLOT,
                getLeggings()),
            new EntityEquipmentMessage(entityId, EntityEquipmentMessage.CHESTPLATE_SLOT,
                getChestplate()),
            new EntityEquipmentMessage(entityId, EntityEquipmentMessage.HELMET_SLOT,
                getHelmet())
        );
    }

    @Override
    public List<Message> createUpdateMessage(GlowSession session) {
        List<Message> messages = super.createUpdateMessage(session);
        if (needsKill) {
            messages.add(new DestroyEntitiesMessage(Collections.singletonList(entityId)));
        }

        return messages;
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public ItemStack getItemInHand() {
        return equipment.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        equipment.setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return equipment.getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        equipment.setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return equipment.getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        equipment.setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return equipment.getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        equipment.setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return equipment.getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        equipment.setHelmet(item);
    }

    @Override
    public EulerAngle getHeadPose() {
        return pose[0];
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        this.pose[0] = pose;
        metadata.set(MetadataIndex.ARMORSTAND_HEAD_POSITION, pose);
    }

    @Override
    public EulerAngle getBodyPose() {
        return pose[1];
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        this.pose[1] = pose;
        metadata.set(MetadataIndex.ARMORSTAND_BODY_POSITION, pose);
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return pose[2];
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        this.pose[2] = pose;
        metadata.set(MetadataIndex.ARMORSTAND_LEFT_ARM_POSITION, pose);
    }

    @Override
    public EulerAngle getRightArmPose() {
        return pose[3];
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        this.pose[3] = pose;
        metadata.set(MetadataIndex.ARMORSTAND_RIGHT_ARM_POSITION, pose);
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return pose[4];
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        this.pose[4] = pose;
        metadata.set(MetadataIndex.ARMORSTAND_LEFT_LEG_POSITION, pose);
    }

    @Override
    public EulerAngle getRightLegPose() {
        return pose[5];
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        this.pose[5] = pose;
        metadata.set(MetadataIndex.ARMORSTAND_RIGHT_LEG_POSITION, pose);
    }

    @Override
    public boolean hasBasePlate() {
        return hasBasePlate;
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        hasBasePlate = basePlate;
        metadata.setBit(MetadataIndex.ARMORSTAND_FLAGS, ArmorStandFlags.NO_BASE_PLATE, !basePlate);
    }

    @Override
    public boolean hasGravity() {
        return hasGravity;
    }

    @Override
    public void setGravity(boolean gravity) {
        hasGravity = gravity;
        metadata.setBit(MetadataIndex.ARMORSTAND_FLAGS, ArmorStandFlags.HAS_GRAVITY, gravity);
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        isVisible = visible;
        metadata.setBit(MetadataIndex.STATUS, StatusFlags.INVISIBLE, !visible);
    }

    @Override
    public boolean hasArms() {
        return hasArms;
    }

    @Override
    public void setArms(boolean arms) {
        hasArms = arms;
        metadata.setBit(MetadataIndex.ARMORSTAND_FLAGS, ArmorStandFlags.HAS_ARMS, arms);
    }

    @Override
    public boolean isSmall() {
        return isSmall;
    }

    @Override
    public void setSmall(boolean small) {
        isSmall = small;
        metadata.setBit(MetadataIndex.ARMORSTAND_FLAGS, ArmorStandFlags.IS_SMALL, small);
        setSize(small);
    }

    private void setSize(boolean small) {
        if (small) {
            setSize(0.25f, 0.9875f);
        } else {
            setSize(0.5f, 1.975f);
        }
    }

    @Override
    public boolean isMarker() {
        return isMarker;
    }

    @Override
    public void setMarker(boolean marker) {
        isMarker = marker;
        metadata.setBit(MetadataIndex.ARMORSTAND_FLAGS, ArmorStandFlags.IS_MARKER, marker);
    }

    @Override
    public boolean canMove() {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void setCanMove(boolean move) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean canTick() {
        return canTick;
    }

    @Override
    public boolean isGliding() {
        return false;
    }

    @Override
    public void setCanTick(boolean tick) {
        canTick = tick;
    }

    @Override
    public ItemStack getItem(EquipmentSlot equipmentSlot) {
        return equipment.getItem(equipmentSlot);
    }

    @Override
    public void setItem(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        equipment.setItem(equipmentSlot, itemStack);
    }

    @Override
    public Set<EquipmentSlot> getDisabledSlots() {
        // TODO: 1.13
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void setDisabledSlots(EquipmentSlot... equipmentSlots) {
        // TODO: 1.13
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void addDisabledSlots(EquipmentSlot... equipmentSlots) {
        // TODO: 1.13
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void removeDisabledSlots(EquipmentSlot... equipmentSlots) {
        // TODO: 1.13
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isSlotDisabled(EquipmentSlot equipmentSlot) {
        // TODO: 1.13
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public @NotNull Rotations getBodyRotations() {
        return null;
    }

    @Override
    public void setBodyRotations(@NotNull Rotations rotations) {

    }

    @Override
    public @NotNull Rotations getLeftArmRotations() {
        return null;
    }

    @Override
    public void setLeftArmRotations(@NotNull Rotations rotations) {

    }

    @Override
    public @NotNull Rotations getRightArmRotations() {
        return null;
    }

    @Override
    public void setRightArmRotations(@NotNull Rotations rotations) {

    }

    @Override
    public @NotNull Rotations getLeftLegRotations() {
        return null;
    }

    @Override
    public void setLeftLegRotations(@NotNull Rotations rotations) {

    }

    @Override
    public @NotNull Rotations getRightLegRotations() {
        return null;
    }

    @Override
    public void setRightLegRotations(@NotNull Rotations rotations) {

    }

    @Override
    public @NotNull Rotations getHeadRotations() {
        return null;
    }

    @Override
    public void setHeadRotations(@NotNull Rotations rotations) {

    }

    @Override
    public void addEquipmentLock(@NotNull EquipmentSlot equipmentSlot, @NotNull LockType lockType) {
        // TODO: 1.16
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void removeEquipmentLock(@NotNull EquipmentSlot equipmentSlot,
                                    @NotNull LockType lockType) {
        // TODO: 1.16
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean hasEquipmentLock(@NotNull EquipmentSlot equipmentSlot,
                                    @NotNull LockType lockType) {
        // TODO: 1.16
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public EntityEquipment getEquipment() {
        return this.equipment;
    }

    @Override
    public @NotNull TriState getFrictionState() {
        return null;
    }

    @Override
    public void setFrictionState(@NotNull TriState state) {

    }

    @Override
    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity, @Nullable Consumer<T> function) {
        return null;
    }
}
