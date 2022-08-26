package noppes.npcs;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.CNpcsChunkManagerHelper;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event.Result;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

import javax.annotation.Nullable;
import java.util.*;

public class NPCSpawning {
	
	
    public static void findChunksForSpawning(ServerWorld level){
    	if(SpawnController.instance.data.isEmpty() || level.getGameTime() % 400L != 0L)
    		return;
        ChunkManager chunkManager = level.getChunkSource().chunkMap;
        List<ChunkHolder> list = Lists.newArrayList(Iterables.unmodifiableIterable(chunkManager.visibleChunkMap.values()));
        Collections.shuffle(list);
        list.forEach((chunkHolder) -> {
            Optional<Chunk> optional1 = chunkHolder.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
            if(optional1.isPresent()){
                Chunk chunk = optional1.get();
                ChunkPos pos = chunk.getPos();
                Biome biome = level.getBiome(pos.getWorldPosition());
                if(SpawnController.instance.hasSpawnList(biome.getRegistryName())){
                    AxisAlignedBB bb = new AxisAlignedBB(pos.getMinBlockX(), 0, pos.getMinBlockZ(), pos.getMaxBlockX(), level.getMaxBuildHeight(), pos.getMaxBlockZ());
                    List<Entity> entities = Lists.newArrayList();
                    chunk.getEntitiesOfClass(PlayerEntity.class, bb.inflate(4), entities, null);
                    if(entities.isEmpty()){
                        chunk.getEntitiesOfClass(EntityCustomNpc.class, bb, entities, null);
                        if(entities.size() < CustomNpcs.NpcNaturalSpawningChunkLimit){
                            spawnChunk(level, chunk);
                        }
                    }
                }
            }
        });

//        for (int i = 0; i < level.players.size(); ++i){
//            PlayerEntity entityplayer = (PlayerEntity)level.players.get(i);
//            if(entityplayer.isSpectator())
//            	continue;
//            int j = MathHelper.floor(entityplayer.getX() / 16.0D);
//            int k = MathHelper.floor(entityplayer.getZ() / 16.0D);
//            byte size = 7;
//
//            for (int x = -size; x <= size; ++x){
//                for (int z = -size; z <= size; ++z){
//                	ChunkPos pos = new ChunkPos(x + j, z + k);
//                    if (!eligibleChunksForSpawning.contains(pos) && level.getWorldBorder().isWithinBounds(pos)){
//                        if (CNpcsChunkManagerHelper.canSpawn(level.getChunkSource().chunkMap, pos)) {
//                            eligibleChunksForSpawning.add(pos);
//                        }
//                    }
//                }
//            }
//        }

    }

    private static void spawnChunk(ServerWorld level, Chunk chunk){
        BlockPos chunkposition = getChunk(level, chunk);
        int j1 = chunkposition.getX();
        int k1 = chunkposition.getY();
        int l1 = chunkposition.getZ();

        for(int i = 0; i < 3; i++){
            int x = j1;
            int y = k1;
            int z = l1;
            byte b1 = 6;

            x += level.random.nextInt(b1) - level.random.nextInt(b1);
            z += level.random.nextInt(b1) - level.random.nextInt(b1);

            BlockPos pos = new BlockPos(x, y, z);

            ResourceLocation name = level.getBiome(pos).getRegistryName();
            SpawnData data = SpawnController.instance.getRandomSpawnData(name);

            if (data == null || data.data.isEmpty() || !canCreatureTypeSpawnAtLocation(data, level, pos))
                continue;

            spawnData(data, level, pos);
        }
    }
    
    public static int countNPCs(ServerWorld level){
        int count = 0;
        Collection<Entity> list = level.entitiesByUuid.values();
        for (Entity entity : list){
            if (entity instanceof EntityNPCInterface){
                count++;
            }
        }
        return count;
    }


    private static BlockPos getChunk(World level, Chunk chunk) {
        ChunkPos chunkpos = chunk.getPos();
        int i = chunkpos.getMinBlockX() + level.random.nextInt(16);
        int j = chunkpos.getMinBlockZ() + level.random.nextInt(16);
        int k = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, i, j) + 1;
        int l = level.random.nextInt(k + 1);
        return new BlockPos(i, l, j);
    }
    
    public static void performWorldGenSpawning(World level, Biome biome, int x, int z, Random rand){
        if(biome.getMobSettings().getCreatureProbability() >= 1 || biome.getMobSettings().getCreatureProbability() < 0 || !SpawnController.instance.hasSpawnList(biome.getRegistryName())) {
            return;
        }
        int tries = 0;
    	while (rand.nextFloat() < biome.getMobSettings().getCreatureProbability()){
            tries++;
            if(tries > 20){
                break;
            }

    		SpawnData data = SpawnController.instance.getRandomSpawnData(biome.getRegistryName());
    		
    		int size = 16;
    		
            int j1 = x + rand.nextInt(size);
            int k1 = z + rand.nextInt(size);
            int l1 = j1;
            int i2 = k1;
            
            for (int k2 = 0; k2 < 4; ++k2){
                BlockPos pos = getTopNonCollidingPos(level, CustomEntities.entityCustomNpc, 0, k1);

                if (!canCreatureTypeSpawnAtLocation(data, level, pos)){
                    j1 += rand.nextInt(5) - rand.nextInt(5);

                    for (k1 += rand.nextInt(5) - rand.nextInt(5); j1 < x || j1 >= x + size || k1 < z || k1 >= z + size; k1 = i2 + rand.nextInt(5) - rand.nextInt(5))
                    {
                        j1 = l1 + rand.nextInt(5) - rand.nextInt(5);
                    }
                }
                else if(spawnData(data, level, pos))
	                break;
                
            }
        }
    }
    
    private static boolean spawnData(SpawnData data, World level, BlockPos pos){
        MobEntity entityliving;

        try{
            CompoundNBT nbt = data.getCompound(1);
            if(nbt == null)
                return false;
			Entity entity = EntityType.create(nbt, level).orElse(null);
			if(entity == null || !(entity instanceof MobEntity))
				return false;
			
			entityliving = (MobEntity) entity;
			
			if(entity instanceof EntityCustomNpc){
				EntityCustomNpc npc = (EntityCustomNpc) entity;
				npc.stats.spawnCycle = 4;
				npc.stats.respawnTime = 0;
				npc.ais.returnToStart = false;
				npc.ais.setStartPos(pos);
			}
			entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360.0F, 0.0F);
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }

        Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, level, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, null, SpawnReason.NATURAL);
        if (canSpawn == Result.DENY || (canSpawn == Result.DEFAULT && !entityliving.checkSpawnRules(level, SpawnReason.NATURAL)))
        	return false;

        level.getServer().submit(() -> {
            level.addFreshEntity(entityliving);
        });
    	
    	return true;
    }
    
    public static boolean canCreatureTypeSpawnAtLocation(SpawnData data, World level, BlockPos pos){
        if (!level.getWorldBorder().isWithinBounds(pos) || !level.noCollision(CustomEntities.entityCustomNpc.getAABB(pos.getX(), pos.getY(), pos.getZ()))){
            return false;
        }
        if(data.type == 1 && level.getLightEmission(pos) > 8 || data.type == 2 && level.getLightEmission(pos) <= 8){
        	return false;
        }
        
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (data.liquid){
            return state.getMaterial().isLiquid() && level.getBlockState(pos.below()).getMaterial().isLiquid() && !level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above());
        }
        
        BlockPos blockpos1 = pos.below();
        
        BlockState state1 = level.getBlockState(blockpos1);
        Block block1 = state1.getBlock();
//        if(!state1.isSideSolid(level, blockpos1, Direction.UP))
//        	return false;
        
        boolean flag = block1 != Blocks.BEDROCK && block1 != Blocks.BARRIER;
        BlockPos down = blockpos1.below();
        flag |= level.getBlockState(down).getBlock().canCreatureSpawn(level.getBlockState(down), level, down, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, CustomEntities.entityCustomNpc);
        return flag && !state.isSignalSource() && !state.getMaterial().isLiquid() && !level.getBlockState(pos.above()).isSignalSource();
    }


    private static BlockPos getTopNonCollidingPos(IWorldReader p_208498_0_, EntityType<?> p_208498_1_, int p_208498_2_, int p_208498_3_) {
        int i = p_208498_0_.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, p_208498_2_, p_208498_3_);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_208498_2_, i, p_208498_3_);
        if (p_208498_0_.dimensionType().hasCeiling()) {
            do {
                blockpos$mutable.move(Direction.DOWN);
            } while(!p_208498_0_.getBlockState(blockpos$mutable).isAir());

            do {
                blockpos$mutable.move(Direction.DOWN);
            } while(p_208498_0_.getBlockState(blockpos$mutable).isAir() && blockpos$mutable.getY() > 0);
        }

        BlockPos blockpos = blockpos$mutable.below();
        if (p_208498_0_.getBlockState(blockpos).isPathfindable(p_208498_0_, blockpos, PathType.LAND)) {
            return blockpos;
        }

        return blockpos$mutable.immutable();
    }
}

