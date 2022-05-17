package oethever.realisticstorage.containerguard;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oethever.realisticstorage.ModConfig;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ContainerGuard {
    private final ArrayList<Pattern> alwaysEjectedPatterns = new ArrayList<>();
    private final ArrayList<Pattern> neverEjectedPatterns = new ArrayList<>();
    private final Logger logger;

    public ContainerGuard(Logger logger) {
        this.logger = logger;
        MinecraftForge.EVENT_BUS.register(this);
        updateConfig();
    }

    @SubscribeEvent
    public void onInventoryClose(PlayerContainerEvent.Close event) {
        // Our inventory we are working with.
        Container container = event.getContainer();
        EntityPlayer player = event.getEntityPlayer();
        World world = player.getEntityWorld();
        String containerName = container.getClass().getName();

        // Get the slots that are checked
        ArrayList<Slot> slotsToCheck = getSlots(container, new ArrayList<>(Arrays.asList(ModConfig.ignoredSlots)), containerName);

        // Checks what mode the size list is in.
        if (Arrays.asList(ModConfig.checkedContainers).contains(containerName)) {
            // ray-trace to get the block the user is looking at.
            BlockPos tracedPos = getTracedPos(rayTrace(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()));
            // Check the size.
            doSizeCheck(player, world, tracedPos, slotsToCheck);
        }
    }

    public void updateConfig() {
        updateRegexList(alwaysEjectedPatterns, ModConfig.alwaysEjectedItems);
        updateRegexList(neverEjectedPatterns, ModConfig.neverEjectedItems);
    }

    private void updateRegexList(ArrayList<Pattern> patterns, String[] stringPatterns) {
        patterns.clear();
        for (String pattern : stringPatterns) {
            try {
                patterns.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException e) {
                logger.warn("Invalid pattern: " + pattern);
            }
        }
    }

    private static RayTraceResult rayTrace(Entity entity, double blockReachDistance) {
        Vec3d eyePosition = entity.getPositionEyes(1);
        Vec3d lookVector = entity.getLook(1);
        Vec3d rayTraceVector = eyePosition.addVector(lookVector.x * blockReachDistance, lookVector.y * blockReachDistance, lookVector.z * blockReachDistance);
        return entity.world.rayTraceBlocks(eyePosition, rayTraceVector, false, false, true);
    }

    private static BlockPos getTracedPos(RayTraceResult rayTrace) {
        // if the ray-trace isn't null & we have a block to "eject" from
        if (rayTrace != null && rayTrace.typeOfHit != RayTraceResult.Type.MISS) {
            // block pos of our traced target.
            return rayTrace.getBlockPos();
        }
        return null;
    }

    private void doSizeCheck(EntityPlayer player, World world, BlockPos tracedPos, ArrayList<Slot> slotsToEffect) {
        // Do the check of size
        checkSize(world, tracedPos, slotsToEffect, player);
    }

    private void checkSize(World world, BlockPos tracedPos, ArrayList<Slot> slotsToCheck, EntityPlayer player) {
        // Loop over each slot that needs to be acted on.
        for (Slot slot : slotsToCheck) {
            // make sure our slot has a stack
            if (slot.getHasStack() && isBigBlock(slot.getStack())) {
                yeetSlot(world, tracedPos, slot, player);

                // tell the player about what it if configured to.
                if (ModConfig.messageSend)
                    player.sendStatusMessage(new TextComponentString(ModConfig.messageText), ModConfig.messageLocation);
            }
        }
    }

    private boolean isBigBlock(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation itemRegistryName = item.getRegistryName();
        if (itemRegistryName == null)
            return false;
        String itemName = itemRegistryName.toString();
        String itemNameWithMeta = itemRegistryName.toString() + ":" + stack.getMetadata();
        for (Pattern pattern : alwaysEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches())
                return true;
        }
        for (Pattern pattern : neverEjectedPatterns) {
            if (pattern.matcher(itemName).matches() || pattern.matcher(itemNameWithMeta).matches())
                return false;
        }
        return stack.getItem() instanceof ItemBlock;
    }

    private ArrayList<Slot> getSlots(Container container, ArrayList<String> slotClassNameList, String containerName) {
        ArrayList<Slot> slotsToCheck = new ArrayList<>();
        // Remove this entry if people put it in cause a shit ton of stuff uses this and warn them.
        if (slotClassNameList.remove("net.minecraft.inventory.InventoryBasic")) {
            logger.warn("Ignoring basic slot! Don't put \"net.minecraft.inventory.InventoryBasic\" in your config, like the config says! this is not a bug!");
        }

        // simple debug to get the container class name.
        if (ModConfig.debugLog) {
            logger.info("Container name: " + containerName);
        }

        // Check over every slot inside the inventory to get the slots we want to mess with.
        for (Slot slot : container.inventorySlots) {

            // simple debug to get the slot class names.
            if (ModConfig.debugLog) {
                logger.info("Slot class name:" + slot.inventory.getClass().toString());
            }

            // If the blacklist list doesn't contain this slot class, we want to add it to the list of things to be yeeted.
            if (!slotClassNameList.contains(slot.inventory.getClass().getName()))
                slotsToCheck.add(slot);
        }
        return slotsToCheck;
    }

    private static void yeetSlot(World world, BlockPos tracedPos, Slot yeetslot, EntityPlayer player) {
        // Check to see if our pos is valid and then if our block has a TE otherwise the player isn't looking in a block.
        if (tracedPos == null || world.getTileEntity(tracedPos) == null)
            tracedPos = player.getPosition();

        spawnYeetItem(world, tracedPos, yeetslot.getStack());
        yeetslot.getStack().setCount(0);
        yeetslot.inventory.markDirty();
    }

    private static void spawnYeetItem(World world, BlockPos pos, ItemStack item) {
        float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
        // the item to be spawned and thrown
        EntityItem entityitem = new EntityItem(world, pos.getX() + (double) f, pos.getY() + (double) f1, pos.getZ() + (double) f2, item.splitStack(world.rand.nextInt(21) + 10));
        //set a delay so the player doesn't instantly collect it if they are in the way
        entityitem.setPickupDelay(30);
        //Set the motion on the item
        entityitem.motionX = world.rand.nextGaussian() * 0.07D;
        entityitem.motionY = world.rand.nextGaussian() * 0.07D + 0.20000000298023224D;
        entityitem.motionZ = world.rand.nextGaussian() * 0.07D;
        // Spawn the entity with the constructed Entityitem.
        world.spawnEntity(entityitem);
    }
}
