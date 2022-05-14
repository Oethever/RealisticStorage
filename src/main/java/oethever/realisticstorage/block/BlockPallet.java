package oethever.realisticstorage.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPallet extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Properties PROPERTIES = Properties.of(Material.WOOD);
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box( 1,  0,  0,  4,  3, 16),
            Block.box(12,  0,  0, 15,  3, 16),
            Block.box( 0,  3,  0, 16,  4,  4),
            Block.box( 0,  3,  6, 16,  4, 10),
            Block.box( 0,  3, 12, 16,  4, 16),

            Block.box( 1,  4,  0,  4,  7, 16),
            Block.box(12,  4,  0, 15,  7, 16),
            Block.box( 0,  7,  0, 16,  8,  4),
            Block.box( 0,  7,  6, 16,  8, 10),
            Block.box( 0,  7, 12, 16,  8, 16),

            Block.box( 1,  8,  0,  4, 11, 16),
            Block.box(12,  8,  0, 15, 11, 16),
            Block.box( 0, 11,  0, 16, 12,  4),
            Block.box( 0, 11,  6, 16, 12, 10),
            Block.box( 0, 11, 12, 16, 12, 16),

            Block.box( 1, 12,  0,  4, 15, 16),
            Block.box(12, 12,  0, 15, 15, 16),
            Block.box( 0, 15,  0, 16, 16,  4),
            Block.box( 0, 15,  6, 16, 16, 10),
            Block.box( 0, 15, 12, 16, 16, 16)
    );

    private static final VoxelShape FLIPPED_SHAPE = SHAPE.toAabbs().stream().map(
        aabb -> Shapes.create(new AABB(
                aabb.minZ, aabb.minY, aabb.minX,
                aabb.maxZ, aabb.maxY, aabb.maxX
        ))
    ).reduce(Shapes::or).get();

    public BlockPallet() {
        super(PROPERTIES);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
            case SOUTH:
                return SHAPE;
            default:
                return FLIPPED_SHAPE;
        }
    }
}
