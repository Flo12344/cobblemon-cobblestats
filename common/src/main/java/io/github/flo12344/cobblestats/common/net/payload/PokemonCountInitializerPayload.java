package io.github.flo12344.cobblestats.common.net.payload;

import io.github.flo12344.cobblestats.common.net.payload.utils.PokemonStateInit;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record PokemonCountInitializerPayload(String uuid, List<PokemonStateInit> team) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PokemonCountInitializerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("cobblestats", "count_init"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PokemonCountInitializerPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    PokemonCountInitializerPayload::uuid,
                    PokemonStateInit.LIST_CODEC,
                    PokemonCountInitializerPayload::team,
                    PokemonCountInitializerPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

