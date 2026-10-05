package io.github.flo12344.cobblestats.common.net.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerStatsSyncForcePayload(String target, String stat, Integer value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerStatsSyncForcePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("cobblestats", "stat_from_server"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerStatsSyncForcePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    ServerStatsSyncForcePayload::target,
                    ByteBufCodecs.STRING_UTF8,
                    ServerStatsSyncForcePayload::stat,
                    ByteBufCodecs.INT,
                    ServerStatsSyncForcePayload::value,
                    ServerStatsSyncForcePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
