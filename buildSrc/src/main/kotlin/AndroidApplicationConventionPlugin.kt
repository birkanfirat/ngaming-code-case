import com.android.build.api.dsl.ApplicationExtension
import com.ngaming.ngamingcase.convention.configureAndroid
import com.ngaming.ngamingcase.convention.int
import com.ngaming.ngamingcase.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** app modülünün ortak Android ayarlarını uyguluyor. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension> {
            configureAndroid(this)
            defaultConfig.targetSdk = libs.int("targetSdk")
            buildFeatures.viewBinding = true
        }
    }
}
