package dev._100media.rgrfreddy.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev._100media.hundredmediageckolib.client.animatable.IHasGeoRenderer;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoPlayerModel;
import dev._100media.hundredmediageckolib.client.renderer.GeoPlayerRenderer;
import dev._100media.hundredmediamorphs.client.renderer.MorphRenderers;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.hundredmediaquests.client.screen.QuestSkillScreen;
import dev._100media.hundredmediaquests.client.screen.SkillScreen;
import dev._100media.hundredmediaquests.client.screen.TreeScreen;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.client.animatable.FreddyAnimatable;
import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import dev._100media.rgrfreddy.client.renderer.blockentity.DimensionalTrapDoorRenderer;
import dev._100media.rgrfreddy.client.renderer.blockentity.EMPGeneratorBlockRenderer;
import dev._100media.rgrfreddy.client.renderer.blockentity.JailDoorBlockRenderer;
import dev._100media.rgrfreddy.client.renderer.blockentity.SnareBlockRenderer;
import dev._100media.rgrfreddy.client.renderer.item.PizzaSliceItemRenderer;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.EntityInit;
import dev._100media.rgrfreddy.init.MenuInit;
import dev._100media.rgrfreddy.init.MorphInit;
import dev._100media.rgrfreddy.init.SkillInit;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = RGRFreddy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    public static final KeyMapping SKILL_TREE_KEY = new KeyMapping("key." + RGRFreddy.MODID + ".skill_tree", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G,
            "key.category." + RGRFreddy.MODID);
    public static final KeyMapping SWITCH_CONTROL_KEY = new KeyMapping("key." + RGRFreddy.MODID + ".switch_control", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "key.category." + RGRFreddy.MODID);

    @SubscribeEvent
    public static void registerKeybind(RegisterKeyMappingsEvent event) {
        event.register(SKILL_TREE_KEY);
        event.register(SWITCH_CONTROL_KEY);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockInit.DIMENSIONAL_TRAPDOOR_BE.get(), DimensionalTrapDoorRenderer::new);
        event.registerBlockEntityRenderer(BlockInit.TOY_BOX_TRAP_BE.get(), ctx -> new GeoBlockRenderer<>(
                new DefaultedBlockGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "toy_box_trap_be"))
        ));
        event.registerBlockEntityRenderer(BlockInit.MYSTIC_MUSIC_BOX_BE.get(), ctx -> new GeoBlockRenderer<>(
                new DefaultedBlockGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "mystic_music_box_be"))
        ));
        event.registerBlockEntityRenderer(BlockInit.SNARE_BE.get(), ctx -> new SnareBlockRenderer<>(
                new DefaultedBlockGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "snare_be"))
        ));
        event.registerBlockEntityRenderer(BlockInit.JAIL_DOOR_BE.get(), ctx -> new JailDoorBlockRenderer<>(
                new DefaultedBlockGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "jail_door_be"))
        ));
        event.registerBlockEntityRenderer(BlockInit.EMP_GENERATOR_BE.get(), ctx -> new EMPGeneratorBlockRenderer<>(
                new DefaultedBlockGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "emp_generator_be"))
        ).withScale(2));
        event.registerEntityRenderer(EntityInit.TOY_FREDDY.get(), ctx -> new GeoEntityRenderer<>(ctx, new SimpleGeoEntityModel<>(RGRFreddy.MODID, "toy_army")).withScale(0.5f));
        event.registerEntityRenderer(EntityInit.PIZZA.get(), PizzaSliceItemRenderer::new);
        event.registerEntityRenderer(EntityInit.FREDDY_HAT_PROJECTILE.get(), ctx -> new GeoEntityRenderer<>(ctx, new SimpleGeoEntityModel<>(RGRFreddy.MODID, "freddy_hat")));
        createSimpleMorphRenderer(MorphInit.KID_FREDDY.get(), "kid_freddy", new FreddyAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.TOY_FREDDY.get(), "toy_freddy", new FreddyAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.FREDDY_FAZBEAR.get(), "freddy_fazbear", new FreddyAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.GOLDEN_FREDDY_FAZBEAR.get(), "golden_freddy_fazbear", new FreddyAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.NIGHTMARE_FREDDY_FAZBEAR.get(), "nightmare_freddy_fazbear", new FreddyAnimatable(), 1.0f);
    }

    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockInit.DIMENSIONAL_TRAPDOOR_BLOCK.get(), RenderType.cutout());
        MenuScreens.register(MenuInit.SKILL_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new TreeScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/skill_tree.png"), 23, 23,
                Arrays.asList(
                        new Pair<>(SkillInit.EVOLUTION_TREE, new Pair<>(66, 140)),
                        new Pair<>(SkillInit.COMBAT_TREE, new Pair<>(114, 140)),
                        new Pair<>(SkillInit.UTILITY_TREE, new Pair<>(163, 140))
                ), 256, 256, 256, 256
        ));
        MenuScreens.register(MenuInit.EVOLUTION_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new SkillScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/evolution.png"), 27, 29,
                Arrays.asList(
                        new Pair<>(37, 120),
                        new Pair<>(78, 120),
                        new Pair<>(118, 120),
                        new Pair<>(158, 120),
                        new Pair<>(200, 120)
                ), SkillInit.EVOLUTION_TREE.get(), 256, 256, 256, 256
        ));
        MenuScreens.register(MenuInit.COMBAT_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new QuestSkillScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/combat.png"), 30, 30,
                Arrays.asList(
                        new Pair<>(38, 80),
                        new Pair<>(75, 80),
                        new Pair<>(111, 80),
                        new Pair<>(148, 80),
                        new Pair<>(184, 80)
                ), SkillInit.COMBAT_TREE.get(), 256, 256, 256, 256
        ));
        MenuScreens.register(MenuInit.UTILITY_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new SkillScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/utility.png"), 27, 29,
                Arrays.asList(
                        new Pair<>(44, 72),
                        new Pair<>(77, 72),
                        new Pair<>(111, 72),
                        new Pair<>(142, 72),
                        new Pair<>(174, 72)
                ), SkillInit.UTILITY_TREE.get(), 256, 256, 256, 256
        ));
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("jumpscare", JumpscareOverlay.INSTANCE);
    }

    private static <T extends IHasGeoRenderer & GeoAnimatable> void createSimpleMorphRenderer(Morph morph, String name, T animatable, float scale) {
        MorphRenderers.registerPlayerMorphRenderer(morph, context -> {
            var renderer = new GeoPlayerRenderer<>(context, new SimpleGeoPlayerModel<>(RGRFreddy.MODID, name) {
                private final ResourceLocation defaultLocation = new ResourceLocation(RGRFreddy.MODID, "textures/entity/" + name + ".png");

                @Override
                public ResourceLocation getTextureResource(T animatable1, @Nullable AbstractClientPlayer player) {
                    return defaultLocation;
                }
            }, animatable) {

                @Override
                public void render(AbstractClientPlayer player, T animatable1, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
                    if (!player.hasEffect(MobEffects.INVISIBILITY)) {
                        poseStack.pushPose();
                        if (player.getVehicle() != null) {
                            poseStack.translate(0, 0.10, 0);
                        }
                        poseStack.scale(scale, scale, scale);
                        super.render(player, animatable1, entityYaw, partialTick, poseStack, bufferSource, packedLight);
                        poseStack.popPose();
                    }
                }
            };
            return renderer;
        });
    }
}
