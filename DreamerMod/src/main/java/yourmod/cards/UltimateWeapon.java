package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.ApplyModToHandCardAction;
import yourmod.cardmods.DeadlyModifier;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class UltimateWeapon extends AbstractEasyCard {
    public final static String ID = makeID("UltimateWeapon");

    public UltimateWeapon() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new ApplyModToHandCardAction(new DeadlyModifier(), "DEADLY"));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}