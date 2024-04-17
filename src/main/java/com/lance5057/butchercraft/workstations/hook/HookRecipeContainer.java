package com.lance5057.butchercraft.workstations.hook;

import com.lance5057.butchercraft.workstations.bases.recipes.EmptyInventory;

import net.minecraft.world.item.ItemStack;

public class HookRecipeContainer extends EmptyInventory {

    private final ItemStack insertedItem;

    public HookRecipeContainer(ItemStack insertedItem) {

        this.insertedItem = insertedItem;
    }

    public ItemStack getInsertedItem() {
        return insertedItem;
    }
}
