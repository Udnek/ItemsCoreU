package me.udnek.itemscoreu.customentitylike.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customentitylike.EntityLikeManager;
import me.udnek.itemscoreu.nms.NmsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.block.BedBlock;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomBlockManager extends EntityLikeManager<TileState, CustomBlockType, CustomBlockEntity> implements Listener {

    public static final int MAX_CUSTOM_BLOCK_PACKETS_PER_TICK = 50;

    private static CustomBlockManager instance;

    private final HashMap<Player, BreakData> breaking = new HashMap<>();

    public static CustomBlockManager getInstance() {
        if (instance == null) {
            instance = new CustomBlockManager();
            Bukkit.getPluginManager().registerEvents(instance, ItemsCoreU.getInstance());
        }
        return instance;
    }

    private CustomBlockManager(){}

    @Override
    protected boolean equals(@NotNull TileState r1, @NotNull TileState r2) {
        return r1.equals(r2);
    }

    public void loadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get((TileState) blockState);
            if (customBlockType != null) loadAny(customBlockType, (TileState) blockState);
        }
    }

    // TODO FIX SERVER CALLS
    public void unloadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get((TileState) blockState);
            if (customBlockType != null) unloadAny(customBlockType, (TileState) blockState);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event){
        CustomBlockType blockType = CustomBlockType.get(event.getBlock());
        if (blockType == null) return;
        if (!blockType.doCustomBreakTimeAndAnimation()) return;
        if (breaking.containsKey(event.getPlayer())) return;
        event.setCancelled(true);
        blockType.onDamage(event);
        breaking.put(event.getPlayer(), new BreakData(event.getBlock(), blockType));
    }

    @EventHandler
    public void onBlockAbortDamage(BlockDamageAbortEvent event){
        CustomBlockType blockType = CustomBlockType.get(event.getBlock());
        if (blockType == null) return;
        breaking.remove(event.getPlayer());
    }

    @Override
    public void run() {
        super.run();
        if (breaking.isEmpty()) return;
        List<Player> toRemove = new ArrayList<>();
        for (Map.Entry<Player, BreakData> entry : breaking.entrySet()) {
            BreakData breakData = entry.getValue();
            Player player = entry.getKey();

            breakData.progress = Math.clamp(breakData.progress + breakData.customBlock.getCustomBreakProgress(player, breakData.block), 0, 1);

            if (breakData.progress == 1) {
                player.breakBlock(breakData.block);
                toRemove.add(player);
            } else {
                breakData.customBlock.customBreakTickProgress(breakData.block, player, breakData.progress);
            }
        }
        toRemove.forEach(breaking::remove);
    }

    public static class BreakData {
        private final @NotNull Block block;
        private final @NotNull CustomBlockType customBlock;
        private float progress = 0;

        public BreakData(@NotNull Block block, @NotNull CustomBlockType customBlock) {
            this.block = block;
            this.customBlock = customBlock;
        }
    }

    // EVENTS

    @EventHandler
    public void onDestroy(BlockDestroyEvent event){
        CustomBlockType.consumeIfCustom(event.getBlock(), block -> block.onDestroy(event));
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        CustomBlockType.consumeIfCustom(event.getBlock(), block -> block.onDestroy(event));
    }
    @EventHandler
    public void onExplode(BlockExplodeEvent event){
        CustomBlockType.consumeIfCustom(event.getExplodedBlockState().getBlock(), block -> block.onDestroy(event));
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        for (Block block : event.blockList()) {
            CustomBlockType.consumeIfCustom(block, type -> type.onDestroy(event, block));
        }
    }
    @EventHandler
    public void onFade(BlockFadeEvent event){
        CustomBlockType.consumeIfCustom(event.getBlock(), block -> block.onDestroy(event));
    }
    @EventHandler
    public void onBurn(BlockBurnEvent event){
        CustomBlockType.consumeIfCustom(event.getBlock(), block -> block.onDestroy(event));
    }

    @EventHandler
    public void onHopperInteract(HopperInventorySearchEvent event){
        CustomBlockType.consumeIfCustom(event.getSearchBlock(), block ->
                block.getComponents().getOrDefault(CustomComponentType.HOPPER_INTERACTING_BLOCK).onHopperInteract(block, event)
        );
    }

    @EventHandler
    public void onInventoryItemMove(InventoryMoveItemEvent event){
        Location destination = event.getDestination().getLocation();
        Location source = event.getSource().getLocation();
        if (source != null){
            CustomBlockType.consumeIfCustom(source.getBlock(), block ->
                    block.getComponents().getOrDefault(CustomComponentType.HOPPER_INTERACTING_BLOCK).onItemMoveFrom(block, event)
            );
        }
        if (destination != null){
            CustomBlockType.consumeIfCustom(destination.getBlock(), block ->
                    block.getComponents().getOrDefault(CustomComponentType.HOPPER_INTERACTING_BLOCK).onItemMoveInto(block, event)
            );
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        CustomBlockType customBlockType = CustomBlockType.get(event.getClickedBlock());
        if (customBlockType == null) return;
        customBlockType.getComponents().getOrDefault(CustomComponentType.RIGHT_CLICKABLE_BLOCK).onRightClick(customBlockType, event);
    }
    @EventHandler
    public void onPlayerChunkLoad(@NotNull PlayerChunkLoadEvent event){
        List<BlockState> blocksToSendPackets = new ArrayList<>();
        for (BlockState blockState : event.getChunk().getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get((TileState) blockState);
            if (customBlockType == null) continue;
            if (customBlockType.getFakeState() == null) return;
            blocksToSendPackets.add(blockState);
        }
        if (blocksToSendPackets.isEmpty()) return;
        new BukkitRunnable(){
            int packetId = 0;
            @Override
            public void run() {
                for (int i = packetId; i < blocksToSendPackets.size(); i++) {
                    BlockState blockState = blocksToSendPackets.get(i);
                    NmsUtils.sendPacket(event.getPlayer(), new ClientboundBlockUpdatePacket(
                            new BlockPos(blockState.getX(), blockState.getY(), blockState.getZ()),
                            NmsUtils.toNmsBlockState(blockState))
                    );
                }
                packetId += MAX_CUSTOM_BLOCK_PACKETS_PER_TICK;
                if (packetId > blocksToSendPackets.size()-1) cancel();
            }
        }.runTaskTimer(ItemsCoreU.getInstance(), 8, 1);
    }
    @EventHandler
    public void onLoad(ChunkLoadEvent event){loadChunk(event.getChunk());}
    @EventHandler
    public void onUnload(ChunkUnloadEvent event){unloadChunk(event.getChunk());}
    @EventHandler
    public void onReload(ServerLoadEvent event){
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) return;
        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                loadChunk(loadedChunk);
            }
        }
    }

}
