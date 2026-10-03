package io.github.flo12344.cobblestats.common.client;

import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.net.messages.client.battle.BattleInitializePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Objects;

public class BattleProcess {

    public static Object[] processBattleData(TranslatableContents messagePacket, String current_atk, String current_pkm, PokemonBattleState tmp_stat_holder) {
        String[] MainActionSplit = messagePacket.getKey().split("\\.");

        if (MainActionSplit.length < 3)
            return new Object[]{current_atk, current_pkm, tmp_stat_holder};
        if (Objects.equals(MainActionSplit[1], "status")) {
            if (Objects.equals(MainActionSplit[2], "sleep")) {
                BattleStateTracker.getPokemon(current_pkm).removeExtraEffect("yawn");
            }
        }

        if (!Objects.equals(MainActionSplit[1], "battle"))
            return new Object[]{current_atk, current_pkm, tmp_stat_holder};

        Object[] object_args = messagePacket.getArgs();
        switch (MainActionSplit[2]) {
            case "unboost":
            case "boost":
                BattleStateTracker.changeStats(
                        getPkm(object_args),
                        ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2],
                        MainActionSplit[3],
                        Objects.equals(MainActionSplit[2], "boost"));
                break;
            case "clearallboost":
                BattleStateTracker.clearAllBoosts(current_pkm, false);
                break;
            case "clearallnegativeboost":
                BattleStateTracker.clearAllBoosts(current_pkm, true);
                break;
            case "copyboost":
                BattleStateTracker.copyBoosts(current_pkm, accessPokemonName(object_args, 1));
                break;

            case "used_move_on":
                current_pkm = getPkm(object_args);
                current_atk = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2];
                String target_pkm = accessPokemonName(object_args, 2);
                PokemonBattleState current = BattleStateTracker.getPokemon(current_pkm);
                PokemonBattleState target = BattleStateTracker.getPokemon(target_pkm);
                switch (current_atk) {
                    case "powerswap":
                        current.switchBoost("attack", target);
                        current.switchBoost("special_attack", target);
                        break;
                    case "speedswap":
                        current.switchBoost("speed", target);
                        break;
                    case "guardswap":
                        current.switchBoost("defence", target);
                        current.switchBoost("special_defence", target);
                        break;
                    case "generic":
                    case "heartswap":
                        current.switchBoost("attack", target);
                        current.switchBoost("special_attack", target);
                        current.switchBoost("defence", target);
                        current.switchBoost("special_defence", target);
                        current.switchBoost("speed", target);
                        current.switchBoost("accuracy", target);
                        current.switchBoost("evasion", target);
                        break;
                    default:
                        break;
                }
                break;
            case "used_move":
                current_pkm = getPkm(object_args);
                current_atk = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2];
                break;

            case "switch":
                String switchedTo;
                if (MainActionSplit[3].equals("self")) {
                    String pokemon;
                    if (object_args[0] instanceof MutableComponent) {
                        pokemon = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
                    } else {
                        pokemon = (String) object_args[0];
                    }
                    assert Minecraft.getInstance().player != null;
                    String owner = Minecraft.getInstance().player.getDisplayName().getString();
                    switchedTo = owner + "/" + pokemon;
                } else {
                    String pokemon;
                    if (object_args[1] instanceof MutableComponent) {
                        pokemon = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey();
                    } else {
                        pokemon = (String) object_args[1];
                    }
                    String owner;
                    if (object_args[0] instanceof MutableComponent) {
                        owner = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
                    } else {
                        owner = object_args[0].toString();
                    }
                    switchedTo = owner + "/" + pokemon;
                }
                BattleStateTracker.addPokemon(switchedTo);

                if (Objects.equals(current_atk, "batonpass") && tmp_stat_holder != null) {
                    BattleStateTracker.applyStats(switchedTo, tmp_stat_holder);
                    tmp_stat_holder = null;
                    current_atk = "";
                }
                break;
            case "withdraw":
                String toRemove;
                if (MainActionSplit[3].equals("self")) {
                    String pokemon;
                    if (object_args[0] instanceof MutableComponent) {
                        pokemon = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
                    } else {
                        pokemon = (String) object_args[0];
                    }
                    assert Minecraft.getInstance().player != null;
                    String owner = Minecraft.getInstance().player.getDisplayName().getString();
                    toRemove = owner + "/" + pokemon;
                } else {
                    String pokemon;
                    if (object_args[1] instanceof MutableComponent) {
                        pokemon = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey();
                    } else {
                        pokemon = (String) object_args[1];
                    }
                    String owner;
                    if (object_args[0] instanceof MutableComponent) {
                        owner = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
                    } else {
                        owner = object_args[0].toString();
                    }
                    toRemove = owner + "/" + pokemon;
                }
                BattleStateTracker.removePokemon(toRemove);
                break;

            case "start":
                switch (MainActionSplit[3]) {
                    case "typechange":
                        BattleStateTracker.getPokemon(getPkm(object_args)).overrideType(object_args[1].toString());
                        break;
                    case "typeadd":
                        BattleStateTracker.getPokemon(getPkm(object_args)).addType(object_args[1].toString());
                        break;
                    default:
                        BattleStateTracker.getPokemon(getPkm(object_args)).addExtraEffect(MainActionSplit[3]);
                        break;
                }
                break;
            case "end":
                BattleStateTracker.getPokemon(getPkm(object_args)).removeExtraEffect(MainActionSplit[3]);
                break;

            case "fail":
                current_atk = "";
                break;

            case "setboost":
                BattleStateTracker.changeStats(getPkm(object_args), "attack", "max", true);
                break;
            case "fieldstart":
                if (MainActionSplit[3].contains("terrain"))
                    TerrainBattleState.setTerrain(MainActionSplit[3]);
                else
                    TerrainBattleState.addRoom(MainActionSplit[3]);
                break;
            case "fieldend":
                if (MainActionSplit[3].contains("terrain"))
                    TerrainBattleState.setTerrain("");
                else
                    TerrainBattleState.removeRoom(MainActionSplit[3]);
                break;
            case "sidestart":
                if (MainActionSplit.length == 5) {
                    TerrainBattleState.addHazard(!MainActionSplit[3].equals("opponent"), MainActionSplit[4]);
                } else {
                    TerrainBattleState.addHazard(((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey().contains("ally"), MainActionSplit[3]);
                }
                break;
            case "sideend":
                if (MainActionSplit.length == 5) {
                    TerrainBattleState.removeHazard(!MainActionSplit[3].equals("opponent"), MainActionSplit[4]);
                } else {
                    TerrainBattleState.removeHazard(((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey().contains("ally"), MainActionSplit[3]);
                }
                break;
            case "weather":
                if (Objects.equals(MainActionSplit[4], "start")) {
                    TerrainBattleState.setWeather(MainActionSplit[3]);
                } else if (Objects.equals(MainActionSplit[4], "end")) {
                    TerrainBattleState.setWeather("");
                }
                break;
            case "turn":
                TerrainBattleState.updateRoom();
                TerrainBattleState.updateTerrain();
                TerrainBattleState.updateWeather();
                break;
            case "fainted":
                BattleStateTracker.removePokemon(getPkm(object_args));
                break;
            case "ability":
                if (Objects.equals(MainActionSplit[3], "airlock")) {
                    TerrainBattleState.setWeather("");
                }
                System.out.println("Ability : " + messagePacket.toString());
                break;

            default:
                System.out.println(messagePacket.toString());
                break;
        }
        if (Objects.equals(current_atk, "batonpass") && tmp_stat_holder == null) {
            tmp_stat_holder = BattleStateTracker.getStatsForHold(current_pkm);
        }

        return new Object[]{current_atk, current_pkm, tmp_stat_holder};
    }

    private static String getPkm(Object[] target) {
        return accessPokemonName(target, 0);
    }


    private static String accessPokemonName(Object[] target, int offset) {
        if (((TranslatableContents) ((MutableComponent) target[offset]).getContents()).getKey().contains("species")) {
            return ((TranslatableContents) ((MutableComponent) target[offset]).getContents()).getKey();
        } else {
            var data = ((TranslatableContents) ((MutableComponent) target[offset]).getContents()).getArgs();
            String pokemon;
            if (data[1] instanceof MutableComponent)
                pokemon = ((TranslatableContents) ((MutableComponent) data[1]).getContents()).getKey();
            else
                pokemon = data[1].toString();
            String owner;
            if (data[0] instanceof MutableComponent)
                owner = ((TranslatableContents) ((MutableComponent) data[0]).getContents()).getKey();
            else
                owner = data[0].toString();
            return owner + "/" + pokemon;
        }
    }


    public static void checkSide(BattleInitializePacket.BattleSideDTO side) {
        if (!side.component1().getFirst().getActivePokemon().isEmpty()) {
            side.component1().getFirst().getActivePokemon().forEach(activeBattlePokemonDTO ->
            {
                if (activeBattlePokemonDTO == null)
                    return;
                if (side.component1().getFirst().getType() == ActorType.PLAYER) {
                    BattleStateTracker.addPokemon(side.component1().getFirst().getDisplayName().getString() + "/" + ((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
                } else if (side.component1().getFirst().getType() == ActorType.NPC) {
                    BattleStateTracker.addPokemon(side.component1().getFirst().getDisplayName() + "/" + ((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
                } else {
                    BattleStateTracker.addPokemon(((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
                }
            });
        }
    }
}
