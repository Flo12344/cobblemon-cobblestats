package io.github.flo12344.cobblestats.common.net.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BattleEndedPayload(Boolean finished) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FaintOrRevivePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("cobblestats", "battle_finished"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BattleEndedPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    BattleEndedPayload::finished,
                    BattleEndedPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
