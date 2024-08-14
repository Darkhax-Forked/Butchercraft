package com.lance5057.butchercraft.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class TrimmedBlock extends DirectionalBlock {
	public static final MapCodec<TrimmedBlock> CODEC = simpleCodec(TrimmedBlock::new);

	public TrimmedBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		if (pContext.getClickedFace() == Direction.UP || pContext.getClickedFace() == Direction.DOWN)
			return this.defaultBlockState().setValue(FACING, pContext.getClickedFace().getOpposite());
		return this.defaultBlockState().setValue(FACING, pContext.getClickedFace().getOpposite());
	}
}
