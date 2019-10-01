package com.atherys.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions

class AtherysPlugin implements Plugin<Project> {
    private static final List<String> plugins = [
            "Core", "Quests", "RPG", "Towns", "Economy", "Script", "Parties", "Roleplay", "Skills"
    ]

    @Override
    void apply(Project project) {

        project.tasks.create("atherysdoc", Javadoc.class, {

        })

        RepositoryHandler repositories = project.getRepositories()
        repositories.add(repositories.mavenCentral())
        repositories.addAll([
                repositories.maven {
                    name = "sponge"
                    url = "https://repo.spongepowered.org/maven"
                },
        ])
    }

    private void javadocTask(Project project, Javadoc task) {
        ConfigurationContainer configurations = project.getConfigurations();
        task.classpath = configurations.shadow + configurations.compile

        SourceSetContainer sourceSets = (SourceSetContainer) (project.getProperties().get("sourceSets"))

        task.source = sourceSets.main.allJava

        StandardJavadocDocletOptions options = (StandardJavadocDocletOptions) task.getOptions()

        List<String> links = plugins.stream()
                .map({ plugin -> "https://docs.atherys.com/javadocs/Atherys" + plugin + "/"})
                .collect()

        links.addAll([
                "http://www.slf4j.org/apidocs/",
                "https://google.github.io/guice/api-docs/4.1/javadoc/",
                "https://google.github.io/guava/releases/21.0/api/docs/",
                "https://docs.oracle.com/javase/8/docs/api/",
                "http://jd.spongepowered.org/7.1.0/",
                "https://docs.atherys.com/javadocs/AtherysCore/"
        ])

        options.setLinks(links)
    }
}
