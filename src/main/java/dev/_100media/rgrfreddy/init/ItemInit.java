package dev._100media.rgrfreddy.init;

import dev._100media.hundredmediageckolib.item.animated.AnimatedItemProperties;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RGRFreddy.MODID);

    public static final RegistryObject<Item> FREDDY_MICROPHONE = ITEMS.register("freddy_microphone", () -> new FreddyMicrophoneItem(getItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> FREDDY_HAT = ITEMS.register("freddy_hat", () -> new FreddyHatItem(getItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> PIZZA_SLICE = ITEMS.register("pizza_projectile", () -> new FreddyHatItem(getItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> TOY_ARMY_ITEM = ITEMS.register("toy_army", () -> new ToyArmyItem(getItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> FREDDY_PIZZA = ITEMS.register("pizza", () -> new FreddyPizzaItem(getItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> JUMPSCARE = ITEMS.register("jumpscare", () -> new JumpscareItem(getItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> SMOKE_BOMB = ITEMS.register("smoke_bomb_item", () -> new SmokeBombItem(getItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> GUARD_MASK = ITEMS.register("guard_mask", () -> new GuardMaskArmorItem(new AnimatedItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> PIZZERIA_KEY = ITEMS.register("pizzeria_key", () -> new Item(getItemProperties().stacksTo(1)));

    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }
}
