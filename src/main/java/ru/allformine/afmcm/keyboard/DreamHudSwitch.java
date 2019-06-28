package ru.allformine.afmcm.keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.AFMCoreMod;
import ru.allformine.afmcm.References;

public class DreamHudSwitch implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding("Change AIM mode", Keyboard.KEY_N, References.category);
    public void onPress(InputEvent.KeyInputEvent event){
        Property configNode = AFMCoreMod.config.get("activateDreamHud", "dreamHud",
                References.activateDreamHud, "Activates dream hud");
        References.activateDreamHud = !References.activateDreamHud;
        configNode.set(References.activateDreamHud);
        // TODO: Не сохраняет нихуя, нужен фикс
        AFMCoreMod.config.save();
    }

    public KeyBinding getBinding(){
        return keyBinding;
    }
}
