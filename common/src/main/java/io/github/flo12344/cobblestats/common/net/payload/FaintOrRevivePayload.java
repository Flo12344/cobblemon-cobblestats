package io.github.flo12344.cobblestats.common.net.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record FaintOrRevivePayload(String uuid, Integer pos, boolean is_ko) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FaintOrRevivePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("cobblestats", "faint_or_revive"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FaintOrRevivePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    FaintOrRevivePayload::uuid,
                    ByteBufCodecs.VAR_INT,
                    FaintOrRevivePayload::pos,
                    ByteBufCodecs.BOOL,
                    FaintOrRevivePayload::is_ko,
                    FaintOrRevivePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
