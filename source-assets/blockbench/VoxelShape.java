Stream.of(
Block.makeCuboidShape(3, 3, 13, 13, 15, 14),
Block.makeCuboidShape(3, 3, 2, 13, 15, 3),
Block.makeCuboidShape(13, 3, 3, 14, 15, 13),
Block.makeCuboidShape(2, 3, 3, 3, 15, 13),
Block.makeCuboidShape(3, 19, 3, 13, 21, 13),
Block.makeCuboidShape(3, 21, 3, 13, 28, 13),
Block.makeCuboidShape(13, 4, 0, 16, 28, 3),
Block.makeCuboidShape(13, 4, 13, 16, 28, 16),
Block.makeCuboidShape(0, 4, 0, 3, 28, 3),
Block.makeCuboidShape(0, 4, 13, 3, 28, 16),
Block.makeCuboidShape(0, 28, 0, 16, 32, 16),
Block.makeCuboidShape(0, 0, 0, 16, 4, 16)
).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);});