Stream.of(
Block.makeCuboidShape(4, 4, 4, 12, 12, 12),
Block.makeCuboidShape(6, 0, 6, 10, 4, 10),
Block.makeCuboidShape(6, 12, 6, 10, 16, 10),
Block.makeCuboidShape(12, 6, 6, 16, 10, 10),
Block.makeCuboidShape(6, 6, 12, 10, 10, 16),
Block.makeCuboidShape(6, 6, 0, 10, 10, 4),
Block.makeCuboidShape(0, 6, 6, 4, 10, 10)
).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);});