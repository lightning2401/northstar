package com.lightning.northstar.contraptions;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class RocketAirSound extends AbstractTickableSoundInstance {

	private float pitch;

	public RocketAirSound(SoundEvent snd, float pitch) {
		super(snd, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
		this.pitch = pitch;
		volume = 0.01f;
		looping = true;
		delay = 0;
		relative = true;
	}

	@Override
	public void tick() {}

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
