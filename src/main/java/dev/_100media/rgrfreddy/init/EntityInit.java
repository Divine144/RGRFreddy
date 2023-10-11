package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.entity.FreddyHatProjectileEntity;
import dev._100media.rgrfreddy.entity.PizzaProjectileEntity;
import dev._100media.rgrfreddy.entity.ToyFreddyEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = RGRFreddy.MODID)
public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RGRFreddy.MODID);
    private static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<ToyFreddyEntity>> TOY_FREDDY = registerEntity("toy_army", () ->
            EntityType.Builder.of(ToyFreddyEntity::new, MobCategory.MISC).sized(0.5F, 0.5F), ToyFreddyEntity::createAttributes);

    public static final RegistryObject<EntityType<PizzaProjectileEntity>> PIZZA = registerEntity("pizza", () ->
            EntityType.Builder.of(PizzaProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F));

    public static final RegistryObject<EntityType<FreddyHatProjectileEntity>> FREDDY_HAT_PROJECTILE = registerEntity("freddy_hat", () ->
            EntityType.Builder.of(FreddyHatProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F));

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(RGRFreddy.MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier,
            Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier.get(), p.factory.get().build()));
    }

    private record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}
