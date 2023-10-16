package dev._100media.rgrfreddy.init;

import dev._100media.hundredmediaabilities.capability.MarkerHolderAttacher;
import dev._100media.hundredmediaabilities.init.HMAMarkerInit;
import dev._100media.hundredmediamorphs.HundredMediaMorphsMod;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.StartHeartbeatSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class MorphInit {
    public static final DeferredRegister<Morph> MORPHS = DeferredRegister.create(new ResourceLocation(HundredMediaMorphsMod.MODID, "morphs"), RGRFreddy.MODID);

    public static final RegistryObject<Morph> KID_FREDDY = MORPHS.register("kid_freddy", () -> new Morph(new FreddyMorphProperties<>()
            .maxHealth(10)
            .dimensions(0.65f, 0.65f)
            .eyeHeight(0.5f)
            .morphedTo(entity -> {
                if (entity instanceof ServerPlayer player) {
                    player.getInventory().add(new ItemStack(ItemInit.JUMPSCARE.get()));
                }
            })
            .demorph(entity -> {

            })
    ));

    public static final RegistryObject<Morph> TOY_FREDDY = MORPHS.register("toy_freddy", () -> new Morph(new FreddyMorphProperties<>()
            .maxHealth(30)
            .dimensions(1f, 2f)
            .morphedTo(entity -> {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 0, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 0, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 0, false, false, false));
            })
            .demorph(entity -> {
                entity.removeEffect(MobEffects.DAMAGE_BOOST);
                entity.removeEffect(MobEffects.MOVEMENT_SPEED);
                entity.removeEffect(MobEffects.JUMP);
            })
    ));
    public static final RegistryObject<Morph> FREDDY_FAZBEAR = MORPHS.register("freddy_fazbear", () -> new Morph(new FreddyMorphProperties<>()
            .maxHealth(50)
            .dimensions(1.5f, 2f)
            .morphedTo(entity -> {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 1, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 1, false, false, false));
                var reachDistance = entity.getAttribute(ForgeMod.BLOCK_REACH.get());
                var attackDistance = entity.getAttribute(ForgeMod.ENTITY_REACH.get());
                if (reachDistance != null && attackDistance != null) {
                    reachDistance.setBaseValue(reachDistance.getAttribute().getDefaultValue() + 2);
                    attackDistance.setBaseValue(attackDistance.getAttribute().getDefaultValue() + 2);
                }
            })
            .demorph(entity -> {
                entity.removeEffect(MobEffects.DAMAGE_BOOST);
                entity.removeEffect(MobEffects.MOVEMENT_SPEED);
                entity.removeEffect(MobEffects.JUMP);
                var reachDistance = entity.getAttribute(ForgeMod.BLOCK_REACH.get());
                var attackDistance = entity.getAttribute(ForgeMod.ENTITY_REACH.get());
                if (reachDistance != null && attackDistance != null) {
                    reachDistance.setBaseValue(reachDistance.getAttribute().getDefaultValue());
                    attackDistance.setBaseValue(attackDistance.getAttribute().getDefaultValue());
                }
            })
    ));
    public static final RegistryObject<Morph> GOLDEN_FREDDY_FAZBEAR = MORPHS.register("golden_freddy_fazbear", () -> new Morph(new FreddyMorphProperties<>()
            .maxHealth(80)
            .dimensions(1.5f, 2f)
            .morphedTo(entity -> {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 2, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 2, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false, false));
                var reachDistance = entity.getAttribute(ForgeMod.BLOCK_REACH.get());
                var attackDistance = entity.getAttribute(ForgeMod.ENTITY_REACH.get());
                if (reachDistance != null && attackDistance != null) {
                    reachDistance.setBaseValue(reachDistance.getAttribute().getDefaultValue() + 4);
                    attackDistance.setBaseValue(attackDistance.getAttribute().getDefaultValue() + 4);
                }
            })
            .demorph(entity -> {
                entity.removeEffect(MobEffects.DAMAGE_BOOST);
                entity.removeEffect(MobEffects.MOVEMENT_SPEED);
                entity.removeEffect(MobEffects.JUMP);
                var reachDistance = entity.getAttribute(ForgeMod.BLOCK_REACH.get());
                var attackDistance = entity.getAttribute(ForgeMod.ENTITY_REACH.get());
                if (reachDistance != null && attackDistance != null) {
                    reachDistance.setBaseValue(reachDistance.getAttribute().getDefaultValue());
                    attackDistance.setBaseValue(attackDistance.getAttribute().getDefaultValue());
                }
            })
    ));
    public static final RegistryObject<Morph> NIGHTMARE_FREDDY_FAZBEAR = MORPHS.register("nightmare_freddy_fazbear", () -> new Morph(new FreddyMorphProperties<>()
            .maxHealth(120)
            .dimensions(2f, 2f)
            .morphedTo(entity -> {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 4, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 4, false, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 3, false, false, false));
                var reachDistance = entity.getAttribute(ForgeMod.BLOCK_REACH.get());
                var attackDistance = entity.getAttribute(ForgeMod.ENTITY_REACH.get());
                if (reachDistance != null && attackDistance != null) {
                    reachDistance.setBaseValue(reachDistance.getAttribute().getDefaultValue() + 8);
                    attackDistance.setBaseValue(attackDistance.getAttribute().getDefaultValue() + 8);
                }
                MarkerHolderAttacher.getMarkerHolder(entity).ifPresent(m -> m.addMarker(HMAMarkerInit.FLIGHT.get(), true));
            })
            .demorph(entity -> {
                entity.removeEffect(MobEffects.DAMAGE_BOOST);
                entity.removeEffect(MobEffects.MOVEMENT_SPEED);
                entity.removeEffect(MobEffects.JUMP);
                var reachDistance = entity.getAttribute(ForgeMod.BLOCK_REACH.get());
                var attackDistance = entity.getAttribute(ForgeMod.ENTITY_REACH.get());
                if (reachDistance != null && attackDistance != null) {
                    reachDistance.setBaseValue(reachDistance.getAttribute().getDefaultValue());
                    attackDistance.setBaseValue(attackDistance.getAttribute().getDefaultValue());
                }
                MarkerHolderAttacher.getMarkerHolder(entity).ifPresent(m -> m.removeMarker(HMAMarkerInit.FLIGHT.get(), true));
            })
    ));

    private static class FreddyMorphProperties<T extends FreddyMorphProperties<T>> extends Morph.Properties<T> {
        @Override
        public T morphedTo(Consumer<LivingEntity> morphedToConsumer) {
            // CommonForgeEvent has a hook on EntityJoinLevelEvent to run this when a morphed player joins the world; so this will work
            return super.morphedTo(morphedToConsumer.andThen(entity -> {
                if (entity instanceof ServerPlayer player)
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new StartHeartbeatSoundPacket(player.getId()));
            }));
        }
    }
}
