package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import yourmod.dream.DreamManager;

public class DreamCombatPatch {

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "endBattle"
    )
    public static class EndCombatPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractRoom __instance) {
            DreamManager.getInstance().onCombatEnd();
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "setBoss"
    )
    public static class StartCombatPatch {
        private static boolean combatStarted = false;

        @SpirePostfixPatch
        public static void Postfix() {
            combatStarted = false;
        }
    }

    // Better approach: patch the player's preBattlePrep which is called at combat start
    @SpirePatch(
            clz = com.megacrit.cardcrawl.characters.AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PreBattlePatch {
        @SpirePostfixPatch
        public static void Postfix(com.megacrit.cardcrawl.characters.AbstractPlayer __instance) {
            DreamManager.getInstance().onCombatStart();
        }
    }
}