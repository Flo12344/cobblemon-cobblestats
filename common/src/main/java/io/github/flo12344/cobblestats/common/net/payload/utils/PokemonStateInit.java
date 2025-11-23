package io.github.flo12344.cobblestats.common.net.payload.utils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record PokemonStateInit(int id, String species, String form, String ball, boolean is_ko) {
    public static final StreamCodec<RegistryFriendlyByteBuf, PokemonStateInit> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, PokemonStateInit::id,
                    ByteBufCodecs.STRING_UTF8, PokemonStateInit::species,
                    ByteBufCodecs.STRING_UTF8, PokemonStateInit::form,
                    ByteBufCodecs.STRING_UTF8, PokemonStateInit::ball,
                    ByteBufCodecs.BOOL, PokemonStateInit::is_ko,
                    PokemonStateInit::new
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, List<PokemonStateInit>> LIST_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());

}
