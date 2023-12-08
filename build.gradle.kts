plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.16.0"
}


group = "com.aiwan"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.2.4")
    plugins.set(listOf("com.intellij.java", "Kotlin"))
}

kotlin {
    jvmToolchain {
        version = "17"
    }
}
tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    withType<JavaCompile> {
        targetCompatibility = "17"
        sourceCompatibility = "17"
    }

    publishPlugin {
        token.set(System.getenv("Jetbrains_Compose_Helper_Token"))
    }


    patchPluginXml {
        version.set("1.0.1")
        sinceBuild.set("222.*")
        untilBuild.set("")
        changeNotes.set(
            """
           <ul>
             <li><b>1.0.2</b> fix path show error </li>
             <li><b>1.0.1</b> 显示与资源目录下的导航icon关联 </li>
             <li><b>1.0.0</b> Initial Version </li>
           </ul>    
            """.trimIndent()
        )
    }
}
