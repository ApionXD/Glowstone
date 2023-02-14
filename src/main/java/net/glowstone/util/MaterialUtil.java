package net.glowstone.util;

import net.glowstone.GlowServer;
import net.glowstone.block.data.BlockDataManager;
import net.glowstone.block.data.states.StatefulBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import static org.bukkit.Bukkit.getLogger;

public class MaterialUtil {

    public static int getId(BlockData blockData) {
        BlockDataManager blockDataManager = ((GlowServer) Bukkit.getServer()).getBlockDataManager();
        return blockDataManager.convertToBlockId((StatefulBlockData) blockData);
    }

    public static int getId(Material type) {
        if (type.isLegacy()) {
            return type.getId();
        }
        if (type.isItem()) {
            //This is NOT correct, we need to get the actual protocol id for the material
            return 1;
        }
        if (type.isBlock()) {
            BlockDataManager blockDataManager = ((GlowServer) Bukkit.getServer()).getBlockDataManager();
            try {
                StatefulBlockData blockData = blockDataManager.createBlockData(type);
                return getId(blockData);
            } catch (NullPointerException e) {
                getLogger().warning("Unknown material: " + type);
                throw e;
            }
        }
        throw new UnsupportedOperationException("Material " + type.toString() + " is not an item, block, or legacy material");
    }

}
