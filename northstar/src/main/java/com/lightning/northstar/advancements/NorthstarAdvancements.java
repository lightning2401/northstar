package com.lightning.northstar.advancements;

import static com.lightning.northstar.advancements.NorthstarAdvancement.TaskType.SILENT;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.logging.LogUtils;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

public class NorthstarAdvancements implements DataProvider {

	public static final List<NorthstarAdvancement> ENTRIES = new ArrayList<>();
	public static final NorthstarAdvancement START = null,
			
			ROOT = create("root", b -> b.icon(NorthstarBlocks.TELESCOPE.get().asItem())
					.title("Welcome to Northstar!")
					.description("Shoot for the stars!")
					.awardedForFree()
					.special(SILENT)),

				// Andesite - Central Branch
			
			ONE_SMALL_STEP = create("one_small_step", b -> b.icon(NorthstarBlocks.MOON_SAND.get().asItem())
					.title("One Small Step")
					.description("Set foot on the moon")
					.after(ROOT)),
			
			ONE_GIANT_LEAP = create("one_giant_leap", b -> b.icon(NorthstarBlocks.MARS_SAND.get().asItem())
					.title("One Giant Leap")
					.description("Set foot on Mars")
					.after(ONE_SMALL_STEP)),
			

			END = null;
	
	private static NorthstarAdvancement create(String id, UnaryOperator<NorthstarAdvancement.Builder> b) {
		return new NorthstarAdvancement(id, b);
	}

	// Datagen

	private static final Logger LOGGER = LogUtils.getLogger();
	private final DataGenerator generator;

	public NorthstarAdvancements(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void run(CachedOutput cache) throws IOException {
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (p_204017_3_) -> {
			if (!set.add(p_204017_3_.getId()))
				throw new IllegalStateException("Duplicate advancement " + p_204017_3_.getId());

			Path path1 = getPath(path, p_204017_3_);

			try {
				DataProvider.saveStable(cache, p_204017_3_.deconstruct()
					.serializeToJson(), path1);
			} catch (IOException ioexception) {
				LOGGER.error("Couldn't save advancement {}", path1, ioexception);
			}
		};

		for (NorthstarAdvancement advancement : ENTRIES)
			advancement.save(consumer);
	}

	private static Path getPath(Path pathIn, Advancement advancementIn) {
		return pathIn.resolve("data/" + advancementIn.getId()
			.getNamespace() + "/advancements/"
			+ advancementIn.getId()
				.getPath()
			+ ".json");
	}

	@Override
	public String getName() {
		return "Northstar's Advancements";
	}

	public static JsonObject provideLangEntries() {
		JsonObject object = new JsonObject();
		for (NorthstarAdvancement advancement : ENTRIES)
			advancement.appendToLang(object);
		return object;
	}

	public static void register() {}

}
