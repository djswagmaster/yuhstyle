package yourmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DeadlyModifier extends AbstractCardModifier {

    private static final String ID = "DEADLY";
    private static final float DAMAGE_MULTIPLIER = 2.0f;

    public DeadlyModifier() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "dreamermod:Deadly. NL " + rawDescription;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card.type == AbstractCard.CardType.ATTACK
                && !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage * DAMAGE_MULTIPLIER;
    }

    @Override
    public float modifyDamageFinal(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DeadlyModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}