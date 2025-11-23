package io.github.flo12344.cobblestats.common.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.*;

public class PokemonBattleState {
    private final Map<String, Integer> states = new HashMap<>();
    private final Set<String> typesAdded = new HashSet<>();
    private final Set<String> extraEffects = new HashSet<>();
    private final Map<String, Integer> turnBasedffects = new HashMap<>();

    private String typeOverride = "";

    public PokemonBattleState() {
    }

    public void boostState(String stat, String severity, boolean isBoost) {
        int current = states.getOrDefault(stat, 0);
        int newStage = Math.max(-6, Math.min(6, current + (isBoost ? intSeverity(severity) : -intSeverity(severity)))); // clamp between -6 and +6
        states.put(stat, newStage);
        checkForZero();
    }

    public void addType(String type) {
        typesAdded.add(type);
    }

    public void overrideType(String type) {
        typesAdded.clear();
        typeOverride = type;
    }

    public void addExtraEffect(String effect) {
        extraEffects.add(effect);
    }

    public void removeExtraEffect(String effect) {
        extraEffects.remove(effect);
    }

    public void checkForZero() {
        List<String> keysToRemove = new ArrayList<>();
        states.forEach((s, integer) -> {
            if (integer == 0) keysToRemove.add(s);
        });
        keysToRemove.forEach(states::remove);
    }

    public void printAll() {
        states.forEach((s, integer) -> System.out.println("    |" + s + " : " + integer));
    }

    public List<String> getStats() {
        List<String> result = new ArrayList<>();
        if (!Objects.equals(typeOverride, ""))
            result.add(typeOverride);

        if (!typesAdded.isEmpty()) {
            typesAdded.forEach(s -> result.add("+" + s));
        }

        if (!extraEffects.isEmpty()) {
            extraEffects.forEach(
                    s -> {
                        TranslatableContents translatableContents = new TranslatableContents("cobblemon.move." + s, null, new Object[]{});
                        Component component = MutableComponent.create(translatableContents);
                        result.add(component.getString());
                    }
            );
        }

        states.forEach((s, integer) -> {
            result.add(s + " " + statToString(s, integer));
        });


        return result;
    }


    private Integer intSeverity(String severity) {
        switch (severity) {
            case "slight" -> {
                return 1;
            }
            case "sharp" -> {
                return 2;
            }
            case "severe" -> {
                return 3;
            }
            case "max" -> {
                return 12;
            }
            default -> {
                return 0;
            }
        }
    }

    private String statToString(String stat, Integer value) {
        return switch (stat) {
            case "accuracy" -> switch (value) {
                case -6 -> "0.333x";
                case -5 -> "0.375x";
                case -4 -> "0.429x";
                case -3 -> "0.5x";
                case -2 -> "0.6x";
                case -1 -> "0.75x";
                case -0 -> "1x";
                case 1 -> "1.333x";
                case 2 -> "1.667x";
                case 3 -> "2x";
                case 4 -> "2.333x";
                case 5 -> "2.667x";
                case 6 -> "3x";
                default -> "";
            };
            case "evasion" -> switch (value) {
                case -6 -> "3x";
                case -5 -> "2.667x";
                case -4 -> "2.333x";
                case -3 -> "2x";
                case -2 -> "1.667x";
                case -1 -> "1.333x";
                case -0 -> "1x";
                case 1 -> "0.75x";
                case 2 -> "0.6x";
                case 3 -> "0.5x";
                case 4 -> "0.429x";
                case 5 -> "0.375x";
                case 6 -> "0.333x";
                default -> "";
            };
            default -> switch (value) {
                case -6 -> "0.25x";
                case -5 -> "0.2857x";
                case -4 -> "0.333x";
                case -3 -> "0.4x";
                case -2 -> "0.5x";
                case -1 -> "0.667x";
                case -0 -> "1x";
                case 1 -> "1.5x";
                case 2 -> "2x";
                case 3 -> "2.5x";
                case 4 -> "3x";
                case 5 -> "3.5x";
                case 6 -> "4x";
                default -> "";
            };
        };
    }
}
