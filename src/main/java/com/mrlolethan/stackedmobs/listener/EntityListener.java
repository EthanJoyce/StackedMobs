package com.mrlolethan.stackedmobs.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.mrlolethan.stackedmobs.util.StacksUtil;

public class EntityListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return; // Not a living entity.
		}
		LivingEntity entity = (LivingEntity) event.getEntity();

		if (entity.getType() != EntityType.PLAYER) {
			StacksUtil.attemptUnstackOne(entity);
		}
	}

}
