package xyz.brassgoggledcoders.workshop.util.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config
{
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class ClientConfig {

        ClientConfig(ForgeConfigSpec.Builder builder) {


        }

    }

    public static class CommonConfig {

        CommonConfig(ForgeConfigSpec.Builder builder) {


        }

    }

    public static class ServerConfig {

        ServerConfig(ForgeConfigSpec.Builder builder) {


        }

    }


}
