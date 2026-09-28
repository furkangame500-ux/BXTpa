package top.craft_hello.tpa.objects

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.craft_hello.tpa.TPA
import top.craft_hello.tpa.datas.Language
import top.craft_hello.tpa.enums.LanguageType
import top.craft_hello.tpa.utils.LocaleUtil
import java.io.File

object LanguageManager {
    val plugin = TPA.plugin
    val languages = mutableMapOf<String, Language>()

    init {
        loadAllLanguage()
    }

    fun loadAllLanguage() {
        languages.clear()
        val file = File(plugin.dataFolder, "language/tr_TR.yml")
        languages["tr_TR"] = loadLanguage(file.absolutePath, false)
    }

    fun loadLanguage(path: String, isReplace: Boolean): Language {
        return Language(File(path), isReplace)
    }

    fun getLanguage(languageName: String): Language {
        return languages[languageName]
            ?: languages[ConfigManager.config.language]
            ?: languages.values.first()
    }

    fun getLanguage(languageType: LanguageType): Language {
        return getLanguage(languageType.languageName)
    }

    // BXTpa tamamen Türkçedir. Oyuncunun istemci dili dikkate alınmaz.
    fun getLanguage(sender: CommandSender): Language = getLanguage("tr_TR")

    // 语言名是否存在（大小写不敏感）
    fun hasLanguage(languageName: String): Boolean {
        return languages.keys.any { it.equals(languageName, ignoreCase = true) }
    }

    // 全部语言名
    fun getLanguageNames(): List<String> = languages.keys.toList()

    // 规范化语言名：zh_cn -> zh_CN
    fun formatLangStr(languageName: String): String {
        val parts = languageName.lowercase().split("_")
        if (parts.size < 2) return languageName.lowercase()
        return buildString {
            append(parts[0])
            append("_")
            append(parts[1].uppercase())
        }
    }

    fun reloadLanguage() {
        languages.clear()
        loadAllLanguage()
    }
}