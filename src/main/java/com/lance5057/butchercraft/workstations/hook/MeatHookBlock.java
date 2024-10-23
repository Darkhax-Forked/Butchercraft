package com.lance5057.butchercraft.workstations.hook;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MeatHookBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty CARCASS_HOOKED = BooleanProperty.create("carcass_hooked");
	// TODO Maybe use double plant logic so that you can interact with bottom thirds
	// of the block
	protected static final VoxelShape AABB = Block.box(0.0D, -32.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape AABB2 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);

	public MeatHookBlock() {
		super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(3, 4).noOcclusion());
		this.registerDefaultState(
				this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CARCASS_HOOKED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(CARCASS_HOOKED) ? AABB : super.getShape(state, worldIn, pos, context);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
			CollisionContext pContext) {
		return pState.getValue(CARCASS_HOOKED) ? AABB : AABB2;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED, CARCASS_HOOKED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return !state.getValue(WATERLOGGED);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack heldMain, BlockState state, Level world, BlockPos blockPos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof MeatHookBlockEntity te) {
			if (playerEntity.isCrouching()) {
				if (te.stage == 0 && te.progress == 0) {
					te.extractItem(playerEntity);
					return ItemInteractionResult.SUCCESS;
				}
			} else if (te.getInsertedItem().isEmpty()) {
				te.insertItem(heldMain); 
				return ItemInteractionResult.SUCCESS;
			} else {
				return te.butcher(playerEntity, heldMain);
			}
		}

		return ItemInteractionResult.CONSUME;

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MeatHookBlockEntity(pPos, pState);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());

		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
	}
}
