package top.bettercode.gradle.publish

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

/**
 *
 * 注册task
 *
 * @author Peter Wu
 */
class PublishPlugin : AbstractPlugin() {

    /**
     * {@inheritDoc}
     */
    override fun apply(project: Project) {
        beforeConfigigure(project)

        project.tasks.create("javadocJar", Jar::class.java) {
            it.archiveClassifier.set("javadoc")
            it.from(project.tasks.getByName("javadoc").outputs)
        }
        configPublish(project)
    }

}