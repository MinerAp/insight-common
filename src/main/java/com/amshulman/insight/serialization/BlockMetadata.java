package com.amshulman.insight.serialization;

import org.bukkit.block.BlockState;

import lombok.Value;

@Value
public class BlockMetadata implements StorageMetadata {

    private static final long serialVersionUID = 5118317724058752210L;

    MetadataEntry meta;
    Block previousBlock;

    public BlockMetadata(MetadataEntry metadata, BlockState previousBlock) {
        meta = metadata;
        this.previousBlock = previousBlock == null ? null : new Block(previousBlock.getType(), previousBlock.getRawData());
    }
}
