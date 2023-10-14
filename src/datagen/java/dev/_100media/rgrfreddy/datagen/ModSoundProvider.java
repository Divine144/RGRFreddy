package dev._100media.rgrfreddy.datagen;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.init.SoundInit;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundProvider extends SoundDefinitionsProvider {
    public ModSoundProvider(PackOutput generator, ExistingFileHelper helper) {
        super(generator, RGRFreddy.MODID, helper);
    }

    @Override
    public void registerSounds() {
        SoundInit.SOUNDS.getEntries().forEach(this::addSound);
    }

    public void addSound(RegistryObject<SoundEvent> entry) {
        SoundDefinition.Sound sound = sound(entry.getId());
        if (entry == SoundInit.LULLABY)
            sound.stream(); // Lullaby is 1+ minute long, so stream is good to reduce memory usage
        add(entry, SoundDefinition.definition().with(sound));
    }
}
