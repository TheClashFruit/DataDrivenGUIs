import dev.deftu.gradle.utils.ModData
import dev.deftu.gradle.utils.ProjectData

plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    strictExtraMappings.set(false)

    "1.21.5-fabric"(1215, "yarn") {
        "1.21.4-fabric"(1214, "yarn") {
            "1.21.3-fabric"(1213, "yarn") {
                "1.21.2-fabric"(1212, "yarn") {
                    "1.21.1-fabric"(1211, "yarn") {
                        "1.21-fabric"(1210, "yarn")
                    }
                }
            }
        }
    }
}