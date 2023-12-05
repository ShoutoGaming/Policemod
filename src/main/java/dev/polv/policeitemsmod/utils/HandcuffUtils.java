package dev.polv.policeitemsmod.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class HandcuffUtils {

    public static void sendCuffedMessage(PlayerEntity self, PlayerEntity target) {
        self.displayClientMessage(new TranslationTextComponent("gui.policeitemsmod.self.cuffed", target.getName().getString())
                .withStyle(TextFormatting.RED, TextFormatting.BOLD), true);
        target.displayClientMessage(new TranslationTextComponent("gui.policeitemsmod.target.cuffed")
                .withStyle(TextFormatting.RED, TextFormatting.BOLD), true);
    }

    public static void sendCuffingStatus(PlayerEntity self, PlayerEntity target, int ticksLeft) {
        double seconds = (double) ticksLeft / 20;
        String formattedTimeLeft = MathUtils.format(seconds);

        self.displayClientMessage(new TranslationTextComponent("gui.policeitemsmod.self.cuffing", target.getName().getString(), formattedTimeLeft)
                .withStyle(TextFormatting.GREEN, TextFormatting.BOLD), true);
        target.displayClientMessage(new TranslationTextComponent("gui.policeitemsmod.target.cuffing", self.getName().getString(), formattedTimeLeft)
                .withStyle(TextFormatting.GREEN, TextFormatting.BOLD), true);
    }

}
