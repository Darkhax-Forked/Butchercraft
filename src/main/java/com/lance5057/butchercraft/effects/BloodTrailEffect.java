package com.lance5057.butchercraft.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BloodTrailEffect extends SoapableMobEffect {
	public BloodTrailEffect() {
		super(MobEffectCategory.HARMFUL, 7995392);
	}
	
	@Override
	 public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
			super.applyEffectTick(pLivingEntity, pAmplifier);
			
			for (int i = 0; i < 3; i++)
				pLivingEntity.level().addParticle(ParticleTypes.FALLING_LAVA,
						pLivingEntity.position().x - 0.25f + pLivingEntity.level().random.nextDouble() / 2,
						pLivingEntity.position().y + 0.25f - pLivingEntity.level().random.nextDouble(),
						pLivingEntity.position().z - 0.25f + pLivingEntity.level().random.nextDouble() / 2, 0, 0,
						0);
	 }
	
	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}
}
