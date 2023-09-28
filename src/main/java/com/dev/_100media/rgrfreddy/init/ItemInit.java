package com.dev._100media.rgrfreddy.init;

import com.dev._100media.rgrfreddy.RGRFreddy;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RGRFreddy.MODID);

    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }
}
