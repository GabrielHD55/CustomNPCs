package noppes.npcs;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import noppes.npcs.entity.*;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD,modid=CustomNpcs.MODID)
@ObjectHolder(CustomNpcs.MODID)
public class CustomEntities {

    @ObjectHolder("npcpony")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNpcPony;

    @ObjectHolder("npccrystal")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNpcCrystal;

    @ObjectHolder("npcslime")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNpcSlime;

    @ObjectHolder("npcdragon")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNpcDragon;

    @ObjectHolder("npcgolem")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNPCGolem;

    @ObjectHolder("customnpc")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityCustomNpc;

    @ObjectHolder("customnpc64x32")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNPC64x32;

    @ObjectHolder("customnpcalex")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNpcAlex;

    @ObjectHolder("customnpcclassic")
    public static net.minecraft.entity.EntityType<? extends CreatureEntity> entityNpcClassicPlayer;

    @ObjectHolder("customnpcchairmount")
    public static net.minecraft.entity.EntityType<?> entityChairMount;

    @ObjectHolder("customnpcprojectile")
    public static net.minecraft.entity.EntityType<? extends ThrowableEntity> entityProjectile;

    private static final List<EntityType> types = new ArrayList<>();

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        types.clear();
        registerNpc(event.getRegistry(), EntityNpcPony.class, "npcpony", EntityNpcPony::new);
        registerNpc(event.getRegistry(), EntityNpcCrystal.class, "npccrystal", EntityNpcCrystal::new);
        registerNpc(event.getRegistry(), EntityNpcSlime.class, "npcslime", EntityNpcSlime::new);
        registerNpc(event.getRegistry(), EntityNpcDragon.class, "npcdragon", EntityNpcDragon::new);
        registerNpc(event.getRegistry(), EntityNPCGolem.class, "npcgolem", EntityNPCGolem::new);
        registerNpc(event.getRegistry(), EntityCustomNpc.class, "customnpc", EntityCustomNpc::new);
        registerNpc(event.getRegistry(), EntityNPC64x32.class, "customnpc64x32", EntityNPC64x32::new);
        registerNpc(event.getRegistry(), EntityNpcAlex.class, "customnpcalex", EntityNpcAlex::new);
        registerNpc(event.getRegistry(), EntityNpcClassicPlayer.class, "customnpcclassic", EntityNpcClassicPlayer::new);

        registerNewentity(event.getRegistry(), EntityChairMount.class, "customnpcchairmount", EntityChairMount::new, 64, 10, false, 0.001f, 0.001f);
        registerNewentity(event.getRegistry(), EntityProjectile.class, "customnpcprojectile", EntityProjectile::new, 64, 20, true, 0.5f, 0.5f);
    }

    @SubscribeEvent
    public static void attribute(EntityAttributeCreationEvent event){
        for(EntityType type : types){
            event.put(type, EntityNPCInterface.createMobAttributes().build());
        }
    }

    private static <T extends Entity> void registerNpc(IForgeRegistry<EntityType<?>> registry, Class<? extends Entity> c, String name, EntityType.IFactory<T> factoryIn) {
        EntityType.Builder<?> builder = EntityType.Builder.of(factoryIn, EntityClassification.CREATURE);
        builder.setTrackingRange(10);
        builder.setUpdateInterval(3);
        builder.setShouldReceiveVelocityUpdates(false);
        builder.clientTrackingRange(10);
        builder.sized(1, 1);
        ResourceLocation registryName = new ResourceLocation(CustomNpcs.MODID, name);
        EntityType type = builder.build(registryName.toString()).setRegistryName(registryName);
        types.add(type);
        registry.register(type);

        if(CustomNpcs.FixUpdateFromPre_1_12) {
            registryName = new ResourceLocation("customnpcs." + name);
            registry.register(builder.build(registryName.toString()).setRegistryName(registryName));
            //ForgeRegistries.ENTITIES.register(new EntityType(cl, name).setRegistryName(registryName));
        }
    }

    private static <T extends Entity> void registerNewentity(IForgeRegistry<EntityType<?>> registry, Class<? extends Entity> c, String name, EntityType.IFactory<T> factoryIn, int range, int update, boolean velocity, float width, float height) {
        EntityType.Builder<?> builder = EntityType.Builder.of(factoryIn, EntityClassification.MISC);
        builder.setTrackingRange(range);
        builder.setUpdateInterval(update);
        builder.setShouldReceiveVelocityUpdates(velocity);
        builder.sized(width, height);
        builder.clientTrackingRange(4);
        ResourceLocation registryName = new ResourceLocation(CustomNpcs.MODID, name);
        registry.register(builder.build(registryName.toString()).setRegistryName(registryName));
    }
}
