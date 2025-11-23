package io.github.flo12344.cobblestats.common.client.net.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record JoinServerPayload(String uuid) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<JoinServerPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cobblestats", "player_joined"));
    public static final StreamCodec<RegistryFriendlyByteBuf, JoinServerPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            JoinServerPayload::uuid,
            JoinServerPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
