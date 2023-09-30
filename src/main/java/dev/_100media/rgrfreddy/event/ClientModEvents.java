package dev._100media.rgrfreddy.event;

import dev._100media.hundredmediageckolib.client.animatable.SimpleAnimatable;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import dev._100media.rgrfreddy.init.EntityInit;
import dev._100media.rgrfreddy.init.MenuInit;
import dev._100media.rgrfreddy.init.MorphInit;
import dev._100media.rgrfreddy.init.SkillInit;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev._100media.hundredmediageckolib.client.animatable.IHasGeoRenderer;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoPlayerModel;
import dev._100media.hundredmediageckolib.client.renderer.GeoPlayerRenderer;
import dev._100media.hundredmediamorphs.client.renderer.MorphRenderers;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.hundredmediaquests.client.screen.QuestSkillScreen;
import dev._100media.hundredmediaquests.client.screen.SkillScreen;
import dev._100media.hundredmediaquests.client.screen.TreeScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = RGRFreddy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    public static final KeyMapping SKILL_TREE_KEY = new KeyMapping("key." + RGRFreddy.MODID + ".skill_tree", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.category." + RGRFreddy.MODID);

    @SubscribeEvent
    public static void registerKeybind(RegisterKeyMappingsEvent event) {
        event.register(SKILL_TREE_KEY);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.TOY_FREDDY.get(), ctx -> new GeoEntityRenderer<>(ctx, new SimpleGeoEntityModel<>(RGRFreddy.MODID, "pizza")));
        event.registerEntityRenderer(EntityInit.PIZZA.get(), ctx -> new GeoEntityRenderer<>(ctx, new SimpleGeoEntityModel<>(RGRFreddy.MODID, "pizza")));
        createSimpleMorphRenderer(MorphInit.KID_FREDDY.get(), "pizza", new SimpleAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.TOY_FREDDY.get(), "pizza", new SimpleAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.FREDDY_FAZBEAR.get(), "pizza", new SimpleAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.GOLDEN_FREDDY_FAZBEAR.get(), "pizza", new SimpleAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.NIGHTMARE_FREDDY_FAZBEAR.get(), "pizza", new SimpleAnimatable(), 1.0f);

     /*   event.registerEntityRenderer(EntityInit.MISSILE.get(), MissileEntityRenderer::new);*/

/*        createSimpleMorphRenderer(MorphInit.BABY_MECHA.get(), "baby_mecha", new WardenAnimatable()
                .runAnim(RawAnimation.begin().thenLoop("walk"))
                .hoverboardAnim(RawAnimation.begin().then("hoverboard spawn", Animation.LoopType.PLAY_ONCE).thenLoop("hoverboard on")), 0.5f);
        createSimpleMorphRenderer(MorphInit.MECHA_TEEN.get(), "mecha_teen", new WardenAnimatable()
                .hoverboardAnim(RawAnimation.begin().then("hoverboard on", Animation.LoopType.PLAY_ONCE).thenLoop("hoverboard")), 1.0f);
        createSimpleMorphRenderer(MorphInit.MECHA_WARDEN.get(), "mecha_warden", new WardenAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.MECHA_KING.get(), "mecha_king", new WardenAnimatable(), 1.0f);
        createSimpleMorphRenderer(MorphInit.MECHA_SCULK.get(), "mecha_sculk", new WardenAnimatable().sitAnim(RawAnimation.begin().thenLoop("crouch")), 3f);*/
    }

    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event) {
        MenuScreens.register(MenuInit.SKILL_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new TreeScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/skill_tree.png"), 23, 23,
                Arrays.asList(
                        new Pair<>(SkillInit.EVOLUTION_TREE, new Pair<>(66, 87)),
                        new Pair<>(SkillInit.COMBAT_TREE, new Pair<>(114, 87)),
                        new Pair<>(SkillInit.UTILITY_TREE, new Pair<>(163, 87))
                ), 256, 256, 256, 174
        ));
        MenuScreens.register(MenuInit.EVOLUTION_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new SkillScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/evolution.png"), 27, 29,
                Arrays.asList(
                        new Pair<>(37, 45),
                        new Pair<>(75, 45),
                        new Pair<>(113, 45),
                        new Pair<>(150, 45),
                        new Pair<>(188, 45)
                ), SkillInit.EVOLUTION_TREE.get(), 256, 256, 256, 163
        ));
        MenuScreens.register(MenuInit.COMBAT_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new QuestSkillScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/combat.png"), 30, 30,
                Arrays.asList(
                        new Pair<>(36, 67),
                        new Pair<>(74, 67),
                        new Pair<>(112, 67),
                        new Pair<>(150, 67),
                        new Pair<>(188, 67)
                ), SkillInit.COMBAT_TREE.get(), 256, 256, 256, 161
        ));
        MenuScreens.register(MenuInit.UTILITY_TREE.get(), (AbstractContainerMenu menu, Inventory inv, Component title) -> new SkillScreen(menu, inv, title,
                new ResourceLocation(RGRFreddy.MODID, "textures/gui/screen/utility.png"), 27, 29,
                Arrays.asList(
                        new Pair<>(37, 57),
                        new Pair<>(75, 57),
                        new Pair<>(113, 57),
                        new Pair<>(150, 57),
                        new Pair<>(188, 57)
                ), SkillInit.UTILITY_TREE.get(), 256, 256, 256, 163
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
