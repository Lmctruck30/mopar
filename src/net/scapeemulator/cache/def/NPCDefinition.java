package net.scapeemulator.cache.def;

import java.nio.ByteBuffer;
import net.scapeemulator.cache.util.ByteBufferUtils;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;

public final class NPCDefinition {

    // Basic information
    private String name;
    private String examine;
    private String[] options;
    private int size;
    private int stance;

    private int[] modelIds;

    // Combat information
    private int combatLevel;
    private boolean attackable;
    private int aggressiveRange;
    private int baseHitpoints = 1;
    private int respawnTime;
    private int leashRange = 3;

    private int attackRange;
    private int attackDelay;

    private AttackType attackType;
    private AttackType weakness;

    private int maxHit = 1;
    private int attackEmote;
    private int defendEmote;
    private int deathEmote;

    // Magic information
    private CombatSpell autoCast;

    // Range information
    private int projectileGFX;
    private int projectileSplashGFX;

    // TODO more projectile info needed?

    @SuppressWarnings("unused")
    public static NPCDefinition decode(int id, ByteBuffer buffer) {
        NPCDefinition def = new NPCDefinition();
        def.options = new String[5];
        def.size = 1;
        while (true) {
            int opcode = buffer.get() & 0xFF;
            if (opcode == 0) {
                break;
            }

            if (opcode == 1) {
                def.setModelIds(new int[buffer.get() & 0xFF]);
                for (int i = 0; i < def.getModelIds().length; i++) {
                    int modelId = buffer.getShort() & 0xffff;
                    if (modelId == 65535) {
                        modelId = -1;
                    }
                    def.getModelIds()[i] = modelId;
                }
            }

            if (opcode == 2) {
                def.name = ByteBufferUtils.getString(buffer);
            }

            if (opcode == 12) {
                def.size = buffer.get() & 0xff;
            }

            if (opcode >= 30 && opcode <= 35) {
                def.options[opcode - 30] = ByteBufferUtils.getString(buffer);
                if (def.options[opcode - 30].equals("hidden")) {
                    def.options[opcode - 30] = null;
                }
            }
            if (opcode == 40) {
                int var0 = buffer.get() & 0xff;
                for (int i = 0; i < var0; i++) {
                    int var1 = buffer.getShort() & 0xffff;
                    int var2 = buffer.getShort() & 0xffff;
                }
            }

            if (opcode == 41) {
                int var0 = buffer.get() & 0xff;
                for (int i = 0; i < var0; i++) {
                    int var1 = buffer.getShort() & 0xffff;
                    int var2 = buffer.getShort() & 0xffff;
                }
            }

            if (opcode == 42) {
                int var0 = buffer.get() & 0xff;
                for (int i = 0; i < var0; i++) {
                    int var1 = buffer.get();
                }
            }

            if (opcode == 60) {
                int var0 = buffer.get() & 0xff;
                for (int i = 0; i < var0; i++) {
                    int var1 = buffer.getShort() & 0xffff;
                }
            }

            if (opcode == 95) {
                def.combatLevel = buffer.getShort() & 0xffff;
            }

            if (opcode == 97) {
                int var0 = buffer.getShort() & 0xffff; // Scale x
            }

            if (opcode == 98) {
                int var0 = buffer.getShort() & 0xffff; // scale y
            }

            if (opcode == 100) {
                int var0 = buffer.get() & 0xff;
            }

            if (opcode == 101) {
                int var0 = buffer.get() & 0xff;
            }

            if (opcode == 102) {
                int var0 = buffer.getShort() & 0xffff; // Head icon

            }

            if (opcode == 103) {
                int var0 = buffer.getShort() & 0xffff;
            }

            if (opcode == 106 || opcode == 118) {
                int var0 = buffer.getShort() & 0xffff;
                int var1 = buffer.getShort() & 0xffff;
                if (opcode == 118) {
                    int var3 = buffer.getShort() & 0xffff;

                }

                int var4 = buffer.get() & 0xff;
                for (int i = 0; i <= var4; i++) {
                    int var5 = buffer.getShort() & 0xffff;
                }
            }

            if (opcode == 113) {
                int var0 = buffer.getShort() & 0xffff;
                int var1 = buffer.getShort() & 0xffff;
            }

            if (opcode == 114) {
                int var0 = buffer.get() & 0xff;
                int var1 = buffer.get() & 0xff;
            }

            if (opcode == 115) {
                int var0 = buffer.get() & 0xff;
                int var1 = buffer.get() & 0xff;
            }

            if (opcode == 119) {
                int var0 = buffer.get() & 0xff;
            }

            if (opcode == 121) {
                int var0 = buffer.get() & 0xff;
                for (int i = 0; i < var0; i++) {
                    int var1 = buffer.get() & 0xff;
                    int var2 = buffer.get() & 0xff;
                    int var3 = buffer.get() & 0xff;
                    int var4 = buffer.get() & 0xff;

                }
            }

            if (opcode == 122) {
                int var0 = buffer.getShort() & 0xffff;
            }

            if (opcode == 123) {
                int var0 = buffer.getShort() & 0xffff;
            }

            if (opcode == 125) {
                int var0 = buffer.get();
            }

            if (opcode == 126) {
                int var0 = buffer.getShort() & 0xffff;
            }

            if (opcode == 127) {
                def.stance = buffer.getShort() & 0xffff;
            }

            if (opcode == 128) {
                int var0 = buffer.get() & 0xff;
            }

            if (opcode == 134) {
                int var0 = buffer.getShort() & 0xffff;
                int var1 = buffer.getShort() & 0xffff;
                int var2 = buffer.getShort() & 0xffff;
                int var3 = buffer.getShort() & 0xffff;
                int var4 = buffer.get() & 0xff;
            }

            if (opcode == 135) {
                int var0 = buffer.get() & 0xff;
                int var1 = buffer.getShort() & 0xffff;
            }

            if (opcode == 136) {
                int var0 = buffer.get() & 0xff;
                int var1 = buffer.getShort() & 0xffff;
            }

            if (opcode == 137) {
                int var0 = buffer.getShort() & 0xffff;
            }

            if (opcode == 249) {
                int length = buffer.get() & 0xFF;
                for (int index = 0; index < length; index++) {
                    boolean stringInstance = buffer.get() == 1;
                    int key = ByteBufferUtils.getTriByte(buffer);
                    Object value = stringInstance ? ByteBufferUtils.getJagexString(buffer) : buffer.getInt();
                }
            }

        }
        return def;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String[] getOptions() {
        return options;
    }

    public String getExamine() {
        return examine;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public int getBaseHitpoints() {
        return baseHitpoints;
    }

    public int aggressiveRange() {
        return aggressiveRange;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public int getStance() {
        return stance;
    }

    public int getAttackEmote() {
        return attackEmote;
    }

    public int getDefendEmote() {
        return defendEmote;
    }

    public int getDeathEmote() {
        return deathEmote;
    }

    public int[] getModelIds() {
        return modelIds;
    }

    public void setModelIds(int[] modelIds) {
        this.modelIds = modelIds;
    }

    public int getProjectileGFX() {
        return projectileGFX;
    }

    public int getProjectileSplashGFX() {
        return projectileSplashGFX;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public int getLeashRange() {
        return leashRange;
    }

    public int getAggressiveRange() {
        return aggressiveRange;
    }

    public void setAggressiveRange(int aggressiveRange) {
        this.aggressiveRange = aggressiveRange;
    }

    public AttackType getWeakness() {
        return weakness;
    }

    public CombatSpell getAutoCast() {
        return autoCast;
    }

    public void setAutoCast(CombatSpell autoCast) {
        this.autoCast = autoCast;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public void setStance(int stance) {
        this.stance = stance;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    public void setBaseHitpoints(int baseHitpoints) {
        this.baseHitpoints = baseHitpoints;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public void setAttackEmote(int attackEmote) {
        this.attackEmote = attackEmote;
    }

    public void setDefendEmote(int defendEmote) {
        this.defendEmote = defendEmote;
    }

    public void setDeathEmote(int deathEmote) {
        this.deathEmote = deathEmote;
    }

    public void setWeakness(AttackType weakness) {
        this.weakness = weakness;
    }

    public void setProjectileGFX(int projectileGFX) {
        this.projectileGFX = projectileGFX;
    }

    public void setProjectileSplashGFX(int projectileSplashGFX) {
        this.projectileSplashGFX = projectileSplashGFX;
    }

}
