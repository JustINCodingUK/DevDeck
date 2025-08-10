package dev.justincodinguk.devdeck.core.di.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import dev.justincodinguk.devdeck.core.di.KoinModule
import dev.justincodinguk.devdeck.core.di.KoinModuleProvider
import java.io.OutputStreamWriter

class KoinModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val processed = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(KoinModule::class.java.name)
        symbols.forEach { logger.info(it.javaClass.name) }
        val newEntries = mutableListOf<String>()

        for(symbol in symbols) {
            if (symbol !is KSPropertyDeclaration) continue
            val propName = symbol.simpleName.asString()
            val className = "${propName.replaceFirstChar { it.uppercase() }}Provider"
            val packageName = symbol.packageName.asString()
            val fullClassName = "$packageName.$className"

            if (processed.contains(fullClassName)) continue
            processed += fullClassName
            newEntries += fullClassName

            val file = codeGenerator.createNewFile(
                Dependencies(false, symbol.containingFile!!),
                packageName,
                className
            )

            OutputStreamWriter(file, Charsets.UTF_8).use {
                it.write(
                    """
                    package $packageName

                    import ${KoinModuleProvider::class.java.name}

                    class $className : KoinModuleProvider {
                        override fun load() = listOf($propName)
                    }
                    """.trimIndent()
                )
            }

            if (newEntries.isNotEmpty()) {
                val spiFile = codeGenerator.createNewFile(
                    Dependencies(false),
                    packageName = "META-INF.services",
                    fileName = KoinModuleProvider::class.java.name,
                    extensionName = ""
                )

                OutputStreamWriter(spiFile, Charsets.UTF_8).use { writer ->
                    newEntries.forEach { writer.write("$it\n") }
                }
            }
        }
        return emptyList()
    }
}