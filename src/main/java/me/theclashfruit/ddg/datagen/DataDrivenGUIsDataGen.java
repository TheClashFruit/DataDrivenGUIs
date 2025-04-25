package me.theclashfruit.ddg.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataDrivenGUIsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        Path langDir = Paths.get("../../lang");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(langDir, "*.lang")) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();
                String langCode = fileName.substring(0, fileName.lastIndexOf('.'));

                pack.addProvider((output, registryLookup) -> new LanguageDataProvider(output, langCode, registryLookup, file));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read .lang files", e);
        }
    }

    public static class LanguageDataProvider extends FabricLanguageProvider {
        private final Path file;

        public LanguageDataProvider(FabricDataOutput dataOutput, String langCode, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, Path file) {
            super(dataOutput, langCode, registryLookup);

            this.file = file;
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
            Path langDir = Paths.get("../../lang");

            try {
                Map<String, String> entries = loadLangFile(this.file);
                entries.forEach(translationBuilder::add);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read lang file: " + this.file, e);
            }
        }

        private Map<String, String> loadLangFile(Path file) throws IOException {
            Map<String, String> map = new LinkedHashMap<>();

            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    int sep = line.indexOf('=');
                    if (sep == -1) continue;

                    String key = line.substring(0, sep).trim();
                    String value = line.substring(sep + 1).trim();

                    map.put(key, value);
                }
            }

            return map;
        }
    }
}
