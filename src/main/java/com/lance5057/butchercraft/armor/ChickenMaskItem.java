package com.lance5057.butchercraft.armor;

import java.util.function.Consumer;

import com.lance5057.butchercraft.armor.models.ChickenMaskModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class ChickenMaskItem extends ArmorItem {
	public ChickenMaskItem(ArmorMaterial pMaterial, Properties pProperties) {
		super(pMaterial, Type.HELMET, pProperties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {

			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot,
					HumanoidModel<?> defaultModel) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(ChickenMaskModel.LAYER_LOCATION);
				defaultModel.body.visible = true;

				return new ChickenMaskModel(root);
			}

		});
	}

}