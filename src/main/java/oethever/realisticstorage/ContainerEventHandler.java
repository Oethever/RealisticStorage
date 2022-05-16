package oethever.realisticstorage;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class ContainerEventHandler {
    private static final ArrayList<Pattern> alwaysEjected = new ArrayList<>();
    private static final ArrayList<Pattern> neverEjected = new ArrayList<>();

    @SubscribeEvent
    public static void onInventoryClose(PlayerContainerEvent.Close event) {
        // TODO update config only on config reload event
        updateConfig();
        Player player = event.getPlayer();
        BlockPos blockPos = getTargetedBlock(event.getEntity());
        NonNullList<Slot> slotsToCheck = event.getContainer().slots;
        // Use a set to log only once each type of container
        Set<String> containerNames = new HashSet<>();
        boolean yeet = false;

        // Loop over each slot that needs to be acted on.
        for (Slot slot : slotsToCheck) {
            String slotContainerName = slot.container.getClass().getSimpleName();
            containerNames.add(slotContainerName);
            // make sure our slot has a stack, and avoid checking the player's inventory
            if (slot.hasItem()
                    && Config.getCheckedContainers().contains(slotContainerName)
                    && isBigItem(slot.getItem().getItem())) {
                yeetSlot(player, blockPos, slot);
                yeet = true;
            }
        }
        if (yeet && Config.getSendMessage()) {
            player.sendMessage(new TextComponent("These items are too big for this container!"), Util.NIL_UUID);
        }
        if (Config.getDebugLog()) {
            for (String containerName : containerNames) {
                RealisticStorage.LOGGER.info("Slot container name: " + containerName);
            }
        }
    }


    private static boolean isBigItem(Item item) {
        ResourceLocation itemRegistryName = item.getRegistryName();
        if (itemRegistryName == null) {
            return false;
        }
        String itemName = itemRegistryName.toString();
        for (Pattern pattern : alwaysEjected) {
            if (pattern.matcher(itemName).matches()) {
                return true;
            }
        }
        for (Pattern pattern : neverEjected) {
            if (pattern.matcher(itemName).matches()) {
                return false;
            }
        }
        return item instanceof BlockItem;
    }

    @NotNull
    private static BlockPos getTargetedBlock(Entity entity) {
        HitResult hitResult = entity.pick(20, 0, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult)hitResult).getBlockPos();
        } else {
            return entity.eyeBlockPosition();
        }
    }

    private static void yeetSlot(Player player, BlockPos blockPos, Slot yeetslot) {
        // Spawn the entity with the constructed Entityitem.
        ItemStack stack = yeetslot.getItem().copy();
        yeetslot.getItem().setCount(0);
        yeetslot.setChanged();
        ItemEntity itemEntity = player.spawnAtLocation(stack);
        itemEntity.teleportTo(blockPos.getX() + 0.5, blockPos.getY() + 1.5, blockPos.getZ() + 0.5);
        // Set a delay so the player doesn't instantly collect it if they are in the way
        itemEntity.setPickUpDelay(30);
    }

    public static void updateConfig() {
        updateRegexList(alwaysEjected, Config.getAlwaysEjected());
        updateRegexList(neverEjected, Config.getNeverEjected());
    }

    private static void updateRegexList(ArrayList<Pattern> patterns, List<String> stringPatterns) {
        patterns.clear();
        for (String pattern : stringPatterns) {
            try {
                patterns.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException e) {
                RealisticStorage.LOGGER.warn("Invalid pattern: " + pattern);
            }
        }
    }
}
