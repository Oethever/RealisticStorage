package oethever.realisticstorage.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oethever.realisticstorage.RealisticStorage;
import oethever.realisticstorage.Registry;
import oethever.realisticstorage.Util;
import oethever.realisticstorage.block.PalletBlock;
import oethever.realisticstorage.blockentity.PalletBlockEntity;

import java.util.*;

@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class PalletEventHandler {

    /**
     * Called when a block is broken, responsible for putting the block in the player inventory if it is above
     * a pallet.
     * @param event The FML event.
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level world = event.getPlayer().level;
        if (world.isClientSide() || event.getPlayer().isCreative()) {
            return;
        }
        BlockPos pos = event.getPos();
        Optional<PalletBlockEntity> pallet = searchPalletBelow(world, pos);
        if (pallet.isPresent() && pallet.get().wasPlacedBefore(pos)) {
            // Cancel the event and put the block in the player's inventory
            event.setCanceled(true);
            Util.tryToFillInventory(event.getPlayer().getInventory(), getSilkTouchDrop(world.getBlockState(pos)), true);
            world.removeBlock(pos, false);

            // Play the sound like if the block was broken
            Block block = world.getBlockState(pos).getBlock();
            SoundType sound = block.getSoundType(block.defaultBlockState(), world, pos, event.getPlayer());
            world.playSound(null, pos, sound.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * Called during the block breaking every tick, if the block is above a pallet we accelerate the mining so that
     * every block is mined instantly.
     * @param event The FML event.
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Level world = event.getPlayer().level;
        if (world.isClientSide() || event.getPlayer().isCreative()) {
            return;
        }
        BlockPos pos = event.getPos();
        Optional<PalletBlockEntity> pallet = searchPalletBelow(world, pos);
        if (pallet.isPresent() && pallet.get().wasPlacedBefore(pos)) {
            // The number has to be really high to work with anvil, obsidian and other hard blocks
            event.setNewSpeed(10000.f);
        }
    }

    /**
     * Called when a blocked is placed in the world, we need to update the list of blocks being placed after a pallet
     * to make sure that blocks placed before have to be mined normally, and those placed after are insta-mined.
     * @param event The FML event.
     */
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity == null) {
            return;
        }
        Level world = entity.level;
        if (world.isClientSide()) {
            return;
        }
        BlockPos pos = event.getPos();
        Optional<PalletBlockEntity> pallet = searchPalletBelow(world, pos);
        if (pallet.isPresent()) {
            pallet.get().setPlaced(pos);
        }
    }

    /**
     * Called when a block is right-clicked, we cancel the event if the block is a pallet. This is done on both client
     * and server, to avoid having a ghost block pop up and disappear on the client side.
     * @param event The FML event.
     */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getEntity().level;
        BlockPos targetPos = event.getHitVec().getBlockPos();
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.getBlock() instanceof PalletBlock) {
            event.setUseBlock(Event.Result.ALLOW);
            event.setUseItem(Event.Result.DENY);
        }
    }

    /**
     * This function is called by PalletBlock.use, and we attempt to place a block above the pallet when the pallet
     * block is right-clicked. This code is here instead of in PalletBlock in order to keep all pallet logic in the
     * same file. Adjacent pallets are also checked, and empty spots are filled layer by layer.
     * @param state The block state of the pallet
     * @param world The world
     * @param pos The pos of the pallet
     * @param player The player
     * @param hand The hand used by the player
     * @return true if a block was placed above the pallet, false otherwise.
     */
    public static boolean tryAddBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            // Don't place the main hand block if there is something in the off-hand
            if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty()) {
                return false;
            }
            ItemStack handStack = player.getItemInHand(hand);
            if (!handStack.isEmpty() && handStack.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) handStack.getItem()).getBlock();
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof PalletBlockEntity pallet) {
                    // Try to add the block above the pallet
                    Optional<BlockPos> optPlacedPos = searchEmptyBlock(world, pos);
                    if (optPlacedPos.isPresent()) {
                        // Add the block to the world
                        BlockPos placedPos = optPlacedPos.get();
                        world.setBlock(placedPos, block.defaultBlockState(), Block.UPDATE_ALL);
                        handStack.shrink(1);
                        // Update the pallet below the placed block
                        BlockPos posOfPalletBelow = new BlockPos(placedPos.getX(), pos.getY(), placedPos.getZ());
                        PalletBlockEntity palletBelow = (PalletBlockEntity) world.getBlockEntity(posOfPalletBelow);
                        palletBelow.setPlaced(placedPos);
                        // Play a sound
                        SoundType sound = block.getSoundType(state, world, pos, player);
                        world.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Look for an empty block above the pallet at palletPos, or above neighboring blocks that have a pallet on the
     * same level.
     * @param world The world.
     * @param palletPos The pos of the pallet being clicked.
     * @return The position of an empty block that can be used if there is one, an empty value if the pallet cluster is
     * full.
     */
    private static Optional<BlockPos> searchEmptyBlock(Level world, BlockPos palletPos) {
        List<BlockPos> palletCluster = findPalletCluster(world, palletPos);
        for (int i = 0; i < PalletBlockEntity.AREA_HEIGHT; ++i) {
            for (BlockPos pos : palletCluster) {
                BlockPos abovePos = pos.above(i + 1);
                if (world.getBlockState(abovePos).isAir()) {
                    return Optional.of(abovePos);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds all pallets connected to this one, even indirectly through other pallets, on the same Y-level.
     * @param world The world.
     * @param start The position of the first pallet.
     * @return A list of all connected pallets.
     */
    private static List<BlockPos> findPalletCluster(Level world, BlockPos start) {
        Direction[] directions = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        List<BlockPos> exploredBlocks = new ArrayList<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        exploredBlocks.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            BlockPos pos = queue.remove();
            for (Direction direction : directions) {
                BlockPos neighbor = pos.relative(direction);
                if (world.getBlockEntity(neighbor) instanceof PalletBlockEntity && !exploredBlocks.contains(neighbor)) {
                    exploredBlocks.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return exploredBlocks;
    }

    /**
     * Look for a pallet below the given position.
     * @param world The world.
     * @param pos The position from which to start the search.
     * @return An optional value if a pallet was found, an empty value otherwise.
     */
    private static Optional<PalletBlockEntity> searchPalletBelow(LevelAccessor world, BlockPos pos) {
        if (pos == null) {
            return Optional.empty();
        }
        for (int i = 0; i < PalletBlockEntity.AREA_HEIGHT; ++i) {
            BlockPos lowerPos = pos.below(i + 1);
            Optional<PalletBlockEntity> pallet = world.getBlockEntity(lowerPos, Registry.PALLET_BLOCK_ENTITY.get());
            if (pallet.isPresent()) {
                return pallet;
            }
        }
        return Optional.empty();
    }

    private static ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(state.getBlock().asItem());
    }
}
