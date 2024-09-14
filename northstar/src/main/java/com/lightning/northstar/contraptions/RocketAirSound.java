package com.lightning.northstar.contraptions;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class RocketAirSound extends AbstractTickableSoundInstance {

	private float pitch;
	private RocketContraptionEntity parent;

	public RocketAirSound(SoundEvent snd, float pitch, RocketContraptionEntity pParent) {
		super(snd, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
		this.pitch = pitch;
		this.parent = pParent;
		volume = 0.01f;
		looping = true;
		delay = 0;
		relative = true;
	}

	@Override
	public void tick() {
		this.x = parent.getX();
		this.y = parent.getY();
		this.z = parent.getZ();
		if(parent.isRemoved())
			stop();
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	public void setVolume(float vol) {
		this.volume = vol;
	}

	public void fadeIn(float maxVolume) {
		volume = Math.min(maxVolume, volume + .05f);
	}

	public void fadeOut() {
		volume = Math.max(0, volume - .05f);
	}

	public boolean isFaded() {
		return volume == 0;
	}

	@Override
	public float getPitch() {
		return pitch;
	}

	public void stopSound() {
		stop();
	}

}
