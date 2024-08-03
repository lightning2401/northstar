package com.lightning.northstar.advancements;

import static com.lightning.northstar.advancements.NorthstarAdvancement.TaskType.SILENT;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.logging.LogUtils;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
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

	private final PackOutput output;
	private static final Logger LOGGER = LogUtils.getLogger();

	public NorthstarAdvancements(PackOutput output) {
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
		List<CompletableFuture<?>> futures = new ArrayList<>();

		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (advancement) -> {
			ResourceLocation id = advancement.getId();
			if (!set.add(id))
				throw new IllegalStateException("Duplicate advancement " + id);
			Path path = pathProvider.json(id);
			futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
				.serializeToJson(), path));
		};

		for (NorthstarAdvancement advancement : ENTRIES)
			advancement.save(consumer);

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
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
