plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "26.3-fabric"

stonecutter parameters {
    constants.match(
        node.metadata.project.substringAfterLast('-'),
        "fabric", "neoforge", "forge"
    )

    // String replacements
    replacements {
        string(current.parsed < "1.21.11") {
            replace("Identifier", "ResourceLocation")
        }
        string(current.parsed < "26.1") {
            replace("GuiGraphicsExtractor", "GuiGraphics")
        }
    }
}