package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import noppes.npcs.CustomContainer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.constants.EnumGuiType;

public class BlockCarpentryBench extends BlockInterface{
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    
    public BlockCarpentryBench(){
        super(Block.Properties.copy(Blocks.CRAFTING_TABLE).sound(SoundType.WOOD).strength(5.0F, 10));
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
    	if(!level.isClientSide){
            NoppesUtilServer.openContainerGui((ServerPlayerEntity) player, EnumGuiType.PlayerAnvil, (buffer) -> buffer.writeBlockPos(pos));
    	}
		return ActionResultType.SUCCESS;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState p_196247_1_, IBlockReader p_196247_2_, BlockPos p_196247_3_) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }
    
//    @Override
//    public boolean isFullCube(BlockState state){
//        return false;
//    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        int var6 = MathHelper.floor((double)(context.getPlayer().yRot / 90.0F) + 0.5D) & 3;
        return defaultBlockState().setValue(ROTATION, var6);
    }

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new TileBlockAnvil();
	}
}
