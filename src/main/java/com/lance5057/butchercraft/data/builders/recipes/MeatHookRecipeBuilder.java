package com.lance5057.butchercraft.data.builders.recipes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

public class MeatHookRecipeBuilder implements RecipeBuilder {
	private final Item result;
	private final List<AnimatedRecipeItemUse> tools = NonNullList.create();
	private final List<Ingredient> jei = NonNullList.create();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private String group;

	public MeatHookRecipeBuilder(Item carcassIn) {
		this.result = carcassIn;
		this.unlockedBy(RecipeBuilderUtil.getHasName(carcassIn), RecipeBuilderUtil.has(carcassIn));
	}

	public static MeatHookRecipeBuilder shapedRecipe(Item resultIn) {
		return new MeatHookRecipeBuilder(resultIn);
	}

	public MeatHookRecipeBuilder tool(Ingredient tool, int count, int uses, boolean damage, ResourceKey<LootTable> table,
			BlacklistedModel... model) {
		this.tools.add(new AnimatedRecipeItemUse(uses, tool, count, damage, table.location(), List.of(model)));
		return this;
	}

	public MeatHookRecipeBuilder tool(Ingredient tool, int uses, boolean damage, ResourceKey<LootTable> table,
			BlacklistedModel... model) {
		this.tools.add(new AnimatedRecipeItemUse(uses, tool, 1, damage, table.location(), List.of(model)));
		return this;
	}

	public MeatHookRecipeBuilder JEIIngredient(Ingredient i) {
		this.jei.add(i);
		return this;
	}

	/**
	 * Makes sure that this recipe is valid and obtainable.
	 */
	private void validate(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
		if (this.tools.isEmpty()) {
			throw new IllegalStateException("No toolset is defined for shaped recipe %s!".formatted(id));
		}
	}

	/**
	 * Adds a criterion needed to unlock the recipe.
	 */
	@Override
	public RecipeBuilder unlockedBy(String pCriterionName, Criterion<?> pCriterionTrigger) {
		this.criteria.put(pCriterionName, pCriterionTrigger);
		return this;
	}

	@Override
	public RecipeBuilder group(String pGroupName) {
		this.group = pGroupName;
		return this;
	}

	@Override
	public Item getResult() {
		return this.result;
	}

	@Override
	public void save(RecipeOutput consumerIn, ResourceLocation pRecipeId) {
		this.validate(pRecipeId);
		Advancement.Builder builder = consumerIn.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
				.rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(builder::addCriterion);
		consumerIn.accept(pRecipeId, new HookRecipe(this.group == null ? "" : this.group, Ingredient.of(this.result),
				NonNullList.copyOf(this.tools), NonNullList.copyOf(this.jei)), builder.build(ResourceLocation.fromNamespaceAndPath(pRecipeId.getNamespace(), "recipes/meat_hook/" + pRecipeId.getPath())));
	}
}
