package yourmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import javassist.CtBehavior;
import yourmod.ui.DreamSlotPanel;

import java.util.HashSet;
import java.util.UUID;

public class DreamSlotPanelPatches {

    // Add the panel as a field on OverlayMenu
    @SpirePatch(clz = OverlayMenu.class, method = SpirePatch.CLASS)
    public static class DreamSlotPanelField {
        public static SpireField<DreamSlotPanel> panel = new SpireField<>(DreamSlotPanel::new);
    }

    // Update the panel
    @SpirePatch2(clz = OverlayMenu.class, method = "update")
    public static class UpdatePanel {
        @SpireInsertPatch(locator = Locator.class)
        public static void update(OverlayMenu __instance) {
            DreamSlotPanelField.panel.get(__instance).updatePositions();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(ExhaustPanel.class, "updatePositions");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    // Show the panel when combat panels are shown
    @SpirePatch2(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class ShowPanel {
        @SpireInsertPatch(locator = Locator.class)
        public static void show(OverlayMenu __instance) {
            DreamSlotPanelField.panel.get(__instance).show();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(ExhaustPanel.class, "show");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    // Hide the panel when combat panels are hidden
    @SpirePatch2(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class HidePanel {
        @SpireInsertPatch(locator = Locator.class)
        public static void hide(OverlayMenu __instance) {
            DreamSlotPanelField.panel.get(__instance).hide();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(ExhaustPanel.class, "hide");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    // Render the panel (we don't actually render here, DreamSlotRenderer handles it)
    @SpirePatch2(clz = OverlayMenu.class, method = "render")
    public static class RenderPanel {
        @SpireInsertPatch(locator = Locator.class)
        public static void render(OverlayMenu __instance, SpriteBatch sb) {
            DreamSlotPanelField.panel.get(__instance).render(sb);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(ExhaustPanel.class, "render");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    // Update the dream pile during combat
    @SpirePatch2(clz = AbstractPlayer.class, method = "combatUpdate")
    public static class UpdatePile {
        @SpirePostfixPatch
        public static void update(AbstractPlayer __instance) {
            DreamSlotPanel.dreamPile.update();
        }
    }

    // Clear the pile at combat start and end via DreamManager (which handles timesDreamed too)
    @SpirePatch2(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class ClearPileOnBattleStart {
        @SpirePrefixPatch
        public static void clear(AbstractPlayer __instance) {
            yourmod.dream.DreamManager.getInstance().onCombatStart();
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "onVictory")
    public static class ClearPileOnVictory {
        @SpirePrefixPatch
        public static void clear(AbstractPlayer __instance) {
            yourmod.dream.DreamManager.getInstance().onCombatEnd();
        }
    }

    /**
     * THIS IS THE CRITICAL PATCH!
     * This makes GetAllInBattleInstances include cards in our dream pile.
     * Without this, Rampage and other cards that track across plays won't work!
     */
    @SpirePatch2(clz = GetAllInBattleInstances.class, method = "get")
    public static class AddDreamPileToGetAllInBattleInstances {
        @SpireInsertPatch(
                rloc = 23,
                localvars = {"cards"}
        )
        public static void get(UUID uuid, HashSet<AbstractCard> cards) {
            for (AbstractCard c : DreamSlotPanel.dreamPile.group) {
                // Use == because it's the same card instance (not a copy)
                if (c.uuid == uuid) {
                    cards.add(c);
                }
            }
        }
    }
}