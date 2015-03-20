package com.mrlolethan.stackedmobs.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.mrlolethan.stackedmobs.util.StacksUtil;

public class EntityListener implements Listener {

	@EventHandler
	public void onEntityPreDeath(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return; // Not a living entity.
		}
		LivingEntity entity = (LivingEntity) event.getEntity();

		if (entity.getHealth() - event.getDamage() > 0) {
			return; // The damage wasn't enough to kill the entity.
		}

		if (entity.getType() != EntityType.PLAYER) {
			boolean stillStacked = StacksUtil.attemptUnstackOne(entity);
			if (stillStacked) {
				event.setCancelled(true);
				// Reset this entity's health
				entity.setHealth(entity.getMaxHealth());
			}
		}
	}

}
