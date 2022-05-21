package oethever.realisticstorage.handlers;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oethever.realisticstorage.Config;
import oethever.realisticstorage.RealisticStorage;

// Code inspired from the mod TrophySlot by Lomeli12, under GNU Lesser General Public License v3.0.
// https://github.com/Lomeli12/TrophySlots
@Mod.EventBusSubscriber(
        modid = RealisticStorage.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class DrawEventHandler {
    private static final int GREY_COLOR = 0xFF555555;
    private static final int SLOT_SIZE = 16;
    private static final int SLOTS_PER_ROW = 9;
    public static void init() {

        OverlayRegistry.registerOverlayAbove(
                ForgeIngameGui.HOTBAR_ELEMENT,
                "Realistic Storage Locked Slots",
                (gui, poseStack, partialTicks, screenWidth, screenHeight) -> drawLockedSlots(poseStack)
        );

    }

    private static void drawLockedSlots(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (Minecraft.getInstance().options.hideGui
                || player == null
                || player.isCreative()
                || Config.CONFIG.getSlotLimit() == -1) {
            return;
        }
        // See net/minecraft/client/gui/Gui.java:545 to 548 for reference on these calculations
        int middleX = mc.getWindow().getGuiScaledWidth() / 2;
        int hotbarTop = mc.getWindow().getGuiScaledHeight() - SLOT_SIZE - 3;
        int hotbarBottom = hotbarTop + SLOT_SIZE;
        int hotbarSlotGap = SLOT_SIZE + 4;
        int hotbarLeft = middleX - (SLOTS_PER_ROW * hotbarSlotGap) / 2 + 2;
        for (int i = Config.CONFIG.getSlotLimit(); i < SLOTS_PER_ROW; ++i) {
            int left = hotbarLeft + hotbarSlotGap * i;
            int right = left + SLOT_SIZE;
            fill(poseStack, left, hotbarTop, right, hotbarBottom, GREY_COLOR);
        }
    }

    @SubscribeEvent
    public static void onDrawForeground(ContainerScreenEvent.DrawForeground event) {
        if (Config.CONFIG.getSlotLimit() == -1) {
            return;
        }
        // Draw grey squares on top of locked inventory slots
        AbstractContainerScreen<?> screen = event.getContainerScreen();
        for (Slot slot : screen.getMenu().slots) {
            if (slot.container instanceof Inventory) {
                Player player = ((Inventory) slot.container).player;
                if (!player.isCreative() && SlotEventHandler.isLockedSlot(slot.getSlotIndex())) {
                    PoseStack poseStack = new PoseStack();
                    poseStack.pushPose();
                    fill(poseStack, slot.x, slot.y, slot.x + SLOT_SIZE, slot.y + SLOT_SIZE, GREY_COLOR);
                    poseStack.popPose();
                }
            }
        }
    }

    private static void fill(PoseStack poseStack, int xMin, int yMin, int xMax, int yMax, int color) {
        if (xMax < xMin) {
            int i = xMax;
            xMax = xMin;
            xMin = i;
        }

        if (yMax < yMin) {
            int j = yMax;
            yMax = yMin;
            yMin = j;
        }

        Lighting.setupForEntityInInventory();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f transform = poseStack.last().pose();
        bufferbuilder.vertex(transform, (float)xMax, (float)yMin, 0.0F).color(color).endVertex();
        bufferbuilder.vertex(transform, (float)xMin, (float)yMin, 0.0F).color(color).endVertex();
        bufferbuilder.vertex(transform, (float)xMin, (float)yMax, 0.0F).color(color).endVertex();
        bufferbuilder.vertex(transform, (float)xMax, (float)yMax, 0.0F).color(color).endVertex();
        tesselator.end();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        Lighting.setupFor3DItems();
    }
}
