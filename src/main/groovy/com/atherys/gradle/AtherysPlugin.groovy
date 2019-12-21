package com.atherys.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions

class AtherysPlugin implements Plugin<Project> {
    private static final def plugins = [
            "Core", "Quests", "RPG", "Towns", "Economy", "Script", "Parties", "Roleplay", "Skills"
    ]

    @Override
    void apply(Project project) {
        project.plugins.apply("java")

        def repositories = project.getRepositories()
        repositories.add(repositories.mavenCentral())
        repositories.addAll([
                repositories.maven {
                    name = "sponge"
                    url = "https://repo.spongepowered.org/maven"
                },
                repositories.maven {
                    name = "jitpack"
                    url = "https://jitpack.io"
                }
        ])

        project.tasks.getByName("shadowJar").configure {
            classifier = ""
        }

        def shadow = project.configurations["shadow"]
        def deps = project.dependencies

        shadow.dependencies.add(deps.create("org.spongepowered:spongeapi:7.1.0"))

        if (project.name != "AtherysCore") {
            shadow.dependencies.add(deps.create("com.github.Atherys-Horizons:AtherysCore:1.16.0"))
        }

        project.tasks.create("atherysdoc", Javadoc.class, { task ->
            javadocTask(project, task)
        })
    }

    private void javadocTask(Project project, Javadoc task) {
        def configurations = project.getConfigurations();
        def sourceSets = (SourceSetContainer) project.properties.get("sourceSets")

        task.classpath = configurations.shadow + configurations.compile
        task.title = project.name
        task.source = sourceSets.main.allJava
        task.group = "documentation"

        def options = (StandardJavadocDocletOptions) task.getOptions()

        List<String> links = plugins.stream()
                .map({ plugin -> "https://docs.atherys.com/javadocs/Atherys" + plugin + "/"})
                .collect()

        links.addAll([
                "https://www.slf4j.org/apidocs/",
                "https://google.github.io/guice/api-docs/4.1/javadoc/",
                "https://google.github.io/guava/releases/21.0/api/docs/",
                "https://docs.oracle.com/javase/8/docs/api/",
                "https://jd.spongepowered.org/7.1.0/",
                "https://docs.atherys.com/javadocs/AtherysCore/"
        ])

        options.links = links

        options.addStringOption("-Xdoclint:none")
    }
}
