package io.github.flo12344.cobblestats.common.utils;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeamInfo {
    public static class TeamPokemon {
        public String ball = "poke_ball";
        public String species = "";
        public String form = "";
        public boolean is_ko = false;

        public TeamPokemon(String _ball, String _species, String _form, boolean _is_ko) {
            ball = _ball;
            species = _species;
            form = _form;
            is_ko = _is_ko;
        }
    }

    private List<TeamPokemon> teamPokemons = new ArrayList<>();

    public List<TeamPokemon> getTeamPokemons() {
        return teamPokemons;
    }

    public void addPokemonToTeam(int id, TeamPokemon pokemon) {
        teamPokemons.add(pokemon);
    }

    public void faintOrRevive(int id, boolean fainted) {
        teamPokemons.get(id).is_ko = fainted;
    }
}
